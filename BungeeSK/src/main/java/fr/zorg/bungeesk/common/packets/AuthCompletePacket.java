package fr.zorg.bungeesk.common.packets;

public class AuthCompletePacket implements BungeeSKPacket {

    private final boolean encrypting;

    public AuthCompletePacket(boolean encrypting) {
        this.encrypting = encrypting;
    }

    public boolean isEncrypting() {
        return this.encrypting;
    }

}