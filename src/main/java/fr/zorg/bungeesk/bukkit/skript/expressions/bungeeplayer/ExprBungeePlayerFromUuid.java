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
import fr.zorg.bungeesk.common.packets.BungeePlayerWithUUIDPacket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Name("Bungee player from uuid")
@Description("Get bungee player from his uuid")
@Examples("set {_player} to bungee player with uuid \"06c80842-1780-4c51-97bf-d37759bc4ed1\"")
@Since("1.1.0")
public class ExprBungeePlayerFromUuid extends SimpleExpression<BungeePlayer> {

    static {
        Skript.registerExpression(ExprBungeePlayerFromUuid.class,
                BungeePlayer.class,
                ExpressionType.SIMPLE,
                "bungee[ ]player with uuid %string%");
    }

    private Expression<String> uuid;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.uuid = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected BungeePlayer[] get(Event e) {
        final BungeePlayerWithUUIDPacket packet = new BungeePlayerWithUUIDPacket(UUID.fromString(this.uuid.getSingle(e)));
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
        return "bungee player with uuid " + this.uuid.toString(e, debug);
    }

}