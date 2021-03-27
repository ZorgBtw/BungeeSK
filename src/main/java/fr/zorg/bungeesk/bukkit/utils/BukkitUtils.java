package fr.zorg.bungeesk.bukkit.utils;

import org.bukkit.configuration.file.YamlConfiguration;

public class BukkitUtils {

    public static boolean addDefault(final YamlConfiguration config, final String path, final Object o) {
        if (config.get(path) == null) {
            config.set(path, o);
            return true;
        }
        return false;
    }

}
