package fr.zorg.bungeesk.bukkit.skript;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import fr.zorg.bungeesk.bukkit.sockets.ClientSettings;
import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;

public class Types {

    static {
        Classes.registerClass(new ClassInfo<>(ClientSettings.class, "bungeeconn")
                .defaultExpression(new EventValueExpression<>(ClientSettings.class))
                .user("bungeeconn")
                .name("Bungee connection")
                .description("Represents a new bungee connection")
                .since("1.0.0")
                .parser(new Parser<ClientSettings>() {

                    @Override
                    public String getVariableNamePattern() {
                        return ".+";
                    }

                    @Override
                    public ClientSettings parse(final String id, final ParseContext context) {
                        return null;
                    }

                    @Override
                    public String toString(final ClientSettings settings, final int arg1) {
                        return settings.toString();
                    }

                    @Override
                    public String toVariableNameString(final ClientSettings client) {
                        return client.toString();
                    }

                }));


        Classes.registerClass(new ClassInfo<>(BungeePlayer.class, "bungee-player")
                .defaultExpression(new EventValueExpression<>(BungeePlayer.class))
                .user("bungeeplayer")
                .name("Bungee player")
                .description("Represents a player on the network")
                .since("1.0.0")
                .parser(new Parser<BungeePlayer>() {

                    @Override
                    public String getVariableNamePattern() {
                        return ".+";
                    }

                    @Override
                    public BungeePlayer parse(final String id, final ParseContext context) {
                        return null;
                    }

                    @Override
                    public String toString(final BungeePlayer player, final int arg1) {
                        return player.getPlayer();
                    }

                    @Override
                    public String toVariableNameString(final BungeePlayer player) {
                        return player.getPlayer();
                    }

                }));


    }
}