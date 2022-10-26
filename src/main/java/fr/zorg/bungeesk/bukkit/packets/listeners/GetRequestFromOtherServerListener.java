package fr.zorg.bungeesk.bukkit.packets.listeners;

import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.api.BungeeSKBukkitListener;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.CustomRequestEvent;
import fr.zorg.bungeesk.common.entities.EmptyFutureResponse;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GetRequestFromOtherServerPacket;

import java.util.UUID;

public class GetRequestFromOtherServerListener extends BungeeSKBukkitListener {

    @Override
    public Object onFutureRequest(UUID uuid, BungeeSKPacket packet) {
        if (packet instanceof GetRequestFromOtherServerPacket) {
            final GetRequestFromOtherServerPacket getRequestFromOtherServerPacket = (GetRequestFromOtherServerPacket) packet;
            final CustomRequestEvent event = new CustomRequestEvent(getRequestFromOtherServerPacket.getRequest(), getRequestFromOtherServerPacket.getServer());
            BungeeSK.getInstance().getServer().getPluginManager().callEvent(event);
            return event.getResponse() == null ? new EmptyFutureResponse() : event.getResponse();
        }
        return null;
    }

}