package fr.zorg.bungeesk.bukkit;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import fr.zorg.bungeesk.bukkit.api.BukkitAPI;
import fr.zorg.bungeesk.bukkit.utils.Metrics;
import fr.zorg.bungeesk.common.AutoUpdater;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class BungeeSK extends JavaPlugin implements Listener {

    private static BukkitAPI api;
    private Metrics metrics;

    @Override
    public void onEnable() {
        this.launchAutoUpdater();
        api = new BukkitAPI();
        this.metrics = new Metrics(this, 10655);

        api.registerListeners("fr.zorg.bungeesk.bukkit.packets.listeners", this);

        final SkriptAddon addon = Skript.registerAddon(this);
        try {
            addon.loadClasses("fr.zorg.bungeesk.bukkit.skript");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        this.metrics.addCustomChart(new Metrics.SimplePie("skript_version", () -> Skript.getVersion().toString()));
    }

    public static BungeeSK getInstance() {
        return JavaPlugin.getPlugin(BungeeSK.class);
    }

    public static void runAsync(Runnable task) {
        new Thread(task).start();
    }

    private void launchAutoUpdater() {
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            if (!AutoUpdater.isUpToDate(this.getDescription().getVersion())) {
                this.getLogger().warning("BungeeSK is not up to date ! Please download the latest version here: https//github.com/ZorgBtw/BungeeSK/releases/latest");
            }
        }, 10L, 1728000L); // Everyday
    }

    public static BukkitAPI getApi() {
        return api;
    }

    public static void callEvent(Event event) {
        getInstance().getServer().getScheduler().runTask(getInstance(), () -> {
            getInstance().getServer().getPluginManager().callEvent(event);
        });
    }

    public static int getMinecraftPort() {
        return getInstance().getServer().getPort();
    }

}