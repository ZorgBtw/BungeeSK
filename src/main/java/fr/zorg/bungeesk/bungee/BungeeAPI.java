package fr.zorg.bungeesk.bungee;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.PacketServer;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import org.reflections.Reflections;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BungeeAPI {

    private final List<BungeeSKListener> listeners;

    protected BungeeAPI() {
        this.listeners = new ArrayList<>();
    }

    public BungeeAPI registerListener(BungeeSKListener listener) {
        this.listeners.add(listener);
        return this;
    }

    public BungeeAPI registerListeners(String packageName) {
        new Reflections(packageName).getSubTypesOf(BungeeSKListener.class).forEach(clazz -> {
            try {
                this.registerListener(clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        });
        return this;
    }

    public BungeeAPI sendPacket(InetAddress address, BungeeSKPacket packet) {
        this.getClients().stream().filter(client -> client.getSocket().getInetAddress().equals(address)).findFirst().ifPresent(client -> client.send(packet));
        return this;
    }

    public List<BungeeSKListener> getListeners() {
        return listeners;
    }

    public List<SocketServer> getClients() {
        return PacketServer.getClientSockets();
    }

    public Optional<SocketServer> getClientWithInetAddress(InetAddress address) {
        return this.getClients().stream().filter(client -> client.getSocket().getInetAddress().equals(address)).findFirst();
    }

}