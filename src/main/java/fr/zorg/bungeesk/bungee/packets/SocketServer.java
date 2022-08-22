package fr.zorg.bungeesk.bungee.packets;

import fr.zorg.bungeesk.bungee.BungeeConfig;
import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.Debug;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.AuthCompletePacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.BungeeServerStopPacket;
import fr.zorg.bungeesk.common.packets.HandshakePacket;
import fr.zorg.bungeesk.common.utils.EncryptionUtils;
import fr.zorg.bungeesk.common.utils.PacketUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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

    public SocketServer(Socket socket) {
        this.socket = socket;
        this.encrypting = false;
        this.authenticated = false;
        this.readThread = new Thread(this::read);
        if (!this.socket.isClosed()) {
            try {
                this.writer = new ObjectOutputStream(socket.getOutputStream());
                this.reader = new ObjectInputStream(socket.getInputStream());
                this.readThread.start();
                Debug.log("ClientSocket started on " + socket.getInetAddress().getHostAddress());

                this.sendPacket(new HandshakePacket(0));
            } catch (IOException ex) {
                this.disconnect();
                ex.printStackTrace();
            }
        }
    }

    public void read() {
        while (this.isConnected()) {
            try {
                Object data = reader.readObject();
                if (this.encrypting) {
                    data = EncryptionUtils.decryptPacket((byte[]) data, ((String) BungeeConfig.PASSWORD.get()).toCharArray());
                    if (data == null)
                        continue;

                    data = PacketUtils.packetFromBytes((byte[]) data);
                    if (data == null)
                        continue;
                }
                if (!(data instanceof BungeeSKPacket)) {
                    this.disconnect();
                    return;
                }
                final BungeeSKPacket packet = (BungeeSKPacket) data;
                Debug.log("Received packet " + packet.getClass().getSimpleName() + " from " + socket.getInetAddress().getHostAddress() + ":" + this.minecraftPort);
                this.handleReceiveListeners(packet);
            } catch (IOException | ClassNotFoundException ex) {
                this.disconnect();
            }
        }
    }

    public void sendPacket(BungeeSKPacket packet) {
        if (this.writer == null || this.reader == null)
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
        } catch (IOException ignored) {
            Debug.log("Error while sending packet " + packet.getClass().getSimpleName() + " to " + socket.getInetAddress().getHostAddress() + ":" + this.minecraftPort);
        }
    }

    public void disconnect() {
        if (this.isConnected()) {
            Debug.log("Disconnecting client with IP " + socket.getInetAddress().getHostAddress() + ":" + this.minecraftPort);
            final BungeeServer server = BungeeUtils.getServerFromSocket(this);
            try {
                this.reader.close();
                this.writer.close();
                this.socket.close();
                this.readThread.interrupt();
                Debug.log("Client with IP " + socket.getInetAddress().getHostAddress() + ":" + this.minecraftPort + " disconnected");
                PacketServer.getClientSockets().remove(this);
                if (server != null)
                    PacketServer.broadcastPacket(new BungeeServerStopPacket(server));
            } catch (IOException ignored) {
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

    public void completeChallenge(UUID uuid) {
        if (this.waitingForAuth && this.challengeUUID.compareTo(uuid) == 0 && !this.authenticated) {
            this.sendPacket(new AuthCompletePacket(BungeeConfig.ENCRYPT.get()));
            Debug.log("Client with IP " + socket.getInetAddress().getHostAddress() + ":" + this.minecraftPort + " authenticated");
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

}