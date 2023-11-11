package fr.zorg.bungeesk.bukkit.packets.listeners;

import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.api.BungeeSKBukkitListener;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.BungeePingEvent;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.PingGetPacket;

import java.util.UUID;

public class PingGetListener extends BungeeSKBukkitListener {

    @Override
    public Object onFutureRequest(UUID uuid, BungeeSKPacket packet) {
        if (packet instanceof PingGetPacket) {
            final PingGetPacket pingGetPacket = (PingGetPacket) packet;
            final BungeePingEvent event = new BungeePingEvent(pingGetPacket.getAddress(), pingGetPacket.getPing());
            BungeeSK.getInstance().getServer().getPluginManager().callEvent(event);
            return event.getPing();
        }
        return null;
    }

}
