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
import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;
import fr.zorg.bungeesk.bukkit.utils.BungeeServer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Send bungee player to server")
@Description("Send a player on the network to a specific server")
@Examples("send bungee player named \"Zorg_btw\" to bungee server named \"lobby2\"")
@Since("1.0.0 - 1.1.0: Usage of BungeeServer type")
public class EffSendBungeePlayerToServer extends Effect {

    static {
        Skript.registerEffect(EffSendBungeePlayerToServer.class,
                "send %bungeeplayer% to %bungeeserver%");
    }

    private Expression<BungeePlayer> player;
    private Expression<BungeeServer> server;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.player = (Expression<BungeePlayer>) exprs[0];
        this.server = (Expression<BungeeServer>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (BungeeSK.isClientConnected()) {
            if (this.player.getSingle(e) == null)
                return;

            ConnectionClient.get().write(true, "effectSendBungeePlayerToServer",
                    "playerUniqueId", this.player.getSingle(e).getUuid(),
                    "serverAddress", this.server.getSingle(e).getAddress(),
                    "serverPort", String.valueOf(this.server.getSingle(e).getPort()));
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "send bungee player " + player.toString(e, debug) + " to server " + server.toString(e, debug);
    }

}
