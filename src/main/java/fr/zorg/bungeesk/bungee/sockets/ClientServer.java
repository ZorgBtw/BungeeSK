package fr.zorg.bungeesk.bungee.sockets;

import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.storage.BungeeConfig;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
                        if (!data.contains("µ")) continue;
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
                            BungeeSK.getInstance().getLogger().log(Level.INFO, "§6New server trying to connect under name §c"
                                    + this.name
                                    + " §6with address §c"
                                    + this.socket.getInetAddress().getHostAddress()
                                    + "§6with §cname already in system !");
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
                final String args = separateDatas[1];

                switch (header.toUpperCase()) {
                    case "DISCONNECT": {
                        this.forceDisconnect();
                        break reader;
                    }

                    case "RETRIEVE_SKRIPTS": {
                        final List<String> received = new ArrayList<>(Arrays.asList(separateDatas).subList(1, separateDatas.length));
                        this.sendFiles();
                        break;
                    }

                    case "EFFEXECUTECOMMAND": {
                        if (args.equalsIgnoreCase("bungee")) {
                            BungeeSK.getInstance().getProxy().getPluginManager().dispatchCommand(BungeeSK.getInstance().getProxy().getConsole(), separateDatas[2]);
                        } else if (args.equalsIgnoreCase("all")) {
                            server.writeAll("CONSOLECOMMANDµ" + separateDatas[2]);
                        } else {
                            server.getClient(args).get().write("CONSOLECOMMANDµ" + separateDatas[2]);
                        }
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
            if (this.identified) {
                if (this.name != null)
                    this.writer.println("ALREADY_CONNECTED");
                else
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
                BungeeSK.getInstance().getServer().removeClient(this.getName());
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

