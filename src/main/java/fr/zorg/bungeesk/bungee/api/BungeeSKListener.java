package fr.zorg.bungeesk.bungee.api;

import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

import java.net.InetAddress;

public abstract class BungeeSKListener {

    /**
     * Listen for packets received on Bungeecord
     *
     * @param address The server's address from where the packet is from
     * @param packet  The packet received
     */
    public void onReceive(InetAddress address, BungeeSKPacket packet) {
    }

    /**
     * Listen for packets sent from the Bungeecord
     *
     * @param address The server's address the packet will be sent
     * @param packet  Which packet is going to be sent
     */
    public void onSend(InetAddress address, BungeeSKPacket packet) {
    }

}
