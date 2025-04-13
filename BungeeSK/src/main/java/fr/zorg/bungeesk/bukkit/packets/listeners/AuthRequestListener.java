package fr.zorg.bungeesk.bukkit.packets.listeners;

import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.api.BungeeSKBukkitListener;
import fr.zorg.bungeesk.bukkit.packets.PacketClient;
import fr.zorg.bungeesk.common.packets.AuthRequestPacket;
import fr.zorg.bungeesk.common.packets.AuthResponsePacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.utils.EncryptionUtils;

import java.security.GeneralSecurityException;
import java.util.UUID;

public class AuthRequestListener extends BungeeSKBukkitListener {

    @Override
    public void onReceive(BungeeSKPacket packet) {
        if (packet instanceof AuthRequestPacket) {
            final AuthRequestPacket authRequestPacket = (AuthRequestPacket) packet;
            final byte[] encryptedUUID = authRequestPacket.getEncryptedUuid();
            try {
                final UUID uuid = EncryptionUtils.decryptUUID(encryptedUUID, PacketClient.getBuilder().getPassword());
                PacketClient.sendPacket(new AuthResponsePacket(uuid));
            } catch (GeneralSecurityException ex) {
                BungeeSK.getInstance().getLogger().severe("§7Connection error: §cWrong password");
                PacketClient.getClient().disconnect();
            }
        }
    }
}
