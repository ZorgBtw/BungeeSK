package fr.zorg.bungeesk.bukkit.skript.scopes;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.bukkit.EffectSection;
import fr.zorg.bungeesk.common.entities.BungeeServerBuilder;
import org.bukkit.event.Event;

import java.io.IOException;

public class ScopeCreateBungeeServer extends EffectSection {

    public static BungeeServerBuilder builder;

    static {
        Skript.registerCondition(ScopeCreateBungeeServer.class, "create new bungee server");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (super.checkIfCondition() || !super.hasSection())
            return false;

        loadSection(true);
        SectionNode topNode = (SectionNode) SkriptLogger.getNode();

        return true;
    }

    @Override
    protected void execute(Event e) throws IOException {
        builder = new BungeeServerBuilder();
        super.runSection(e);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "create new bungee server from scratch";
    }

}