package fr.zorg.bungeesk.bukkit.skript.expressions.bungeeserver;

import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

public class GetBungeeServerMOTDPacket implements BungeeSKPacket {

    private final BungeeServer bungeeServer;

    public GetBungeeServerMOTDPacket(BungeeServer bungeeServer) {
        this.bungeeServer = bungeeServer;
    }

    public BungeeServer getBungeeServer() {
        return this.bungeeServer;
    }

}