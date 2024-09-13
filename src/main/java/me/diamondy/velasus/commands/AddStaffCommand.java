package me.diamondy.velasus.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AddStaffCommand implements SimpleCommand {
    private final ProxyServer server;

    public AddStaffCommand(ProxyServer server) {
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
            source.sendMessage(Component.text("Usage: /addstaff <player>"));
            String newStaffMember = args[0];
            StaffMembers.addStaffMember(newStaffMember);
            source.sendMessage(Component.text("Player " + newStaffMember + " has been added as a staff member."));
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length == 1) {
            return server.getAllPlayers().stream()
                    .map(Player::getUsername)
                    .filter(username -> !StaffMembers.isStaffMember(username))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
