package fr.zorg.bungeesk.bukkit.skript.scopes;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.bukkit.sockets.ClientSettings;
import fr.zorg.bungeesk.bukkit.utils.EffectSection;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ScopeConnectToServer extends EffectSection {

    /* EXAMPLE SYNTAX
    (create|init) bungee connection:
        set (adress|ip) of connection to "127.0.0.1" #IP of server
        set port of server 100 #Needs to be the same as the one in the server's config
        set password of server to "dada" #Needs to be the same as the one in the server's config
        set name of server to "truc" #Has to be unique
    start new connection with last created connection
    */

    public static ClientSettings settings;

    static {
        Skript.registerCondition(ScopeConnectToServer.class, "(create|init) new bungee connection");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (checkIfCondition()) {
            return false;
        }
        if (!hasSection()) {
            return false;
        }
        loadSection(true);
        SectionNode topNode = (SectionNode) SkriptLogger.getNode();

        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return null;
    }

    @Override
    protected void execute(Event e) {
        settings = new ClientSettings();
        runSection(e);
    }

}
