package fr.zorg.bungeesk.bungee.sockets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.storage.BungeeConfig;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.encryption.GlobalEncryption;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

public final class ClientServer {

    private final Socket socket;
    private String address;
    private final Thread readThread;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private final GlobalEncryption encryption;
    private final Map<String, LinkedList<CompletableFuture<String>>> toComplete;
    private final static Gson gson = new GsonBuilder().create();
    private final String password;

    private boolean identified;

    public ClientServer(final Socket socket) throws IOException {
        this.socket = socket;
        this.identified = false;
        this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), StandardCharsets.UTF_8));
        this.writer = new PrintWriter(this.socket.getOutputStream(), true);
        this.toComplete = new HashMap<>();
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

                        if (player == null || !(player.isConnected()) || optionalServer.isEmpty())
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

                    // Futures

                    case "futureGet": {
                        final String uuid = args.get("uuid").getAsString();
                        final String localAction = args.get("action").getAsString();
                        Map<String, Object> toSend = new HashMap<>();
                        toSend.put("uuid", uuid);
                        Map<String, Object> argsMap = new HashMap<>();
                        boolean notFound = false;
                        switch (localAction) {
                            case "expressionGetBungeeServerFromName": {
                                ServerInfo server = BungeeSK.getInstance().getProxy().getServerInfo(args.get("name").getAsString());

                                if (server == null) {
                                    notFound = true;
                                    break;
                                }

                                argsMap.put("name", server.getName());
                                argsMap.put("address", server.getAddress().getAddress().getHostAddress());
                                argsMap.put("port", String.valueOf(server.getAddress().getPort()));
                                argsMap.put("motd", server.getMotd());

                                break;
                            }

                            case "expressionGetBungeeServerFromAddress": {
                                final Optional<ServerInfo> optionalServer = BungeeUtils.findServer(args.get("address").getAsString(), args.get("port").getAsInt());

                                if (optionalServer.isEmpty()) {
                                    notFound = true;
                                    break;
                                }

                                final ServerInfo server = optionalServer.get();

                                argsMap.put("name", server.getName());
                                argsMap.put("address", server.getAddress().getAddress().getHostAddress());
                                argsMap.put("port", String.valueOf(server.getAddress().getPort()));
                                argsMap.put("motd", server.getMotd());

                                break;
                            }

                            case "expressionGetAllBungeeServers": {
                                if (BungeeSK.getInstance().getProxy().getServers().values().size() == 0) {
                                    notFound = true;
                                    break;
                                }

                                List<Map<String, String>> serverInfos = new ArrayList<>();
                                for (ServerInfo server : BungeeSK.getInstance().getProxy().getServers().values()) {
                                    Map<String, String> serverInfo = new HashMap<>();
                                    serverInfo.put("name", server.getName());
                                    serverInfo.put("address", server.getAddress().getAddress().getHostAddress());
                                    serverInfo.put("port", String.valueOf(server.getAddress().getPort()));
                                    serverInfo.put("motd", server.getMotd());
                                    serverInfos.add(serverInfo);
                                }
                                argsMap.put("servers", serverInfos);
                                break;
                            }

                            case "expressionGetBungeePlayerFromName": {
                                final ProxiedPlayer player = BungeeSK.getInstance().getProxy().getPlayer(args.get("name").getAsString());

                                if (player == null) {
                                    notFound = true;
                                    break;
                                }

                                argsMap.put("name", player.getName());
                                argsMap.put("uniqueId", player.getUniqueId().toString());

                                break;
                            }

                            case "expressionGetBungeePlayerFromUUID": {
                                final ProxiedPlayer player = BungeeSK.getInstance().getProxy().getPlayer(UUID.fromString(args.get("uniqueId").getAsString()));

                                if (player == null) {
                                    notFound = true;
                                    break;
                                }

                                argsMap.put("name", player.getName());
                                argsMap.put("uniqueId", player.getUniqueId().toString());

                                break;
                            }

                            case "expressionGetAllBungeePlayers": {
                                if (BungeeSK.getInstance().getProxy().getPlayers().size() == 0) {
                                    notFound = true;
                                    break;
                                }

                                List<Map<String, String>> players = new ArrayList<>();
                                BungeeSK.getInstance().getProxy().getPlayers().forEach(player -> {
                                    Map<String, String> playerData = new HashMap<>();
                                    playerData.put("name", player.getName());
                                    playerData.put("uniqueId", player.getUniqueId().toString());
                                    players.add(playerData);
                                });
                                argsMap.put("players", players);
                                break;
                            }
                            case "expressionGetServerOfBungeePlayer": {
                                final ProxiedPlayer player = BungeeSK.getInstance().getProxy().getPlayer(UUID.fromString(args.get("uniqueId").getAsString()));

                                if (player == null) {
                                    notFound = true;
                                    break;
                                }

                                Map<String, String> map = new HashMap<>();
                                final ServerInfo server = player.getServer().getInfo();

                                map.put("name", server.getName());
                                map.put("address", server.getAddress().getAddress().getHostAddress());
                                map.put("port", String.valueOf(server.getAddress().getPort()));
                                map.put("motd", server.getMotd());

                                argsMap.put("server", map);
                                break;
                            }
                            case "expressionGetAllPlayersOnServer": {
                                final Optional<ServerInfo> optionalServer = BungeeUtils.findServer(args.get("address").getAsString(), args.get("port").getAsInt());

                                if (optionalServer.isEmpty()) {
                                    notFound = true;
                                    break;
                                }

                                final ServerInfo server = optionalServer.get();

                                List<Map<String, String>> players = new ArrayList<>();
                                server.getPlayers().forEach(player -> {
                                    Map<String, String> playerData = new HashMap<>();
                                    playerData.put("name", player.getName());
                                    playerData.put("uniqueId", player.getUniqueId().toString());
                                    players.add(playerData);
                                });

                                argsMap.put("players", players);
                                break;
                            }
                            case "expressionGetPlayerConnectionState": {
                                final ProxiedPlayer player = BungeeSK.getInstance().getProxy().getPlayer(UUID.fromString(args.get("uniqueId").getAsString()));

                                if (player == null || !(player.isConnected())) {
                                    notFound = true;
                                    break;
                                }
                            }
                        }
                        argsMap.put("error", notFound);
                        toSend.put("response", argsMap);
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
        final Map<String, String[]> files = BungeeSK.getInstance().filesToString();
        this.writeRaw(true, "sendFiles", files);
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

    public void writeRaw(final boolean encryption, final String action, final Map<?, ?> argsMap) {
        final Map<String, Object> map = new HashMap<>();
        map.put("action", action);
        map.put("args", argsMap);
        if (encryption) {
            this.writer.println(this.encryption.encrypt(gson.toJson(map), this.password));
            return;
        }
        this.writer.println(gson.toJson(map));
    }

    public String getAddress() {
        return this.address;
    }

    public boolean isConnected() {
        return this.socket != null && this.socket.isConnected() && !this.socket.isClosed();
    }

    public Map<String, LinkedList<CompletableFuture<String>>> getToComplete() {
        return this.toComplete;
    }

    public void putFuture(final String key, final String value) {
        LinkedList<CompletableFuture<String>> future = this.toComplete.get(key);
        if (future != null && future.size() > 0) {
            future.poll().complete(value);
            if (future.size() == 0) this.toComplete.remove(key, future);
        }
    }

    public String future(final String value) {
        LinkedList<CompletableFuture<String>> futureList = new LinkedList<>();
        if (this.toComplete.containsKey(value)) futureList = this.toComplete.get(value);
        final CompletableFuture<String> future = new CompletableFuture<>();
        futureList.add(future);
        this.toComplete.put(value, futureList);
        this.write(true, value);
        String result;
        try {
            result = future.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return null;
        }
        return result;
    }

    public static Gson getGson() {
        return gson;
    }
}



