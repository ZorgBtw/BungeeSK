package fr.zorg.bungeesk.bungee.listeners;

import com.google.gson.JsonObject;
import fr.zorg.bungeesk.bungee.BungeeSK;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServSwitchEvent implements Listener {

    @EventHandler
    public void onSwitch(ServerSwitchEvent e) {
        if (e.getFrom() == null || e.getFrom().getAddress() == null) return;


        final ServerInfo server = e.getFrom();
        final JsonObject serverInfos = new JsonObject();
        serverInfos.addProperty("name", server.getName() == null ? "" : server.getName());
        serverInfos.addProperty("address", server.getAddress().getAddress().getHostAddress() == null ? "" : server.getAddress().getAddress().getHostAddress());
        serverInfos.addProperty("port", String.valueOf(server.getAddress().getPort()));
        serverInfos.addProperty("motd", server.getMotd() == null ? "" : server.getMotd());

        final JsonObject playerInfos = new JsonObject();
        playerInfos.addProperty("name", e.getPlayer().getName());
        playerInfos.addProperty("uniqueId", e.getPlayer().getUniqueId().toString());

        final JsonObject infos = new JsonObject();
        infos.add("server", serverInfos);
        infos.add("player", playerInfos);

        BungeeSK.getInstance().getServer().writeRawAll(true, "eventBungeePlayerServerSwitch", infos);
    }

}
