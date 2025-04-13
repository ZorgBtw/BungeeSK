package fr.zorg.bungeesk.bukkit.packets.listeners;

import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.api.BungeeSKBukkitListener;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.BungeeMessageReceiveEvent;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.CustomBungeeMessagePacket;

public class CustomBungeeMessageListener extends BungeeSKBukkitListener {

    @Override
    public void onReceive(BungeeSKPacket packet) {
        if (packet instanceof CustomBungeeMessagePacket) {
            final CustomBungeeMessagePacket customBungeeMessagePacket = (CustomBungeeMessagePacket) packet;
            BungeeSK.callEvent(new BungeeMessageReceiveEvent(
                    customBungeeMessagePacket.getFrom(),
                    customBungeeMessagePacket.getMessage()
            ));
        }
    }

}