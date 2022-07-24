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
import fr.zorg.bungeesk.common.packets.GetBungeeServerFromAddressPacket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Get a bungee server from its address and port")
@Description("Get one of the servers precised in the Bungeecord config whose address and port match")
@Examples("set {_server} to bungee server with address \"127.0.0.1\" and port 25566")
@Since("1.1.0")
public class ExprBungeeServerFromAddress extends SimpleExpression<BungeeServer> {

    static {
        Skript.registerExpression(ExprBungeeServerFromAddress.class,
                BungeeServer.class,
                ExpressionType.SIMPLE,
                "bungee[ ]server with address %string% and port %integer%");
    }

    private Expression<String> address;
    private Expression<Integer> port;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.address = (Expression<String>) exprs[0];
        this.port = (Expression<Integer>) exprs[1];
        return true;
    }

    @Override
    protected BungeeServer[] get(Event e) {
        final GetBungeeServerFromAddressPacket packet = new GetBungeeServerFromAddressPacket(this.address.getSingle(e), this.port.getSingle(e));
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
        return "bungee server with address " + this.address.toString(e, debug) + " and port " + this.port.toString(e, debug);
    }
}