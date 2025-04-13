package fr.zorg.bungeesk.bukkit.skript.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.bukkit.utils.ClientBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Name("Connect to server")
@Description("This scope allows you to connect to your bungeecord server easily !")
@Since("1.0.0")
@Examples("on load:\n" +
        "\tcreate new bungee connection:\n" +
        "\t\tset address of connection to \"127.0.0.1\"\n" +
        "\t\tset port of connection to 20000\n" +
        "\t\tset password of connection to \"abcd\"\n" +
        "\tstart new connection with connection")
public class SecConnectToServer extends EffectSection {

    public static ClientBuilder builder;

    static {
        Skript.registerSection(SecConnectToServer.class, "(create|init) new bungee connection");
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult, @Nullable SectionNode sectionNode, @Nullable List<TriggerItem> list) {
        if (!super.hasSection()) {
            Skript.error("You must have a section after the create new bungee connection section");
            return false;
        }
        assert sectionNode != null;
        super.loadOptionalCode(sectionNode);
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(Event event) {
        builder = new ClientBuilder();
        return super.walk(event, true);
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "create new bungee connection";
    }
}