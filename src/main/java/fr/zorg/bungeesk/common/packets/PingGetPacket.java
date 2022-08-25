package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.Ping;

import java.net.InetAddress;

public class PingGetPacket implements BungeeSKPacket {

    private final Ping ping;
    private final InetAddress address;

    public PingGetPacket(Ping ping, InetAddress address) {
        this.ping = ping;
        this.address = address;
    }

    public Ping getPing() {
        return this.ping;
    }

    public InetAddress getAddress() {
        return this.address;
    }

}