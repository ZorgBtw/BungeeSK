package fr.zorg.bungeesk.bukkit.updater;

import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.common.updater.Scheduler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Updater extends BukkitRunnable {

    private static Updater instance;

    public static Updater get() {
        if (instance == null)
            instance = new Updater();
        return instance;
    }

    private final List<Scheduler> toUpdate;

    private Updater() {
        this.toUpdate = new ArrayList<>();
        this.runTaskTimer(BungeeSK.getInstance(), 0L, 1L);
    }

    @Override
    public void run() {
        this.toUpdate.forEach(Scheduler::run);
    }

    public void stop() {
        if (this.isCancelled())
            return;
        this.cancel();
    }

    public <T extends Scheduler> Scheduler getByClass(Class<T> clazz) {
        for (final Scheduler scheduler : this.toUpdate) {
            if (scheduler.getClass().equals(clazz))
                return scheduler;
        }
        return null;
    }


    public void register(final Scheduler... schedulers) {
        Arrays.stream(schedulers).filter(scheduler -> !this.toUpdate.contains(scheduler)).forEach(this.toUpdate::add);
    }

    public void unregister(final Scheduler... schedulers) {
        Arrays.stream(schedulers).filter(this.toUpdate::contains).forEach(this.toUpdate::remove);
    }

}
