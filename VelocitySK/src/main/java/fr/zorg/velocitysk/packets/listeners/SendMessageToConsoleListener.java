package fr.zorg.velocitysk.packets.listeners;

import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.SendMessageToConsolePacket;
import fr.zorg.velocitysk.BungeeSK;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;

public class SendMessageToConsoleListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof SendMessageToConsolePacket) {
            final SendMessageToConsolePacket sendMessageToConsolePacket = (SendMessageToConsolePacket) packet;
            final String message = sendMessageToConsolePacket.getMessage();
            BungeeSK.getLogger().info(message);
        }
    }

}