package fr.zorg.bungeesk.bukkit.skript;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import fr.zorg.bungeesk.bukkit.utils.ClientBuilder;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.entities.BungeeServerBuilder;
import org.jetbrains.annotations.Nullable;

public class Types {

    static {
        Classes.registerClass(new ClassInfo<>(ClientBuilder.class, "bungeeconn")
                .defaultExpression(new EventValueExpression<>(ClientBuilder.class))
                .user("bungeeconn")
                .name("Bungee connection")
                .description("Represents a new bungee connection")
                .since("1.0.0")
                .parser(new Parser<ClientBuilder>() {

                    @Override
                    public @Nullable ClientBuilder parse(String s, ParseContext context) {
                        return null;
                    }

                    @Override
                    public String toString(ClientBuilder builder, int flags) {
                        return builder.toString();
                    }

                    @Override
                    public String toVariableNameString(ClientBuilder builder) {
                        return builder.toString();
                    }
                }));

        Classes.registerClass(new ClassInfo<>(BungeePlayer.class, "bungeeplayer")
                .defaultExpression(new EventValueExpression<>(BungeePlayer.class))
                .user("bungeeplayer")
                .name("Bungee player")
                .description("Represents a player on the network")
                .since("1.0.0")
                .parser(new Parser<BungeePlayer>() {

                    @Override
                    public BungeePlayer parse(String id, ParseContext context) {
                        return null;
                    }

                    @Override
                    public String toString(BungeePlayer player, int arg1) {
                        return player.getName();
                    }

                    @Override
                    public String toVariableNameString(BungeePlayer player) {
                        return player.getName();
                    }

                }));

        Classes.registerClass(new ClassInfo<>(BungeeServer.class, "bungeeserver")
                .defaultExpression(new EventValueExpression<>(BungeeServer.class))
                .user("bungeeserver")
                .name("Bungee player")
                .description("Represents a player on the network")
                .since("1.1.0")
                .parser(new Parser<BungeeServer>() {

                    @Override
                    public BungeeServer parse(final String id, final ParseContext context) {
                        return null;
                    }

                    @Override
                    public String toString(final BungeeServer server, final int arg1) {
                        return server.getName();
                    }

                    @Override
                    public String toVariableNameString(final BungeeServer server) {
                        return server.getName();
                    }

                }));

        Classes.registerClass(new ClassInfo<>(BungeeServerBuilder.class, "serverbuilder")
                .defaultExpression(new EventValueExpression<>(BungeeServerBuilder.class))
                .user("serverbuilder")
                .name("Bungee server builder")
                .description("Represents a bungee server builder")
                .since("2.0.0")
                .parser(new Parser<BungeeServerBuilder>() {

                    @Override
                    public BungeeServerBuilder parse(final String id, final ParseContext context) {
                        return null;
                    }

                    @Override
                    public String toString(final BungeeServerBuilder server, final int arg1) {
                        return server.getName();
                    }

                    @Override
                    public String toVariableNameString(final BungeeServerBuilder server) {
                        return server.getName();
                    }

                }));
    }

}
