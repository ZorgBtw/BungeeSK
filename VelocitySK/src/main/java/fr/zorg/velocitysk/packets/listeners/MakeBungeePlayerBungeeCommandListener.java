package fr.zorg.velocitysk.packets.listeners;

import com.velocitypowered.api.proxy.Player;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.MakeBungeePlayerBungeeCommandPacket;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;
import fr.zorg.velocitysk.utils.VelocityUtils;

public class MakeBungeePlayerBungeeCommandListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof MakeBungeePlayerBungeeCommandPacket) {
            final MakeBungeePlayerBungeeCommandPacket makeBungeePlayerBungeeCommandPacket = (MakeBungeePlayerBungeeCommandPacket) packet;
            final BungeePlayer bungeePlayer = makeBungeePlayerBungeeCommandPacket.getPlayer();
            final String command = makeBungeePlayerBungeeCommandPacket.getCommand();
            final Player player = VelocityUtils.getPlayer(bungeePlayer);
            if (player != null)
                player.spoofChatInput(command.startsWith("/") ? command.substring(1) : command);
        }
    }

}