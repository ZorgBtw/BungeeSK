package fr.zorg.velocitysk.storage;

import fr.zorg.bungeesk.common.packets.DeleteGlobalScriptPacket;
import fr.zorg.bungeesk.common.packets.GlobalScriptsPacket;
import fr.zorg.velocitysk.BungeeSK;
import fr.zorg.velocitysk.packets.PacketServer;
import fr.zorg.velocitysk.packets.SocketServer;
import fr.zorg.velocitysk.utils.BungeeConfig;
import fr.zorg.velocitysk.utils.Debug;
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

public class GlobalScripts {

    private static final File GLOBAL_SCRIPTS_FOLDER = new File(BungeeSK.getDataDirectory().toFile().getAbsolutePath(), "global-scripts");

    public static void sendGlobalScripts(SocketServer socketServer) {
        if (!GLOBAL_SCRIPTS_FOLDER.exists()) {
            GLOBAL_SCRIPTS_FOLDER.mkdirs();
            return;
        }

        final HashMap<String, ArrayList<String>> scripts = new HashMap<>(); //Name | Script's lines
        for (File file : GLOBAL_SCRIPTS_FOLDER.listFiles()) {
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
            final Path path = Paths.get(GLOBAL_SCRIPTS_FOLDER.getAbsolutePath());
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
