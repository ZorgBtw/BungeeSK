package fr.zorg.bungeesk.bukkit.api;

import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

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

}
