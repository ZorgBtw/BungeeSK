package fr.zorg.bungeesk.bukkit.packets.listeners;

import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.api.BungeeSKBukkitListener;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.BungeePlayerLeaveEvent;
import fr.zorg.bungeesk.common.packets.BungeePlayerLeavePacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

public class BungeePlayerLeaveListener extends BungeeSKBukkitListener {

    @Override
    public void onReceive(BungeeSKPacket packet) {
        if (packet instanceof BungeePlayerLeavePacket) {
            BungeeSK.callEvent(new BungeePlayerLeaveEvent(((BungeePlayerLeavePacket) packet).getBungeePlayer()));
        }
    }

}