package fr.zorg.bungeesk.bukkit.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.bukkit.utils.CompletableFutureUtils;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.packets.GetBungeePlayerConnectionPacket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Is bungee player connected")
@Description("Checks if a bungee player is connected")
@Since("1.0.0")
@Examples("broadcast \"Notch is connected ! :o\" if bungee player named \"Notch\" is connected")
public class CondBungeePlayerConnected extends Condition {

    static {
        Skript.registerCondition(CondBungeePlayerConnected.class,
                "%bungeeplayer% is connected",
                "%bungeeplayer% is(n't| not) connected");
    }

    private Expression<BungeePlayer> player;
    private boolean negate;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.negate = (matchedPattern == 1);
        this.player = (Expression<BungeePlayer>) exprs[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (this.player.getSingle(e) == null)
            return this.negate;

        final GetBungeePlayerConnectionPacket packet = new GetBungeePlayerConnectionPacket(this.player.getSingle(e));
        final Boolean response = (Boolean) CompletableFutureUtils.generateFuture(packet);
        if (response == null)
            return this.negate;

        return response ^ this.negate;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "conenction state of a bungee player";
    }

}