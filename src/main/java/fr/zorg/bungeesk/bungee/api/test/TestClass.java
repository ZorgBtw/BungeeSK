package fr.zorg.bungeesk.bungee.api.test;

import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.api.BungeeListener;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

public class TestClass extends BungeeListener {

    public static void main(String[] args) {
        BungeeSK.getApi().registerListener(new TestClass());
    }

    @Override
    public void onSend(BungeeServer server, BungeeSKPacket packet) {
        System.out.println("server = " + server);
        System.out.println(packet);
    }

    @Override
    public void onReceive(BungeeServer server, BungeeSKPacket packet) {
        this.onSend(server, packet);
    }
}