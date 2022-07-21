package fr.zorg.bungeesk.bungee;

public class Debug {

    public static void log(String... text) {
        if (BungeeConfig.DEBUG.get())
            for (String line : text)
                BungeeSK.getInstance().getLogger().info(line);
    }

}