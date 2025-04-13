package fr.zorg.bungeesk.bukkit.packets.listeners;

import fr.zorg.bungeesk.bukkit.api.BungeeSKBukkitListener;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.KeepAlivePacket;

import java.util.UUID;

public class KeepAliveListener extends BungeeSKBukkitListener {

    @Override
    public Object onFutureRequest(UUID uuid, BungeeSKPacket packet) {
        if (packet instanceof KeepAlivePacket)
            return System.currentTimeMillis();
        return null;
    }
}