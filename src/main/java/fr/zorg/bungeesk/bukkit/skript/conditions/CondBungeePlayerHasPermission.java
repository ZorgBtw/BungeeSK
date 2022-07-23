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
import fr.zorg.bungeesk.common.packets.BungeePlayerPermissionPacket;
import org.bukkit.event.Event;

@Name("Does bungee player have / doesn't have permission")
@Description("Check if a bungee player has a specific permission on the bungeecord")
@Examples("broadcast \"Notch has the BungeeSK permission ! :o\" if bungee player named \"Notch\" has permission \"bungeesk.perm\"")
@Since("1.1.1")
public class CondBungeePlayerHasPermission extends Condition {

    static {
        Skript.registerCondition(CondBungeePlayerHasPermission.class,
                "%bungeeplayer% has permission %string%",
                "%bungeeplayer% (doesn't|does not) have permission %string%");
    }

    private boolean invert;
    private Expression<BungeePlayer> player;
    private Expression<String> permission;

    @Override
    public boolean init(Expression<?>[] exprs, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        this.invert = (pattern == 1);
        this.player = (Expression<BungeePlayer>) exprs[0];
        this.permission = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (this.player.getSingle(e) == null)
            return false;

        final BungeePlayerPermissionPacket packet = new BungeePlayerPermissionPacket(this.player.getSingle(e), this.permission.getSingle(e));
        final Boolean response = (Boolean) CompletableFutureUtils.generateFuture(packet);

        if (response == null)
            return false;

        return this.invert ^ response;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "check if bungee player " + this.player.toString(e, debug) + " has / doesn't have permission " + this.permission.toString(e, debug);
    }

}