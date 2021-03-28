package fr.zorg.bungeesk.bukkit.sockets;

import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.common.encryption.AESEncryption;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    private final Socket socket;
    private final Thread readThread;
    private final BufferedReader reader;
    private final PrintWriter writer;

    private final AESEncryption encryption;

    private ConnectionClient(final Socket socket, final String name, final String password) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
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
                    case "DISCONNECT": {
                        this.disconnect();
                        break;
                    } case "SEND_SKRIPTS": {
                        final String[] flux = received.get(0).split(", ");
                        final File folder = new File("plugins/Skript/scripts/BungeeSK");
                        if (!folder.exists())
                            folder.mkdirs();
                        File file = null;
                        FileWriter fileWriter = null;
                        PrintWriter writer = null;
                        System.out.println(flux);
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
        this.writer.println(this.encryption.encrypt(message));
    }

    public String getAddress() {
        return this.socket.getInetAddress().getHostAddress();
    }

    public int getPort() {
        return this.socket.getPort();
    }

}
