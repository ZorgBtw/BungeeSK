package fr.zorg.bungeesk.bukkit.skript;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import fr.zorg.bungeesk.bukkit.utils.ClientBuilder;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
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
                        }
                ));

        Classes.registerClass(new ClassInfo<>(BungeePlayer.class, "bungeeplayer")
                .defaultExpression(new EventValueExpression<>(BungeePlayer.class))
                .user("bungeeplayer")
                .name("Bungee player")
                .description("Represents a player on the network")
                .since("1.0.0")
                .parser(new Parser<BungeePlayer>() {

                    @Override
                    public BungeePlayer parse(final String id, final ParseContext context) {
                        return null;
                    }

                    @Override
                    public String toString(final BungeePlayer player, final int arg1) {
                        return player.getName();
                    }

                    @Override
                    public String toVariableNameString(final BungeePlayer player) {
                        return player.getName();
                    }

                }));
    }

}
