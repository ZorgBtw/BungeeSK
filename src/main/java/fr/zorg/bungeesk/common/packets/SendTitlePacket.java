package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeePlayer;

public class SendTitlePacket implements BungeeSKPacket {

    private final BungeePlayer bungeePlayer;
    private final String title;
    private final String subTitle;
    private final Long time;
    private final Long fadeIn;
    private final Long fadeOut;

    public SendTitlePacket(BungeePlayer bungeePlayer, String title, String subTitle, Long time, Long fadeIn, Long fadeOut) {
        this.bungeePlayer = bungeePlayer;
        this.title = title;
        this.subTitle = subTitle;
        this.time = time;
        this.fadeIn = fadeIn;
        this.fadeOut = fadeOut;
    }

    public BungeePlayer getBungeePlayer() {
        return this.bungeePlayer;
    }

    public String getTitle() {
        return this.title;
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public Long getTime() {
        return this.time;
    }

    public Long getFadeIn() {
        return this.fadeIn;
    }

    public Long getFadeOut() {
        return this.fadeOut;
    }

}