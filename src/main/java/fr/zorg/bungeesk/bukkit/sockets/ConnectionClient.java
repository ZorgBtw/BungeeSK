package fr.zorg.bungeesk.bukkit.sockets;

import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.BungeePlayerJoinEvent;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.BungeePlayerLeaveEvent;
import fr.zorg.bungeesk.bukkit.updater.Commands;
import fr.zorg.bungeesk.bukkit.updater.Updater;
import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;
import fr.zorg.bungeesk.common.encryption.AESEncryption;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CompletableFuture;
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
            try {
                instance.finalize();
                instance = null;
            } catch (final Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        try {
            final Socket socket = new Socket(settings.getAddress(), settings.getPort().intValue());
            instance = new ConnectionClient(socket, settings.getName(), settings.getPassword());
        } catch (Exception e) {
            BungeeSK.getInstance().getLogger().log(Level.SEVERE, "BungeeSK couldn't find server with this address/port !");
        }
        return instance;
    }

    private final Socket socket;
    private final Thread readThread;
    private final BufferedReader reader;
    private final PrintWriter writer;

    private final Map<String, LinkedList<CompletableFuture<String>>> toComplete;

    private final AESEncryption encryption;

    private ConnectionClient(final Socket socket, final String name, final String password) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.toComplete = new HashMap<>();
        this.encryption = new AESEncryption(password);
        this.write("name=" + name + "µpassword=" + password);
        this.readThread = new Thread(this::read);
        this.readThread.setDaemon(true);
        this.readThread.start();
    }

    public void read() {
        try {
            while (this.isConnected()) {

                final String rawData = this.reader.readLine();
                if (rawData == null) {
                    this.forceDisconnect();
                    break;
                }

                final String data = this.encryption.decrypt(rawData);
                final String[] separateDatas = data.split("µ");

                final String header = separateDatas[0];
                final List<String> received = new ArrayList<>(Arrays.asList(separateDatas).subList(1, separateDatas.length));
                switch (header.toUpperCase()) {
                    case "ALREADY_CONNECTED": {
                        BungeeSK.getInstance().getLogger().log(Level.WARNING, "§6Trying to connect to §c"
                                + this.socket.getInetAddress().getHostAddress()
                                + " §6and returned failure: §cServer already connected under this name !");
                        this.disconnect();
                        break;
                    }
                    case "WRONG_PASSWORD": {
                        BungeeSK.getInstance().getLogger().log(Level.WARNING, "§6Trying to connect to §c"
                                + this.socket.getInetAddress().getHostAddress()
                                + " §6and returned failure: §cWrong password !");
                        this.disconnect();
                        break;
                    }
                    case "DISCONNECT": {
                        this.disconnect();
                        break;
                    }
                    case "SEND_SKRIPTS": {
                        final String[] flux = received.get(0).split(", ");
                        final File folder = new File("plugins/Skript/scripts/BungeeSK");
                        if (!folder.exists())
                            folder.mkdirs();
                        File file = null;
                        FileWriter fileWriter = null;
                        PrintWriter writer = null;
                        for (final String line : flux) {
                            try {
                                if (line.equals("END_SKRIPTS"))
                                    break;
                                if (line.startsWith("newFile:")) {
                                    file = new File(folder, line.substring(8));
                                    fileWriter = new FileWriter(file);
                                    file.createNewFile();
                                    fileWriter.write("");
                                    writer = new PrintWriter(fileWriter, true);
                                    continue;
                                }
                                if (file == null || writer == null)
                                    continue;
                                if (line.equalsIgnoreCase("endFile")) {
                                    writer.close();
                                    fileWriter.close();
                                    file = null;
                                    fileWriter = null;
                                    writer = null;
                                    continue;
                                }
                                writer.println(line);

                            } catch (IOException ignored) {
                            }
                        }
                        break;
                    }
                    case "CONSOLECOMMAND": {
                        Updater.get().getByClass(Commands.class).addCommandToSend(Bukkit.getConsoleSender(), separateDatas[1]);
                        break;
                    }
                    case "LOGINEVENT": {
                        String player = separateDatas[1];
                        String uuid = separateDatas[2];
                        Event event = new BungeePlayerJoinEvent(new BungeePlayer(player, uuid));
                        BungeeSK.getInstance().getServer().getPluginManager().callEvent(event);
                        break;
                    }
                    case "LEAVEEVENT": {
                        String player = separateDatas[1];
                        String uuid = separateDatas[2];
                        Event event = new BungeePlayerLeaveEvent(new BungeePlayer(player, uuid));
                        BungeeSK.getInstance().getServer().getPluginManager().callEvent(event);
                        break;
                    }
                    case "ALLBUNGEEPLAYERS": {
                        final LinkedList<CompletableFuture<String>> completableFutures = this.toComplete.get("ALLBUNGEEPLAYERS");
                        if (completableFutures != null && completableFutures.size() > 0) {
                            CompletableFuture<String> complete = completableFutures.poll();
                            StringBuilder builder = new StringBuilder();
                            builder.append(separateDatas[1]).append("^");
                            complete.complete(builder.toString());
                            if (completableFutures.size() == 0)
                                this.toComplete.remove("ALLBUNGEEPLAYERS", completableFutures);
                        }
                        break;
                    }
                    case "PLAYERSERVER": {
                        String[] dataArray = separateDatas[1].split("\\^");
                        String playerData = dataArray[0];
                        String server = dataArray[1];
                        final LinkedList<CompletableFuture<String>> completableFutures = this.toComplete.get("EXPRBUNGEEPLAYERSERVERµ" + playerData);
                        if (completableFutures != null && completableFutures.size() > 0) {
                            final CompletableFuture<String> complete = completableFutures.poll();
                            complete.complete(server);
                            if (completableFutures.size() == 0)
                                this.toComplete.remove("EXPRBUNGEEPLAYERSERVERµ" + playerData, completableFutures);
                        }
                        break;
                    }
                    case "ISCONNECTED": {
                        String[] dataArray = separateDatas[1].split("\\^");
                        String playerData = dataArray[0];
                        String state = dataArray[1];
                        final LinkedList<CompletableFuture<String>> completableFutures = this.toComplete.get("ISCONNECTEDµ" + playerData);
                        if (completableFutures != null && completableFutures.size() > 0) {
                            final CompletableFuture<String> complete = completableFutures.poll();
                            complete.complete(state);
                            if (completableFutures.size() == 0)
                                this.toComplete.remove("ISCONNECTEDµ" + playerData, completableFutures);
                        }
                        break;
                    }
                    case "GETPLAYER": {
                        String player = separateDatas[1].split("\\$")[0];
                        final LinkedList<CompletableFuture<String>> completableFutures = this.toComplete.get("GETPLAYERµ" + player);
                        if (completableFutures != null && completableFutures.size() > 0) {
                            final CompletableFuture<String> complete = completableFutures.poll();
                            complete.complete(separateDatas[1]);
                            if (completableFutures.size() == 0)
                                this.toComplete.remove("GETPLAYERµ" + player, completableFutures);
                        }
                        break;
                    }
                }
            }
        } catch (IOException e) {
            this.forceDisconnect();
        }
    }

    public void disconnect() {
        try {
            this.writer.println("DISCONNECT");
        } catch (final Exception ignored) {
        }
        this.forceDisconnect();
    }

    public void forceDisconnect() {
        try {
            if (!this.socket.isClosed()) {
                this.socket.close();
                this.reader.close();
                this.writer.close();
                this.readThread.interrupt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return this.socket != null && this.socket.isConnected() && !this.socket.isClosed();
    }

    public void write(final String message) {
        this.writer.println(this.encryption.encrypt(message));
    }

    public Map<String, LinkedList<CompletableFuture<String>>> getToComplete() {
        return this.toComplete;
    }

    public String getAddress() {
        return this.socket.getInetAddress().getHostAddress();
    }

    public int getPort() {
        return this.socket.getPort();
    }

}
