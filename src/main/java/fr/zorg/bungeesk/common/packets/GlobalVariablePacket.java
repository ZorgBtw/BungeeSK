package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.GlobalVariableChanger;

public class GlobalVariablePacket implements BungeeSKPacket {

    private final String variableName;
    private final byte[] value;
    private final String type;
    private final GlobalVariableChanger changer;

    public GlobalVariablePacket(String variableName, byte[] value, String type, GlobalVariableChanger changer) {
        this.variableName = variableName;
        this.value = value;
        this.type = type;
        this.changer = changer;
    }

    public String getVariableName() {
        return this.variableName;
    }

    public byte[] getValue() {
        return this.value;
    }

    public String getType() {
        return this.type;
    }



    public GlobalVariableChanger getChanger() {
        return this.changer;
    }

}