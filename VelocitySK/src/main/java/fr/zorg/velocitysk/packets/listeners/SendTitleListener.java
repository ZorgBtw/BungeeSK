package fr.zorg.velocitysk.packets.listeners;

import com.velocitypowered.api.proxy.Player;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.SendTitlePacket;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;
import fr.zorg.velocitysk.utils.VelocityUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class SendTitleListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof SendTitlePacket) {
            final SendTitlePacket sendTitlePacket = (SendTitlePacket) packet;

            final BungeePlayer bungeePlayer = sendTitlePacket.getBungeePlayer();
            final Player player = VelocityUtils.getPlayer(bungeePlayer);
            if (player == null)
                return;

            Title title;
            final Component titleComponent = VelocityUtils.getTextComponent(sendTitlePacket.getTitle());
            final Component subTitle = sendTitlePacket.getSubTitle() == null ? Component.empty() : VelocityUtils.getTextComponent(sendTitlePacket.getSubTitle());
            Title.Times times;

            if (sendTitlePacket.getTime() != null) {
                if (sendTitlePacket.getFadeIn() != null && sendTitlePacket.getFadeOut() != null) {
                    times = Title.Times.times(
                            Duration.of(sendTitlePacket.getFadeIn().intValue(), TimeUnit.SECONDS.toChronoUnit()),
                            Duration.of(sendTitlePacket.getTime().intValue(), TimeUnit.SECONDS.toChronoUnit()),
                            Duration.of(sendTitlePacket.getFadeOut().intValue(), TimeUnit.SECONDS.toChronoUnit())
                    );
                } else if (sendTitlePacket.getFadeIn() != null) {
                    times = Title.Times.times(
                            Duration.of(sendTitlePacket.getFadeIn().intValue(), TimeUnit.SECONDS.toChronoUnit()),
                            Duration.of(sendTitlePacket.getTime().intValue(), TimeUnit.SECONDS.toChronoUnit()),
                            Duration.ZERO
                    );
                } else if (sendTitlePacket.getFadeOut() != null) {
                    times = Title.Times.times(
                            Duration.ZERO,
                            Duration.of(sendTitlePacket.getTime().intValue(), TimeUnit.SECONDS.toChronoUnit()),
                            Duration.of(sendTitlePacket.getFadeOut().intValue(), TimeUnit.SECONDS.toChronoUnit())
                    );
                } else {
                    times = Title.Times.times(
                            Duration.ZERO,
                            Duration.of(sendTitlePacket.getTime().intValue(), TimeUnit.SECONDS.toChronoUnit()),
                            Duration.ZERO
                    );
                }
                title = Title.title(titleComponent, subTitle, times);

            } else {
                title = Title.title(titleComponent, subTitle);
            }

            player.showTitle(title);
        }
    }

}