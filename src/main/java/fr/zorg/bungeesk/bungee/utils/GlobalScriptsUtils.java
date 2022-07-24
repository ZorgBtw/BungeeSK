package fr.zorg.bungeesk.bungee.utils;

import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.Debug;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.common.packets.GlobalScriptsPacket;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class GlobalScriptsUtils {

    public static void sendGlobalScripts(SocketServer socketServer) {
        final File folder = new File(BungeeSK.getInstance().getDataFolder().getAbsolutePath(), "common-skript");
        if (!folder.exists()) {
            folder.mkdirs();
            return;
        }

        final HashMap<String, ArrayList<String>> scripts = new HashMap<>(); //Name | Script's lines
        for (File file : folder.listFiles()) {
            if (file.getName().endsWith(".sk")) {
                final String name = file.getName();
                final ArrayList<String> lines;
                try {
                    lines = (ArrayList<String>) Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
                    scripts.put(name, lines);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        final GlobalScriptsPacket packet = new GlobalScriptsPacket(scripts);
        socketServer.sendPacket(packet);
        Debug.log("Sent global scripts to " + socketServer.getSocket().getInetAddress().getHostAddress() + ":" + socketServer.getMinecraftPort());
    }

}