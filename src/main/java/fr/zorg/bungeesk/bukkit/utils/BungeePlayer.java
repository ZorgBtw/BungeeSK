package fr.zorg.bungeesk.bukkit.utils;

public class BungeePlayer {

    private final String name;
    private final String uuid;

    public BungeePlayer(final String name, final String uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public String getUuid() {
        return this.uuid;
    }

}
