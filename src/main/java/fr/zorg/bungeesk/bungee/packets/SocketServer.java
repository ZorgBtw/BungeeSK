package fr.zorg.bungeesk.bungee.packets;

import fr.zorg.bungeesk.bungee.BungeeConfig;
import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.Debug;
import fr.zorg.bungeesk.bungee.utils.GlobalScriptsUtils;
import fr.zorg.bungeesk.common.packets.AuthCompletePacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.HandshakePacket;
import fr.zorg.bungeesk.common.utils.EncryptionUtils;
import fr.zorg.bungeesk.common.utils.PacketUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

public class SocketServer {

    private final Socket socket;
    private DataInputStream reader;
    private DataOutputStream writer;
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
                this.reader = new DataInputStream(socket.getInputStream());
                this.writer = new DataOutputStream(socket.getOutputStream());
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
        while (this.socket.isConnected()) {
            try {
                final int length = reader.readInt();
                byte[] data = new byte[length];
                reader.readFully(data, 0, data.length);
                if (this.encrypting) {
                    data = EncryptionUtils.decryptPacket(data, ((String) BungeeConfig.PASSWORD.get()).toCharArray());
                    if (data == null)
                        continue;
                }
                final BungeeSKPacket packet = PacketUtils.packetFromBytes(data);
                Debug.log("Received packet " + packet.getClass().getSimpleName() + " from " + socket.getInetAddress().getHostAddress() + ":" + this.minecraftPort);
                this.handleReceiveListeners(packet);
            } catch (IOException ex) {
                this.disconnect();
            }
        }
    }

    public void sendPacket(BungeeSKPacket packet) {
        if (this.writer == null || this.reader == null)
            return;
        this.handleSendListeners(packet);
        byte[] bytes = PacketUtils.packetToBytes(packet);
        if (encrypting) {
            Debug.log("Encrypting packet " + packet.getClass().getSimpleName());
            bytes = EncryptionUtils.encryptPacket(bytes, ((String) BungeeConfig.PASSWORD.get()).toCharArray());
            if (bytes == null) {
                Debug.log("Failed to encrypt packet");
                return;
            }
        }
        Debug.log("Sending packet " + packet.getClass().getSimpleName() + " to " + socket.getInetAddress().getHostAddress() + ":" + this.minecraftPort);
        try {
            writer.writeInt(bytes.length);
            writer.write(bytes);
            Debug.log("Packet " + packet.getClass().getSimpleName() + " sent to " + socket.getInetAddress().getHostAddress() + ":" + this.minecraftPort);
        } catch (IOException ignored) {
            Debug.log("Error while sending packet " + packet.getClass().getSimpleName() + " to " + socket.getInetAddress().getHostAddress() + ":" + this.minecraftPort);
        }
    }

    public void disconnect() {
        if (this.socket.isConnected() && !this.socket.isClosed() && !this.readThread.isInterrupted()) {
            Debug.log("Disconnecting client with IP " + socket.getInetAddress().getHostAddress() + ":" + this.minecraftPort);
            try {
                this.reader.close();
                this.writer.close();
                this.socket.close();
                this.readThread.interrupt();
                Debug.log("Client with IP " + socket.getInetAddress().getHostAddress() + ":" + this.minecraftPort + " disconnected");
                PacketServer.getClientSockets().remove(this);
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
            } catch (InterruptedException ignored) {
            }
        });
        return this.challengeUUID;
    }

    public void completeChallenge(UUID uuid) {
        if (this.waitingForAuth && this.challengeUUID.compareTo(uuid) == 0 && !this.authenticated) {
            this.authenticated = true;
            this.sendPacket(new AuthCompletePacket(BungeeConfig.ENCRYPT.get()));
            this.encrypting = BungeeConfig.ENCRYPT.get();
            Debug.log("Client with IP " + socket.getInetAddress().getHostAddress() + ":" + this.minecraftPort + " authenticated");
            if (BungeeConfig.FILES$SYNC_AT_CONNECT.get()) {
                GlobalScriptsUtils.sendGlobalScripts(this);
            }
        } else {
            this.disconnect();
        }
    }

    public boolean isAuthenticated() {
        return this.authenticated;
    }

    public boolean isEncrypting() {
        return this.encrypting;
    }

    public void setMinecraftPort(int minecraftPort) {
        this.minecraftPort = minecraftPort;
    }

    public int getMinecraftPort() {
        return this.minecraftPort;
    }

}