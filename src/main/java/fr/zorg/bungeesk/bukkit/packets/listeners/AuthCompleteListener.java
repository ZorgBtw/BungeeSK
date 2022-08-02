package fr.zorg.bungeesk.bukkit.packets.listeners;

import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.api.BungeeSKBukkitListener;
import fr.zorg.bungeesk.bukkit.packets.PacketClient;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.ClientConnectEvent;
import fr.zorg.bungeesk.common.packets.AuthCompletePacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

public class AuthCompleteListener extends BungeeSKBukkitListener {

    @Override
    public void onReceive(BungeeSKPacket packet) {
        if (packet instanceof AuthCompletePacket) {
            final AuthCompletePacket authCompletePacket = (AuthCompletePacket) packet;
            final boolean encrypting = authCompletePacket.isEncrypting();
            PacketClient.getClient().setEncrypting(encrypting);
            PacketClient.sendPacket(new AuthCompletePacket(encrypting));
            BungeeSK.callEvent(new ClientConnectEvent());
        }
    }

}