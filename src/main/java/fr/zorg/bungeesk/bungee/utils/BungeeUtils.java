package fr.zorg.bungeesk.bungee.utils;

import fr.zorg.bungeesk.bungee.BungeeSK;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.config.Configuration;

import java.util.HashMap;
import java.util.Map;
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

    public static Map<String, String> getBungeeServer(final ServerInfo server) {
        Map<String, String> infos = new HashMap<>();

        infos.put("name", server.getName());
        infos.put("address", server.getAddress().getAddress().getHostAddress());
        infos.put("port", String.valueOf(server.getAddress().getPort()));
        infos.put("motd", server.getMotd());

        return infos;
    }

}
