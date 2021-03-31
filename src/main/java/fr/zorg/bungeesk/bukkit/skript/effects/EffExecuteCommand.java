package fr.zorg.bungeesk.bukkit.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.bukkit.sockets.ConnectionClient;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Execute console command")
@Description("Make bungee or spigot execute command")
@Examples({"make bungee console execute command \"alert This is an alert !\"",
           "make server \"hub\" execute command \"say Hi everyone !\""})

public class EffExecuteCommand extends Effect {

    static {
        Skript.registerEffect(EffExecuteCommand.class,
                "make bungee console execute command %string%",
                "make server %string% console execute command %string%",
                "make all servers console execute command %string%"

        );
    }

    private Expression<String> command;
    private Expression<String> server;
    private int pattern;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        command = (Expression<String>) exprs[0];
        this.pattern = matchedPattern;
        if (this.pattern == 1) {
            command = (Expression<String>) exprs[1];
            server = (Expression<String>) exprs[0];
        }
        return true;
    }

    @Override
    protected void execute(Event e) {
        String server = null;
        switch (this.pattern) {
            case 0:
                server = "Bungee";
                break;
            case 1:
                server = this.server.getSingle(e);
                break;
            case 2:
                server = "All";
                break;
        }
        ConnectionClient.get().write("EffExecuteCommandµ" + server + "µ" + command.getSingle(e));
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return null;
    }

}
