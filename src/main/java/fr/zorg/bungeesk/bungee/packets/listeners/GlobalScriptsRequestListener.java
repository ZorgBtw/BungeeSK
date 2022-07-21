package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.utils.GlobalScriptsUtils;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GlobalScriptsRequestPacket;

import java.net.InetAddress;

public class GlobalScriptsRequestListener extends BungeeSKListener {

    @Override
    public void onReceive(InetAddress address, BungeeSKPacket packet) {
        if (packet instanceof GlobalScriptsRequestPacket) {
            GlobalScriptsUtils.sendGlobalScripts(address);
        }
    }

}