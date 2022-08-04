package fr.zorg.bungeesk.bukkit.skript.expressions.bungeeserver;

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
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.GetSelfBungeeServerPacket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Get bungee server from Spigot")
@Description("Get the bungee server associated to the Spigot server")
@Examples("set {_server} to this bungee server")
@Since("2.0.0")
public class ExprGetSelfBungeeServer extends SimpleExpression<BungeeServer> {

    static {
        Skript.registerExpression(ExprGetSelfBungeeServer.class,
                BungeeServer.class,
                ExpressionType.SIMPLE,
                "this bungee[ ]server");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected BungeeServer[] get(Event e) {
        final GetSelfBungeeServerPacket packet = new GetSelfBungeeServerPacket();
        final BungeeServer response = (BungeeServer) CompletableFutureUtils.generateFuture(packet);
        if (response == null)
            return new BungeeServer[0];
        return new BungeeServer[]{response};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends BungeeServer> getReturnType() {
        return BungeeServer.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "bungee server associated to this spigot";
    }

}