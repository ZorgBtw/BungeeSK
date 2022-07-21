package fr.zorg.bungeesk.bungee.utils;

import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.packets.PacketServer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.CompletableFutureResponsePacket;

import java.net.InetAddress;
import java.util.UUID;

public class FutureUtils {

    public static void initFuture(InetAddress address, UUID uuid, BungeeSKPacket input) {
        BungeeSK.getApi().getListeners().forEach(listener -> {
            try {
                listener.getClass().getMethod("onFutureRequest", UUID.class, InetAddress.class, BungeeSKPacket.class);
                final Object response = listener.onFutureRequest(uuid, address, input);
                if (response != null)
                    PacketServer.sendPacket(address, new CompletableFutureResponsePacket(uuid, response));
            } catch (NoSuchMethodException ignored) {
            }
        });
    }

}