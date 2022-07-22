package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.SendMessagePacket;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetAddress;

public class SendMessageListener extends BungeeSKListener {

    @Override
    public void onReceive(InetAddress address, BungeeSKPacket packet) {
        if (packet instanceof SendMessagePacket) {
            final SendMessagePacket sendMessagePacket = (SendMessagePacket) packet;
            final BungeePlayer bungeePlayer = sendMessagePacket.getBungeePlayer();
            final String message = sendMessagePacket.getMessage();
            final ProxiedPlayer proxiedPlayer = BungeeUtils.getPlayer(bungeePlayer);
            if (proxiedPlayer == null)
                return;

            proxiedPlayer.sendMessage(BungeeUtils.getTextComponent(message));
        }
    }

}