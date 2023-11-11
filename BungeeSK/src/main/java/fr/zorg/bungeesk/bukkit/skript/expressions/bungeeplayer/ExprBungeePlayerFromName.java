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
import fr.zorg.bungeesk.common.packets.BungeePlayerNamedPacket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Bungee player from name")
@Description("Get bungee player from his name")
@Examples("set {_player} to bungee player named \"Notch\"")
@Since("1.0.0, 1.1.0: Add of 'named'")
public class ExprBungeePlayerFromName extends SimpleExpression<BungeePlayer> {

    static {
        Skript.registerExpression(ExprBungeePlayerFromName.class,
                BungeePlayer.class,
                ExpressionType.SIMPLE,
                "bungee[ ]player (with name|named) %string%");
    }

    private Expression<String> player;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.player = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected BungeePlayer[] get(Event e) {
        final BungeePlayerNamedPacket packet = new BungeePlayerNamedPacket(this.player.getSingle(e));
        final BungeePlayer response = (BungeePlayer) CompletableFutureUtils.generateFuture(packet);
        return response == null ? new BungeePlayer[0] : new BungeePlayer[]{response};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends BungeePlayer> getReturnType() {
        return BungeePlayer.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "bungee player named " + this.player.toString(e, debug);
    }

}