package me.diamondy.velasus.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;

public class ListPlayerCommand implements SimpleCommand {
    public final ProxyServer server;

    public ListPlayerCommand(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();

        if (!(source instanceof Player player) || !AuthorizedUsers.isAuthorized(((Player) source).getUsername())) {
            source.sendMessage(Component.text("You do not have permission to use this command."));
            return ;
        }
        player.sendMessage(Component.text("Players online: " + server.getAllPlayers().size()));
        player.sendMessage(Component.text("Players: " + server.getAllPlayers()));
    }
}
