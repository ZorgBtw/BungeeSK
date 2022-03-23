package fr.zorg.bungeesk.common;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class AutoUpdater {

    public static boolean isUpdated(String actualVersion) {
        try {
            final URL url = new URL("https://api.github.com/repos/ZorgBtw/BungeeSK/releases/latest");
            final InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            final JsonObject result = new JsonParser().parse(bufferedReader).getAsJsonObject();
            return result.get("tag_name").getAsString().equalsIgnoreCase(actualVersion);
        } catch (IOException ex) {
            System.err.println("An error occured during the update-checking process !");
        }
        return false;
    }

    //TODO: add auto downloader

}