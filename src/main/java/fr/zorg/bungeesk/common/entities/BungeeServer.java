package fr.zorg.bungeesk.common.entities;

import java.net.InetAddress;

public class BungeeServer {

    private final InetAddress address;
    private AuthenticationState state;

    public BungeeServer(InetAddress address) {
        this.address = address;
        this.state = AuthenticationState.HANDSHAKE;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public AuthenticationState getState() {
        return this.state;
    }

    public void setState(AuthenticationState state) {
        this.state = state;
    }

}