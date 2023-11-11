package fr.zorg.velocitysk.utils;

import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;
import fr.zorg.bungeesk.common.entities.Ping;
import fr.zorg.bungeesk.common.packets.PingGetPacket;
import fr.zorg.bungeesk.common.utils.Pair;
import fr.zorg.velocitysk.packets.SocketServer;
import net.kyori.adventure.text.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PingUtils {

    private static SocketServer pingListener;
    private static final Pair<String, Favicon> cachedFavicon = Pair.from("", null);

    public static ServerPing getPing(ServerPing serverPing, InetAddress address) {
        if (pingListener == null || !pingListener.isConnected())
            return serverPing;

        final ArrayList<String> hoverList = new ArrayList<>();
        if (serverPing.getPlayers().isPresent() && serverPing.getPlayers().get().getSample() != null)
            Arrays.stream(serverPing.getPlayers().get().getSample().toArray(new ServerPing.SamplePlayer[0]))
                    .forEach(playerInfo -> {
                        hoverList.add(playerInfo.getName());
                    });

        final Ping initialPing = new Ping(
                serverPing.getVersion().getProtocol(),
                serverPing.getVersion().getName(),
                serverPing.getPlayers().get().getOnline(),
                serverPing.getPlayers().get().getMax(),
                hoverList,
                serverPing.getDescriptionComponent().examinableName(),
                serverPing.getFavicon().isPresent() ? serverPing.getFavicon().get().getBase64Url() : ""
        );
        final PingGetPacket packet = new PingGetPacket(initialPing, address);
        final Ping returnedPing = (Ping) FutureUtils.generateFuture(pingListener, packet);

        if (returnedPing == null)
            return serverPing;

        Favicon favicon = null;
        if (returnedPing.getFavicon() != null && (!returnedPing.getFavicon().equalsIgnoreCase(cachedFavicon.getFirstValue()))) {
            cachedFavicon.setFirstValue(returnedPing.getFavicon())
                    .setSecondValue(generateFavicon(returnedPing.getFavicon()));
        }

        favicon = cachedFavicon.getSecondValue();

        final List<ServerPing.SamplePlayer> samplePlayers = new ArrayList<>();
        for (int i = 0; i < returnedPing.getHoverMessages().size(); i++) {
            samplePlayers.add(new ServerPing.SamplePlayer(
                    returnedPing.getHoverMessages().get(i),
                    UUID.randomUUID()
            ));
        }


        final ServerPing.Version version = new ServerPing.Version(returnedPing.getProtocolVersion(), returnedPing.getProtocolMessage());
        final ServerPing.Players playerInfos = new ServerPing.Players(returnedPing.getPlayers(), returnedPing.getMaxPlayers(), samplePlayers);
        final Component motd = Component.text(returnedPing.getMotd());

        return new ServerPing(version, playerInfos, motd, favicon);
    }

    private static Favicon generateFavicon(String url) {
        try {
            final BufferedImage image = ImageIO.read(new URL(url));
            return Favicon.create(image);

        } catch (IOException ignored) {
        }
        return null;
    }

    public static void setPingListener(SocketServer pingListener) {
        PingUtils.pingListener = pingListener;
    }

}