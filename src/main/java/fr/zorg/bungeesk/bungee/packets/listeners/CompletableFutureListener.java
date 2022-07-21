package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.utils.FutureUtils;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.CompletableFuturePacket;

import java.net.InetAddress;

public class CompletableFutureListener extends BungeeSKListener {

    @Override
    public void onReceive(InetAddress address, BungeeSKPacket packet) {
        if (packet instanceof CompletableFuturePacket) {
            final CompletableFuturePacket completableFuturePacket = (CompletableFuturePacket) packet;
            FutureUtils.initFuture(address, completableFuturePacket.getUuid(), completableFuturePacket.getPacket());
        }
    }

}