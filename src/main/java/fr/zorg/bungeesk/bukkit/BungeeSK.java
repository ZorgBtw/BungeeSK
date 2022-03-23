package fr.zorg.bungeesk.bukkit;

import fr.zorg.bungeesk.common.AutoUpdater;
import org.bukkit.plugin.java.JavaPlugin;

public class BungeeSK extends JavaPlugin {

    private BukkitAPI api;

    @Override
    public void onEnable() {
        this.launchAutoUpdater();
        this.api = new BukkitAPI();
    }

    public static BungeeSK getInstance() {
        return JavaPlugin.getPlugin(BungeeSK.class);
    }

    private void launchAutoUpdater() {
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            if (AutoUpdater.isUpdated(this.getDescription().getVersion())) {
                this.getLogger().warning("BungeeSK is not up to date ! Please download the latest version here: https//github.com/ZorgBtw/BungeeSK/releases/latest");
            }
        }, 10L, 1728000L); // Everyday
    }

    public BukkitAPI getApi() {
        return this.api;
    }
}
