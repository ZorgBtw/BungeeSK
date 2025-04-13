package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.MakeBungeePlayerBungeeCommandPacket;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MakeBungeePlayerBungeeCommandListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof MakeBungeePlayerBungeeCommandPacket) {
            final MakeBungeePlayerBungeeCommandPacket makeBungeePlayerBungeeCommandPacket = (MakeBungeePlayerBungeeCommandPacket) packet;
            final BungeePlayer player = makeBungeePlayerBungeeCommandPacket.getPlayer();
            final String command = makeBungeePlayerBungeeCommandPacket.getCommand();
            final ProxiedPlayer proxiedPlayer = BungeeUtils.getPlayer(player);
            if (proxiedPlayer != null)
                BungeeSK.getInstance().getProxy().getPluginManager().dispatchCommand(proxiedPlayer, command.startsWith("/") ? command.substring(1) : command);
        }
    }

}