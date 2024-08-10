package me.diamondy.velasus.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;

public class SendCommand implements SimpleCommand {

    private final ProxyServer server;

    public SendCommand(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (args.length != 2) {
            source.sendMessage(Component.text("Usage: /send <player/all> <server>"));
            return;
        }

        String target = args[0];
        String serverName = args[1];

        RegisteredServer targetServer = server.getServer(serverName).orElse(null);

        if (targetServer == null) {
            source.sendMessage(Component.text("Server not found: " + serverName));
            return;
        }

        if ("all".equalsIgnoreCase(target)) {
            for (Player player : server.getAllPlayers()) {
                player.createConnectionRequest(targetServer).fireAndForget();
            }
            source.sendMessage(Component.text("All players have been sent to " + serverName));
        } else {
            Player player = server.getPlayer(target).orElse(null);
            if (player == null) {
                source.sendMessage(Component.text("Player not found: " + target));
                return;
            }
            player.createConnectionRequest(targetServer).fireAndForget();
            source.sendMessage(Component.text("Player " + target + " has been sent to " + serverName));
        }
    }
}