package fr.zorg.bungeesk.bungee;

import fr.zorg.bungeesk.bungee.listeners.LeaveEvent;
import fr.zorg.bungeesk.bungee.listeners.LoginEvent;
import fr.zorg.bungeesk.bungee.listeners.ServSwitchEvent;
import fr.zorg.bungeesk.bungee.sockets.Server;
import fr.zorg.bungeesk.bungee.storage.BungeeConfig;
import fr.zorg.bungeesk.common.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class BungeeSK extends Plugin {

    public static BungeeSK instance;
    private Server server;
    private PluginManager pm;

    @Override
    public void onEnable() {
        instance = this;
        pm = getProxy().getPluginManager();

        this.getLogger().log(Level.INFO, ChatColor.GOLD + "BungeeSK has been successfully started !");
        BungeeConfig.get().load();

        pm.registerListener(this, new LoginEvent());
        pm.registerListener(this, new LeaveEvent());
        pm.registerListener(this, new ServSwitchEvent());

        final File file = new File(this.getDataFolder().getAbsolutePath(), "common-skript");
        if (!file.exists())
            file.mkdir();
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

    public List<String> filesToString() {
        final File folder = new File(this.getDataFolder().getAbsolutePath(), "common-skript");
        if (!folder.exists())
            return new ArrayList<>();
        final FilenameFilter filter = (dir, name) -> !name.replaceAll("--", "").startsWith("-") && name.endsWith(".sk");
        final ArrayList<String> list = new ArrayList<>();
        for (final File file : folder.listFiles(filter)) {
            list.add("newFile:" + file.getName());
            try {
                list.add(Utils.toBase64(FileUtils.readFileToByteArray(file)));
            } catch (IOException ignored) {

            }
            list.add("endFile");
        }
        return list;
    }

    public static BungeeSK getInstance() {
        return instance;
    }

    public Server getServer() {
        return this.server;
    }

}
