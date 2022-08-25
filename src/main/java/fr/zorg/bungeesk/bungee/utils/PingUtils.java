package fr.zorg.bungeesk.bungee.utils;

import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.common.entities.Ping;
import fr.zorg.bungeesk.common.packets.PingGetPacket;
import fr.zorg.bungeesk.common.utils.Pair;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class PingUtils {

    private static SocketServer pingListener;
    private static Pair<String, Favicon> cachedFavicon = Pair.from("", null);

    public static ServerPing getPing(ServerPing serverPing, InetAddress address) {
        if (pingListener == null || !pingListener.isConnected())
            return serverPing;

        final ArrayList<String> hoverList = new ArrayList<>();
        if (serverPing.getPlayers().getSample() != null)
            Arrays.stream(serverPing.getPlayers().getSample())
                    .forEach(playerInfo -> {
                        hoverList.add(playerInfo.getName());
                    });

        final Ping initialPing = new Ping(
                serverPing.getVersion().getProtocol(),
                serverPing.getVersion().getName(),
                serverPing.getPlayers().getOnline(),
                serverPing.getPlayers().getMax(),
                hoverList,
                serverPing.getDescriptionComponent().toLegacyText(),
                serverPing.getFaviconObject() == null ? "" : serverPing.getFaviconObject().getEncoded()
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

        final ServerPing.PlayerInfo[] playerInfos = new ServerPing.PlayerInfo[returnedPing.getHoverMessages().size()];
        for (int i = 0; i < returnedPing.getHoverMessages().size(); i++) {
            playerInfos[i] = new ServerPing.PlayerInfo(returnedPing.getHoverMessages().get(i), UUID.randomUUID());
        }


        final ServerPing.Protocol protocol = new ServerPing.Protocol(returnedPing.getProtocolMessage(), returnedPing.getProtocolVersion());
        final ServerPing.Players players = new ServerPing.Players(returnedPing.getMaxPlayers(), returnedPing.getPlayers(), playerInfos);
        final BaseComponent motd = new ComponentBuilder(returnedPing.getMotd()).getCurrentComponent();

        serverPing.setVersion(protocol);
        serverPing.setPlayers(players);
        serverPing.setDescriptionComponent(motd);
        serverPing.setFavicon(favicon);

        return serverPing;
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