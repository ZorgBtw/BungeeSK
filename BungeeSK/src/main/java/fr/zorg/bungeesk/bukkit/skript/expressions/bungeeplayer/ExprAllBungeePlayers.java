package fr.zorg.bungeesk.bukkit.skript.expressions.bungeeplayer;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.bukkit.utils.CompletableFutureUtils;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.packets.GetAllBungeePlayersPacket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@Name("All of the bungee players")
@Description("Returns every bungee player on the network")
@Examples("loop all bungee players:\n" +
        "\tsend \"%loop-value%\" is connected !")
@Since("1.0.0")
public class ExprAllBungeePlayers extends SimpleExpression<BungeePlayer> {

    static {
        Skript.registerExpression(ExprAllBungeePlayers.class, BungeePlayer.class, ExpressionType.SIMPLE,
                "[(all [[of] the]|the)] bungee players");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected @Nullable BungeePlayer[] get(Event e) {
        final GetAllBungeePlayersPacket packet = new GetAllBungeePlayersPacket();
        final ArrayList<BungeePlayer> response = (ArrayList<BungeePlayer>) CompletableFutureUtils.generateFuture(packet);

        if (response == null)
            return new BungeePlayer[0];

        return response.toArray(new BungeePlayer[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends BungeePlayer> getReturnType() {
        return BungeePlayer.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "every bungee player";
    }

}