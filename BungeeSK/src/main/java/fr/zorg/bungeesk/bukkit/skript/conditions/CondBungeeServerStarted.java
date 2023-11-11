package fr.zorg.bungeesk.bukkit.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.bukkit.utils.CompletableFutureUtils;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.GetBungeeServerOnlineStatusPacket;
import org.bukkit.event.Event;

@Name("Is bungee server started")
@Description("Checks if a bungee server is started")
@Examples("broadcast \"Lobby 2 is started\" if bungee server named \"lobby2\" is started")
@Since("2.0.0")
public class CondBungeeServerStarted extends Condition {

    static {
        Skript.registerCondition(CondBungeeServerStarted.class,
                "%bungeeserver% is (started|online)",
                "%bungeeserver% is(n't| not) (started|online)");
    }

    private boolean invert;
    private Expression<BungeeServer> server;

    @Override
    public boolean init(Expression<?>[] exprs, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        this.invert = (pattern == 1);
        this.server = (Expression<BungeeServer>) exprs[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (this.server.getSingle(e) == null)
            return this.invert;

        final GetBungeeServerOnlineStatusPacket packet = new GetBungeeServerOnlineStatusPacket(this.server.getSingle(e));
        final Boolean response = (Boolean) CompletableFutureUtils.generateFuture(packet);

        if (response == null)
            return this.invert;

        return this.invert ^ response;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "check if bungee server " + this.server.toString(e, debug) + " is started";
    }

}