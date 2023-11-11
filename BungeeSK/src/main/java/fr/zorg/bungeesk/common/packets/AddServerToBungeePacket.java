package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeeServerBuilder;

public class AddServerToBungeePacket implements BungeeSKPacket {

    private final BungeeServerBuilder builder;

    public AddServerToBungeePacket(BungeeServerBuilder builder) {
        this.builder = builder;
    }

    public BungeeServerBuilder getBuilder() {
        return this.builder;
    }

}