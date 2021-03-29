package fr.zorg.bungeesk.bukkit.sockets;

public final class ClientSettings {

    private String address;
    private Long port;
    private String name;
    private String password;

    public String getAddress() {
        return this.address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public Long getPort() {
        return this.port;
    }

    public void setPort(final Long port) {
        this.port = port;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void run() {
        ConnectionClient.generateConnection(this);
    }

}
