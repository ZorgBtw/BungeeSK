package fr.zorg.bungeesk.common.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class Ping implements Serializable {

    private int protocolVersion;
    private String protocolMessage;
    private int players;
    private int maxPlayers;
    private ArrayList<String> hoverMessages;
    private String motd;
    private String favicon;

    public Ping(int protocolVersion, String protocolMessage, int players, int maxPlayers, ArrayList<String> hoverMessages, String motd, String favicon) {
        this.protocolVersion = protocolVersion;
        this.protocolMessage = protocolMessage;
        this.players = players;
        this.maxPlayers = maxPlayers;
        this.hoverMessages = hoverMessages;
        this.motd = motd;
        this.favicon = favicon;
    }

    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    public Ping setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
        return this;
    }

    public String getProtocolMessage() {
        return this.protocolMessage;
    }

    public Ping setProtocolMessage(String protocolMessage) {
        this.protocolMessage = protocolMessage;
        return this;
    }

    public int getPlayers() {
        return this.players;
    }

    public Ping setPlayers(int players) {
        this.players = players;
        return this;
    }

    public Ping setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public ArrayList<String> getHoverMessages() {
        return this.hoverMessages;
    }

    public Ping setHoverMessages(ArrayList<String> hoverMessages) {
        this.hoverMessages = hoverMessages;
        return this;
    }

    public String getMotd() {
        return this.motd;
    }

    public Ping setMotd(String motd) {
        this.motd = motd;
        return this;
    }

    public String getFavicon() {
        return this.favicon;
    }

    public Ping setFavicon(String favicon) {
        this.favicon = favicon;
        return this;
    }

}