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
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.variables.SerializedVariable;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import fr.zorg.bungeesk.bukkit.packets.PacketClient;
import fr.zorg.bungeesk.bukkit.utils.CompletableFutureUtils;
import fr.zorg.bungeesk.common.entities.GlobalVariableChanger;
import fr.zorg.bungeesk.common.packets.GlobalVariablePacket;
import fr.zorg.bungeesk.common.utils.Pair;
import org.bukkit.event.Event;

@Name("Global variables stored on the Bungeecord")
@Description("Set, get and delete a global variable stored on the Bungeecord. Value can be any type.")
@Examples("set global variable \"rank.%player%\" to \"Admin\"\n" +
        "set {_rank} to global variable \"rank.%player%\"\n" +
        "set global variable \"money.%player%\" to 1000\n" +
        "set global variable \"test.%player%\" to true")
@Since("1.1.0")
public class ExprGlobalVariables extends SimpleExpression<Object> {

    static {
        Skript.registerExpression(ExprGlobalVariables.class, Object.class, ExpressionType.SIMPLE, "global var[iable] [named] %string%");
    }

    private Expression<String> varName;

    @Override
    public boolean init(Expression<?>[] exprs, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        this.varName = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected Object[] get(Event e) {
        final GlobalVariablePacket packet = new GlobalVariablePacket(this.varName.getSingle(e), null, null, GlobalVariableChanger.GET);
        final Object responseObject = CompletableFutureUtils.generateFuture(packet);

        if (responseObject == null)
            return new Object[0];

        final Pair<byte[], String> response = (Pair<byte[], String>) responseObject;

        final byte[] value = response.getFirstValue();
        final String type = response.getSecondValue();

        return new Object[]{Classes.deserialize(type, value)};
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
        return "global variable named " + this.varName.toString(e, debug);
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.DELETE)
            return CollectionUtils.array(Object.class);
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if (mode != Changer.ChangeMode.DELETE && delta == null)
            return;

        switch (mode) {
            case SET: {
                final SerializedVariable.Value variable = Classes.serialize(delta[0]);

                if (variable == null)
                    return;

                final byte[] value = variable.data;
                final String type = variable.type;

                final GlobalVariablePacket packet = new GlobalVariablePacket(this.varName.getSingle(e), value, type, GlobalVariableChanger.SET);
                PacketClient.sendPacket(packet);
                break;
            }
            case DELETE: {
                final GlobalVariablePacket packet = new GlobalVariablePacket(this.varName.getSingle(e), null, null, GlobalVariableChanger.DELETE);
                PacketClient.sendPacket(packet);
                break;
            }
        }
    }
}