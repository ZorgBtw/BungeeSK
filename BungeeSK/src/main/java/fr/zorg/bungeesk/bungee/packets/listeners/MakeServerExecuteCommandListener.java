package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.PacketServer;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.ExecuteCommandPacket;
import fr.zorg.bungeesk.common.packets.MakeServerExecuteCommandPacket;

public class MakeServerExecuteCommandListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof MakeServerExecuteCommandPacket) {
            final MakeServerExecuteCommandPacket makeServerExecuteCommandPacket = (MakeServerExecuteCommandPacket) packet;
            final String command = makeServerExecuteCommandPacket.getCommand();
            final Object toSend = makeServerExecuteCommandPacket.getToSend();
            if (toSend instanceof String) {
                if (((String) toSend).equalsIgnoreCase("all")) {
                    PacketServer.broadcastPacket(new ExecuteCommandPacket(command));
                    return;
                }
                BungeeSK.getInstance().getProxy().getPluginManager().dispatchCommand(BungeeSK.getInstance().getProxy().getConsole(), command);
                return;
            }
            if (toSend instanceof BungeeServer) {
                final BungeeServer bungeeServer = (BungeeServer) toSend;
                final SocketServer server = BungeeUtils.getSocketFromBungeeServer(bungeeServer);
                if (server != null)
                    server.sendPacket(new ExecuteCommandPacket(command));
            }
        }
    }

}