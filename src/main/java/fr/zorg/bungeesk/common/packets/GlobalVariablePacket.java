package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.GlobalVariableChanger;

public class GlobalVariablePacket implements BungeeSKPacket {

    private final String variableName;
    private final Object value;
    private final GlobalVariableChanger changer;

    public GlobalVariablePacket(String variableName, Object value, GlobalVariableChanger changer) {
        this.variableName = variableName;
        this.value = value;
        this.changer = changer;
    }

    public String getVariableName() {
        return this.variableName;
    }

    public Object getValue() {
        return this.value;
    }

    public GlobalVariableChanger getChanger() {
        return this.changer;
    }

}