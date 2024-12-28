package fr.zorg.velocitysk.utils;

import fr.zorg.velocitysk.BungeeSK;

public class Debug {

    public static void log(String... text) {
        if ((boolean) BungeeConfig.DEBUG.get())
            for (String line : text)
                BungeeSK.getLogger().info(line);
    }

    public static void throwEx(Exception ex) {
        if ((boolean) BungeeConfig.DEBUG.get())
            ex.printStackTrace();
    }

}