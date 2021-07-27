package fr.zorg.bungeesk.bukkit.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.sockets.ConnectionClient;
import org.bukkit.event.Event;

@Name("Add dynamic server into Bungeecord")
@Description("Add a dynamic server into Bungeecord. The server will not appear in the Bungeecord config and will be deleted from the servers list if the Bungeecord is shut down")
@Examples("put server with address \"127.0.0.1\", port 25566, motd \"This is the second lobby !\" and name \"lobby2\" into bungeecord")
@Since("1.1.1")
public class EffAddServerToBungee extends Effect {

    static {
        Skript.registerEffect(EffAddServerToBungee.class,
                "put server with (address|ip) %string%, port %integer%, motd %string% and name %string% into bungeecord");
    }

    private Expression<String> address;
    private Expression<Long> port;
    private Expression<String> motd;
    private Expression<String> name;

    @Override
    public boolean init(Expression<?>[] exprs, int patter, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        this.address = (Expression<String>) exprs[0];
        this.port = (Expression<Long>) exprs[1];
        this.motd = (Expression<String>) exprs[2];
        this.name = (Expression<String>) exprs[3];
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (BungeeSK.isClientConnected()) {
            ConnectionClient.get().write(true, "effectAddDynamicServer",
                    "address", this.address.getSingle(e),
                    "port", String.valueOf(this.port.getSingle(e)),
                    "motd", this.motd.getSingle(e),
                    "name", this.name.getSingle(e));
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "add server with address " + this.address.toString(e, debug)
                + " and port " + this.port.toString(e, debug)
                + " with name " + this.name.toString(e, debug)
                + " and motd " + this.motd.toString(e, debug);
    }
}
