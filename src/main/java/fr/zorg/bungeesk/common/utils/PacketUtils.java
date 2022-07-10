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

    public static <T extends BungeeSKPacket> T packetFromBytes(byte[] bytes) {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInput objectInput = null;
        T packet = null;
        try {
            objectInput = new ObjectInputStream(byteArrayInputStream);
            packet = (T) objectInput.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
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