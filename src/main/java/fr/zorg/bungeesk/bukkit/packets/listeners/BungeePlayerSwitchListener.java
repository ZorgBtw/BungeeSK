package fr.zorg.bungeesk.bukkit.packets.listeners;

import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.api.BungeeSKBukkitListener;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.ServerSwitchEvent;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.BungeePlayerSwitchPacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

public class BungeePlayerSwitchListener extends BungeeSKBukkitListener {

    @Override
    public void onReceive(BungeeSKPacket packet) {
        if (packet instanceof BungeePlayerSwitchPacket) {
            final BungeePlayerSwitchPacket bungeePlayerSwitchPacket = (BungeePlayerSwitchPacket) packet;
            final BungeePlayer bungeePlayer = bungeePlayerSwitchPacket.getBungeePlayer();
            final BungeeServer bungeeServer = bungeePlayerSwitchPacket.getBungeeServer();
            BungeeSK.callEvent(new ServerSwitchEvent(bungeePlayer, bungeeServer));
        }
    }

}