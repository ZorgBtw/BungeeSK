package fr.zorg.bungeesk.bukkit.packets.listeners;

import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.api.BungeeSKBukkitListener;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.BungeeServerStartEvent;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.BungeeServerStartPacket;

public class BungeeServerStartListener extends BungeeSKBukkitListener {

    @Override
    public void onReceive(BungeeSKPacket packet) {
        if (packet instanceof BungeeServerStartPacket) {
            final BungeeServerStartPacket bungeeServerStartPacket = (BungeeServerStartPacket) packet;
            final BungeeServer server = bungeeServerStartPacket.getServer();
            BungeeSK.callEvent(new BungeeServerStartEvent(server));
        }
    }

}