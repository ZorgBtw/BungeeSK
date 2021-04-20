package fr.zorg.bungeesk.bungee.sockets;

import fr.zorg.bungeesk.bungee.storage.BungeeConfig;
import fr.zorg.bungeesk.common.encryption.AESEncryption;
import fr.zorg.bungeesk.common.utils.Utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public final class Server {

    private final List<ClientServer> clients;
    private final String password;
    private final ServerSocket servSocket;
    private final Thread waitingConnection;

    final AESEncryption encryption;

    public Server(final int port, final String passwd) throws IOException {
        this.password = passwd;
        this.clients = new ArrayList<>();
        this.servSocket = new ServerSocket(port);
        this.encryption = new AESEncryption(passwd);

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

    public Optional<ClientServer> getClient(final String name) {
        return this.clients.stream().filter(client -> name.equalsIgnoreCase(client.getName())).findFirst();
    }

    public void write(final String message, ClientServer... clients) {
        if (clients == null || clients.length == 0)
            clients = this.clients.toArray(new ClientServer[0]);
        for (final ClientServer client : clients) {
            client.write(message);
        }
    }

    public void writeAll(final String message) {
        for (final ClientServer client : this.clients) {
            client.write(message);
        }
    }

    public List<ClientServer> getClients() {
        return clients;
    }
}
