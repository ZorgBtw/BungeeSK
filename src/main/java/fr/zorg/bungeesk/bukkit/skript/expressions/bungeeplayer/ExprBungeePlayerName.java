package fr.zorg.bungeesk.bukkit.skript.expressions.bungeeplayer;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Name of BungeePlayer")
@Description("Gets the name of a BungeePlayer")
@Examples("send (bungee player named \"Notch\")'s name #returns 'Notch'")
@Since("2.0.0")
public class ExprBungeePlayerName extends SimplePropertyExpression<BungeePlayer, String> {

    static {
        register(ExprBungeePlayerIP.class,
                String.class,
                "[user]name",
                "bungeeplayer");
    }

    @Nullable
    @Override
    public String convert(BungeePlayer player) {
        if (player == null)
            return null;
        return player.getName();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        return;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "bungee player's username";
    }

}