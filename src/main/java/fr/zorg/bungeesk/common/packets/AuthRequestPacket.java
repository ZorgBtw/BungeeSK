package fr.zorg.bungeesk.common.packets;

public class AuthRequestPacket implements BungeeSKPacket {

    private final char[] password;

    public AuthRequestPacket(char[] password) {
        this.password = password;
    }

    public char[] getPassword() {
        return password;
    }

}