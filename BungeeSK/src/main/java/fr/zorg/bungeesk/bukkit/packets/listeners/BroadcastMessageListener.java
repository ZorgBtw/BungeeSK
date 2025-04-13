package fr.zorg.bungeesk.bukkit.packets.listeners;

import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.api.BungeeSKBukkitListener;
import fr.zorg.bungeesk.common.packets.BroadcastMessagePacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

public class BroadcastMessageListener extends BungeeSKBukkitListener {

    @Override
    public void onReceive(BungeeSKPacket packet) {
        if (packet instanceof BroadcastMessagePacket) {
            final BroadcastMessagePacket broadcastMessagePacket = (BroadcastMessagePacket) packet;
            BungeeSK.getInstance().getServer().broadcastMessage(broadcastMessagePacket.getMessage());
        }
    }

}