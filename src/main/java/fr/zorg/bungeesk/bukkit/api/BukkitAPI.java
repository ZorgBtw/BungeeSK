package fr.zorg.bungeesk.bukkit.api;

import fr.zorg.bungeesk.bukkit.packets.PacketClient;
import fr.zorg.bungeesk.bukkit.packets.SocketClient;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.utils.ReflectionUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    public BukkitAPI registerListeners(String packageName, JavaPlugin plugin) {

        try {
            final Method method = plugin.getClass().getSuperclass().getDeclaredMethod("getFile");
            method.setAccessible(true);
            final File file = (File) method.invoke(plugin);

            ReflectionUtils.getClassesFromPackage(packageName, BungeeSKBukkitListener.class, file).forEach((clazz) -> {
                try {
                    this.listeners.add((BungeeSKBukkitListener) clazz.getConstructor().newInstance());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException ex) {
                    ex.printStackTrace();
                }
            });
        } catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public BukkitAPI sendPacket(BungeeSKPacket packet) {
        PacketClient.sendPacket(packet);
        return this;
    }

    public List<BungeeSKBukkitListener> getListeners() {
        return this.listeners;
    }

    public SocketClient getClient() {
        return PacketClient.getClient();
    }

}
