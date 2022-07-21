package fr.zorg.bungeesk.bungee.packets;

import fr.zorg.bungeesk.bungee.BungeeConfig;
import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.Debug;
import fr.zorg.bungeesk.bungee.utils.GlobalScriptsUtils;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.HandshakePacket;
import fr.zorg.bungeesk.common.utils.PacketUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
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

    public SocketServer(Socket socket) {
        this.socket = socket;
        try {
            this.reader = new DataInputStream(socket.getInputStream());
            this.writer = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            this.disconnect();
        }
        this.readThread = new Thread(this::read);
        this.readThread.start();
        this.authenticated = false;
        Debug.log("ClientSocket started on " + socket.getInetAddress().getHostAddress());

        this.send(new HandshakePacket());
    }

    public void read() {
        while (this.socket.isConnected()) {
            try {
                final int length = reader.readInt();
                byte[] data = new byte[length];
                reader.readFully(data, 0, data.length);
                final BungeeSKPacket packet = PacketUtils.packetFromBytes(data);
                Debug.log("Received packet " + packet.getClass().getSimpleName() + " from " + socket.getInetAddress().getHostAddress());
                this.handleReceiveListeners(packet);
            } catch (IOException ex) {
                this.disconnect();
            }
        }
    }

    public void send(BungeeSKPacket packet) {
        this.handleSendListeners(packet);
        final byte[] bytes = PacketUtils.packetToBytes(packet);
        Debug.log("Sending packet " + packet.getClass().getSimpleName() + " to " + socket.getInetAddress().getHostAddress());
        try {
            writer.writeInt(bytes.length);
            writer.write(bytes);
            Debug.log("Packet " + packet.getClass().getSimpleName() + " sent to " + socket.getInetAddress().getHostAddress());
        } catch (IOException ignored) {
            Debug.log("Error while sending packet " + packet.getClass().getSimpleName() + " to " + socket.getInetAddress().getHostAddress());
        }
    }

    public void disconnect() {
        if (this.socket.isConnected() && !this.socket.isClosed() && !this.readThread.isInterrupted()) {
            Debug.log("Disconnecting client with IP " + socket.getInetAddress().getHostAddress());
            try {
                this.reader.close();
                this.writer.close();
                this.socket.close();
                this.readThread.interrupt();
                Debug.log("Client with IP " + socket.getInetAddress().getHostAddress() + " disconnected");
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
                listener.getClass().getMethod("onSend", InetAddress.class, BungeeSKPacket.class);
                listener.onSend(this.socket.getInetAddress(), packet);
            } catch (Exception ignored) {
            }
        });
    }

    private void handleReceiveListeners(BungeeSKPacket packet) {
        BungeeSK.getApi().getListeners().forEach(listener -> {
            try {
                listener.getClass().getMethod("onReceive", InetAddress.class, BungeeSKPacket.class);
                listener.onReceive(this.socket.getInetAddress(), packet);
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
            Debug.log("Client with IP " + socket.getInetAddress().getHostAddress() + " authenticated");
            if (BungeeConfig.FILES$SYNC_AT_CONNECT.get()) {
                GlobalScriptsUtils.sendGlobalScripts(this.socket.getInetAddress());
            }
        } else {
            this.disconnect();
        }
    }

    public boolean isAuthenticated() {
        return this.authenticated;
    }
}
