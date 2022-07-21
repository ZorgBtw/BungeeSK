package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.utils.GlobalVariablesUtils;
import fr.zorg.bungeesk.common.entities.GlobalVariableChanger;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GlobalVariablePacket;

import java.net.InetAddress;
import java.util.UUID;

public class GlobalVariableListener extends BungeeSKListener {

    @Override
    public void onReceive(InetAddress address, BungeeSKPacket packet) {
        if (packet instanceof GlobalVariablePacket) {

            final GlobalVariablePacket globalVariablePacket = (GlobalVariablePacket) packet;
            final String varName = globalVariablePacket.getVariableName();
            final Object globalVarObj = GlobalVariablesUtils.getGlobalVariable(varName);

            switch (globalVariablePacket.getChanger()) {

                case ADD:
                    if (globalVarObj == null || !(globalVarObj instanceof Number))
                        return;
                    GlobalVariablesUtils.setGlobalVariable(varName, ((Number) globalVarObj).longValue() + ((Number) globalVariablePacket.getValue()).longValue());

                    break;

                case REMOVE:
                    if (globalVarObj == null || !(globalVarObj instanceof Number))
                        return;
                    GlobalVariablesUtils.setGlobalVariable(varName, ((Number) globalVarObj).longValue() - ((Number) globalVariablePacket.getValue()).longValue());
                    break;

                case SET:
                    GlobalVariablesUtils.setGlobalVariable(varName, globalVariablePacket.getValue());
                    break;

                case DELETE:
                    GlobalVariablesUtils.deleteGlobalVariable(varName);
                    break;
            }
        }
    }

    @Override
    public Object onFutureRequest(UUID uuid, InetAddress address, BungeeSKPacket packet) {
        if (packet instanceof GlobalVariablePacket) {
            final GlobalVariablePacket globalVariablePacket = (GlobalVariablePacket) packet;
            if (globalVariablePacket.getChanger() == GlobalVariableChanger.GET) {
                return GlobalVariablesUtils.getGlobalVariable(globalVariablePacket.getVariableName());
            }
        }
        return null;
    }

}