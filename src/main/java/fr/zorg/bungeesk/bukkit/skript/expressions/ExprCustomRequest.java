package fr.zorg.bungeesk.bukkit.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.registrations.Classes;
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.bukkit.utils.CompletableFutureUtils;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.GetRequestFromOtherServerPacket;
import fr.zorg.bungeesk.common.utils.Pair;
import org.bukkit.event.Event;

@Name("Custom request from other Bungee server")
@Description("Send a custom request to another Bungee server, and get the response. This can be any value, as long as it could be stored in a variable")
@Examples("broadcast custom request \"hello\" from bungee server named \"lobby\"\n" +
        "\n" +
        "#On the other server\n" +
        "on custom request:\n" +
        "\tif event-string is \"hello\":\n" +
        "\t\tset custom request response to \"hi !\"")
@Since("2.0.0")
public class ExprCustomRequest extends SimpleExpression<Object> {

    static {
        Skript.registerExpression(ExprCustomRequest.class, Object.class, ExpressionType.SIMPLE, "custom request [named] %string% from [server] %bungeeserver%");
    }

    private Expression<String> request;
    private Expression<BungeeServer> server;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.request = (Expression<String>) exprs[0];
        this.server = (Expression<BungeeServer>) exprs[1];
        return true;
    }

    @Override
    protected Object[] get(Event e) {
        final String request = this.request.getSingle(e);
        final BungeeServer server = this.server.getSingle(e);

        if (server == null)
            return new Object[0];

        final GetRequestFromOtherServerPacket packet = new GetRequestFromOtherServerPacket(request, server);
        final Object response = CompletableFutureUtils.generateFuture(packet);

        if (response == null)
            return new Object[0];

        if (response instanceof Pair) {
            final Pair<String, byte[]> pair = (Pair<String, byte[]>) response;
            return new Object[]{Classes.deserialize(pair.getFirstValue(), pair.getSecondValue())};
        }

        return new Object[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "custom request " + this.request.toString(e, debug) + " from " + this.server.toString(e, debug);
    }

}