package fr.zorg.bungeesk.bungee.sockets;

import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.storage.BungeeConfig;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level;

public final class ClientServer {

    private final Socket socket;
    private String name;
    private final Thread readThread;
    private final BufferedReader reader;
    private final PrintWriter writer;

    private boolean identified;

    public ClientServer(final Socket socket) throws IOException {
        this.socket = socket;
        this.identified = false;
        this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.writer = new PrintWriter(this.socket.getOutputStream(), true);

        this.readThread = new Thread(this::read);
        this.readThread.setDaemon(true);
        this.readThread.start();
    }

    public void read() {
        try {
            reader:
            while (this.isConnected()) {
                String rawData = reader.readLine();
                if (rawData == null) continue;
                final Server server = BungeeSK.getInstance().getServer();

                final String data = BungeeSK.getInstance().getServer().encryption.decrypt(rawData);
                if (this.name == null) {
                    if (!server.isClient(socket)) {
                        if (!data.contains("µ")) {
                            this.disconnect();
                            break;
                        }
                        String[] datas = data.split("µ");
                        if (datas.length != 2) {
                            this.disconnect();
                            break;
                        }
                        if (datas[0].startsWith("name="))
                            this.name = datas[0].substring(5);
                        else {
                            this.disconnect();
                            break;
                        }
                        if (server.getClient(this.name).isPresent()) {
                            this.disconnect();
                            break;
                        }
                        String password;
                        if (datas[1].startsWith("password="))
                            password = datas[1].substring(9);
                        else {
                            this.disconnect();
                            break;
                        }
                        if (!server.isPassword(password)) {
                            this.disconnect();
                            break;
                        }

                        this.identified = true;
                        server.addClient(this);
                        BungeeSK.getInstance().getLogger().log(Level.INFO, "§6New server connected under name §a"
                                + this.name
                                + " §6with adress §a"
                                + this.socket.getInetAddress().getHostAddress());

                        if (BungeeConfig.get().isSendFilesAutoEnabled())
                            this.sendFiles();

                    } else {
                        this.disconnect();
                        break;
                    }
                    continue;
                }

                final String[] separateDatas = data.split("µ");

                final String header = separateDatas[0];
                String args = null;
                if (separateDatas.length > 1) {
                    args = separateDatas[1];
                }

                switch (header.toUpperCase()) {
                    case "DISCONNECT": {
                        this.forceDisconnect();
                        break reader;
                    }

                    case "RETRIEVE_SKRIPTS": {
                        this.sendFiles();
                        break;
                    }

                    case "EFFEXECUTECOMMAND": {
                        if (args.equalsIgnoreCase("bungee"))
                            BungeeSK.getInstance().getProxy().getPluginManager().dispatchCommand(BungeeSK.getInstance().getProxy().getConsole(), separateDatas[2]);
                        else if (args.equalsIgnoreCase("all"))
                            server.writeAll("CONSOLECOMMANDµ" + separateDatas[2]);
                        else
                            server.getClient(args).get().write("CONSOLECOMMANDµ" + separateDatas[2]);
                        break;
                    }

                    case "EXPRALLBUNGEEPLAYERS": {
                        if (BungeeSK.getInstance().getProxy().getPlayers().size() < 1) {
                            this.write("ALLBUNGEEPLAYERSµNONE");
                            break;
                        }
                        StringBuilder builder = new StringBuilder();
                        builder.append("ALLBUNGEEPLAYERSµ");
                        Object lastPlayer = BungeeSK.getInstance().getProxy().getPlayers().toArray()[BungeeSK.getInstance().getProxy().getPlayers().size() - 1];
                        for (final ProxiedPlayer player : BungeeSK.getInstance().getProxy().getPlayers()) {
                            builder.append(player.getName()).append("$").append(player.getUniqueId().toString());
                            if (!player.equals(lastPlayer)) builder.append("^");
                        }
                        this.write(builder.toString());
                        break;
                    }

                    case "EXPRBUNGEEPLAYERSERVER": {
                        net.md_5.bungee.api.connection.Server playerServer = BungeeSK.getInstance().getProxy().getPlayer(UUID.fromString(args.split("\\$")[1])).getServer();
                        if (playerServer == null) {
                            this.write("PLAYERSERVERµ" + args + "^NONE");
                            break;
                        }
                        this.write("PLAYERSERVERµ" + args + "^" + playerServer.getInfo().getName());
                        break;
                    }
                }

            }
        } catch (IOException e) {
            this.forceDisconnect();
        }

    }

    private void sendFiles() {
        final List<String> result = BungeeSK.getInstance().filesToString();
        result.add("END_SKRIPTS");
        final String toString = Arrays.toString(result.toArray(new String[0])).substring(1);
        this.write("SEND_SKRIPTSµ" + toString.substring(0, toString.length() - 1));
    }

    public void disconnect() {
        try {
            if (!this.identified) {
                if (this.name != null) {
                    this.writer.println("ALREADY_CONNECTED");
                } else
                    this.writer.println("WRONG_PASSWORD");
            } else
                this.write("DISCONNECT");
        } catch (final Exception ignored) {
        }
        this.forceDisconnect();
    }

    public void forceDisconnect() {
        if (!this.socket.isClosed()) {
            try {
                this.socket.close();
                this.reader.close();
                this.writer.close();
                this.readThread.interrupt();
                BungeeSK.getInstance().getServer().removeClient(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket() {
        return this.socket;
    }

    public void write(final String message) {
        this.writer.println(BungeeSK.getInstance().getServer().encryption.encrypt(message));
    }

    @Nullable
    public String getName() {
        return this.name;
    }

    public boolean isConnected() {
        return this.socket != null && this.socket.isConnected() && !this.socket.isClosed();
    }

}

