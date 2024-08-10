package me.diamondy.velasus.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;

public class JumpCommand implements SimpleCommand {
    private final ProxyServer server;

    public JumpCommand(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (args.length != 1) {
            source.sendMessage(Component.text("Usage: /jump <player>"));
            return;
        }

        String target = args[0];
        String serverName = args[1];

        Player player = server.getPlayer(target).orElse(null);
        RegisteredServer targetServer = server.getServer(serverName).orElse(null);

        if (server.getPlayer(target).isPresent()) {
            source.sendMessage(Component.text("Player not found: " + target));
            return;
        }

        if (server.getServer(serverName).isPresent()) {
            source.sendMessage(Component.text("Server not found: " + serverName));
            return;
        }

        if (player.getCurrentServer().isPresent()) {
            player.createConnectionRequest(targetServer).fireAndForget();
            source.sendMessage(Component.text("Player " + target + " has been sent to " + serverName));
        } else {
            source.sendMessage(Component.text("Player " + target + " is not on any server"));
        }
    }
}
