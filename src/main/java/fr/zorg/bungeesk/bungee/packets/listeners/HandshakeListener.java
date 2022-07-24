package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.BungeeConfig;
import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.Debug;
import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.common.packets.AuthRequestPacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.HandshakePacket;
import fr.zorg.bungeesk.common.utils.EncryptionUtils;

import java.net.InetAddress;
import java.util.Optional;
import java.util.UUID;

public class HandshakeListener extends BungeeSKListener {

    @Override
    public void onReceive(InetAddress address, BungeeSKPacket packet) {
        if (packet instanceof HandshakePacket) {
            final Optional<SocketServer> client = BungeeSK.getApi().getClientWithInetAddress(address);
            if (client.isPresent()) {
                client.get().setMinecraftPort(((HandshakePacket) packet).getMinecraftPort());
                final UUID uuid = client.get().initChallenge();
                try {
                    final byte[] encryptedUUID = EncryptionUtils.encryptUUID(uuid, ((String) BungeeConfig.PASSWORD.get()).toCharArray());
                    BungeeSK.getApi().sendPacket(address, new AuthRequestPacket(encryptedUUID));
                } catch (Exception ex) {
                    Debug.log("Error while encrypting UUID");
                    client.get().disconnect();
                }
            }
        }
    }

}