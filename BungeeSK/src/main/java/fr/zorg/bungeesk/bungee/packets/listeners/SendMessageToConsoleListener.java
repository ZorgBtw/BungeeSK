package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.SendMessageToConsolePacket;

public class SendMessageToConsoleListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof SendMessageToConsolePacket) {
            final SendMessageToConsolePacket sendMessageToConsolePacket = (SendMessageToConsolePacket) packet;
            final String message = sendMessageToConsolePacket.getMessage();
            BungeeSK.getInstance().getLogger().info(message);
        }
    }

}