package fr.zorg.bungeesk.common.entities;

import java.net.InetAddress;

public class BungeeServer {

    private final InetAddress inetAddress;
    private AuthenticationState state;

    public BungeeServer(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
        this.state = AuthenticationState.HANDSHAKE;
    }

    public InetAddress getInetAddress() {
        return this.inetAddress;
    }

    public AuthenticationState getState() {
        return this.state;
    }

    public void setState(AuthenticationState state) {
        this.state = state;
    }

}