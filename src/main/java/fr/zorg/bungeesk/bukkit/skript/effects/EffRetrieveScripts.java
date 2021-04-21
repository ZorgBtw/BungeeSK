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
import fr.zorg.bungeesk.bukkit.sockets.ConnectionClient;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Retrieve scripts")
@Description("Retrieve every script from the bungee")
@Since("1.0.0")
@Examples("retrieve all scripts from bungee")
public class EffRetrieveScripts extends Effect {

    static {
        Skript.registerEffect(EffRetrieveScripts.class, "retrieve all (scripts|skripts) from bungee");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected void execute(Event e) {
        ConnectionClient.get().write("RETRIEVE_SKRIPTSµ");
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "retrieve every scripts from the bungeecord";
    }
}
