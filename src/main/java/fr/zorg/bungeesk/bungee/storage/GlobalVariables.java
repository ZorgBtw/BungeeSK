package fr.zorg.bungeesk.bungee.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.zorg.bungeesk.bungee.BungeeSK;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class GlobalVariables {

    private static GlobalVariables instance;
    private File variablesFile = new File(BungeeSK.getInstance().getDataFolder().getAbsolutePath(), "variables.json");
    private JsonObject globalVariables;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public GlobalVariables() {
        instance = this;


        try {
            if (!this.variablesFile.exists())
                FileUtils.copyInputStreamToFile(BungeeSK.getInstance().getResourceAsStream("variables.json"), this.variablesFile);
            this.globalVariables = new JsonParser().parse(new BufferedReader(new FileReader(this.variablesFile))).getAsJsonObject();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static GlobalVariables get() {
        return instance;
    }

    public void setVar(final String name, final String value) {
        if (this.globalVariables.has(name))
            this.globalVariables.remove("name");
        this.globalVariables.addProperty(name, value);
        this.saveConfig();
    }

    public void deleteVar(final String name) {
        this.globalVariables.remove(name);
        this.saveConfig();
    }

    public String getVar(final String name) {
        if (this.globalVariables.has(name)) {
            return this.globalVariables.get(name).getAsString();
        }
        return "NONE";
    }

    private void saveConfig() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.variablesFile));
            this.gson.toJson(this.globalVariables, bw);
            bw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
