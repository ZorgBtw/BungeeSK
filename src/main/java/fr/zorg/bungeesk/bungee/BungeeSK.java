package fr.zorg.bungeesk.bungee;

import com.rockaport.alice.AliceContext;
import fr.zorg.bungeesk.bungee.listeners.LeaveEvent;
import fr.zorg.bungeesk.bungee.listeners.LoginEvent;
import fr.zorg.bungeesk.bungee.listeners.ServSwitchEvent;
import fr.zorg.bungeesk.bungee.sockets.Server;
import fr.zorg.bungeesk.bungee.storage.BungeeConfig;
import fr.zorg.bungeesk.bungee.storage.GlobalVariables;
import fr.zorg.bungeesk.bungee.utils.Metrics;
import fr.zorg.bungeesk.common.encryption.GlobalEncryption;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class BungeeSK extends Plugin {

    public static BungeeSK instance;
    private static GlobalEncryption encryption;
    private Server server;
    private PluginManager pm;
    private Metrics metrics;
    private GlobalVariables globalVariables;

    @Override
    public void onEnable() {
        instance = this;

        this.metrics = new Metrics(this, 11146);

        encryption = new GlobalEncryption(AliceContext.Algorithm.AES, 10);
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
        this.globalVariables = new GlobalVariables();
    }

    @Override
    public void onDisable() {
        this.server.disconnect();
    }

    public Map<String, String[]> filesToString() {
        final File folder = new File(this.getDataFolder().getAbsolutePath(), "common-skript");
        if (!folder.exists())
            return new HashMap<>();
        final FilenameFilter filter = (dir, name) -> !name.replaceAll("--", "").startsWith("-") && name.endsWith(".sk");
        final Map<String, String[]> map = new HashMap<>();
        for (final File file : folder.listFiles(filter)) {
            try {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                List<String> lines = new ArrayList<>();
                int i = 0;
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                    i++;
                }
                fr.close();
                map.put(file.getName(), lines.toArray(new String[0]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static BungeeSK getInstance() {
        return instance;
    }

    public Server getServer() {
        return this.server;
    }

    public static GlobalEncryption getEncryption() {
        return encryption;
    }

}
