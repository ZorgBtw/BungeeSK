package fr.zorg.bungeesk.common.entities;

import java.io.Serializable;
import java.net.InetAddress;

public class BungeeServer implements Serializable {

    private final InetAddress address;
    private final int port;
    private final String name;

    public BungeeServer(InetAddress address, int port, String name) {
        this.address = address;
        this.port = port;
        this.name = name;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public int getPort() {
        return this.port;
    }

    public String getName() {
        return this.name;
    }

}