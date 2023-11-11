package fr.zorg.velocitysk.packets.listeners;

import com.velocitypowered.api.proxy.Player;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.MakeBungeePlayerCommandPacket;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;
import fr.zorg.velocitysk.utils.VelocityUtils;

public class MakeBungeePlayerCommandListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof MakeBungeePlayerCommandPacket) {
            final MakeBungeePlayerCommandPacket makeBungeePlayerCommandPacket = (MakeBungeePlayerCommandPacket) packet;
            final BungeePlayer bungeePlayer = makeBungeePlayerCommandPacket.getBungeePlayer();
            String command = makeBungeePlayerCommandPacket.getCommand();

            if (!command.startsWith("/"))
                command = "/" + command;

            final Player player = VelocityUtils.getPlayer(bungeePlayer);
            if (player != null)
                player.spoofChatInput(command);
        }
    }
}