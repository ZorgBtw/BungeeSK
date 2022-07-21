package fr.zorg.bungeesk.bungee.api;

import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

import java.net.InetAddress;
import java.util.UUID;

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

    /**
     * Listen for get requests from Spigot servers
     *
     * @param uuid    The uuid of the said request
     * @param address The server's address from where the get request is from
     * @param packet  The packet specified from the Spigot server
     * @return What will be sent to the Spigot server
     */
    public Object onFutureRequest(UUID uuid, InetAddress address, BungeeSKPacket packet) {
        return null;
    }

}
