package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.MakeBungeePlayerCommandPacket;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MakeBungeePlayerCommandListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof MakeBungeePlayerCommandPacket) {
            final MakeBungeePlayerCommandPacket makeBungeePlayerCommandPacket = (MakeBungeePlayerCommandPacket) packet;
            final BungeePlayer bungeePlayer = makeBungeePlayerCommandPacket.getBungeePlayer();
            String command = makeBungeePlayerCommandPacket.getCommand();

            if (!command.startsWith("/"))
                command = "/" + command;

            final ProxiedPlayer player = BungeeUtils.getPlayer(bungeePlayer);
            if (player != null)
                player.chat(command);
        }
    }
}