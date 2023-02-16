package fr.zorg.bungeesk.bungee.utils;

import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.utils.BungeeConfig;

public class Debug {

    public static void log(String... text) {
        if ((boolean) BungeeConfig.DEBUG.get())
            for (String line : text)
                BungeeSK.getInstance().getLogger().info(line);
    }

    public static void throwEx(Exception ex) {
        if ((boolean) BungeeConfig.DEBUG.get())
            ex.printStackTrace();
    }

}