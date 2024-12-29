package fr.zorg.velocitysk.utils;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.velocitysk.BungeeSK;
import fr.zorg.velocitysk.packets.PacketServer;
import fr.zorg.velocitysk.packets.SocketServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.UUID;

public class VelocityUtils {

    public static Player getPlayer(BungeePlayer bungeePlayer) {
        final Optional<Player> player = BungeeSK.getServer().getPlayer(bungeePlayer.getName());
        return (player.isPresent() && player.get().isActive()) ? player.get() : null;
    }

    public static Player getPlayer(String name) {
        final Optional<Player> player = BungeeSK.getServer().getPlayer(name);
        return (player.isPresent() && player.get().isActive()) ? player.get() : null;
    }

    public static Player getPlayer(UUID uuid) {
        final Optional<Player> player = BungeeSK.getServer().getPlayer(uuid);
        return (player.isPresent() && player.get().isActive()) ? player.get() : null;
    }

    public static BungeePlayer getBungeePlayer(Player player) {
        return new BungeePlayer(player.getUsername(), player.getUniqueId());
    }

    public static Component getTextComponent(String... text) {
        final TextComponent.Builder builder = Component.text();
        for (int i = 0; i < text.length; i++) {
            builder.append(LegacyComponentSerializer.legacySection().deserialize(text[i]).asComponent());
            if (i != text.length - 1)
                builder.appendNewline();
        }
        return builder.build();
    }

    public static BungeeServer getServerFromName(String name) {
        final Optional<RegisteredServer> registeredServer = BungeeSK.getServer().getServer(name);
        return registeredServer.map(server -> new BungeeServer(
                server.getServerInfo().getAddress().getAddress(),
                server.getServerInfo().getAddress().getPort(),
                server.getServerInfo().getName())).orElse(null);
    }

    public static BungeeServer getServerFromInfo(ServerInfo serverInfo) {
        return serverInfo != null ? new BungeeServer(serverInfo.getAddress().getAddress(), serverInfo.getAddress().getPort(), serverInfo.getName()) : null;
    }

    public static BungeeServer getServerFromAddress(String address, int port) {
        try {
            final InetAddress inetAddress = InetAddress.getByName(address);
            final boolean isLocal = inetAddress.isLoopbackAddress() || inetAddress.isAnyLocalAddress();
            final RegisteredServer registeredServer = BungeeSK
                    .getServer()
                    .getAllServers()
                    .stream()
                    .filter(server ->
                            (server.getServerInfo().getAddress().getAddress().getHostAddress().equalsIgnoreCase(address) ||
                                    server.getServerInfo().getAddress().getAddress().isAnyLocalAddress() ||
                                    server.getServerInfo().getAddress().getAddress().isLoopbackAddress() && isLocal) &&
                                    server.getServerInfo().getAddress().getPort() == port)
                    .findFirst()
                    .orElse(null);

            final ServerInfo serverInfo = registeredServer != null ? registeredServer.getServerInfo() : null;

            return VelocityUtils.getServerFromInfo(serverInfo);

        } catch (UnknownHostException ignored) {
        }
        return null;
    }

    public static BungeeServer getServerFromSocket(SocketServer socketServer) {
        return getServerFromAddress(socketServer.getSocket().getInetAddress().getHostAddress(), socketServer.getMinecraftPort());
    }

    public static RegisteredServer getRegisteredServer(BungeeServer bungeeServer) {
        final Optional<RegisteredServer> registeredServer = BungeeSK
                .getServer()
                .getAllServers()
                .stream()
                .filter(server ->
                        server.getServerInfo().getAddress().getAddress().getHostAddress().equalsIgnoreCase(bungeeServer.getAddress().getHostAddress()) &&
                                server.getServerInfo().getAddress().getPort() == bungeeServer.getPort())
                .findFirst();

        return registeredServer.orElse(null);
    }

    public static ServerInfo getServerInfo(BungeeServer bungeeServer) {
        final RegisteredServer registeredServer = getRegisteredServer(bungeeServer);
        return registeredServer != null ? registeredServer.getServerInfo() : null;
    }


    public static SocketServer getSocketFromBungeeServer(BungeeServer bungeeServer) {

        final boolean isLocal = bungeeServer.getAddress().isLoopbackAddress() || bungeeServer.getAddress().isAnyLocalAddress();

        return PacketServer
                .getClientSockets()
                .stream()
                .filter(clientSocket ->
                        (clientSocket.getSocket().getInetAddress().getHostAddress().equalsIgnoreCase(
                                bungeeServer
                                        .getAddress()
                                        .getHostAddress()
                        ) ||
                                (clientSocket.getSocket().getInetAddress().isAnyLocalAddress() || clientSocket.getSocket().getInetAddress().isLoopbackAddress() && isLocal)) &&
                                clientSocket.getMinecraftPort() == bungeeServer.getPort()
                )
                .findFirst().orElse(null);

    }

}