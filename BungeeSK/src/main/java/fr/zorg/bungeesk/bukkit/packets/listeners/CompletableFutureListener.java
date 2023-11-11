package fr.zorg.bungeesk.bukkit.packets.listeners;

import fr.zorg.bungeesk.bukkit.api.BungeeSKBukkitListener;
import fr.zorg.bungeesk.bukkit.utils.CompletableFutureUtils;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.CompletableFuturePacket;

public class CompletableFutureListener extends BungeeSKBukkitListener {

    @Override
    public void onReceive(BungeeSKPacket packet) {
        if (packet instanceof CompletableFuturePacket) {
            final CompletableFuturePacket completableFuturePacket = (CompletableFuturePacket) packet;
            CompletableFutureUtils.initFuture(completableFuturePacket.getUuid(), completableFuturePacket.getPacket());
        }
    }

}