package fr.zorg.bungeesk.bukkit.skript.expressions.bungeeplayer;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("UUID of bungee player")
@Description("Get the UUID of a player on the network")
@Examples("set {_uuid} to event-bungeeplayer's uuid")
@Since("1.0.0")
public class ExprBungeePlayerUuid extends SimplePropertyExpression<BungeePlayer, String> {

    static {
        register(ExprBungeePlayerUuid.class,
                String.class,
                "uuid",
                "bungeeplayer");
    }

    @Nullable
    @Override
    public String convert(BungeePlayer player) {
        return player.getUuid();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
        return;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "bungee player's uuid";
    }
}
