package fr.zorg.bungeesk.bungee;

import fr.zorg.bungeesk.bungee.listeners.LeaveEvent;
import fr.zorg.bungeesk.bungee.listeners.LoginEvent;
import fr.zorg.bungeesk.bungee.sockets.Server;
import fr.zorg.bungeesk.bungee.storage.BungeeConfig;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class BungeeSK extends Plugin {

    public static BungeeSK instance;
    private Server server;

    @Override
    public void onEnable() {
        instance = this;

        this.getLogger().log(Level.INFO, ChatColor.GOLD + "BungeeSK has been successfully started !");
        BungeeConfig.get().load();

        this.getProxy().getPluginManager().registerListener(this, new LoginEvent());
        this.getProxy().getPluginManager().registerListener(this, new LeaveEvent());

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
            try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null)
                    list.add(line);
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
