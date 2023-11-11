package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.bungee.utils.FutureUtils;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.CompletableFuturePacket;

public class CompletableFutureListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof CompletableFuturePacket) {
            final CompletableFuturePacket completableFuturePacket = (CompletableFuturePacket) packet;
            FutureUtils.completeFuture(socketServer, completableFuturePacket.getUuid(), completableFuturePacket.getPacket());
        }
    }

}