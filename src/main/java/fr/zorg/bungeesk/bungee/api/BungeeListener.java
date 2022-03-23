package fr.zorg.bungeesk.bungee.api;

import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

public abstract class BungeeListener {

    /**
     * Listen for packets received on Bungeecord
     *
     * @param server The server where the packet is from
     * @param packet The packet received
     */
    public void onReceive(BungeeServer server, BungeeSKPacket packet) {
    }

    /**
     * Listen for packets sent from the Bungeecord
     *
     * @param server On which server the packet will be sent
     * @param packet Which packet is going to be sent
     */
    public void onSend(BungeeServer server, BungeeSKPacket packet) {
    }

}
