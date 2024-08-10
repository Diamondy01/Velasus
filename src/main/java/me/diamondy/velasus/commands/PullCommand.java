package me.diamondy.velasus.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public class PullCommand implements SimpleCommand {
    private final ProxyServer server;

    public PullCommand(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        if (!(source instanceof Player) || !AuthorizedUsers.isAuthorized(((Player) source).getUsername())) {
            source.sendMessage(Component.text("You do not have permission to use this command."));
            return;
        }

        String[] args = invocation.arguments();
        if (args.length != 1) {
            source.sendMessage(Component.text("Usage: /pull <player>"));
            return;
        }

        String target = args[0];
        Player player = server.getPlayer(target).orElse(null);

        if (player == null) {
            source.sendMessage(Component.text("Player not found: " + target));
            return;
        }

        if (!player.getCurrentServer().isPresent()) {
            source.sendMessage(Component.text("Player " + target + " is not on any server"));
            return;
        }

        RegisteredServer targetServer = player.getCurrentServer().get().getServer();
        RegisteredServer sourceServer = ((Player) source).getCurrentServer().get().getServer();

        if (targetServer.equals(sourceServer)) {
            source.sendMessage(Component.text("Player " + target + " is already on your server"));
            return;
        }

        player.createConnectionRequest(sourceServer).fireAndForget();
        source.sendMessage(Component.text("Player " + target + " has been pulled to your server"));
    }
    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length == 1) {
            String partial_player_command = args[0];
            return server.getAllPlayers().stream()
                    .map(Player::getUsername)
                    .filter(playerName -> playerName.startsWith(partial_player_command))
                    .toList();
        }
        else if (args.length == 2) {
            String partial_server_command = args[1];
            return server.getAllServers().stream()
                    .map(RegisteredServer::getServerInfo)
                    .map(serverInfo -> serverInfo.getName().toLowerCase())
                    .filter(serverName -> serverName.startsWith(partial_server_command))
                    .toList();
        }
        return new ArrayList<>();
    }
}