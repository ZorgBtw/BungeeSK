package fr.zorg.bungeesk.bukkit.sockets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.*;
import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;
import fr.zorg.bungeesk.bukkit.utils.BungeeServer;
import fr.zorg.bungeesk.common.encryption.GlobalEncryption;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

public final class ConnectionClient {

    private static ConnectionClient instance;

    @Nullable
    public static ConnectionClient get() {
        return instance;
    }

    public static ConnectionClient generateConnection(final ClientSettings settings) {

        if (instance != null) {
            instance.disconnect();
            instance = null;
        }
        try {
            final Socket socket = new Socket(settings.getAddress(), settings.getPort().intValue());
            instance = new ConnectionClient(socket, settings.getPassword());
        } catch (Exception e) {
            BungeeSK.getInstance().getLogger().log(Level.SEVERE, "BungeeSK couldn't find server with this address/port !");
        }
        return instance;
    }

    private final Socket socket;
    private final Thread readThread;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private final String address;
    private final String password;
    private final static Gson gson = new GsonBuilder().create();

    private final Map<UUID, CompletableFuture<JsonObject>> toComplete;

    private final GlobalEncryption encryption;

    private ConnectionClient(final Socket socket, final String password) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.toComplete = new HashMap<>();
        this.address = socket.getInetAddress().toString() + ":" + BungeeSK.getInstance().getServer().getPort();
        this.password = password;
        this.encryption = BungeeSK.getEncryption();
        this.write(true, "connectionRequest", "address", this.address.substring(1), "password", this.password);
        this.readThread = new Thread(this::read);
        this.readThread.setDaemon(true);
        this.readThread.start();
    }

    public void read() {
        try {
            while (this.isConnected()) {

                final String rawData = this.reader.readLine();
                if (rawData == null) {
                    this.disconnect();
                    break;
                }

                final String data = this.encryption.decrypt(rawData, this.password);

                final JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
                final String action = jsonObject.get("action").getAsString();
                final JsonObject args = jsonObject.get("args").getAsJsonObject();


                switch (action) {

                    // Connection

                    case "connectionInformation": {
                        if (args.get("status").getAsString().equals("wrongPassword")) {
                            this.forceDisconnect();
                            BungeeSK.getInstance().getLogger().log(Level.WARNING, "§6Trying to connect to §c"
                                    + this.socket.getInetAddress().getHostAddress()
                                    + " §6and returned failure: §cWrong password !");
                        } else if (args.get("status").getAsString().equals("alreadyConnected")) {
                            BungeeSK.getInstance().getLogger().log(Level.WARNING, "§6Trying to connect to §c"
                                    + this.socket.getInetAddress().getHostAddress()
                                    + " §6and returned failure: §cServer already connected under this address !");
                            this.forceDisconnect();
                        } else if (args.get("status").getAsString().equals("disconnect")) {
                            this.disconnect();
                        } else if (args.get("status").getAsString().equals("connected")) {
                            Bukkit.getScheduler().runTask(BungeeSK.getInstance(), () -> Bukkit.getPluginManager().callEvent(new ClientConnectEvent()));
                        }
                        break;
                    }

                    // Global scripts

                    case "sendFiles": {
                        final File folder = new File("plugins/Skript/scripts/BungeeSK");
                        if (!folder.exists())
                            folder.mkdirs();

                        args.entrySet().forEach(fileArg -> {
                            try {
                                File file = new File(folder, fileArg.getKey());
                                if (file.exists())
                                    file.delete();
                                file.createNewFile();
                                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                                fileArg.getValue().getAsJsonArray().forEach(jsonElement -> {
                                    try {
                                        bufferedWriter.write(jsonElement.getAsString());
                                        bufferedWriter.newLine();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                                bufferedWriter.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        });
                        break;
                    }

                    // Executables

                    case "executeConsoleCommand": {
                        Bukkit.getScheduler().runTask(BungeeSK.getInstance(),
                                () -> Bukkit.dispatchCommand(BungeeSK.getInstance().getServer().getConsoleSender(), args.get("command").getAsString()));
                        break;
                    }
                    case "broadcastMessage": {
                        Bukkit.getScheduler().runTask(BungeeSK.getInstance(),
                                () -> Bukkit.broadcastMessage(args.get("message").getAsString()));
                        break;
                    }

                    // Futures

                    case "futureResponse": {
                        final UUID uuid = UUID.fromString(args.get("uuid").getAsString());
                        this.completeFuture(uuid, args.get("response").getAsJsonObject());
                        break;
                    }

                    // Events

                    case "eventBungeePlayerConnect": {
                        final Event event = new BungeePlayerJoinEvent(new BungeePlayer(args.get("name").getAsString(), args.get("uniqueId").getAsString()));
                        Bukkit.getScheduler().runTask(BungeeSK.getInstance(), () -> Bukkit.getPluginManager().callEvent(event));
                        break;
                    }
                    case "eventBungeePlayerDisconnect": {
                        final Event event = new BungeePlayerLeaveEvent(new BungeePlayer(args.get("name").getAsString(), args.get("uniqueId").getAsString()));
                        Bukkit.getScheduler().runTask(BungeeSK.getInstance(), () -> Bukkit.getPluginManager().callEvent(event));
                        break;
                    }
                    case "eventBungeePlayerServerSwitch": {
                        final BungeePlayer player = new BungeePlayer(args.get("player").getAsJsonObject().get("name").getAsString(),
                                args.get("player").getAsJsonObject().get("uniqueId").getAsString());
                        final JsonObject serverObj = args.get("server").getAsJsonObject();
                        final BungeeServer server = new BungeeServer(serverObj.get("address").getAsString(),
                                serverObj.get("port").getAsInt(),
                                serverObj.get("name").getAsString(),
                                serverObj.get("motd").getAsString());

                        final Event event = new ServerSwitchEvent(player, server);
                        Bukkit.getScheduler().runTask(BungeeSK.getInstance(), () -> Bukkit.getPluginManager().callEvent(event));
                        break;
                    }
                }
            }
        } catch (IOException e) {
            this.disconnect();
        }

    }

    public void disconnect() {
        try {
            this.write(false, "connectionInformation", "status", "disconnect");
            Bukkit.getScheduler().runTask(BungeeSK.getInstance(), () -> Bukkit.getPluginManager().callEvent(new ClientDisconnectEvent()));
        } catch (final Exception ignored) {
        }
        this.forceDisconnect();
    }

    public void forceDisconnect() {
        try {
            if (!this.socket.isClosed()) {
                this.reader.close();
                this.writer.close();
                this.readThread.interrupt();
                this.socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return this.socket != null && this.socket.isConnected() && !this.socket.isClosed();
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
            final String toSend = this.encryption.encrypt(gson.toJson(map), this.password);
            this.writer.println(toSend);
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

    public Map<UUID, CompletableFuture<JsonObject>> getToComplete() {
        return this.toComplete;
    }

    public void completeFuture(final UUID uuid, final JsonObject toComplete) {
        CompletableFuture<JsonObject> future = this.toComplete.get(uuid);
        if (future != null) {
            future.complete(toComplete);
            this.toComplete.remove(uuid);
        }
    }

    public JsonObject future(final String action, final String... args) {
        final UUID randomUUID = UUID.randomUUID(); // Using a random UUID here to prevent from mixing between 2 actions at the same time
        CompletableFuture<JsonObject> future = new CompletableFuture<>();
        this.toComplete.put(randomUUID, future);

        Map<String, String> map = new HashMap<>();

        map.put("action", action);
        map.put("uuid", randomUUID.toString());
        if (args != null)
            for (int i = 0; i < args.length; i = i + 2)
                map.put(args[i], args[i + 1]);

        this.writeRaw(true, "futureGet", map);

        JsonObject jsonObject;
        try {
            jsonObject = future.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            BungeeSK.getInstance().getLogger().log(Level.WARNING, "An error occurred during the get of something !");
            return null;
        }
        return jsonObject;
    }

    public String getAddress() {
        return this.address;
    }

    public int getPort() {
        return this.socket.getPort();
    }

    public static Gson getGson() {
        return gson;
    }
}
