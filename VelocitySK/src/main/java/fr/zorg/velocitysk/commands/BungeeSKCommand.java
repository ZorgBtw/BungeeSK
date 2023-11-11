package fr.zorg.velocitysk.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.velocitysk.packets.PacketServer;
import fr.zorg.velocitysk.packets.SocketServer;
import fr.zorg.velocitysk.utils.VelocityUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

import java.util.ArrayList;

public class BungeeSKCommand implements SimpleCommand {

    public static final String PREFIX = "§6BungeeSK §7» ";

    @Override
    public void execute(Invocation invocation) {

        final CommandSource sender = invocation.source();
        final String[] args = invocation.arguments();

        if (!sender.hasPermission("bungeesk.command")) {
            sender.sendMessage(VelocityUtils.getTextComponent("§cYou don't have permission to use this command"));
            return;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(VelocityUtils.getTextComponent(PREFIX + "§bHelp"));
            sender.sendMessage(VelocityUtils.getTextComponent("  §8» §6/§fbungeesk §3servers§e: §7Displays all servers connected to BungeeSK"));
            sender.sendMessage(VelocityUtils.getTextComponent("  §8» §6/§fbungeesk §cdisconnect <IP:PORT / ALL>§e: §7Disconnect a specific server under BungeeSK"));
            sender.sendMessage(VelocityUtils.getTextComponent("  §8» §6/§fbungeesk §a<start|stop|restart>§e: §7Start, stop or restart BungeeSK"));
        } else if (args[0].equalsIgnoreCase("servers")) {
            if (PacketServer.getServerSocket() == null) {
                sender.sendMessage(VelocityUtils.getTextComponent(PREFIX + "§cBungeeSK is currently stopped"));
                return;
            }

            if (PacketServer.getClientSockets().size() == 0) {
                sender.sendMessage(VelocityUtils.getTextComponent(PREFIX + "§fNo servers are connected to BungeeSK"));
                return;
            }
            sender.sendMessage(VelocityUtils.getTextComponent(PREFIX + "§3Servers"));
            PacketServer.getClientSockets().forEach(socket -> {
                String message = "  §8» §6" + socket.getSocket().getInetAddress().getHostAddress() + ":" + socket.getMinecraftPort();
                final BungeeServer server = VelocityUtils.getServerFromSocket(socket);
                if (server != null)
                    message += " §f(§e" + server.getName() + " §7-> §b" + socket.getPing() + "ms§f)";
                if (sender instanceof Player) {
                    final Component component = Component.text(message);
                    final Component disconnectServerComponent = Component.text(" §c[✖]")
                            .hoverEvent(HoverEvent.showText(Component.text("§cDisconnect this server")))
                            .clickEvent(ClickEvent.runCommand("/bungeesk disconnect " + socket.getSocket().getInetAddress().getHostAddress() + ":" + socket.getMinecraftPort()));
                    component.append(disconnectServerComponent);
                    sender.sendMessage(component);
                } else
                    sender.sendMessage(VelocityUtils.getTextComponent(message));
            });
        } else if (args[0].equalsIgnoreCase("disconnect")) {
            if (args.length < 2) {
                sender.sendMessage(VelocityUtils.getTextComponent(PREFIX + "§cYou must specify a server to disconnect !"));
                return;
            }

            if (args[1].equalsIgnoreCase("all")) {
                new ArrayList<>(PacketServer.getClientSockets()).forEach(SocketServer::disconnect);
                sender.sendMessage(VelocityUtils.getTextComponent(PREFIX + "§aAll BungeeSK clients are now disconnected !"));
                return;
            }

            if (!args[1].contains(":")) {
                sender.sendMessage(VelocityUtils.getTextComponent(PREFIX + "§cThe server IP:PORT is invalid !"));
                return;
            }

            final SocketServer server = PacketServer.getClientSockets().stream().filter(socket -> socket.getSocket().getInetAddress().getHostAddress().equals(args[1].split(":")[0]) && socket.getMinecraftPort() == Integer.parseInt(args[1].split(":")[1])).findFirst().orElse(null);

            if (server == null) {
                sender.sendMessage(VelocityUtils.getTextComponent(PREFIX + "§cNo server exists under this IP:PORT"));
                return;
            }

            server.disconnect();
            sender.sendMessage(VelocityUtils.getTextComponent(PREFIX +
                    String.format("§7Disconnected server under address %s and port %s", server.getSocket().getInetAddress().getHostAddress(), server.getMinecraftPort())));
        } else if (args[0].equalsIgnoreCase("stop")) {
            if (PacketServer.getServerSocket() == null || PacketServer.getServerSocket().isClosed()) {
                sender.sendMessage(VelocityUtils.getTextComponent(PREFIX + "§cBungeeSK is already stopped !"));
                return;
            }
            PacketServer.stop();
            sender.sendMessage(VelocityUtils.getTextComponent(PREFIX + "§cBungeeSK has been stopped successfully !"));
        } else if (args[0].equalsIgnoreCase("start")) {
            if (PacketServer.getServerSocket() != null && !PacketServer.getServerSocket().isClosed()) {
                sender.sendMessage(VelocityUtils.getTextComponent(PREFIX + "§cBungeeSK is already started !"));
                return;
            }
            PacketServer.start();
            sender.sendMessage(VelocityUtils.getTextComponent(PREFIX + "§aBungeeSK has been started successfully !"));
        } else if (args[0].equalsIgnoreCase("restart")) {
            if (PacketServer.getServerSocket() == null || PacketServer.getServerSocket().isClosed()) {
                sender.sendMessage(VelocityUtils.getTextComponent(PREFIX + "§cBungeeSK is already started !"));
                return;
            }
            PacketServer.stop();
            PacketServer.start();
            sender.sendMessage(VelocityUtils.getTextComponent(PREFIX + "§aBungeeSK has been restarted successfully !"));
        } else {
            sender.sendMessage(VelocityUtils.getTextComponent(PREFIX + "§cUnknown command !"));
        }
    }

}