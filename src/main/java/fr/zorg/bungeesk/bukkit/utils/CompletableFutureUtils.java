package fr.zorg.bungeesk.bukkit.utils;

import fr.zorg.bungeesk.bukkit.packets.PacketClient;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.CompletableFuturePacket;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CompletableFutureUtils {

    private static final Map<UUID, CompletableFuture<Object>> futures = new HashMap<>();

    public static Object generateFuture(BungeeSKPacket packet) {
        final UUID randomUUID = UUID.randomUUID(); // Using a random UUID here to prevent from mixing between 2 actions at the same time
        final CompletableFuture<Object> future = new CompletableFuture<>();
        futures.put(randomUUID, future);

        final CompletableFuturePacket completableFuturePacket = new CompletableFuturePacket(packet, randomUUID);
        PacketClient.sendPacket(completableFuturePacket);

        Object response = null;
        try {
            response = future.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ignored) {
        }

        return response == "NULL" ? null : response; //todo fix that, if value is 'NULL' on bungee then it will be interpreted as empty value
    }

    public static void completeFuture(UUID uuid, Object response) {
        if (futures.containsKey(uuid)) {
            futures.get(uuid).complete(response);
            futures.remove(uuid);
        }
    }

}