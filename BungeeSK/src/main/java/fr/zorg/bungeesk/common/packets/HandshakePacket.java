package fr.zorg.bungeesk.common.packets;

public class HandshakePacket implements BungeeSKPacket {

    private final int minecraftPort;

    public HandshakePacket(int minecraftPort) {
        this.minecraftPort = minecraftPort;
    }

    public int getMinecraftPort() {
        return this.minecraftPort;
    }

}