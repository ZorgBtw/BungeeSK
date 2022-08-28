package fr.zorg.bungeesk.bungee.utils;

import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.packets.PacketServer;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.common.entities.EmptyFutureResponse;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.CompletableFuturePacket;
import fr.zorg.bungeesk.common.packets.CompletableFutureResponsePacket;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureUtils {

    public static void completeFuture(SocketServer socketServer, UUID uuid, BungeeSKPacket input) {
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

    private static final Map<UUID, CompletableFuture<Object>> futures = new HashMap<>();

    public static Object generateFuture(SocketServer server, BungeeSKPacket packet) {

        if (!PacketServer.isConnected())
            return null;

        final UUID randomUUID = UUID.randomUUID(); // Using a random UUID here to prevent from mixing between 2 actions at the same time
        final CompletableFuture<Object> future = new CompletableFuture<>();
        futures.put(randomUUID, future);

        final CompletableFuturePacket completableFuturePacket = new CompletableFuturePacket(packet, randomUUID);
        PacketServer.sendPacket(server.getSocket().getInetAddress(), completableFuturePacket);

        Object response = null;
        try {
            response = future.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ignored) {
        }

        return response;
    }

    public static void completeFuture(UUID uuid, Object response) {
        if (futures.containsKey(uuid)) {
            futures.get(uuid).complete(response);
            futures.remove(uuid);
        }
    }


}