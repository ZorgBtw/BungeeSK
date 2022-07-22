package fr.zorg.bungeesk.bungee.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import fr.zorg.bungeesk.bungee.BungeeSK;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class GlobalVariablesUtils {

    private static final File variablesFile = new File(BungeeSK.getInstance().getDataFolder().getAbsolutePath(), "variables.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static JsonObject globalVariables;

    public static void init() {
        BungeeSK.runAsync(() -> {
            try {
                if (!variablesFile.exists()) {
                    FileUtils.copyInputStreamToFile(BungeeSK.getInstance().getResourceAsStream("variables.json"), variablesFile);
                }
                globalVariables = gson.fromJson(new BufferedReader(new FileReader(variablesFile)), JsonObject.class).getAsJsonObject();
                migrateFromV1();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public static Object getGlobalVariable(String name) {
        if (globalVariables.has(name)) {
            final JsonObject jsonObject = globalVariables.get(name).getAsJsonObject();
            final String type = jsonObject.get("type").getAsString();
            switch (type) {
                case "LONG": {
                    return jsonObject.get("value").getAsLong();
                }
                case "DOUBLE": {
                    return jsonObject.get("value").getAsDouble();
                }
                case "STRING": {
                    return jsonObject.get("value").getAsString();
                }
                case "BOOLEAN": {
                    return jsonObject.get("value").getAsBoolean();
                }
            }
            return null;
        }
        return null;
    }

    public static void setGlobalVariable(String name, Object value) {
        final JsonObject jsonObject = new JsonObject();
        if (value instanceof Number)
            jsonObject.addProperty("value", ((Number) value));
        else if (value instanceof String)
            jsonObject.addProperty("value", ((String) value));
        else if (value instanceof Boolean)
            jsonObject.addProperty("value", ((Boolean) value));
        jsonObject.addProperty("type", value.getClass().getSimpleName().toUpperCase());
        globalVariables.add(name, jsonObject);
        saveFile();
    }

    public static void deleteGlobalVariable(String name) {
        globalVariables.remove(name);
        saveFile();
    }

    private static void migrateFromV1() {
        globalVariables.entrySet().forEach(stringJsonElementEntry -> {
            final String varName = stringJsonElementEntry.getKey();
            if (!globalVariables.get(varName).isJsonObject()) {
                final JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("value", globalVariables.get(varName).getAsString());
                jsonObject.addProperty("type", "STRING");
                globalVariables.add(varName, jsonObject);
            }
        });
        saveFile();
    }

    public static void saveFile() {
        BungeeSK.runAsync(() -> {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(variablesFile));
                gson.toJson(globalVariables, bufferedWriter);
                bufferedWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

}
