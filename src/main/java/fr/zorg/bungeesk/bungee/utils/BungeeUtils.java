package fr.zorg.bungeesk.bungee.utils;

import net.md_5.bungee.config.Configuration;

public class BungeeUtils {

    public static boolean addDefault(final Configuration config, final String path, final Object o) {
        if (config.get(path) == null) {
            config.set(path, o);
            return true;
        }
        return false;
    }

}
