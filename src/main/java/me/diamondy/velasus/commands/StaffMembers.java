package me.diamondy.velasus.commands;

import me.diamondy.velasus.utils.Databasemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class StaffMembers {
    private static final Set<String> staffMembers = new HashSet<>();

    static {
        loadStaffMembers();
    }

    public static boolean isStaffMember(String username) {
        return staffMembers.contains(username);
    }

    public static void addStaffMember(String username) {
        staffMembers.add(username);
        saveStaffMember(username);
    }

    public static void removeStaffMember(String username) {
        staffMembers.remove(username);
        deleteStaffMember(username);
    }

    public static Set<String> getStaffMembers() {
        return new HashSet<>(staffMembers);
    }

    private static void saveStaffMember(String username) {
        try (Connection connection = Databasemanager.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO staff_members (username) VALUES (?)")) {
            statement.setString(1, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteStaffMember(String username) {
        try (Connection connection = Databasemanager.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM staff_members WHERE username = ?")) {
            statement.setString(1, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void loadStaffMembers() {
        try (Connection connection = Databasemanager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT username FROM staff_members");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                staffMembers.add(resultSet.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}