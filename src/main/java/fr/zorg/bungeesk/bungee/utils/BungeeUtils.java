package fr.zorg.bungeesk.bungee.utils;

import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeUtils {

    public static ProxiedPlayer getPlayer(BungeePlayer bungeePlayer) {
        final ProxiedPlayer player = BungeeSK.getInstance().getProxy().getPlayer(bungeePlayer.getName());
        return player != null && player.isConnected() ? player : null;
    }

    public static TextComponent[] getTextComponent(String... text) {
        final TextComponent[] textComponents = new TextComponent[text.length];
        for (int i = 0; i < text.length; i++) {
            textComponents[i] = new TextComponent(text[i]);
        }
        return textComponents;
    }

    public static BungeeServer getServerFromName(String name) {
        final ServerInfo serverInfo = BungeeSK.getInstance().getProxy().getServerInfo(name);
        return serverInfo != null ? new BungeeServer(serverInfo.getAddress().getAddress(), serverInfo.getAddress().getPort(), serverInfo.getName()) : null;
    }

    public static BungeeServer getServerFromAddress(String address, int port) {
        final ServerInfo serverInfo = BungeeSK
                .getInstance()
                .getProxy()
                .getServers()
                .values()
                .stream()
                .filter(server ->
                        server.getAddress().getAddress().getHostAddress().equalsIgnoreCase(address) &&
                                server.getAddress().getPort() == port)
                .findFirst()
                .orElse(null);

        return serverInfo != null ? new BungeeServer(serverInfo.getAddress().getAddress(), serverInfo.getAddress().getPort(), serverInfo.getName()) : null;
    }

    public static ServerInfo getServerInfo(BungeeServer bungeeServer) {
        return BungeeSK
                .getInstance()
                .getProxy()
                .getServers()
                .values()
                .stream()
                .filter(server ->
                        server.getAddress().getAddress().getHostAddress().equalsIgnoreCase(bungeeServer.getAddress().getHostAddress()) &&
                                server.getAddress().getPort() == bungeeServer.getPort())
                .findFirst()
                .orElse(null);
    }

}