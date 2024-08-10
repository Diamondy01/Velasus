package me.diamondy.velasus.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;

public class PullCommand implements SimpleCommand {
    private final ProxyServer server;

    public PullCommand(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (args.length != 1) {
            source.sendMessage(Component.text("Usage: /pull <player>"));
            return;
        }

        String target = args[0];
        String serverName = args[1];

        RegisteredServer targetServer = server.getServer(serverName).orElse(null);
        Player player = server.getPlayer(target).orElse(null);

        if (targetServer == null) {
            source.sendMessage(Component.text("Server not found: " + serverName));
            return;
        }

        if (player == null) {
            source.sendMessage(Component.text("Player not found: " + target));
            return;
        }

        if (player.getCurrentServer().isPresent()) {
            player.createConnectionRequest((RegisteredServer) player.getCurrentServer().get()).fireAndForget();
            source.sendMessage(Component.text("Player " + target + " has been pulled back to " + player.getCurrentServer().get().getServerInfo().getName()));
        } else {
            source.sendMessage(Component.text("Player " + target + " is not on any server"));
        }
    }

}
