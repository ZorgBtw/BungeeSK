package fr.zorg.bungeesk.bukkit.updater;

import fr.zorg.bungeesk.common.updater.Scheduler;
import fr.zorg.bungeesk.common.utils.Pair;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.LinkedList;

public class Commands implements Scheduler {

    private final LinkedList<Pair<CommandSender, String>> commandToSend;

    public Commands() {
        this.commandToSend = new LinkedList<>();
    }

    @Override
    public void run() {
        synchronized (this.commandToSend) {
            Pair<CommandSender, String> toExecute;
            while ((toExecute = this.commandToSend.poll()) != null) {
                Bukkit.dispatchCommand(toExecute.getFirstValue(), toExecute.getSecondValue());
            }
        }
    }

    public void addCommandToSend(final CommandSender sender, final String command) {
        synchronized (this.commandToSend) {
            this.commandToSend.add(Pair.from(sender, command));
        }
    }

}
