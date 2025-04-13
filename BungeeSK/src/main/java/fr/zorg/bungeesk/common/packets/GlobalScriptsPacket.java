package fr.zorg.bungeesk.common.packets;

import java.util.ArrayList;
import java.util.HashMap;

public class GlobalScriptsPacket implements BungeeSKPacket {

    private final HashMap<String, ArrayList<String>> scripts;

    public GlobalScriptsPacket(HashMap<String, ArrayList<String>> scripts) {
        this.scripts = scripts;
    }

    public HashMap<String, ArrayList<String>> getScripts() {
        return this.scripts;
    }

}