package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.PacketServer;
import fr.zorg.bungeesk.common.packets.BroadcastMessagePacket;
import fr.zorg.bungeesk.common.packets.BroadcastMessageToServerPacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

import java.net.InetAddress;

public class BroadcastMessageToServerListener extends BungeeSKListener {

    @Override
    public void onReceive(InetAddress address, BungeeSKPacket packet) { //wow code here
        if (packet instanceof BroadcastMessageToServerPacket) {
            final BroadcastMessageToServerPacket broadcastMessageToServerPacket = (BroadcastMessageToServerPacket) packet;
            PacketServer
                    .getClientSockets()
                    .stream()
                    .filter(clientSocket ->
                            clientSocket
                                    .getSocket()
                                    .getInetAddress()
                                    .getHostAddress()
                                    .equalsIgnoreCase(
                                            broadcastMessageToServerPacket
                                                    .getBungeeServer()
                                                    .getAddress()
                                                    .getHostAddress()
                                    ) &&
                                    clientSocket
                                            .getMinecraftPort() == broadcastMessageToServerPacket
                                            .getBungeeServer()
                                            .getPort()
                    )
                    .findFirst()
                    .ifPresent(clientSocket ->
                            clientSocket
                                    .send(
                                            new BroadcastMessagePacket(broadcastMessageToServerPacket.getMessage())
                                    )
                    );
        }
    }
}