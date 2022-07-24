package fr.zorg.bungeesk.common.packets;

public class MakeServerExecuteCommandPacket implements BungeeSKPacket {

    private final Object toSend;
    private final String command;

    public MakeServerExecuteCommandPacket(Object toSend, String command) {
        this.toSend = toSend;
        this.command = command;
    }

    public Object getToSend() {
        return this.toSend;
    }

    public String getCommand() {
        return this.command;
    }

}
