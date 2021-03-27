package fr.zorg.bungeesk.bungee;

import fr.zorg.bungeesk.bungee.sockets.Server;
import fr.zorg.bungeesk.bungee.storage.BungeeConfig;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;

public class BungeeSK extends Plugin {

    public static BungeeSK instance;

    private Server server;

    @Override
    public void onEnable() {
        instance = this;
        this.getLogger().log(Level.INFO, ChatColor.GOLD + "BungeeSK has been succesfully started !");
        BungeeConfig.get().load();
        try {
            this.server = new Server(BungeeConfig.get().getPort(), BungeeConfig.get().getPassword());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        this.server.disconnect();
    }

    public static BungeeSK getInstance() {
        return instance;
    }

    public Server getServer() {
        return this.server;
    }
}