package fr.zorg.bungeesk.bungee.commands;

import fr.zorg.bungeesk.bungee.packets.PacketServer;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;

public class BungeeSKCommand extends Command implements Listener {

    public static final String PREFIX = "§6BungeeSK §7» ";

    public BungeeSKCommand() {
        super("bungeesk");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("bungeesk.command")) {
            sender.sendMessage(BungeeUtils.getTextComponent("&cYou don't have permission to use this command"));
            return;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(BungeeUtils.getTextComponent(PREFIX + "§bHelp"));
            sender.sendMessage(BungeeUtils.getTextComponent("  §8» §6/§fbungeesk §3servers§e: §7Displays all servers connected to BungeeSK"));
            sender.sendMessage(BungeeUtils.getTextComponent("  §8» §6/§fbungeesk §cdisconnect <IP:PORT / ALL>§e: §7Disconnect a specific server under BungeeSK"));
            sender.sendMessage(BungeeUtils.getTextComponent("  §8» §6/§fbungeesk §a<start|stop|restart>§e: §7Start, stop or restart BungeeSK"));
        } else if (args[0].equalsIgnoreCase("servers")) {
            if (PacketServer.getServerSocket() == null) {
                sender.sendMessage(BungeeUtils.getTextComponent(PREFIX + "§cBungeeSK is currently stopped"));
                return;
            }

            if (PacketServer.getClientSockets().size() == 0) {
                sender.sendMessage(BungeeUtils.getTextComponent(PREFIX + "§fNo servers are connected to BungeeSK"));
                return;
            }
            sender.sendMessage(BungeeUtils.getTextComponent(PREFIX + "§3Servers"));
            PacketServer.getClientSockets().forEach(socket -> {
                String message = "  §8» §6" + socket.getSocket().getInetAddress().getHostAddress() + ":" + socket.getMinecraftPort();
                final BungeeServer server = BungeeUtils.getServerFromSocket(socket);
                if (server != null)
                    message += " §f(" + server.getName() + ")";
                sender.sendMessage(BungeeUtils.getTextComponent(message));
            });
        } else if (args[0].equalsIgnoreCase("disconnect")) {
            if (args.length < 2) {
                sender.sendMessage(BungeeUtils.getTextComponent(PREFIX + "§cYou must specify a server to disconnect !"));
                return;
            }

            if (args[1].equalsIgnoreCase("all")) {
                new ArrayList<>(PacketServer.getClientSockets()).forEach(SocketServer::disconnect);
                sender.sendMessage(BungeeUtils.getTextComponent(PREFIX + "§aAll BungeeSK clients are now disconnected !"));
                return;
            }

            if (!args[1].contains(":")) {
                sender.sendMessage(BungeeUtils.getTextComponent(PREFIX + "§cThe server IP:PORT is invalid !"));
                return;
            }

            final SocketServer server = PacketServer.getClientSockets().stream().filter(socket -> socket.getSocket().getInetAddress().getHostAddress().equals(args[1].split(":")[0]) && socket.getMinecraftPort() == Integer.parseInt(args[1].split(":")[1])).findFirst().orElse(null);

            if (server == null) {
                sender.sendMessage(BungeeUtils.getTextComponent(PREFIX + "§cNo server exists under this IP:PORT"));
                return;
            }

            server.disconnect();
            sender.sendMessage(BungeeUtils.getTextComponent(PREFIX +
                    String.format("§7Disconnected server under address %s and port %s", server.getSocket().getInetAddress().getHostAddress(), server.getMinecraftPort())));
        } else if (args[0].equalsIgnoreCase("stop")) {
            if (PacketServer.getServerSocket() == null || PacketServer.getServerSocket().isClosed()) {
                sender.sendMessage(BungeeUtils.getTextComponent(PREFIX + "§cBungeeSK is already stopped !"));
                return;
            }
            PacketServer.stop();
            sender.sendMessage(BungeeUtils.getTextComponent(PREFIX + "§cBungeeSK has been stopped successfully !"));
        } else if (args[0].equalsIgnoreCase("start")) {
            if (PacketServer.getServerSocket() != null && !PacketServer.getServerSocket().isClosed()) {
                sender.sendMessage(BungeeUtils.getTextComponent(PREFIX + "§cBungeeSK is already started !"));
                return;
            }
            PacketServer.start();
            sender.sendMessage(BungeeUtils.getTextComponent(PREFIX + "§aBungeeSK has been started successfully !"));
        } else if (args[0].equalsIgnoreCase("restart")) {
            if (PacketServer.getServerSocket() == null || PacketServer.getServerSocket().isClosed()) {
                sender.sendMessage(BungeeUtils.getTextComponent(PREFIX + "§cBungeeSK is already started !"));
                return;
            }
            PacketServer.stop();
            PacketServer.start();
            sender.sendMessage(BungeeUtils.getTextComponent(PREFIX + "§aBungeeSK has been restarted successfully !"));
        } else {
            sender.sendMessage(BungeeUtils.getTextComponent(PREFIX + "§cUnknown command !"));
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