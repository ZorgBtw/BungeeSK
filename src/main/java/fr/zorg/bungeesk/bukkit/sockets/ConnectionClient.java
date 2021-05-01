package fr.zorg.bungeesk.bukkit.sockets;

import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.*;
import fr.zorg.bungeesk.bukkit.updater.Commands;
import fr.zorg.bungeesk.bukkit.updater.Updater;
import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;
import fr.zorg.bungeesk.common.encryption.AESEncryption;
import fr.zorg.bungeesk.common.utils.Utils;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
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
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.toComplete = new HashMap<>();
        this.encryption = new AESEncryption(password, BungeeSK.getInstance().getLogger());
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
                    this.disconnect();
                    break;
                }

                final String data = this.encryption.decrypt(Utils.getMessage(rawData));
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
                    case "CONNECTED_SUCCESSFULLY": {
                        final Event event = new ClientConnectEvent();
                        Bukkit.getScheduler().runTask(BungeeSK.getInstance(), () -> Bukkit.getPluginManager().callEvent(event));
                        break;
                    }
                    case "SEND_SKRIPTS": {
                        final String[] flux = received.get(0).split("™");
                        final File folder = new File("plugins/Skript/scripts/BungeeSK");
                        if (!folder.exists())
                            folder.mkdirs();
                        String file = null;
                        byte[] content;
                        File skript;
                        for (final String line : flux) {
                            if (line.equals("endFile")) {
                                file = null;
                            } else if (file == null && line.startsWith("newFile:")) {
                                file = line.substring(8);
                            } else if (file != null) {
                                try {
                                    content = Utils.fromBase64(line);
                                    skript = new File(folder, file);
                                    if (skript.exists())
                                        skript.delete();
                                    skript.createNewFile();
                                    FileUtils.writeByteArrayToFile(skript, content);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (line.equals("END_SKRIPTS"))
                                break;
                        }
                        break;
                    }
                    case "CONSOLECOMMAND": {
                        Updater.get().getByClass(Commands.class).addCommandToSend(Bukkit.getConsoleSender(), separateDatas[1]);
                        break;
                    }
                    case "LOGINEVENT": {
                        final Event event = new BungeePlayerJoinEvent(new BungeePlayer(separateDatas[1], separateDatas[2]));
                        Bukkit.getScheduler().runTask(BungeeSK.getInstance(), () -> Bukkit.getPluginManager().callEvent(event));
                        break;
                    }
                    case "LEAVEEVENT": {
                        final Event event = new BungeePlayerLeaveEvent(new BungeePlayer(separateDatas[1], separateDatas[2]));
                        Bukkit.getScheduler().runTask(BungeeSK.getInstance(), () -> Bukkit.getPluginManager().callEvent(event));
                        break;
                    }
                    case "ALLBUNGEEPLAYERS": {
                        this.putFuture("ALLBUNGEEPLAYERSµ", separateDatas[1] + "^");
                        break;
                    }
                    case "PLAYERSERVER": {
                        final String[] dataArray = separateDatas[1].split("\\^");
                        this.putFuture("PLAYERSERVERµ" + dataArray[0], dataArray[1]);
                        break;
                    }
                    case "GETPLAYER": {
                        final String player = separateDatas[1].split("\\$")[0];
                        this.putFuture("GETPLAYERµ" + player, separateDatas[1]);
                        break;
                    }

                    case "ISCONNECTED": {
                        final String[] dataArray = separateDatas[1].split("\\^");
                        this.putFuture("ISCONNECTEDµ" + dataArray[0], dataArray[1]);
                        break;
                    }

                    case "SERVERSWITCHEVENT": {
                        final String[] dataArray = separateDatas[1].split("\\^");
                        final BungeePlayer bungeePlayer = new BungeePlayer(dataArray[0].split("\\$")[0], dataArray[0].split("\\$")[1]);
                        final Event event = new ServerSwitchEvent(bungeePlayer, dataArray[1]);
                        Bukkit.getScheduler().runTask(BungeeSK.getInstance(), () -> Bukkit.getPluginManager().callEvent(event));
                        break;
                    }
                    case "ALLBUNGEESERVERS": {
                        this.putFuture("ALLBUNGEESERVERSµ", separateDatas[1]);
                        break;
                    }
                    case "CLIENTREALNAME": {
                        this.putFuture("CLIENTREALNAMEµ" + separateDatas[1].split("\\^")[0], separateDatas[1].split("\\^")[1]);
                        break;
                    }
                    case "ALLBUNGEEPLAYERSONSERVER": {
                        final String server = separateDatas[1].split("\\^")[0];
                        this.putFuture("ALLBUNGEEPLAYERSONSERVERµ" + server, separateDatas[1]);
                        break;
                    }
                    case "BROADCAST": {
                        Bukkit.getScheduler().runTask(BungeeSK.getInstance(), () -> Bukkit.broadcastMessage(separateDatas[1]));
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
            this.writer.println(Arrays.toString("DISCONNECT".getBytes(StandardCharsets.UTF_8)));
            Bukkit.getScheduler().runTask(BungeeSK.getInstance(), () -> Bukkit.getPluginManager().callEvent(new ClientDisconnectEvent()));
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
        this.writer.println(Arrays.toString(this.encryption.encrypt(message).getBytes(StandardCharsets.UTF_8)));
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
        this.write(value);
        String result;
        try {
            result = future.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return null;
        }
        return result;
    }

    public String getAddress() {
        return this.socket.getInetAddress().getHostAddress();
    }

    public int getPort() {
        return this.socket.getPort();
    }
}
