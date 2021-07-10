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
import org.bukkit.event.Event;

@Name("Kick bungee player")
@Description("Kicks a player on the network from the network")
@Examples("kick bungee player named \"Zorg_btw\" from bungeecord due to \"&6Spamming in the chat is not good :'(\"")
@Since("1.1.0")
public class EffKickBungeePlayer extends Effect {

    static {
        Skript.registerEffect(EffKickBungeePlayer.class,
                "kick %bungeeplayer% from bungeecord [(due to|because of) %-string%]");
    }

    private Expression<BungeePlayer> player;
    private Expression<String> reason;

    @Override
    public boolean init(Expression<?>[] exprs, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        this.player = (Expression<BungeePlayer>) exprs[0];
        this.reason = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (BungeeSK.isClientConnected()) {
            if (this.player.getSingle(e) == null)
                return;
            System.out.println("this.reason.getSingle(e) = " + this.reason.getSingle(e));
            ConnectionClient.get().write(true, "effectKickBungeePlayer",
                    "playerUniqueId", this.player.getSingle(e).getUuid(),
                    "reason", (this.reason == null ? "NONE" : this.reason.getSingle(e)));
        }
    }

    @Override
    public String toString(Event event, boolean b) {
        return "kicks bungee player with uuid " + this.player.toString(event, b);
    }

}