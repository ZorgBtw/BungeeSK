package fr.zorg.bungeesk.common;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AutoUpdater {

    public static boolean isUpToDate(String currentVersion) {
        try {
            final URL url = new URL("https://api.github.com/repos/ZorgBtw/BungeeSK/releases/latest");
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() != 200) {
                System.err.println("Too many update-checking requests sent to GitHub, try again later.");
                return true;
            }
            final InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            final JsonObject result = new JsonParser().parse(bufferedReader).getAsJsonObject();
            final int latestVersionInt = Integer.parseInt(result.get("tag_name").getAsString().replaceAll("\\.", ""));
            final int currentVersionInt = Integer.parseInt(currentVersion.replaceAll("\\.", ""));
            return latestVersionInt > currentVersionInt;
        } catch (IOException ex) {
            System.err.println("An error occured during the update-checking process !");
        }
        return false;
    }

    //TODO: add auto downloader

}