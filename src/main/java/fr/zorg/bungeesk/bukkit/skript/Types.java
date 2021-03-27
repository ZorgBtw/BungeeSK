package fr.zorg.bungeesk.bukkit.skript;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import fr.zorg.bungeesk.bukkit.sockets.ClientSettings;
import fr.zorg.bungeesk.bukkit.sockets.ConnectionClient;

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
                        return null; // x parsed as Client
                    }

                    @Override
                    public String toString(final ClientSettings settings, final int arg1) {
                        return settings.toString(); // send "%Client%"
                    }

                    @Override
                    public String toVariableNameString(final ClientSettings client) {
                        return client.toString(); // set {players::%player%::%Client%} to "hello type"
                    }

                }));


    }
}
