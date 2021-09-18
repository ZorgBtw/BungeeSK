package fr.zorg.bungeesk.bungee;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rockaport.alice.AliceContext;
import fr.zorg.bungeesk.bungee.listeners.LeaveEvent;
import fr.zorg.bungeesk.bungee.listeners.LoginEvent;
import fr.zorg.bungeesk.bungee.listeners.ServSwitchEvent;
import fr.zorg.bungeesk.bungee.sockets.ClientServer;
import fr.zorg.bungeesk.bungee.sockets.Server;
import fr.zorg.bungeesk.bungee.storage.BungeeConfig;
import fr.zorg.bungeesk.bungee.storage.GlobalVariables;
import fr.zorg.bungeesk.bungee.utils.Metrics;
import fr.zorg.bungeesk.common.encryption.GlobalEncryption;
import io.methvin.watcher.DirectoryChangeEvent;
import io.methvin.watcher.DirectoryWatcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.*;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchService;
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

        BungeeConfig.init();

        pm.registerListener(this, new LoginEvent());
        pm.registerListener(this, new LeaveEvent());
        pm.registerListener(this, new ServSwitchEvent());

        final File file = new File(this.getDataFolder().getAbsolutePath(), "common-skript");
        if (!file.exists())
            file.mkdir();

        this.globalVariables = new GlobalVariables();

        try {
            this.server = new Server(BungeeConfig.PORT.get(), BungeeConfig.PASSWORD.get());
            this.listenFileChanging();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        this.server.disconnect();
    }

    public JsonObject filesToString() {
        final File folder = new File(this.getDataFolder().getAbsolutePath(), "common-skript");
        if (!folder.exists())
            return new JsonObject();
        final FilenameFilter filter = (dir, name) -> !name.replaceAll("--", "").startsWith("-") && name.endsWith(".sk");
        final JsonObject files = new JsonObject();
        for (final File file : folder.listFiles(filter)) {
            try {
                final FileReader fr = new FileReader(file);
                final BufferedReader br = new BufferedReader(fr);
                final JsonArray lines = new JsonArray();
                int i = 0;
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                    i++;
                }
                fr.close();
                files.add(file.getName(), lines);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return files;
    }

    public void listenFileChanging() throws IOException {
        if (BungeeConfig.FILES$AUTO_UPDATE.get()) {
            final Path path = Paths.get(this.getDataFolder().getPath(), "common-skript");
            DirectoryWatcher watcher = DirectoryWatcher.builder()
                    .path(path)
                    .listener(e -> {
                        if (e.path().toString().toLowerCase().endsWith(".sk") && !(e.eventType().equals(DirectoryChangeEvent.EventType.DELETE)))
                            this.server.getClients().forEach(ClientServer::sendFiles);
                    })
                    .build();
            watcher.watchAsync();
        }
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
