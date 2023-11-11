package fr.zorg.bungeesk.bukkit.packets.listeners;

import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.api.BungeeSKBukkitListener;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.GlobalScriptReceiveEvent;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GlobalScriptsPacket;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class GlobalScriptsListener extends BungeeSKBukkitListener {

    @Override
    public void onReceive(BungeeSKPacket packet) {
        if (packet instanceof GlobalScriptsPacket) {
            final GlobalScriptsPacket globalScriptsPacket = (GlobalScriptsPacket) packet;
            final HashMap<String, ArrayList<String>> scripts = globalScriptsPacket.getScripts();

            if (scripts.isEmpty())
                return;

            final File folder = new File("plugins/Skript/scripts/BungeeSK");

            if (!folder.exists())
                folder.mkdirs();

            scripts.forEach((name, lines) -> {
                BungeeSK.callEvent(new GlobalScriptReceiveEvent(name));
                final File file = new File(folder, name);
                if (file.exists())
                    file.delete();
                try {
                    file.createNewFile();
                    Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

        }
    }

}