package fr.zorg.bungeesk.bukkit.utils;

public class ClientBuilder {

    private String address;
    private int port;
    private char[] password;

    public String getAddress() {
        return this.address;
    }

    public ClientBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public int getPort() {
        return this.port;
    }

    public ClientBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public char[] getPassword() {
        return this.password;
    }

    public ClientBuilder setPassword(char[] password) {
        this.password = password;
        return this;
    }

}
