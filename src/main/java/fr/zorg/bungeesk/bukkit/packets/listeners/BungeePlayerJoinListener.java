package fr.zorg.bungeesk.bukkit.packets.listeners;

import fr.zorg.bungeesk.bukkit.api.BungeeSKBukkitListener;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.BungeePlayerJoinEvent;
import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.common.packets.BungeePlayerJoinPacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

public class BungeePlayerJoinListener extends BungeeSKBukkitListener {

    @Override
    public void onReceive(BungeeSKPacket packet) {
        if (packet instanceof BungeePlayerJoinPacket) {
            BungeeSK.callEvent(new BungeePlayerJoinEvent(((BungeePlayerJoinPacket) packet).getBungeePlayer()));
        }
    }

}