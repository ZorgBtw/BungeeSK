package fr.zorg.bungeesk.bukkit;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.rockaport.alice.AliceContext;
import fr.zorg.bungeesk.bukkit.sockets.ConnectionClient;
import fr.zorg.bungeesk.bukkit.utils.Metrics;
import fr.zorg.bungeesk.common.encryption.GlobalEncryption;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BungeeSK extends JavaPlugin {

    private Metrics metrics;
    private static Logger logger;
    private static GlobalEncryption encryption;

    @Override
    public void onEnable() {
        this.metrics = new Metrics(this, 10655);
        encryption = new GlobalEncryption(AliceContext.Algorithm.AES, 10);

        logger = getLogger();
        final SkriptAddon addon = Skript.registerAddon(this);
        try {
            addon.loadClasses("fr.zorg.bungeesk.bukkit.skript");
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.metrics.addCustomChart(new Metrics.SimplePie("skript_version", () -> Skript.getVersion().toString()));
    }

    @Override
    public void onDisable() {
        if (isClientConnected()) {
            ConnectionClient.get().disconnect();
        }
    }

    public static BungeeSK getInstance() {
        return JavaPlugin.getPlugin(BungeeSK.class);
    }


    public static boolean isClientConnected() {
        if (ConnectionClient.get() != null && ConnectionClient.get().isConnected()) {
            return true;
        }
        logger.log(Level.WARNING, "BungeeSK client is not connected to any server !");
        return false;
    }

    public static GlobalEncryption getEncryption() {
        return encryption;
    }
}
