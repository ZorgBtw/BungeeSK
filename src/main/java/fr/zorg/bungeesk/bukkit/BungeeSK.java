package fr.zorg.bungeesk.bukkit;

import java.io.IOException;

import fr.zorg.bungeesk.bukkit.updater.Commands;
import fr.zorg.bungeesk.bukkit.updater.Updater;
import fr.zorg.bungeesk.bukkit.utils.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;

public class BungeeSK extends JavaPlugin {

    private Metrics metrics;

    @Override
    public void onEnable() {
        this.metrics = new Metrics(this, 10655);
        final SkriptAddon addon = Skript.registerAddon(this);
        try {
            addon.loadClasses("fr.zorg.bungeesk.bukkit.skript");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Updater.get().register(new Commands());


        this.metrics.addCustomChart(new Metrics.SimplePie("skript_version", () -> Skript.getVersion().toString()));
    }

    @Override
    public void onDisable() {
        Updater.get().stop();
    }

    public static BungeeSK getInstance() {
        return JavaPlugin.getPlugin(BungeeSK.class);
    }

}
