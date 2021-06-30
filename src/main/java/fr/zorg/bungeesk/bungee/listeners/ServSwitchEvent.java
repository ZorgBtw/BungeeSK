package fr.zorg.bungeesk.bungee.listeners;

import fr.zorg.bungeesk.bungee.BungeeSK;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.Map;

public class ServSwitchEvent implements Listener {

    @EventHandler
    public void onSwitch(ServerSwitchEvent e) {
        if (e.getFrom() == null) return;


        final ServerInfo server = e.getFrom();
        Map<String, String> serverMap = new HashMap<>();
        serverMap.put("name", server.getName());
        serverMap.put("address", server.getAddress().getAddress().getHostAddress());
        serverMap.put("port", String.valueOf(server.getAddress().getPort()));
        serverMap.put("motd", server.getMotd());

        Map<String, String> playerMap = new HashMap<>();
        playerMap.put("name", e.getPlayer().getName());
        playerMap.put("uniqueId", e.getPlayer().getUniqueId().toString());

        Map<String, Map<String, String>> map = new HashMap<>();
        map.put("server", serverMap);
        map.put("player", playerMap);

        BungeeSK.getInstance().getServer().writeRawAll(true, "eventBungeePlayerServerSwitch", map);
    }

}
