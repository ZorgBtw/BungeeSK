package fr.zorg.bungeesk.bukkit;

import fr.zorg.bungeesk.bukkit.api.BungeeSKBukkitListener;
import fr.zorg.bungeesk.bukkit.packets.PacketClient;
import fr.zorg.bungeesk.bukkit.packets.SocketClient;
import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

public class BukkitAPI {

    private final List<BungeeSKBukkitListener> listeners;

    protected BukkitAPI() {
        this.listeners = new ArrayList<>();
    }

    public BukkitAPI registerListener(BungeeSKBukkitListener listener) {
        this.listeners.add(listener);
        return this;
    }
    public BukkitAPI registerListeners(String packageName) {
        new Reflections(packageName).getSubTypesOf(BungeeSKBukkitListener.class).forEach(clazz -> {
            try {
                this.registerListener(clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        });
        return this;
    }

    public BukkitAPI sendPacket(BungeeSKPacket packet) {
        this.getClient().send(packet);
        return this;
    }

    public List<BungeeSKBukkitListener> getListeners() {
        return this.listeners;
    }

    public SocketClient getClient() {
        return PacketClient.getClient();
    }

}
