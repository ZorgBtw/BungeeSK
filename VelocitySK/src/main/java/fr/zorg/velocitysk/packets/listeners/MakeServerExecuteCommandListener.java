package fr.zorg.velocitysk.packets.listeners;

import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.ExecuteCommandPacket;
import fr.zorg.bungeesk.common.packets.MakeServerExecuteCommandPacket;
import fr.zorg.velocitysk.BungeeSK;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.PacketServer;
import fr.zorg.velocitysk.packets.SocketServer;
import fr.zorg.velocitysk.utils.VelocityUtils;

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
                BungeeSK.getServer().getCommandManager().executeAsync(BungeeSK.getServer().getConsoleCommandSource(), command);
                return;
            }
            if (toSend instanceof BungeeServer) {
                final BungeeServer bungeeServer = (BungeeServer) toSend;
                final SocketServer server = VelocityUtils.getSocketFromBungeeServer(bungeeServer);
                if (server != null)
                    server.sendPacket(new ExecuteCommandPacket(command));
            }
        }
    }

}