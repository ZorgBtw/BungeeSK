package fr.zorg.bungeesk.bukkit.packets.listeners;

import fr.zorg.bungeesk.bukkit.api.BungeeSKBukkitListener;
import fr.zorg.bungeesk.bukkit.utils.CompletableFutureUtils;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.CompletableFutureResponsePacket;

import java.util.UUID;

public class CompletableFutureResponseListener extends BungeeSKBukkitListener {

    @Override
    public void onReceive(BungeeSKPacket packet) {
        if (packet instanceof CompletableFutureResponsePacket) {
            final CompletableFutureResponsePacket completableFutureResponsePacket = (CompletableFutureResponsePacket) packet;
            final UUID uuid = completableFutureResponsePacket.getUuid();
            final Object response = completableFutureResponsePacket.getResponse();
            CompletableFutureUtils.completeFuture(uuid, response);
        }
    }

}