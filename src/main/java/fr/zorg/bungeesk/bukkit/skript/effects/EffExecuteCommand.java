package fr.zorg.bungeesk.bukkit.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.sockets.ConnectionClient;
import fr.zorg.bungeesk.bukkit.utils.BungeeServer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Execute console command")
@Description("Make bungee or spigot execute command")
@Since("1.0.0 - 1.1.0: Usage of BungeeServer type")
@Examples({"make bungee execute console command \"alert This is an alert !\"",
        "make bungee server named \"hub\" execute console command \"say Hi everyone !\""})

public class EffExecuteCommand extends Effect {

    static {
        Skript.registerEffect(EffExecuteCommand.class,
                "make bungee[cord] [server] execute console command %string%",
                "make %bungeeserver% execute console command %string%",
                "make all servers execute console command %string%"
        );
    }

    private Expression<String> command;
    private Expression<BungeeServer> server;
    private int pattern;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.pattern = matchedPattern;

        if (this.pattern == 1) {
            this.command = (Expression<String>) exprs[1];
            this.server = (Expression<BungeeServer>) exprs[0];
        } else
            this.command = (Expression<String>) exprs[0];

        return true;
    }

    @Override
    protected void execute(Event e) {
        if (BungeeSK.isClientConnected()) {
            String toSend = null;
            switch (this.pattern) {
                case 0:
                    toSend = "bungeecord";
                    break;
                case 1: {
                    if (this.server.getSingle(e) == null)
                        return;
                    toSend = this.server.getSingle(e).getAddress() + ":" + this.server.getSingle(e).getPort();
                    break;
                }
                case 2:
                    toSend = "all";
                    break;
            }

            ConnectionClient.get().write(true, "effectExecuteCommand",
                    "server", toSend,
                    "command", this.command.getSingle(e));
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        if (this.pattern == 0)
            return "make bungeecord execute command " + this.command.toString(e, debug);
        if (this.pattern == 1)
            return "make server " + this.server.toString(e, debug) + " execute command " + this.command.toString(e, debug);
        return "make all servers execute console command " + this.command.toString(e, debug);
    }

}
