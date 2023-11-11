package fr.zorg.bungeesk.common.entities;

import java.io.Serializable;

public class BungeeServerBuilder implements Serializable {

    public String name;
    public String motd;
    public String ip;
    public int port;

    public String getName() {
        return this.name;
    }

    public BungeeServerBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public String getMotd() {
        return this.motd;
    }

    public BungeeServerBuilder setMotd(String motd) {
        this.motd = motd;
        return this;
    }

    public String getIp() {
        return this.ip;
    }

    public BungeeServerBuilder setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public int getPort() {
        return this.port;
    }

    public BungeeServerBuilder setPort(int port) {
        this.port = port;
        return this;
    }

}