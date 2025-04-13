package fr.zorg.bungeesk.bukkit.packets.listeners;

import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.api.BungeeSKBukkitListener;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.BungeeServerStopEvent;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.BungeeServerStopPacket;

public class BungeeServerStopListener extends BungeeSKBukkitListener {

    @Override
    public void onReceive(BungeeSKPacket packet) {
        if (packet instanceof BungeeServerStopPacket) {
            BungeeSK.callEvent(new BungeeServerStopEvent(((BungeeServerStopPacket) packet).getServer()));
        }
    }

}