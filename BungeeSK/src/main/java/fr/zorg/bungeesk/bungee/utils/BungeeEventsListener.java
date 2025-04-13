package fr.zorg.bungeesk.bungee.utils;

import fr.zorg.bungeesk.bungee.packets.PacketServer;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.BungeeCommandPacket;
import fr.zorg.bungeesk.common.packets.BungeePlayerJoinPacket;
import fr.zorg.bungeesk.common.packets.BungeePlayerLeavePacket;
import fr.zorg.bungeesk.common.packets.BungeePlayerSwitchPacket;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeEventsListener implements Listener {

    @EventHandler
    public void onJoin(PostLoginEvent e) {
        final ProxiedPlayer player = e.getPlayer();
        final BungeePlayer bungeePlayer = new BungeePlayer(player.getName(), player.getUniqueId());
        final BungeePlayerJoinPacket packet = new BungeePlayerJoinPacket(bungeePlayer);
        PacketServer.broadcastPacket(packet);
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent e) {
        final ProxiedPlayer player = e.getPlayer();
        if (player == null)
            return;

        final ServerInfo server = player.getServer().getInfo();
        if (server == null)
            return;

        final BungeePlayer bungeePlayer = new BungeePlayer(player.getName(), player.getUniqueId());
        final BungeeServer bungeeServer = BungeeUtils.getServerFromInfo(server);
        final BungeePlayerLeavePacket packet = new BungeePlayerLeavePacket(bungeePlayer, bungeeServer);
        PacketServer.broadcastPacket(packet);
    }

    @EventHandler
    public void onSwitch(ServerSwitchEvent e) {
        if (e.getFrom() == null)
            return;

        final BungeeServer server = BungeeUtils.getServerFromInfo(e.getFrom());
        final BungeePlayer player = BungeeUtils.getBungeePlayer(e.getPlayer());
        final BungeePlayerSwitchPacket packet = new BungeePlayerSwitchPacket(player, server);
        PacketServer.broadcastPacket(packet);
    }

    @EventHandler
    public void onChat(ChatEvent e) {
        if (!e.getMessage().startsWith("/"))
            return;
        final ProxiedPlayer player = (ProxiedPlayer) e.getSender();

        if (player == null)
            return;

        final BungeePlayer bungeePlayer = BungeeUtils.getBungeePlayer(player);
        final BungeeCommandPacket packet = new BungeeCommandPacket(e.getMessage().substring(1), bungeePlayer);
        PacketServer.broadcastPacket(packet);
    }

    @EventHandler
    public void onPing(ProxyPingEvent e) {
        final ServerPing response = PingUtils.getPing(e.getResponse(), e.getConnection().getAddress().getAddress());
        e.setResponse(response);
    }

}