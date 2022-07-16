package fr.zorg.bungeesk.bukkit.packets;

import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bungee.Debug;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.utils.PacketUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketClient {

    private final Socket socket;
    private DataInputStream reader;
    private DataOutputStream writer;
    private final Thread readThread;

    public SocketClient(Socket socket) {
        Debug.log("ClientSocket started on " + socket.getInetAddress().getHostAddress());
        this.socket = socket;
        try {
            this.reader = new DataInputStream(socket.getInputStream());
            this.writer = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            this.disconnect();
        }
        this.readThread = new Thread(this::read);
        this.readThread.start();
    }

    public void read() {
        while (this.socket.isConnected()) {
            try {
                final int length = reader.readInt();
                byte[] data = new byte[length];
                reader.readFully(data, 0, data.length);
                final BungeeSKPacket packet = PacketUtils.packetFromBytes(data);
                this.handleReceiveListeners(packet);
            } catch (IOException ex) {
                this.disconnect();
            }
        }
    }

    public void send(BungeeSKPacket packet) {
        this.handleSendListeners(packet);
        final byte[] bytes = PacketUtils.packetToBytes(packet);
        try {
            writer.writeInt(bytes.length);
            writer.write(bytes);
        } catch (IOException ignored) {
        }
    }

    public void disconnect() {
        try {
            this.writer.close();
            this.reader.close();
            this.socket.close();
            this.readThread.interrupt();
        } catch (IOException ignored) {
        }
    }

    private void handleSendListeners(BungeeSKPacket packet) {
        BungeeSK.getApi().getListeners().forEach(listener -> {
            try {
                listener.getClass().getMethod("onSend", BungeeSKPacket.class);
                listener.onSend(packet);
            } catch (Exception ignored) {
            }
        });
    }

    private void handleReceiveListeners(BungeeSKPacket packet) {
        BungeeSK.getApi().getListeners().forEach(listener -> {
            try {
                listener.getClass().getMethod("onReceive", BungeeSKPacket.class);
                listener.onReceive(packet);
            } catch (Exception ignored) {
            }
        });
    }

}