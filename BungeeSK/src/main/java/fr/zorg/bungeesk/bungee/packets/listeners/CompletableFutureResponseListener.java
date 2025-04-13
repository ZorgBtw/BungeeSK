package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.bungee.utils.FutureUtils;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.CompletableFutureResponsePacket;

public class CompletableFutureResponseListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof CompletableFutureResponsePacket) {
            final CompletableFutureResponsePacket completableFutureResponsePacket = (CompletableFutureResponsePacket) packet;
            FutureUtils.completeFuture(completableFutureResponsePacket.getUuid(), completableFutureResponsePacket.getResponse());
        }
    }
}
