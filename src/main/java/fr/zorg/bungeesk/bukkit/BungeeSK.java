package fr.zorg.bungeesk.bukkit;

import fr.zorg.bungeesk.bukkit.packets.PacketClient;
import fr.zorg.bungeesk.common.AutoUpdater;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class BungeeSK extends JavaPlugin {

    private static BukkitAPI api;
    private static final char[] password = "".toCharArray();

    @Override
    public void onEnable() {
        this.launchAutoUpdater();
        api = new BukkitAPI();

        api.registerListeners("fr.zorg.bungeesk.bukkit.packets.listeners");

        try {
            PacketClient.start(InetAddress.getByName("localhost"), 20000);
        } catch (UnknownHostException ignored) {
        }
    }

    public static BungeeSK getInstance() {
        return JavaPlugin.getPlugin(BungeeSK.class);
    }

    private void launchAutoUpdater() {
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            if (AutoUpdater.isUpToDate(this.getDescription().getVersion())) {
                this.getLogger().warning("BungeeSK is not up to date ! Please download the latest version here: https//github.com/ZorgBtw/BungeeSK/releases/latest");
            }
        }, 10L, 1728000L); // Everyday
    }

    public static BukkitAPI getApi() {
        return api;
    }

    public static char[] getPassword() {
        return password;
    }

}