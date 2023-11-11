package fr.zorg.bungeesk.common.entities;

import java.io.Serializable;
import java.util.UUID;

public class BungeePlayer implements Serializable {

    private final String name;
    private final UUID uuid;

    public BungeePlayer(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public UUID getUuid() {
        return this.uuid;
    }

}