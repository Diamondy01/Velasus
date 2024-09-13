package me.diamondy.velasus.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServerCMD implements SimpleCommand {
    private final ProxyServer server;

    public ServerCMD(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (args.length != 1) {
            source.sendMessage(Component.text("Usage: /server <server>").color(NamedTextColor.RED));
            return;
        }

        String serverName = args[0];

        if (source instanceof Player player) {
            Optional<RegisteredServer> targetServer = server.getServer(serverName);
            if (targetServer.isPresent()) {
                player.createConnectionRequest(targetServer.get()).fireAndForget();
                player.sendMessage(Component.text("Connecting to server: " + serverName).color(NamedTextColor.GREEN));
            } else {
                source.sendMessage(Component.text("Server not found.").color(NamedTextColor.RED));
            }
        } else {
            source.sendMessage(Component.text("You must be a player to use this command.").color(NamedTextColor.RED));
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length == 1) {
            String partial_server_command = args[0];
            return server.getAllServers().stream()
                    .map(RegisteredServer::getServerInfo)
                    .map(serverInfo -> serverInfo.getName().toLowerCase())
                    .filter(serverName -> serverName.startsWith(partial_server_command))
                    .toList();

        }
        return new ArrayList<>();
    }
}
