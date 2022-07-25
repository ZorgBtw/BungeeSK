package fr.zorg.bungeesk.bungee.utils;

import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.common.entities.EmptyFutureResponse;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.CompletableFutureResponsePacket;

import java.util.UUID;

public class FutureUtils {

    public static void initFuture(SocketServer socketServer, UUID uuid, BungeeSKPacket input) {
        BungeeSK.getApi().getListeners().forEach(listener -> {
            try {
                listener.getClass().getMethod("onFutureRequest", UUID.class, SocketServer.class, BungeeSKPacket.class);
                final Object response = listener.onFutureRequest(uuid, socketServer, input);
                if (response != null) {
                    socketServer.sendPacket(
                            new CompletableFutureResponsePacket(
                                    uuid,
                                    response instanceof EmptyFutureResponse ? null : response
                            ));
                }
            } catch (NoSuchMethodException ignored) {
            }
        });
    }

}