package fr.zorg.velocitysk.packets.listeners;

import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.CompletableFutureResponsePacket;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;
import fr.zorg.velocitysk.utils.FutureUtils;

public class CompletableFutureResponseListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof CompletableFutureResponsePacket) {
            final CompletableFutureResponsePacket completableFutureResponsePacket = (CompletableFutureResponsePacket) packet;
            FutureUtils.completeFuture(completableFutureResponsePacket.getUuid(), completableFutureResponsePacket.getResponse());
        }
    }
}
