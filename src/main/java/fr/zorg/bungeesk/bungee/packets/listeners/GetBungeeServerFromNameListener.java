package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.entities.EmptyFutureResponse;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GetBungeeServerFromNamePacket;

import java.net.InetAddress;
import java.util.UUID;

public class GetBungeeServerFromNameListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, InetAddress address, BungeeSKPacket packet) {
        if (packet instanceof GetBungeeServerFromNamePacket) {
            final GetBungeeServerFromNamePacket getBungeeServerFromNamePacket = (GetBungeeServerFromNamePacket) packet;
            final String name = getBungeeServerFromNamePacket.getName();
            return BungeeUtils.getServerFromName(name) == null ? new EmptyFutureResponse() : BungeeUtils.getServerFromName(name);
        }
        return null;
    }

}