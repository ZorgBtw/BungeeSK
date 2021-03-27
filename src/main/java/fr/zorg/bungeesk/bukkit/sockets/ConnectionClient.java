package fr.zorg.bungeesk.bukkit.sockets;

import fr.zorg.bungeesk.bukkit.BungeeSK;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
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
            BungeeSK.getInstance().getLogger().log(Level.INFO, e.toString());
            e.printStackTrace();
        }
        return instance;
    }

    // Big thank to @BakaAless (https://github.com/BakaAless) for this code in its entirety !

    private final Socket socket;
    private final Thread readThread;
    private final BufferedReader reader;
    private final PrintWriter writer;

    private ConnectionClient(final Socket socket, final String name, final String password) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.writer.println("name=" + name + "µpassword=" + password);
        this.readThread = new Thread(this::read);
        this.readThread.setDaemon(true);
        this.readThread.start();
    }

    public void read() {
        try {
            while (this.isConnected()) {

                final String data = this.reader.readLine();
                if (data == null) {
                    this.forceDisconnect();
                    break;
                }

                switch (data.toUpperCase()) {
                    case "BB": {
                        BungeeSK.getInstance().getLogger().log(Level.INFO, "bb recieved");
                    }
                    case "DISCONNECT": {
                        this.disconnect();
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
        } catch (final Exception ignored) { }
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
        this.writer.println(message);
    }

    public String getAddress() {
        return this.socket.getInetAddress().getHostAddress();
    }

    public int getPort() {
        return this.socket.getPort();
    }

}
