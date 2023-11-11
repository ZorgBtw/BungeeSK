package fr.zorg.bungeesk.common.packets;

public class GetBungeeServerFromAddressPacket implements BungeeSKPacket {

    private final String address;
    private final int port;

    public GetBungeeServerFromAddressPacket(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return this.address;
    }

    public int getPort() {
        return this.port;
    }

}