package fr.zorg.bungeesk.bungee;

import fr.zorg.bungeesk.bungee.api.BungeeListener;
import fr.zorg.bungeesk.bungee.packets.PacketServer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

import java.util.ArrayList;
import java.util.List;

public class BungeeAPI {

    private final List<BungeeListener> listeners;

    protected BungeeAPI() {
        this.listeners = new ArrayList<>();
    }

    public BungeeAPI registerListener(BungeeListener listener) {
        this.listeners.add(listener);
        return this;
    }

    public BungeeAPI registerPacket(Class<? extends BungeeSKPacket> clazz) {
        PacketServer.getServer().getKryo().register(clazz);
        return this;
    }

    public List<BungeeListener> getListeners() {
        return listeners;
    }

}