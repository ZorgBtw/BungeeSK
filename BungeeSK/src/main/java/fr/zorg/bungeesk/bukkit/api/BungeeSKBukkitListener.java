package fr.zorg.bungeesk.bukkit.api;

import fr.zorg.bungeesk.common.entities.EmptyFutureResponse;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

import java.util.UUID;

public abstract class BungeeSKBukkitListener {

    /**
     * Listen for packets received on the Bukkit server (client)
     *
     * @param packet The packet received
     */
    public void onReceive(BungeeSKPacket packet) {
    }

    /**
     * Listen for packets sent from the Bukkit server (client)
     *
     * @param packet Which packet is going to be sent
     */
    public void onSend(BungeeSKPacket packet) {
    }

    /**
     * Listen for get requests from Bungeecord server
     *
     * @param uuid   The uuid of the said request
     * @param packet The packet specified from the Bungeecord server
     * @return What the Bungeecord will receive.
     * Return a new {@link EmptyFutureResponse} to send nothing, {@link null} to be ignored
     */
    public Object onFutureRequest(UUID uuid, BungeeSKPacket packet) {
        return null;
    }

}