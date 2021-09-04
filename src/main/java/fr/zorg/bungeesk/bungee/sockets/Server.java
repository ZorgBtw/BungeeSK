package fr.zorg.bungeesk.bungee.sockets;

import com.google.gson.JsonObject;
import fr.zorg.bungeesk.bungee.storage.BungeeConfig;
import fr.zorg.bungeesk.common.utils.Utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Server {

    private final List<ClientServer> clients;
    private final String password;
    private final ServerSocket servSocket;
    private final Thread waitingConnection;


    public Server(final int port, final String passwd) throws IOException {
        this.password = passwd;
        this.clients = new ArrayList<>();
        this.servSocket = new ServerSocket(port);

        this.waitingConnection = new Thread(this::waitForConn);
        this.waitingConnection.setDaemon(true);
        this.waitingConnection.start();
    }

    public void waitForConn() {
        while (!this.servSocket.isClosed()) {
            try {
                final ClientServer client = new ClientServer(this.servSocket.accept());
                if (BungeeConfig.get().isWhitelistIp()) {
                    final Socket clientSocket = client.getSocket();
                    if (!BungeeConfig.get().getAuthorizedIp().contains(clientSocket.getInetAddress().getHostAddress()))
                        client.forceDisconnect();
                }
            } catch (IOException ignored) {
            }
        }
    }

    public void disconnect() {
        Utils.copy(this.clients).forEach(ClientServer::disconnect);
        try {
            this.waitingConnection.interrupt();
            this.servSocket.close();
        } catch (final Exception ignored) {
        }
    }

    public boolean isPassword(final String pwd) {
        return password.equals(pwd);
    }

    public boolean isClient(final Socket socket) {
        return this.clients.stream().anyMatch(client -> client.getSocket().equals(socket));
    }

    public boolean isClient(final String address) {
        return this.clients.stream().anyMatch(client -> client.getAddress().equals(address));
    }

    public void removeClient(final ClientServer client) {
        this.clients.remove(client);
    }

    public void removeClient(final String name) {
        this.getClient(name).ifPresent(this.clients::remove);
    }

    public void addClient(final ClientServer client) {
        if (this.clients.contains(client))
            return;
        this.clients.add(client);
    }

    public Optional<ClientServer> getClient(final String address) {
        return this.clients.stream().filter(client -> client.getAddress().equalsIgnoreCase(address)).findFirst();
    }

    public void write(final String message, ClientServer... clients) {
        if (clients == null || clients.length == 0)
            clients = this.clients.toArray(new ClientServer[0]);
        for (final ClientServer client : clients) {
            client.write(true, message);
        }
    }

    public void writeAll(final boolean encryption, final String action, final String... args) {
        this.clients.forEach(client -> client.write(encryption, action, args));
    }

    public void writeRawAll(final boolean encryption, final String action, final JsonObject args) {
        this.clients.forEach(client -> client.writeRaw(encryption, action, args));
    }

    public List<ClientServer> getClients() {
        return clients;
    }
}
