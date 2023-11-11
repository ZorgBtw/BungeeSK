package fr.zorg.bungeesk.common.utils;

import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

import java.io.*;

public class PacketUtils {

    public static byte[] packetToBytes(BungeeSKPacket packet) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = null;
        try {
            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(packet);
            objectOutputStream.flush();
            bytes = byteArrayOutputStream.toByteArray();
            objectOutputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException ignored) {
            }
        }
        return bytes;
    }

    public static BungeeSKPacket packetFromBytes(byte[] bytes) {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInput objectInput = null;
        BungeeSKPacket packet = null;
        try {
            objectInput = new ObjectInputStream(byteArrayInputStream);
            packet = (BungeeSKPacket) objectInput.readObject();
            byteArrayInputStream.close();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (objectInput != null)
                    objectInput.close();
            } catch (IOException ignored) {
            }
        }
        return packet;
    }

}