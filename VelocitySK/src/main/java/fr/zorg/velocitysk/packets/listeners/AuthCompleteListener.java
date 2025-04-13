package fr.zorg.velocitysk.packets.listeners;

import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.AuthCompletePacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.BungeeServerStartPacket;
import fr.zorg.velocitysk.BungeeSK;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.commands.BungeeSKCommand;
import fr.zorg.velocitysk.packets.PacketServer;
import fr.zorg.velocitysk.packets.SocketServer;
import fr.zorg.velocitysk.storage.GlobalScripts;
import fr.zorg.velocitysk.utils.BungeeConfig;
import fr.zorg.velocitysk.utils.VelocityUtils;

public class AuthCompleteListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof AuthCompletePacket) {
            socketServer.authenticate();

            if (BungeeConfig.FILES$SYNC_AT_CONNECT.get())
                GlobalScripts.sendGlobalScripts(socketServer);

            final BungeeServer server = VelocityUtils.getServerFromSocket(socketServer);

            if (BungeeConfig.MESSAGES.get()) {
                if (server != null)
                    BungeeSK.getLogger()
                            .info(BungeeSKCommand.PREFIX +
                                    "§7New server connected: §a" + server.getAddress().getHostAddress() + ":" + server.getPort() +
                                    " §f(§3" + server.getName() + "§f)");
                else
                    BungeeSK.getLogger()
                            .info(BungeeSKCommand.PREFIX +
                                    "§7New server connected: §a" + socketServer.getSocket().getInetAddress().getHostAddress() + ":" + socketServer.getMinecraftPort());
            }

            if (server != null)
                PacketServer.broadcastPacket(new BungeeServerStartPacket(server));
        }
    }

}