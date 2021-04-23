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
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.sockets.ConnectionClient;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Client real name")
@Description("Returns the client's name in the Bungeecord proxy configuration")
@Examples("set {_name} to real name of this client")
@Since("1.0.2")
public class ExprClientRealName extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprClientRealName.class,
                String.class,
                ExpressionType.SIMPLE,
                "real name of this client",
                "this client's real name");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Nullable
    @Override
    protected String[] get(Event e) {
        assert ConnectionClient.get() != null;
        final String result = ConnectionClient.get().future("CLIENTREALNAMEµ" + BungeeSK.getInstance().getServer().getIp() + ":" + BungeeSK.getInstance().getServer().getPort());
        return new String[] { result };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
            return "client's real name on the bungeecord";
    }

}
