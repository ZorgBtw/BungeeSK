package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.commands.BungeeSKCommand;
import fr.zorg.bungeesk.bungee.packets.PacketServer;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.bungee.storage.GlobalScripts;
import fr.zorg.bungeesk.bungee.utils.BungeeConfig;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.AuthCompletePacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.BungeeServerStartPacket;

public class AuthCompleteListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof AuthCompletePacket) {
            socketServer.authenticate();

            if (BungeeConfig.FILES$SYNC_AT_CONNECT.get())
                GlobalScripts.sendGlobalScripts(socketServer);

            final BungeeServer server = BungeeUtils.getServerFromSocket(socketServer);

            if (BungeeConfig.MESSAGES.get()) {
                if (server != null)
                    BungeeSK.getInstance().getLogger()
                            .info(BungeeSKCommand.PREFIX +
                                    "§7New server connected: §a" + server.getAddress().getHostAddress() + ":" + server.getPort() +
                                    " §f(§3" + server.getName() + "§f)");
                else
                    BungeeSK.getInstance().getLogger()
                            .info(BungeeSKCommand.PREFIX +
                                    "§7New server connected: §a" + socketServer.getSocket().getInetAddress().getHostAddress() + ":" + socketServer.getMinecraftPort());
            }

            if (server != null)
                PacketServer.broadcastPacket(new BungeeServerStartPacket(server));
        }
    }

}