package fr.zorg.bungeesk.bungee.storage;

import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.utils.Utils;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

public class BungeeConfig {

    private static transient BungeeConfig instance;

    public static BungeeConfig get() {
        if (instance == null)
            instance = new BungeeConfig();
        return instance;
    }

    private int port = 20000;
    private String password = Utils.randomString(16);
    private boolean sendFilesAuto = false;
    private boolean whitelistIp = true;
    private List<String> authorizedIp = Collections.singletonList("127.0.0.1");

    private transient File file;
    private transient Configuration config;


    private BungeeConfig() {
    }

    public void load() {
        this.file = new File(BungeeSK.getInstance().getDataFolder(), "config.yml");
        if (!BungeeSK.getInstance().getDataFolder().exists())
            BungeeSK.getInstance().getDataFolder().mkdir();
        if (!this.file.exists()) {
            try (final InputStream in = BungeeSK.getInstance().getResourceAsStream("config.yml")) {
                Files.copy(in, this.file.toPath());
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        try {
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
            for (final Field field : BungeeConfig.class.getDeclaredFields()) {
                if (Modifier.isTransient(field.getModifiers())) {
                    continue;
                }
                final String pathField = field.getName().replace("__", ".");
                if (!BungeeUtils.addDefault(this.config, pathField, field.get(this)) && !Modifier.isFinal(field.getModifiers())) {
                    field.set(this, this.config.get(pathField));
                } else if (Modifier.isFinal(field.getModifiers())) {
                    this.config.set(pathField, field.get(this));
                }
            }
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.config, this.file);
        } catch (final IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return this.port;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean isSendFilesAutoEnabled() {
        return this.sendFilesAuto;
    }

    public boolean isWhitelistIp() {
        return this.whitelistIp;
    }

    public List<String> getAuthorizedIp() {
        return this.authorizedIp;
    }
}
