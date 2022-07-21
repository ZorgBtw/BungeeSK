package fr.zorg.bungeesk.bungee;

import fr.zorg.bungeesk.bungee.packets.PacketServer;
import fr.zorg.bungeesk.bungee.utils.GlobalVariablesUtils;
import fr.zorg.bungeesk.common.AutoUpdater;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public class BungeeSK extends Plugin {

    private static BungeeSK instance;
    private static BungeeAPI api;

    @Override
    public void onEnable() {
        instance = this;
        api = new BungeeAPI();

        this.launchAutoUpdater();
        BungeeConfig.init();
        GlobalVariablesUtils.init();
        PacketServer.start();

        api.registerListeners("fr.zorg.bungeesk.bungee.packets.listeners");
    }

    public static BungeeSK getInstance() {
        return instance;
    }

    private void launchAutoUpdater() {
        this.getProxy().getScheduler().schedule(this, () -> {
            if (AutoUpdater.isUpToDate(this.getDescription().getVersion())) {
                this.getLogger().warning("BungeeSK is not up to date ! Please download the latest version here: https//github.com/ZorgBtw/BungeeSK/releases/latest");
            }
        }, 0, 1L, TimeUnit.DAYS); // Everyday
    }

    public static void runAsync(Runnable task) {
        instance.getProxy().getScheduler().runAsync(instance, task);
    }

    public static BungeeAPI getApi() {
        return api;
    }

}