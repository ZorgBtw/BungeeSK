package fr.zorg.velocitysk.packets.listeners;

import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GlobalScriptsRequestPacket;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;
import fr.zorg.velocitysk.storage.GlobalScripts;

public class GlobalScriptsRequestListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof GlobalScriptsRequestPacket) {
            GlobalScripts.sendGlobalScripts(socketServer);
        }
    }

}