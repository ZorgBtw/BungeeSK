package fr.zorg.bungeesk.bukkit.packets.listeners;

import fr.zorg.bungeesk.bukkit.api.BungeeSKBukkitListener;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.DeleteGlobalScriptPacket;

import java.io.File;

public class DeleteGlobalScriptListener extends BungeeSKBukkitListener {

    @Override
    public void onReceive(BungeeSKPacket packet) {
        if (packet instanceof DeleteGlobalScriptPacket) {
            final DeleteGlobalScriptPacket deleteGlobalScriptPacket = (DeleteGlobalScriptPacket) packet;
            final String name = deleteGlobalScriptPacket.getScriptName();
            final File file = new File("plugins/Skript/scripts/BungeeSK", name);
            if (file.exists()) {
                file.delete();
            }
        }
    }

}