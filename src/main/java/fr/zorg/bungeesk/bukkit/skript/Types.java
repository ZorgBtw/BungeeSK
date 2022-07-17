package fr.zorg.bungeesk.bukkit.skript;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import fr.zorg.bungeesk.bukkit.utils.ClientBuilder;
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
    }

}
