package fr.zorg.bungeesk.bungee.api;

import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.packets.PacketServer;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.utils.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
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
            ReflectionUtils.getClassesFromPackage(packageName, BungeeSKListener.class, BungeeSK.getInstance().getFile()).forEach((clazz) -> {
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