package fr.zorg.bungeesk.common.packets;

public class DeleteGlobalScriptPacket implements BungeeSKPacket {

    private final String scriptName;

    public DeleteGlobalScriptPacket(String scriptName) {
        this.scriptName = scriptName;
    }

    public String getScriptName() {
        return this.scriptName;
    }

}