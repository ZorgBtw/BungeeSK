package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.bungee.storage.GlobalVariables;
import fr.zorg.bungeesk.common.entities.EmptyFutureResponse;
import fr.zorg.bungeesk.common.entities.GlobalVariableChanger;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GlobalVariablePacket;
import fr.zorg.bungeesk.common.utils.Pair;

import java.util.UUID;

public class GlobalVariableListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof GlobalVariablePacket) {

            final GlobalVariablePacket globalVariablePacket = (GlobalVariablePacket) packet;
            final String varName = globalVariablePacket.getVariableName();
            final byte[] varValue = globalVariablePacket.getValue();
            final String varType = globalVariablePacket.getType();
            final GlobalVariableChanger changer = globalVariablePacket.getChanger();

            switch (changer) {
                case SET: {
                    GlobalVariables.setGlobalVariable(varName, varValue, varType);
                    break;
                }
                case DELETE: {
                    GlobalVariables.deleteGlobalVariable(varName);
                    break;
                }
            }
        }
    }

    @Override
    public Object onFutureRequest(UUID uuid, SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof GlobalVariablePacket) {
            final GlobalVariablePacket globalVariablePacket = (GlobalVariablePacket) packet;
            if (globalVariablePacket.getChanger() == GlobalVariableChanger.GET) {
                final Pair<byte[], String> globalVar = GlobalVariables.getGlobalVariable(globalVariablePacket.getVariableName());
                return globalVar == null ? new EmptyFutureResponse() : globalVar;
            }
            return new EmptyFutureResponse();
        }
        return null;
    }

}