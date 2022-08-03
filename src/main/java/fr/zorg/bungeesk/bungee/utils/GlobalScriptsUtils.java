package fr.zorg.bungeesk.bungee.utils;

import fr.zorg.bungeesk.bungee.BungeeConfig;
import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.Debug;
import fr.zorg.bungeesk.bungee.packets.PacketServer;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.common.packets.DeleteGlobalScriptPacket;
import fr.zorg.bungeesk.common.packets.GlobalScriptsPacket;
import io.methvin.watcher.DirectoryChangeEvent;
import io.methvin.watcher.DirectoryWatcher;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public static GlobalScriptsPacket getSingleScript(File scriptFile) {
        final HashMap<String, ArrayList<String>> scripts = new HashMap<>(); //Name | Script's lines
        if (scriptFile.getName().endsWith(".sk")) {
            final String name = scriptFile.getName();
            final ArrayList<String> lines;
            try {
                lines = (ArrayList<String>) Files.readAllLines(scriptFile.toPath(), StandardCharsets.UTF_8);
                scripts.put(name, lines);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        final GlobalScriptsPacket packet = new GlobalScriptsPacket(scripts);
        return packet;
    }

    public static void listenFileChanging() {
        if (BungeeConfig.FILES$AUTO_UPDATE.get()) {
            final Path path = Paths.get(BungeeSK.getInstance().getDataFolder().getPath(), "common-skript");
            try {
                final DirectoryWatcher watcher = DirectoryWatcher.builder()
                        .path(path)
                        .listener(e -> {
                            if (e.path().toString().toLowerCase().endsWith(".sk")) {
                                if (e.eventType().equals(DirectoryChangeEvent.EventType.DELETE) && (boolean) BungeeConfig.FILES$AUTO_DELETE.get())
                                    PacketServer.broadcastPacket(new DeleteGlobalScriptPacket(e.path().getFileName().toString()));
                                else
                                    PacketServer.broadcastPacket(getSingleScript(e.path().toFile()));
                            }
                        })
                        .build();
                watcher.watchAsync();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}