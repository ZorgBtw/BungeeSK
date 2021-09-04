package fr.zorg.bungeesk.bungee.sockets;

import com.google.gson.*;
import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.storage.BungeeConfig;
import fr.zorg.bungeesk.bungee.storage.GlobalVariables;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.encryption.GlobalEncryption;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

public final class ClientServer {

    private final Socket socket;
    private String address;
    private final Thread readThread;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private final GlobalEncryption encryption;
    private final static Gson gson = new GsonBuilder().create();
    private final String password;

    private boolean identified;

    public ClientServer(final Socket socket) throws IOException {
        this.socket = socket;
        this.identified = false;
        this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), StandardCharsets.UTF_8));
        this.writer = new PrintWriter(this.socket.getOutputStream(), true);
        this.encryption = BungeeSK.getEncryption();
        this.password = BungeeConfig.get().getPassword();

        this.readThread = new Thread(this::read);
        this.readThread.setDaemon(true);
        this.readThread.start();
    }

    public void read() {
        try {
            reader:
            while (this.isConnected()) {
                String rawData = reader.readLine();
                if (rawData == null)
                    continue;


                final String data = this.encryption.decrypt(rawData, this.password);

                if (data.equals("wrongPassword")) {
                    this.disconnect();
                    break reader;
                }

                final JsonParser jsonParser = new JsonParser();
                final JsonObject jsonObject = jsonParser.parse(data).getAsJsonObject();

                final String action = jsonObject.get("action").getAsString();
                final Server serv = BungeeSK.getInstance().getServer();
                final JsonObject args = jsonObject.get("args").getAsJsonObject();

                if (this.address == null) {
                    if (!serv.isClient(socket)) {
                        this.address = args.get("address").getAsString();

                        if (serv.isClient(this.address)) {
                            this.disconnect();
                            break;
                        }

                        if (!serv.isPassword(args.get("password").getAsString())) {
                            this.disconnect();
                            break;
                        }

                        this.identified = true;
                        serv.addClient(this);

                        BungeeSK.getInstance().getLogger().log(Level.INFO, "§6New server connected under address §a"
                                + this.address);

                        this.write(true, "connectionInformation", "status", "connected");

                        if (BungeeConfig.get().isSendFilesAutoEnabled())
                            this.sendFiles();

                    } else {
                        this.disconnect();
                        break;
                    }
                    continue;
                }


                switch (action) {

                    // Connection management

                    case "connectionInformation": {
                        if (args.get("status").getAsString().equals("disconnect")) {
                            BungeeSK.getInstance().getLogger().log(Level.INFO, "§6Server disconnected under address §c" + this.address);
                            this.forceDisconnect();
                        }
                        break reader;
                    }

                    //Global scripts

                    case "sendFilesRequest": {
                        this.sendFiles();
                        break;
                    }

                    // Global variables

                    case "setGlobalVariable": {
                        GlobalVariables.get().setVar(args.get("varName").getAsString(), args.get("value").getAsString());
                        break;
                    }

                    case "deleteGlobalVariable": {
                        GlobalVariables.get().deleteVar(args.get("varName").getAsString());
                        break;
                    }

                    // Effects

                    case "effectExecuteCommand": {
                        final String server = args.get("server").getAsString();
                        final String command = args.get("command").getAsString();
                        if (server.equals("bungeecord"))
                            BungeeSK.getInstance().getProxy().getPluginManager().dispatchCommand(BungeeSK.getInstance().getProxy().getConsole(), command);

                        else if (server.equals("all"))
                            serv.writeAll(true, "executeConsoleCommand", "command", command);

                        else if (serv.getClient(args.get("server").getAsString()).isPresent())
                            serv.getClient(args.get("server").getAsString()).get().write(true, "executeConsoleCommand", "command", command);

                        break;
                    }

                    case "effectBroadcastMessageToServer": {
                        final String server = args.get("server").getAsString();
                        final String message = args.get("message").getAsString();
                        if (serv.getClient(server).isPresent()) {
                            serv.getClient(server).get().write(true, "broadcastMessage",
                                    "message", message);
                        }
                        break;
                    }

                    case "effectSendBungeePlayerToServer": {
                        final ProxiedPlayer player = BungeeSK.getInstance().getProxy().getPlayer(UUID.fromString(args.get("playerUniqueId").getAsString()));
                        final Optional<ServerInfo> optionalServer = BungeeUtils.findServer(args.get("serverAddress").getAsString(), args.get("serverPort").getAsInt());

                        if (player == null || !(player.isConnected()) || !(optionalServer.isPresent()))
                            break;

                        player.connect(optionalServer.get());
                        break;
                    }

                    case "effectSendMessageToBungeePlayer": {
                        final ProxiedPlayer player = BungeeSK.getInstance().getProxy().getPlayer(UUID.fromString(args.get("playerUniqueId").getAsString()));

                        if (player == null || !(player.isConnected()))
                            break;

                        player.sendMessage(TextComponent.fromLegacyText(args.get("message").getAsString()));
                        break;
                    }

                    case "effectMakeBungeePlayerExecuteCommand": {
                        final ProxiedPlayer player = BungeeSK.getInstance().getProxy().getPlayer(UUID.fromString(args.get("playerUniqueId").getAsString()));

                        if (player == null || !(player.isConnected()))
                            break;

                        String command = args.get("command").getAsString();

                        if (!command.startsWith("/"))
                            command = "/" + command;
                        player.chat(command);
                        break;
                    }

                    case "effectKickBungeePlayer": {
                        final ProxiedPlayer player = BungeeSK.getInstance().getProxy().getPlayer(UUID.fromString(args.get("playerUniqueId").getAsString()));

                        if (player == null || !(player.isConnected()))
                            break;

                        final String reason = args.get("reason").getAsString();
                        if (reason.equalsIgnoreCase("NONE")) {
                            player.disconnect();
                            break;
                        }
                        player.disconnect(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', reason)));
                        break;
                    }

                    case "effectSendBungeeCustomMessage": {
                        final String server = args.get("serverAddress").getAsString() + ":" + args.get("serverPort").getAsInt();

                        final Optional<ServerInfo> optionnalServer = BungeeUtils.findServer(this.address.split(":")[0], Integer.parseInt(this.address.split(":")[1]));

                        if (serv.getClient(server).isPresent() && optionnalServer.isPresent()) {
                            final JsonObject message = new JsonObject();
                            message.addProperty("message", args.get("message").getAsString());
                            message.add("fromServer", BungeeUtils.getBungeeServer(optionnalServer.get()));

                            serv.getClient(server).get().writeRaw(true, "customBungeeMessage", message);
                        }
                        break;
                    }

                    case "effectBroadcastMessageToNetwork": {
                        final String message = args.get("message").getAsString();

                        BungeeSK.getInstance().getProxy().broadcast(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message)));
                        break;
                    }

                    case "effectSendActionBar": {
                        final ProxiedPlayer player = BungeeSK.getInstance().getProxy().getPlayer(UUID.fromString(args.get("playerUuid").getAsString()));

                        if (player == null || !(player.isConnected())) {
                            break;
                        }

                        player.sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', args.get("message").getAsString())));
                        break;
                    }

                    case "effectSendBungeePlayerTitle": {
                        final ProxiedPlayer player = BungeeSK.getInstance().getProxy().getPlayer(UUID.fromString(args.get("playerUuid").getAsString()));

                        if (player == null || !(player.isConnected())) {
                            break;
                        }

                        final Title title = BungeeSK.getInstance().getProxy().createTitle();
                        if (!args.get("title").getAsString().equalsIgnoreCase("NONE")) {
                            title.title(TextComponent.fromLegacyText(args.get("title").getAsString()));
                        }
                        if (!args.get("subTitle").getAsString().equalsIgnoreCase("NONE")) {
                            title.subTitle(TextComponent.fromLegacyText(args.get("subTitle").getAsString()));
                        }
                        if (!args.get("time").getAsString().equalsIgnoreCase("NONE")) {
                            title.stay(args.get("time").getAsInt());
                        }
                        if (!args.get("fadeIn").getAsString().equalsIgnoreCase("NONE")) {
                            title.fadeIn(args.get("fadeIn").getAsInt());
                        }
                        if (!args.get("fadeOut").getAsString().equalsIgnoreCase("NONE")) {
                            title.fadeOut(args.get("fadeOut").getAsInt());
                        }


                        title.send(player);
                        break;
                    }

                    case "effectAddDynamicServer": {
                        final ServerInfo infos = ProxyServer.getInstance().constructServerInfo(args.get("name").getAsString(),
                                new InetSocketAddress(args.get("address").getAsString(), args.get("port").getAsInt()),
                                args.get("motd").getAsString(), false);

                        ProxyServer.getInstance().getServers().put(args.get("name").getAsString(), infos);
                        break;
                    }

                    case "effectDeleteServerFromBungee": {
                        ProxyServer.getInstance().getServers().remove(args.get("name").getAsString());
                        break;
                    }

                    // Futures

                    case "futureGet": {
                        final String uuid = args.get("uuid").getAsString();
                        final String localAction = args.get("action").getAsString();
                        final JsonObject toSend = new JsonObject();
                        toSend.addProperty("uuid", uuid);
                        JsonObject toComplete = new JsonObject();
                        boolean error = false;
                        switch (localAction) {
                            case "expressionGetBungeeServerFromName": {
                                final ServerInfo server = BungeeSK.getInstance().getProxy().getServerInfo(args.get("name").getAsString());

                                if (server == null) {
                                    error = true;
                                    break;
                                }

                                toComplete = BungeeUtils.getBungeeServer(server);

                                break;
                            }

                            case "expressionGetBungeeServerFromAddress": {
                                final Optional<ServerInfo> optionalServer = BungeeUtils.findServer(args.get("address").getAsString(), args.get("port").getAsInt());

                                if (!(optionalServer.isPresent())) {
                                    error = true;
                                    break;
                                }
                                toComplete = BungeeUtils.getBungeeServer(optionalServer.get());

                                break;
                            }

                            case "expressionGetAllBungeeServers": {
                                if (BungeeSK.getInstance().getProxy().getServers().values().size() == 0) {
                                    error = true;
                                    break;
                                }

                                final JsonArray servers = new JsonArray();
                                BungeeSK.getInstance().getProxy().getServers().values().forEach(serverInfo ->
                                        servers.add(BungeeUtils.getBungeeServer(serverInfo))
                                );
                                toComplete.add("servers", servers);
                                break;
                            }

                            case "expressionGetBungeePlayerFromName": {
                                final ProxiedPlayer player = BungeeSK.getInstance().getProxy().getPlayer(args.get("name").getAsString());

                                if (player == null) {
                                    error = true;
                                    break;
                                }

                                toComplete.addProperty("name", player.getName());
                                toComplete.addProperty("uniqueId", player.getUniqueId().toString());

                                break;
                            }

                            case "expressionGetBungeePlayerFromUUID": {
                                final ProxiedPlayer player = BungeeSK.getInstance().getProxy().getPlayer(UUID.fromString(args.get("uniqueId").getAsString()));

                                if (player == null) {
                                    error = true;
                                    break;
                                }

                                toComplete.addProperty("name", player.getName());
                                toComplete.addProperty("uniqueId", player.getUniqueId().toString());

                                break;
                            }

                            case "expressionGetAllBungeePlayers": {
                                if (BungeeSK.getInstance().getProxy().getPlayers().size() == 0) {
                                    error = true;
                                    break;
                                }

                                final JsonArray players = new JsonArray();

                                BungeeSK.getInstance().getProxy().getPlayers().forEach(player -> {
                                    final JsonObject playerData = new JsonObject();
                                    playerData.addProperty("name", player.getName());
                                    playerData.addProperty("uniqueId", player.getUniqueId().toString());
                                    players.add(playerData);
                                });
                                toComplete.add("players", players);
                                break;
                            }
                            case "expressionGetServerOfBungeePlayer": {
                                final ProxiedPlayer player = BungeeSK.getInstance().getProxy().getPlayer(UUID.fromString(args.get("uniqueId").getAsString()));

                                if (player == null) {
                                    error = true;
                                    break;
                                }

                                final JsonObject serverInfos = BungeeUtils.getBungeeServer(player.getServer().getInfo());

                                toComplete.add("server", serverInfos);
                                break;
                            }
                            case "expressionGetAllPlayersOnServer": {
                                final Optional<ServerInfo> optionalServer = BungeeUtils.findServer(args.get("address").getAsString(), args.get("port").getAsInt());

                                if (!(optionalServer.isPresent())) {
                                    error = true;
                                    break;
                                }

                                final JsonArray players = new JsonArray();
                                optionalServer.get().getPlayers().forEach(player -> {
                                    final JsonObject playerData = new JsonObject();
                                    playerData.addProperty("name", player.getName());
                                    playerData.addProperty("uniqueId", player.getUniqueId().toString());
                                    players.add(playerData);
                                });

                                toComplete.add("players", players);
                                break;
                            }
                            case "expressionGetPlayerConnectionState": {
                                final ProxiedPlayer player = BungeeSK.getInstance().getProxy().getPlayer(UUID.fromString(args.get("playerUniqueId").getAsString()));

                                if (player == null || !(player.isConnected()))
                                    error = true;
                                break;
                            }
                            case "expressionGetBungeePlayerIP": {
                                final ProxiedPlayer player = BungeeSK.getInstance().getProxy().getPlayer(UUID.fromString(args.get("playerUniqueId").getAsString()));

                                if (player == null || !(player.isConnected())) {
                                    error = true;
                                    break;
                                }

                                toComplete.addProperty("address", player.getAddress().getAddress().getHostAddress());
                                break;
                            }
                            case "getGlobalVariable": {
                                toComplete.addProperty("varName", args.get("varName").getAsString());
                                toComplete.addProperty("value", GlobalVariables.get().getVar(args.get("varName").getAsString()));
                                break;
                            }
                            case "conditionDoesBungeePlayerHavePermission": {
                                final ProxiedPlayer player = BungeeSK.getInstance().getProxy().getPlayer(UUID.fromString(args.get("playerUuid").getAsString()));

                                if (player == null || !(player.isConnected()) || !player.hasPermission(args.get("permission").getAsString())) {
                                    error = true;
                                    break;
                                }

                                break;
                            }
                        }
                        toComplete.addProperty("error", error);
                        toSend.add("response", toComplete);
                        this.writeRaw(true, "futureResponse", toSend);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            this.forceDisconnect();
        }

    }

    private void sendFiles() {
        this.writeRaw(true, "sendFiles", BungeeSK.getInstance().filesToString());
    }

    public void disconnect() {
        if (!this.identified) {
            if (this.address != null) {
                this.write(false, "connectionInformation", "status", "alreadyConnected");
            } else {
                this.write(false, "connectionInformation", "status", "wrongPassword");
            }
        } else {
            this.write(false, "connectionInformation", "status", "disconnect");
        }
        this.forceDisconnect();
    }

    public void forceDisconnect() {
        if (!this.socket.isClosed()) {
            try {
                this.reader.close();
                this.writer.close();
                this.readThread.interrupt();
                this.socket.close();
                BungeeSK.getInstance().getServer().removeClient(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket() {
        return this.socket;
    }


    public void write(final boolean encryption, final String action, final String... args) {
        final Map<String, Object> map = new HashMap<>();
        map.put("action", action);
        final Map<String, String> argsMap = new HashMap<>();
        for (int i = 0; i < args.length; i = i + 2) {
            argsMap.put(args[i], args[i + 1]);
        }
        map.put("args", argsMap);
        if (encryption) {
            this.writer.println(this.encryption.encrypt(gson.toJson(map), this.password));
            return;
        }
        this.writer.println(gson.toJson(map));
    }

    public void writeRaw(final boolean encryption, final String action, final JsonObject args) {
        final JsonObject toSend = new JsonObject();
        toSend.addProperty("action", action);
        toSend.add("args", args);
        if (encryption) {
            this.writer.println(this.encryption.encrypt(gson.toJson(toSend), this.password));
            return;
        }
        this.writer.println(gson.toJson(toSend));
    }

    public String getAddress() {
        return this.address;
    }

    public boolean isConnected() {
        return this.socket != null && this.socket.isConnected() && !this.socket.isClosed();
    }

    public static Gson getGson() {
        return gson;
    }
}



