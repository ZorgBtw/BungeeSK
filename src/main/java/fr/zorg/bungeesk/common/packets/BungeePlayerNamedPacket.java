package fr.zorg.bungeesk.common.packets;

public class BungeePlayerNamedPacket implements BungeeSKPacket {

    private final String name;

    public BungeePlayerNamedPacket(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}