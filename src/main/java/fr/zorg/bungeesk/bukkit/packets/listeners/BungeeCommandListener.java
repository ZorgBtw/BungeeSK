package fr.zorg.bungeesk.bukkit.packets.listeners;

import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.api.BungeeSKBukkitListener;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.BungeeCommandEvent;
import fr.zorg.bungeesk.common.packets.BungeeCommandPacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

public class BungeeCommandListener extends BungeeSKBukkitListener {

    @Override
    public void onReceive(BungeeSKPacket packet) {
        if (packet instanceof BungeeCommandPacket) {
            final BungeeCommandPacket commandPacket = (BungeeCommandPacket) packet;
            final BungeeCommandEvent event = new BungeeCommandEvent(commandPacket.getPlayer(), commandPacket.getCommand());
            BungeeSK.callEvent(event);
        }
    }

}
