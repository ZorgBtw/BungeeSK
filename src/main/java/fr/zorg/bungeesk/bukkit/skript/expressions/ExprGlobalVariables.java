package fr.zorg.bungeesk.bukkit.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.google.gson.JsonObject;
import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.sockets.ConnectionClient;
import org.bukkit.event.Event;

@Name("Global variables stored on the Bungeecord")
@Description("Set, get and delete a global variable stored on the Bungeecord. The variable can only be a String")
@Examples("set global variable \"rank.%player%\" to \"Admin\"\n" +
        "set {_rank} to global variable \"rank.%player%\"")
@Since("1.1.0")
public class ExprGlobalVariables extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprGlobalVariables.class, String.class, ExpressionType.SIMPLE, "global variable [named] %string%");
    }

    private Expression<String> varName;

    @Override
    public boolean init(Expression<?>[] exprs, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        this.varName = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected String[] get(Event e) {
        if (BungeeSK.isClientConnected()) {
            final JsonObject result = ConnectionClient.get().future("getGlobalVariable",
                    "varName", this.varName.getSingle(e));
            if (result.get("value").getAsString().equalsIgnoreCase("NONE"))
                return new String[0];
            return new String[]{result.get("value").getAsString()};
        }
        return new String[0];
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
    public String toString(Event e, boolean debug) {
        return "global variable named " + this.varName.toString(e, debug);
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.DELETE)
            return CollectionUtils.array(String.class);
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if (BungeeSK.isClientConnected()) {
            switch (mode) {
                case SET: {
                    if (delta == null)
                        return;
                    ConnectionClient.get().write(true, "setGlobalVariable",
                            "varName", this.varName.getSingle(e),
                            "value", String.valueOf(delta[0]));
                    break;
                }
                case DELETE: {
                    ConnectionClient.get().write(true, "deleteGlobalVariable",
                            "varName", this.varName.getSingle(e));
                    break;
                }
            }
        }
    }
}
