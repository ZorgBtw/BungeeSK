package fr.zorg.bungeesk.bungee.utils;

import com.google.gson.JsonObject;
import fr.zorg.bungeesk.bungee.BungeeSK;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.config.Configuration;

import java.util.Optional;

public class BungeeUtils {

    public static boolean addDefault(final Configuration config, final String path, final Object o) {
        if (config.get(path) == null) {
            config.set(path, o);
            return true;
        }
        return false;
    }

    public static Optional<ServerInfo> findServer(final String address, final int port) {
        return BungeeSK.getInstance().getProxy().getServers().values().stream().filter(
                server -> (server.getAddress().getAddress().getHostAddress().equalsIgnoreCase(address)
                        && server.getAddress().getPort() == port)).findFirst();
    }

    public static JsonObject getBungeeServer(final ServerInfo server) {
        final JsonObject serverInfos = new JsonObject();

        serverInfos.addProperty("name", server.getName() == null ? "" : server.getName());
        serverInfos.addProperty("address", server.getAddress() == null ? "" : server.getAddress().getAddress().getHostAddress());
        serverInfos.addProperty("port", String.valueOf(server.getAddress().getPort()));
        serverInfos.addProperty("motd", server.getMotd() == null ? "" : server.getMotd());

        return serverInfos;
    }

}
