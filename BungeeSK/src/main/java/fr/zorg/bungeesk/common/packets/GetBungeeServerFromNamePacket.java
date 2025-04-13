package fr.zorg.bungeesk.common.packets;

public class GetBungeeServerFromNamePacket implements BungeeSKPacket {
    private final String name;

    public GetBungeeServerFromNamePacket(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}