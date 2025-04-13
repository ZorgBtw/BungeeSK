package fr.zorg.velocitysk.api;

import fr.zorg.bungeesk.common.utils.ReflectionUtils;
import fr.zorg.velocitysk.BungeeSK;
import fr.zorg.velocitysk.packets.PacketServer;
import fr.zorg.velocitysk.packets.SocketServer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class BungeeAPI {

    private final List<BungeeSKListener> listeners;

    public BungeeAPI() {
        this.listeners = new ArrayList<>();
    }

    public BungeeAPI registerListener(BungeeSKListener listener) {
        this.listeners.add(listener);
        return this;
    }

    public BungeeAPI registerListeners(String packageName) {
        try {
            ReflectionUtils.getClassesFromPackage(packageName, BungeeSKListener.class,
                    BungeeSK.getServer().getPluginManager().getPlugin("bungeesk").get().getDescription().getSource().get().toFile()
            ).forEach((clazz) -> {
                try {
                    this.listeners.add((BungeeSKListener) clazz.getConstructor().newInstance());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException ex) {
                    ex.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public List<BungeeSKListener> getListeners() {
        return listeners;
    }

    public List<SocketServer> getClients() {
        return PacketServer.getClientSockets();
    }

}