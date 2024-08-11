package me.diamondy.velasus.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;

import java.util.Map;

public class ListPlayerOnServerCommand implements SimpleCommand {
    public final ProxyServer server;

    public ListPlayerOnServerCommand(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();

        if (!(source instanceof Player) || !AuthorizedUsers.isAuthorized(((Player) source).getUsername())) {
            source.sendMessage(Component.text("You do not have permission to use this command."));
            return;
        }

        Map<String, Integer> serverPlayerCount = server.getAllPlayers().stream()
                .collect(java.util.stream.Collectors.groupingBy(player -> player.getCurrentServer().get().getServerInfo().getName(), java.util.stream.Collectors.reducing(0, e -> 1, Integer::sum)));

        String sortedServers = serverPlayerCount.entrySet().stream()
                .sorted(java.util.Map.Entry.comparingByValue(java.util.Comparator.reverseOrder()))
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(java.util.stream.Collectors.joining(", "));

        source.sendMessage(Component.text("Players online: " + sortedServers));



    }
}
