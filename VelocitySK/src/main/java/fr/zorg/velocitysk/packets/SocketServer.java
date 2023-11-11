package fr.zorg.velocitysk.packets;

import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.*;
import fr.zorg.bungeesk.common.utils.EncryptionUtils;
import fr.zorg.bungeesk.common.utils.PacketUtils;
import fr.zorg.velocitysk.BungeeSK;
import fr.zorg.velocitysk.commands.BungeeSKCommand;
import fr.zorg.velocitysk.utils.BungeeConfig;
import fr.zorg.velocitysk.utils.Debug;
import fr.zorg.velocitysk.utils.FutureUtils;
import fr.zorg.velocitysk.utils.VelocityUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.Duration;
import java.util.UUID;

public class SocketServer {

    private final Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private final Thread readThread;
    private boolean authenticated;
    private UUID challengeUUID;
    private boolean waitingForAuth = false;
    private boolean encrypting;
    private int minecraftPort;
    private long ping;

    public SocketServer(Socket socket) {
        this.socket = socket;
        this.encrypting = false;
        this.authenticated = false;
        this.readThread = new Thread(this::read);
        BungeeSK.getServer().getScheduler().buildTask(BungeeSK.getInstance(), () -> {
            try {
                Thread.sleep(5000); // wait 5 seconds
                if ((!this.authenticated) || this.minecraftPort == 0) {
                    this.disconnect();
                }
            } catch (InterruptedException ex) {
                Debug.throwEx(ex);
                this.disconnect();
            }
        }).schedule();
        if (!this.socket.isClosed()) {
            try {
                this.writer = new ObjectOutputStream(socket.getOutputStream());
                this.reader = new ObjectInputStream(socket.getInputStream());
                this.readThread.start();
                Debug.log("ClientSocket started on " + socket.getInetAddress().getHostAddress());

                this.sendPacket(new HandshakePacket(0));
            } catch (IOException ex) {
                Debug.throwEx(ex);
                this.disconnect();
            }
        } else {
            this.disconnect();
        }
    }

    public void read() {
        while (this.isConnected()) {
            try {
                Object dataRaw = this.reader.readObject();
                if (this.encrypting) {
                    dataRaw = EncryptionUtils.decryptPacket((byte[]) dataRaw, ((String) BungeeConfig.PASSWORD.get()).toCharArray());
                    if (dataRaw == null)
                        return;

                    dataRaw = PacketUtils.packetFromBytes((byte[]) dataRaw);
                    if (dataRaw == null)
                        return;
                }
                if (!(dataRaw instanceof BungeeSKPacket)) {
                    this.disconnect();
                    return;
                }
                final BungeeSKPacket packet = (BungeeSKPacket) dataRaw;
                Debug.log("Received packet " + packet.getClass().getSimpleName() + " from " + socket.getInetAddress().getHostAddress() + ":" + this.minecraftPort);
                this.handleReceiveListeners(packet);
            } catch (IOException | ClassNotFoundException ex) {
                Debug.throwEx(ex);
                this.disconnect();
            }
        }
    }

    public void sendPacket(BungeeSKPacket packet) {
        if (this.writer == null || this.reader == null)
            return;

        if (!this.isConnected())
            return;

        this.handleSendListeners(packet);
        Object toSend = packet;
        if (this.encrypting && this.authenticated) {
            Debug.log("Encrypting packet " + packet.getClass().getSimpleName());
            toSend = EncryptionUtils.encryptPacket(PacketUtils.packetToBytes(packet), ((String) BungeeConfig.PASSWORD.get()).toCharArray());
            if (toSend == null) {
                Debug.log("Failed to encrypt packet");
                return;
            }
        }
        Debug.log("Sending packet " + packet.getClass().getSimpleName() + " to " + socket.getInetAddress().getHostAddress() + ":" + this.minecraftPort);
        try {
            this.writer.writeObject(toSend);
            Debug.log("Packet " + packet.getClass().getSimpleName() + " sent to " + socket.getInetAddress().getHostAddress() + ":" + this.minecraftPort);
        } catch (IOException ex) {
            Debug.throwEx(ex);
            Debug.log("Error while sending packet " + packet.getClass().getSimpleName() + " to " + socket.getInetAddress().getHostAddress() + ":" + this.minecraftPort);
        }
    }

    public void disconnect() {
        if (this.isConnected()) {
            Debug.log("Disconnecting client with IP " + socket.getInetAddress().getHostAddress() + ":" + this.minecraftPort);
            final BungeeServer server = VelocityUtils.getServerFromSocket(this);
            PacketServer.getClientSockets().remove(this);
            try {
                if (this.reader != null)
                    this.reader.close();
                if (this.writer != null)
                    this.writer.close();
                if (this.readThread != null)
                    this.readThread.interrupt();
                this.socket.close();
                Debug.log("Client with IP " + socket.getInetAddress().getHostAddress() + ":" + this.minecraftPort + " disconnected");
                String message = BungeeSKCommand.PREFIX + "§7Server disconnected: §c" + this.socket.getInetAddress().getHostAddress() + ":" + this.getMinecraftPort();
                if (server != null) {
                    PacketServer.broadcastPacket(new BungeeServerStopPacket(server));
                    message += " §f(§3" + server.getName() + "§f)";
                }
                if (BungeeConfig.MESSAGES.get()) {
                    BungeeSK.getLogger().atInfo().log(message);
                }
            } catch (IOException ex) {
                Debug.throwEx(ex);
                Debug.log("An error occurred while disconnecting the client with IP " + socket.getInetAddress().getHostAddress());
            }
        }
    }

    public Socket getSocket() {
        return this.socket;
    }

    private void handleSendListeners(BungeeSKPacket packet) {
        BungeeSK.getApi().getListeners().forEach(listener -> {
            try {
                listener.getClass().getMethod("onSend", SocketServer.class, BungeeSKPacket.class);
                listener.onSend(this, packet);
            } catch (Exception ignored) {
            }
        });
    }

    private void handleReceiveListeners(BungeeSKPacket packet) {
        BungeeSK.getApi().getListeners().forEach(listener -> {
            try {
                listener.getClass().getMethod("onReceive", SocketServer.class, BungeeSKPacket.class);
                listener.onReceive(this, packet);
            } catch (Exception ignored) {
            }
        });
    }

    public UUID initChallenge() {
        this.challengeUUID = UUID.randomUUID();
        this.waitingForAuth = true;
        BungeeSK.runAsync(() -> {
            try {
                Thread.sleep(3000); //Timeout of 3 seconds to authenticate
                this.waitingForAuth = false;
                if (!this.authenticated)
                    this.disconnect();
            } catch (InterruptedException ignored) {
            }
        });
        return this.challengeUUID;
    }

    public void startKeepAlive() {
        BungeeSK
                .getServer()
                .getScheduler()
                .buildTask(BungeeSK.getInstance(), this::sendKeepAlive)
                .delay(Duration.ofSeconds(1))
                .repeat(Duration.ofSeconds(5))
                .schedule();
    }

    public void sendKeepAlive() {
        final long baseTimestamp = System.currentTimeMillis();
        final Object response = FutureUtils.generateFuture(this, new KeepAlivePacket());
        if (response == null) {
            this.disconnect();
            return;
        }
        if (!(response instanceof Long)) {
            this.disconnect();
            return;
        }
        final long responseTimestamp = (long) response;
        this.ping = responseTimestamp - baseTimestamp;
    }

    public void completeChallenge(UUID uuid) {
        if (this.waitingForAuth && this.challengeUUID.compareTo(uuid) == 0 && !this.authenticated) {
            this.sendPacket(new AuthCompletePacket(BungeeConfig.ENCRYPT.get()));
            Debug.log("Client with IP " + socket.getInetAddress().getHostAddress() + ":" + this.minecraftPort + " authenticated");
            this.startKeepAlive();
        } else {
            this.disconnect();
        }
    }

    public boolean isConnected() {
        return this.socket != null && this.socket.isConnected() && !this.socket.isClosed();
    }

    public boolean isAuthenticated() {
        return this.authenticated;
    }

    public boolean isEncrypting() {
        return this.encrypting;
    }

    public void authenticate() {
        if (!this.authenticated) {
            this.authenticated = true;
            this.encrypting = BungeeConfig.ENCRYPT.get();
        }
    }

    public void setMinecraftPort(int minecraftPort) {
        this.minecraftPort = minecraftPort;
    }

    public int getMinecraftPort() {
        return this.minecraftPort;
    }

    public long getPing() {
        return this.ping;
    }

}