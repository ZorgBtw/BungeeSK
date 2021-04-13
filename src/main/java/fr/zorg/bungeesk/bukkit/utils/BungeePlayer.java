package fr.zorg.bungeesk.bukkit.utils;

public class BungeePlayer {

    private final String player;
    private final String uuid;

    public BungeePlayer(String player, String uuid) {
        this.player = player;
        this.uuid = uuid;
    }

    public String getPlayer() {
        return this.player;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getData() {
        return this.player + "$" + this.uuid;
    }

}
