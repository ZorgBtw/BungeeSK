package fr.zorg.bungeesk.bukkit.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.sockets.ConnectionClient;
import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;
import org.bukkit.event.Event;

@Name("Send Bungeecord title to bungee player")
@Description("Send a Bungeecord title to a player on the network")
@Examples("send bungeecord title \"&cHey you !\" with subtitle \"&6How are you ? :)\" for 3 seconds to bungee player named \"Notch\" with fade-in 10 ticks and fade-out 2 seconds")
@Since("1.1.0")
public class EffSendTitle extends Effect {

    static {
        Skript.registerEffect(EffSendTitle.class,
                "send bungeecord title %string% [with subtitle %-string%] [for %-timespan%] to %bungeeplayer% [with fade-in %-timespan%] [(and|with) fade-out %timespan%]");
    }

    private Expression<String> title;
    private Expression<String> subTitle;
    private Expression<Timespan> time;
    private Expression<BungeePlayer> player;
    private Expression<Timespan> fadeIn;
    private Expression<Timespan> fadeOut;

    @Override
    public boolean init(Expression<?>[] exprs, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        this.title = (Expression<String>) exprs[0];
        this.subTitle = (Expression<String>) exprs[1];
        this.time = (Expression<Timespan>) exprs[2];
        this.player = (Expression<BungeePlayer>) exprs[3];
        this.fadeIn = (Expression<Timespan>) exprs[4];
        this.fadeOut = (Expression<Timespan>) exprs[5];
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (BungeeSK.isClientConnected()) {
            if (this.player.getSingle(e) == null)
                return;

            final String title = this.title == null ? "NONE" : this.title.getSingle(e);
            final String subTitle = this.subTitle == null ? "NONE" : this.subTitle.getSingle(e);
            final String time = this.time == null ? "NONE" : String.valueOf(this.time.getSingle(e).getTicks_i());
            final String fadeIn = this.fadeIn == null ? "NONE" : String.valueOf(this.fadeIn.getSingle(e).getTicks_i());
            final String fadeOut = this.fadeOut == null ? "NONE" : String.valueOf(this.fadeOut.getSingle(e).getTicks_i());

            ConnectionClient.get().write(true, "effectSendBungeePlayerTitle",
                    "playerUuid", this.player.getSingle(e).getUuid(),
                    "title", title,
                    "subTitle", subTitle,
                    "time", time,
                    "fadeIn", fadeIn,
                    "fadeOut", fadeOut);
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return null;
    }

}
