package fr.zorg.velocitysk.packets.listeners;

import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.CompletableFuturePacket;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;
import fr.zorg.velocitysk.utils.FutureUtils;

public class CompletableFutureListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof CompletableFuturePacket) {
            final CompletableFuturePacket completableFuturePacket = (CompletableFuturePacket) packet;
            FutureUtils.completeFuture(socketServer, completableFuturePacket.getUuid(), completableFuturePacket.getPacket());
        }
    }

}