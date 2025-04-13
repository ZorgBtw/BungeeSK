package fr.zorg.bungeesk.bukkit.packets;

import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.ClientDisconnectEvent;
import fr.zorg.bungeesk.common.packets.AuthCompletePacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.utils.EncryptionUtils;
import fr.zorg.bungeesk.common.utils.PacketUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

public class SocketClient {

    private final Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private Thread readThread;
    private boolean encrypting;

    public SocketClient(Socket socket) {
        this.socket = socket;
        this.encrypting = false;
        try {
            this.writer = new ObjectOutputStream(socket.getOutputStream());
            this.reader = new ObjectInputStream(socket.getInputStream());
            this.readThread = new Thread(this::read);
            this.readThread.start();
        } catch (IOException ex) {
            ex.printStackTrace();
            this.disconnect();
        }
    }

    public void read() {
        while (this.isConnected()) {
            try {
                Object dataRaw = this.reader.readObject();
                final AtomicReference<?> dataAtomic = new AtomicReference<>(dataRaw);
                BungeeSK.runAsync(() -> {
                    Object data = dataAtomic.get();
                    if (this.encrypting) {
                        data = EncryptionUtils.decryptPacket((byte[]) data, PacketClient.getBuilder().getPassword());
                        if (data == null)
                            return;

                        data = PacketUtils.packetFromBytes((byte[]) data);
                        if (data == null)
                            return;
                    }
                    if (!(data instanceof BungeeSKPacket)) {
                        this.disconnect();
                        return;
                    }
                    final BungeeSKPacket packet = (BungeeSKPacket) data;
                    this.handleReceiveListeners(packet);
                });
            } catch (IOException | ClassNotFoundException ex) {
                this.disconnect();
            }
        }
    }

    public void sendPacket(BungeeSKPacket packet) {
        BungeeSK.runAsync(() -> {
            if (this.isConnected()) {
                this.handleSendListeners(packet);

                Object toSend = packet;
                if (this.encrypting) {
                    toSend = EncryptionUtils.encryptPacket(PacketUtils.packetToBytes(packet), PacketClient.getBuilder().getPassword());
                    if (toSend == null)
                        return;
                }
                try {
                    this.writer.writeObject(toSend);
                } catch (IOException ignored) {
                }

                if (packet instanceof AuthCompletePacket) {
                    setEncrypting(((AuthCompletePacket) packet).isEncrypting());
                }
            }
        });
    }

    public void disconnect() {
        if (this.isConnected()) {
            try {
                if (this.reader != null)
                    this.reader.close();
                if (this.writer != null)
                    this.writer.close();
                if (this.readThread != null)
                    this.readThread.interrupt();
                this.socket.close();
                PacketClient.resetSocket();
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