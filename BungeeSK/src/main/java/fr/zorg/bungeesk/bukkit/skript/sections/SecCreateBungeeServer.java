package fr.zorg.bungeesk.bukkit.skript.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.EffectSection;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.common.entities.BungeeServerBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

public class SecCreateBungeeServer extends EffectSection {

    public static BungeeServerBuilder builder;

    static {
        Skript.registerSection(SecCreateBungeeServer.class, "create new bungee server");
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult, @Nullable SectionNode sectionNode, @Nullable List<TriggerItem> list) {
        if (!super.hasSection()) {
            Skript.error("You must have a section after the create new bungee server section");
            return false;
        }

        assert sectionNode != null;
        super.loadOptionalCode(sectionNode);
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(Event event) {
        builder = new BungeeServerBuilder();
        return super.walk(event, true);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "create new bungee server from scratch";
    }

}