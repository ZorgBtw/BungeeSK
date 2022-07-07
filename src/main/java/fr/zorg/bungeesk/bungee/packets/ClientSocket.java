package fr.zorg.bungeesk.bungee.packets;

import fr.zorg.bungeesk.bungee.Debug;

import java.io.IOException;
import java.net.Socket;

public class ClientSocket extends Thread {

    private final Socket socket;

    public ClientSocket(Socket socket) {
        this.socket = socket;
        super.start();
        Debug.log("ClientSocket started");
    }

    public void disconnect() {
        try {
            this.socket.close();
            super.interrupt();
        } catch (IOException ignored) {
        }
    }

}
