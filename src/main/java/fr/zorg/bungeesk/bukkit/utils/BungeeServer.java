package fr.zorg.bungeesk.bukkit.utils;

public class BungeeServer {

    private final String address;
    private final int port;
    private final String name;
    private final String motd;

    public BungeeServer(final String address, final int port, final String name, final String motd) {
        this.address = address;
        this.port = port;
        this.name = name;
        this.motd = motd;
    }

    public String getAddress() {
        return this.address;
    }

    public int getPort() {
        return this.port;
    }

    public String getName() {
        return this.name;
    }

    public String getMotd() {
        return this.motd;
    }

}
