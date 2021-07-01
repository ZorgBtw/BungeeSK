package fr.zorg.bungeesk.bukkit.skript.scopes;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.bukkit.sockets.ClientSettings;
import fr.zorg.bungeesk.bukkit.utils.EffectSection;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Connect to server")
@Description("This scope allows you to connect to your bungeecord server easily !")
@Since("1.0.0")
@Examples("on load:\n" +
        "\tcreate new bungee connection:\n" +
        "\t\tset address of connection to \"127.0.0.1\"\n" +
        "\t\tset port of connection to 100\n" +
        "\t\tset password of connection to \"abcd\"\n" +
        "\tstart new connection with connection")
public class ScopeConnectToServer extends EffectSection {

    public static ClientSettings settings;

    static {
        Skript.registerCondition(ScopeConnectToServer.class, "(create|init) new bungee connection");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (checkIfCondition() || !hasSection()) return false;
        loadSection(true);
        SectionNode topNode = (SectionNode) SkriptLogger.getNode();

        return true;
    }


    @Override
    protected void execute(Event e) {
        settings = new ClientSettings();
        runSection(e);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "create new bungee connection";
    }

}
