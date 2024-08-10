package me.diamondy.velasus.commands;

import java.util.HashSet;
import java.util.Set;

public class AuthorizedUsers {
    private static final Set<String> authorizedUsers = new HashSet<>();

    static {
        // Add authorized usernames here
        authorizedUsers.add("Endrig");
        authorizedUsers.add("Diamondy");
    }

    public static boolean isAuthorized(String username) {
        return authorizedUsers.contains(username);
    }
}