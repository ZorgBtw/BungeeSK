package fr.zorg.bungeesk.bungee.sockets;

import fr.zorg.bungeesk.bungee.BungeeSK;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;

public final class ClientServer {

    //Big thank to @BakaAless (https://github.com/BakaAless) for this code in its entirety !
    private final Socket socket;
    private String name;
    private final Thread readThread;
    private final BufferedReader reader;
    private final PrintWriter writer;

    public ClientServer(final Socket socket) throws IOException {
        this.socket = socket;
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
                        server.addClient(this);


                        BungeeSK.getInstance().getLogger().log(Level.INFO, "§3New server connected under name §a"
                                + this.name
                                + " §3with adress §a"
                                + this.socket.getInetAddress().getHostAddress()
                                + ":"
                                + this.socket.getPort() );

                    } else {
                        this.disconnect();
                        break;
                    }
                    continue;
                }

                switch (data.toUpperCase()) {
                    case "AA":
                        BungeeSK.getInstance().getLogger().log(Level.INFO, "aa received, bb sent");
                        write("bb");

                    case "DISCONNECT":
                        this.forceDisconnect();
                        break reader;

                }

            }
        } catch (IOException e) {
            this.forceDisconnect();
        }


    }

    public void disconnect() {
        try {
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

