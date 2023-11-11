package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.SendTitlePacket;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class SendTitleListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof SendTitlePacket) {
            final SendTitlePacket sendTitlePacket = (SendTitlePacket) packet;

            final BungeePlayer bungeePlayer = sendTitlePacket.getBungeePlayer();
            final ProxiedPlayer proxiedPlayer = BungeeUtils.getPlayer(bungeePlayer);
            if (proxiedPlayer == null)
                return;

            final Title title = BungeeSK.getInstance().getProxy().createTitle();
            title.title(BungeeUtils.getTextComponent(sendTitlePacket.getTitle()));
            if (sendTitlePacket.getSubTitle() != null)
                title.subTitle(BungeeUtils.getTextComponent(sendTitlePacket.getSubTitle()));
            if (sendTitlePacket.getTime() != null)
                title.stay(sendTitlePacket.getTime().intValue());
            if (sendTitlePacket.getFadeIn() != null)
                title.fadeIn(sendTitlePacket.getFadeIn().intValue());
            if (sendTitlePacket.getFadeOut() != null)
                title.fadeOut(sendTitlePacket.getFadeOut().intValue());
            proxiedPlayer.sendTitle(title);
        }
    }

}