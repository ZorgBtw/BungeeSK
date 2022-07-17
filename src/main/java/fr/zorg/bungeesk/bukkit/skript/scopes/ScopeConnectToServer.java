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
import fr.zorg.bungeesk.bukkit.EffectSection;
import fr.zorg.bungeesk.bukkit.utils.ClientBuilder;
import org.bukkit.event.Event;

import java.io.IOException;

@Name("Connect to server")
@Description("This scope allows you to connect to your bungeecord server easily !")
@Since("1.0.0")
@Examples("on load:\n" +
        "\tcreate new bungee connection:\n" +
        "\t\tset address of connection to \"127.0.0.1\"\n" +
        "\t\tset port of connection to 20000\n" +
        "\t\tset password of connection to \"abcd\"\n" +
        "\tstart new connection with connection")
public class ScopeConnectToServer extends EffectSection {

    public static ClientBuilder settings;

    static {
        Skript.registerCondition(ScopeConnectToServer.class, "(create|init) new bungee connection");
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        if (super.checkIfCondition() || !super.hasSection())
            return false;

        loadSection(true);
        SectionNode topNode = (SectionNode) SkriptLogger.getNode();

        return true;
    }

    @Override
    protected void execute(Event e) throws IOException {
        settings = new ClientBuilder();
        super.runSection(e);
    }

    @Override
    public String toString(Event event, boolean b) {
        return "create new bungee connection";
    }

    public static ClientBuilder getSettings() {
        return settings;
    }

}