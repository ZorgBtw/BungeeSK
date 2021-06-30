package fr.zorg.bungeesk.common.encryption;

import com.rockaport.alice.Alice;
import com.rockaport.alice.AliceContext;
import com.rockaport.alice.AliceContextBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;

public class GlobalEncryption {

    private final Alice alice;

    public GlobalEncryption(final AliceContext.Algorithm algorithm, final int iterations) {
        this.alice = new Alice(new AliceContextBuilder()
                .setAlgorithm(algorithm)
                .setIterations(iterations)
                .build());
    }

    public String decrypt(final String toDecrypt, final String password) {
        if (toDecrypt.equals("{\"args\":{\"status\":\"wrongPassword\"},\"action\":\"connectionInformation\"}")
                || toDecrypt.equals("{\"args\":{\"status\":\"alreadyConnected\"},\"action\":\"connectionInformation\"}")
                || toDecrypt.equals("{\"args\":{\"status\":\"disconnect\"},\"action\":\"connectionInformation\"}"))
            return toDecrypt;
        try {
            return new String(this.alice.decrypt(Base64.getDecoder().decode(toDecrypt), password.toCharArray()));
        } catch (GeneralSecurityException ex) {
            return "wrongPassword";
        }
    }

    public String encrypt(final String toEncrypt, final String password) {
        try {
            return Base64.getEncoder().encodeToString(this.alice.encrypt(toEncrypt.getBytes(StandardCharsets.UTF_8), password.toCharArray()));
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
