package fr.zorg.bungeesk.common.packets;

public class ExecuteCommandPacket implements BungeeSKPacket {

    private final String command;

    public ExecuteCommandPacket(String command) {
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }

}