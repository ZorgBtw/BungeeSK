package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.SendActionBarPacket;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetAddress;

public class SendActionBarListener extends BungeeSKListener {

    @Override
    public void onReceive(InetAddress address, BungeeSKPacket packet) {
        if (packet instanceof SendActionBarPacket) {
            final SendActionBarPacket sendActionBarPacket = (SendActionBarPacket) packet;
            final BungeePlayer bungeePlayer = sendActionBarPacket.getBungeePlayer();
            final String message = sendActionBarPacket.getMessage();
            final ProxiedPlayer proxiedPlayer = BungeeUtils.getPlayer(bungeePlayer);
            if (proxiedPlayer == null)
                return;
            proxiedPlayer.sendMessage(ChatMessageType.ACTION_BAR, BungeeUtils.getTextComponent(message));
        }
    }

}