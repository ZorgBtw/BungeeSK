package fr.zorg.bungeesk.bungee;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.PacketServer;
import fr.zorg.bungeesk.bungee.packets.listeners.HandshakeListener;
import fr.zorg.bungeesk.common.AutoUpdater;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import net.md_5.bungee.api.plugin.Plugin;

import java.net.InetAddress;
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
        PacketServer.start();

        api.registerListeners("fr.zorg.bungeesk.bungee.packets.listeners");
    }

    public static BungeeSK getInstance() {
        return instance;
    }

    private void launchAutoUpdater() {
        this.getProxy().getScheduler().schedule(this, () -> {
            if (AutoUpdater.isUpdated(this.getDescription().getVersion())) {
                this.getLogger().warning("BungeeSK is not up to date ! Please download the latest version here: https//github.com/ZorgBtw/BungeeSK/releases/latest");
            }
        }, 0, 1L, TimeUnit.DAYS); // Everyday
    }

    public void dispatchPacket(InetAddress address, BungeeSKPacket packet) {
        for (BungeeSKListener listener : api.getListeners()) {
            try {
                listener.getClass().getMethod("onReceive", BungeeServer.class, BungeeSKPacket.class);
                listener.onReceive(address, packet);
            } catch (NoSuchMethodException ignored) {
            }
        }
    }

    public static BungeeAPI getApi() {
        return api;
    }

}