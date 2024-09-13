package me.diamondy.velasus.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveStaffCommand implements SimpleCommand {
    private final ProxyServer server;

    public RemoveStaffCommand(ProxyServer server) {
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
            source.sendMessage(Component.text("Usage: /removestaff <player>"));
            return;
        }

        String staffMember = args[0];
        if (StaffMembers.isStaffMember(staffMember)) {
            StaffMembers.removeStaffMember(staffMember);
            source.sendMessage(Component.text("Player " + staffMember + " has been removed as a staff member."));
        } else {
            source.sendMessage(Component.text("Player " + staffMember + " is not a staff member."));
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length == 1) {
            return StaffMembers.getStaffMembers().stream()
                    .filter(username -> username.startsWith(args[0]))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}