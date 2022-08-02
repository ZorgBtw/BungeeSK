package fr.zorg.bungeesk.bukkit.packets;

import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.ClientDisconnectEvent;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.utils.EncryptionUtils;
import fr.zorg.bungeesk.common.utils.PacketUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class SocketClient {

    private final Socket socket;
    private DataInputStream reader;
    private DataOutputStream writer;
    private final Thread readThread;
    private boolean encrypting;

    public SocketClient(Socket socket) {
        this.socket = socket;
        this.encrypting = false;
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
                if (this.encrypting) {
                    data = EncryptionUtils.decryptPacket(data, PacketClient.getBuilder().getPassword());
                    if (data == null)
                        continue;
                }
                final BungeeSKPacket packet = PacketUtils.packetFromBytes(data);
                this.handleReceiveListeners(packet);
            } catch (IOException ex) {
                this.disconnect();
            }
        }
    }

    public void send(BungeeSKPacket packet) {
        BungeeSK.runAsync(() -> {
            if (this.isConnected()) {
                this.handleSendListeners(packet);
                byte[] bytes = PacketUtils.packetToBytes(packet);
                if (this.isEncrypting()) {
                    bytes = EncryptionUtils.encryptPacket(bytes, PacketClient.getBuilder().getPassword());
                    if (bytes == null)
                        return;
                }
                try {
                    writer.writeInt(bytes.length);
                    writer.write(bytes);
                } catch (IOException ignored) {
                }
            }
        });
    }

    public void disconnect() {
        if (this.isConnected()) {
            try {
                this.socket.close();
                this.readThread.interrupt();
                BungeeSK.callEvent(new ClientDisconnectEvent());
            } catch (IOException ignored) {
            }
        }
    }

    public boolean isConnected() {
        return this.socket != null && this.socket.isConnected() && !this.socket.isClosed();
    }

    private void handleSendListeners(BungeeSKPacket packet) {
        BungeeSK.runAsync(() -> {
            BungeeSK.getApi().getListeners().forEach(listener -> {
                try {
                    listener.getClass().getMethod("onSend", BungeeSKPacket.class);
                    listener.onSend(packet);
                } catch (Exception ignored) {
                }
            });
        });
    }

    private void handleReceiveListeners(BungeeSKPacket packet) {
        BungeeSK.runAsync(() -> {
            BungeeSK.getApi().getListeners().forEach(listener -> {
                try {
                    listener.getClass().getMethod("onReceive", BungeeSKPacket.class);
                    listener.onReceive(packet);
                } catch (Exception ignored) {
                }
            });
        });
    }

    public void setEncrypting(boolean encrypting) {
        this.encrypting = encrypting;
    }

    public boolean isEncrypting() {
        return this.encrypting;
    }
}