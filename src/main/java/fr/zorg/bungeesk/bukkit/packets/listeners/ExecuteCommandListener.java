package fr.zorg.bungeesk.bukkit.packets.listeners;

import fr.zorg.bungeesk.bukkit.api.BungeeSKBukkitListener;
import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.ExecuteCommandPacket;
import org.bukkit.Bukkit;

public class ExecuteCommandListener extends BungeeSKBukkitListener {

    @Override
    public void onReceive(BungeeSKPacket packet) {
        if (packet instanceof ExecuteCommandPacket) {
            final ExecuteCommandPacket executeCommandPacket = (ExecuteCommandPacket) packet;
            Bukkit.getScheduler().runTask(BungeeSK.getInstance(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), executeCommandPacket.getCommand()));
        }
    }

}