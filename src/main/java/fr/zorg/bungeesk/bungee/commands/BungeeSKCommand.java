package fr.zorg.bungeesk.bungee.commands;

import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.sockets.ClientServer;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class BungeeSKCommand extends Command implements Listener {

    final String prefix = "§6BungeeSK §7» ";

    public BungeeSKCommand() {
        super("bungeesk");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("bungeesk.command")) {
            sender.sendMessage(TextComponent.fromLegacyText(this.prefix + "§cYou don't have the permission to execute this command !"));
            return;
        }
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(TextComponent.fromLegacyText(this.prefix + "§bHelp"));
            sender.sendMessage(TextComponent.fromLegacyText("  §8» §6/§fbungeesk §3servers§e: §7Displays all servers connected to BungeeSK"));
            sender.sendMessage(TextComponent.fromLegacyText("  §8» §6/§fbungeesk §cdisconnect <IP:PORT / ALL>§e: §7Disconnect a specific server under BungeeSK"));
            return;
        }
        if (args[0].equalsIgnoreCase("servers")) {
            sender.sendMessage(TextComponent.fromLegacyText(this.prefix + "§3Servers"));
            BungeeSK.getInstance().getServer().getClients().forEach(clientServer -> {
                final Optional<ServerInfo> optionalServer = BungeeUtils.findServer(clientServer.getAddress().split(":")[0],
                        Integer.parseInt(clientServer.getAddress().split(":")[1]));
                if (!optionalServer.isPresent())
                    return;
                final ServerInfo server = optionalServer.get();
                sender.sendMessage(TextComponent.fromLegacyText(
                        String.format("  §8» §aName: §6%s§f, §aIP: §6%s§f, §aPort: §6%s",
                                server.getName(),
                                clientServer.getAddress().split(":")[0],
                                server.getAddress().getPort())));
            });
        } else if (args[0].equalsIgnoreCase("disconnect")) {
            if (args.length < 2) {
                sender.sendMessage(TextComponent.fromLegacyText(this.prefix + "§cYou must specify a server to disconnect !"));
                return;
            }

            if (args[1].equalsIgnoreCase("all")) {
                new ArrayList<>(BungeeSK.getInstance().getServer().getClients()).forEach(ClientServer::disconnect);
                sender.sendMessage(TextComponent.fromLegacyText(this.prefix + "§aAll BungeeSK clients are now disconnected !"));
                return;
            }

            if (!args[1].contains(":")) {
                sender.sendMessage(TextComponent.fromLegacyText(this.prefix + "§cThe server IP:PORT is invalid !"));
                return;
            }

            final Optional<ServerInfo> server = BungeeUtils.findServer(args[1].split(":")[0],
                    Integer.parseInt(args[1].split(":")[1]));

            if (!server.isPresent()) {
                sender.sendMessage(TextComponent.fromLegacyText(this.prefix + "§cNo server exists under this IP:PORT"));
                return;
            }

            final AtomicBoolean found = new AtomicBoolean(false);
            final String address = server.get().getAddress().getHostString()
                    .equalsIgnoreCase("localhost") ? "127.0.0.1" : server.get().getAddress().getHostString();

            new ArrayList<>(BungeeSK.getInstance().getServer().getClients()).forEach(clientServer -> {
                if (clientServer.getAddress().equalsIgnoreCase(
                        address + ":" + server.get().getAddress().getPort())) {
                    clientServer.disconnect();
                    found.set(true);
                    sender.sendMessage(TextComponent.fromLegacyText(this.prefix +
                            String.format("§7Disconnected server under address %s and port %s", address, server.get().getAddress().getPort())));
                    return;
                }
            });
            if (!found.get()) {
                sender.sendMessage(TextComponent.fromLegacyText(this.prefix +
                        String.format("§7No BungeeSK client found under address %s and port %s", address, server.get().getAddress().getPort())));
            }
        }
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent e) {
        if (!((CommandSender) e.getSender()).hasPermission("bungeesk.command") &&
                e.getSuggestions().stream().anyMatch(s -> s.equalsIgnoreCase("bungeesk"))) {
            e.getSuggestions().remove("bungeesk");
        }
    }
}
