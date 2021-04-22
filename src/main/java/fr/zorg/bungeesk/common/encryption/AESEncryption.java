package fr.zorg.bungeesk.common.encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AESEncryption {

    private Cipher encrypt;
    private Cipher decrypt;

    public AESEncryption(final String key) {
        try  {
            byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
            byteKey = MessageDigest.getInstance("SHA-1").digest(byteKey);
            byteKey = Arrays.copyOf(byteKey, 16);

            final SecretKeySpec secretKey = new SecretKeySpec(byteKey, "AES");
            this.encrypt = Cipher.getInstance("AES/ECB/PKCS5Padding");
            this.encrypt.init(Cipher.ENCRYPT_MODE, secretKey);
            this.decrypt = Cipher.getInstance("AES/ECB/PKCS5Padding");
            this.decrypt.init(Cipher.DECRYPT_MODE, secretKey);

        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Ooops, something went wrong. Can't initialize encryption method.", e);
        }
    }

    public String encrypt(final String message) {
        if (this.encrypt == null) {
            Logger.getAnonymousLogger().log(Level.WARNING, "The Cypher is null. Can't encrypt the following message: " + message + ". A blank message will be send instead.");
            return "";
        }
        try {
            return Base64.getEncoder().encodeToString(this.encrypt.doFinal(message.getBytes(StandardCharsets.UTF_8)));
        } catch (IllegalArgumentException | IllegalBlockSizeException | BadPaddingException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Ooops, something went wrong. Can't encrypt the following message: " + message + ". A blank message will be send instead.", e);
        }
        return "";
    }

    public String decrypt(final String message) {
        if (this.encrypt == null) {
            Logger.getAnonymousLogger().log(Level.WARNING, "The Cypher is null. Can't decrypt the following message: " + message + ". A blank message will be send instead.");
            return "";
        }
        try {
            return new String(this.decrypt.doFinal(Base64.getDecoder().decode(message)), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException | IllegalBlockSizeException | BadPaddingException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Ooops, something went wrong. Can't decrypt the following message: " + message);
        }
        return message;
    }
}
