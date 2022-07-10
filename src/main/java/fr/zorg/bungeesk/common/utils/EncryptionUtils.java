package fr.zorg.bungeesk.common.utils;

import com.rockaport.alice.Alice;
import com.rockaport.alice.AliceContext;
import com.rockaport.alice.AliceContextBuilder;

import java.security.GeneralSecurityException;
import java.util.UUID;

public class EncryptionUtils {

    private static final Alice alice = new Alice(
            new AliceContextBuilder()
                    .setAlgorithm(AliceContext.Algorithm.AES)
                    .setIterations(16)
                    .build()
    );

    public static byte[] encryptUUID(UUID uuid, char[] password) throws Exception {
        final byte[] uuidBytes = UUIDUtils.UUIDToBytes(uuid);
        return alice.encrypt(uuidBytes, password);
    }

    public static UUID decryptUUID(byte[] encryptedUUID, char[] password) throws GeneralSecurityException {
        final byte[] uuidBytes = alice.decrypt(encryptedUUID, password);
        return UUIDUtils.bytesToUUID(uuidBytes);
    }

}
