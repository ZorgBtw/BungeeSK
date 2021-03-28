package fr.zorg.bungeesk.bukkit.skript;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import fr.zorg.bungeesk.bukkit.sockets.ClientSettings;

public class Types {

    static {
        Classes.registerClass(new ClassInfo<>(ClientSettings.class, "bungeeconn")
                .defaultExpression(new EventValueExpression<>(ClientSettings.class))
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


    }
}