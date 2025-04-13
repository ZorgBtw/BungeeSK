package fr.zorg.velocitysk.utils;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.PlayerAvailableCommandsEvent;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.ServerInfo;
import com.velocitypowered.api.proxy.server.ServerPing;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.BungeeCommandPacket;
import fr.zorg.bungeesk.common.packets.BungeePlayerJoinPacket;
import fr.zorg.bungeesk.common.packets.BungeePlayerLeavePacket;
import fr.zorg.bungeesk.common.packets.BungeePlayerSwitchPacket;
import fr.zorg.velocitysk.packets.PacketServer;

import java.util.Optional;

public class BungeeEventsListener {

    @Subscribe
    public void onJoin(PostLoginEvent e) {
        final Player player = e.getPlayer();
        final BungeePlayer bungeePlayer = new BungeePlayer(player.getUsername(), player.getUniqueId());
        final BungeePlayerJoinPacket packet = new BungeePlayerJoinPacket(bungeePlayer);
        PacketServer.broadcastPacket(packet);
    }

    @Subscribe
    public void onQuit(DisconnectEvent e) {
        final Player player = e.getPlayer();
        if (player == null)
            return;

        final Optional<ServerConnection> serverOptional = player.getCurrentServer();
        if (serverOptional.isEmpty())
            return;

        final ServerInfo server = serverOptional.get().getServerInfo();

        final BungeePlayer bungeePlayer = new BungeePlayer(player.getUsername(), player.getUniqueId());
        final BungeeServer bungeeServer = VelocityUtils.getServerFromInfo(server);
        final BungeePlayerLeavePacket packet = new BungeePlayerLeavePacket(bungeePlayer, bungeeServer);
        PacketServer.broadcastPacket(packet);
    }

    @Subscribe
    public void onSwitch(ServerPreConnectEvent e) {
        if (e.getOriginalServer() == null)
            return;

        final BungeeServer server = VelocityUtils.getServerFromInfo(e.getOriginalServer().getServerInfo());
        final BungeePlayer player = VelocityUtils.getBungeePlayer(e.getPlayer());
        final BungeePlayerSwitchPacket packet = new BungeePlayerSwitchPacket(player, server);
        PacketServer.broadcastPacket(packet);
    }

    @Subscribe
    public void onChat(PlayerChatEvent e) {
        if (!e.getMessage().startsWith("/"))
            return;
        final Player player = e.getPlayer();

        if (player == null)
            return;

        final BungeePlayer bungeePlayer = VelocityUtils.getBungeePlayer(player);
        final BungeeCommandPacket packet = new BungeeCommandPacket(e.getMessage().substring(1), bungeePlayer);
        PacketServer.broadcastPacket(packet);
    }

    @Subscribe
    public void onPing(ProxyPingEvent e) {

        final ServerPing response = PingUtils.getPing(e.getPing(), e.getConnection().getRemoteAddress().getAddress());
        e.setPing(response);
    }

    @Subscribe
    public void onCommandCompletion(PlayerAvailableCommandsEvent e) {
        if (!(e.getPlayer().hasPermission("bungeesk.command") &&
                e.getRootNode().getChildren().contains("bungeesk"))) {
            e.getRootNode().removeChildByName("bungeesk");
        }
    }

}