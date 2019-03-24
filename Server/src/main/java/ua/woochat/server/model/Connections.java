package ua.woochat.server.model;

import ua.woochat.app.Connection;
import ua.woochat.app.Group;
import ua.woochat.app.User;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/*
 This class works with all connections on the server.
*/
public class Connections {
    private static Set<Connection> connections = new LinkedHashSet<>();
    private static Set<Group> groupsList = new LinkedHashSet<>();

    public static void addConnection(Connection connection) {
        connections.add(connection);
    }

    public static void removeConnection(Connection connection) {
        connections.remove(connection);
    }

    public static int getCountConnections() {
        return connections.size();
    }

    /**
     * Finds the current user object by login from all connections.
     *
     * @param login of the user.
     * @return user object or null if user was not found.
     */
    public static User getUserByLogin(String login) {
        for (Connection connection : connections) {
            if (login.equals(connection.getUser().getLogin())) {
                return connection.getUser();
            }
        }
        return null;
    }

    /**
     * Finds Connection object from all connections.
     * @param login user's login in text format.
     * @return Connection object searched from all connections or null if
     * user with current login is not presented in a chat.
     */
    public static Connection getConnectionByLogin(String login) {
        for (Connection entry : connections) {
            if (login.equals(entry.getUser().getLogin())) {
                return entry;
            }
        }
        return null;
    }

    public static Set<Connection> getConnections() {
        return connections;
    }

    /**
     * Creates list of users logins.
     * @return ArrayList of strings of users logins
     */
    public static ArrayList<String> getOnlineUsersLogins() {
        ArrayList<String> result = new ArrayList<>();
        for (Connection entry : connections) {
            result.add(entry.getUser().getLogin());
        }
        return result;
    }

    public static Set<Group> getGroupsList() {
        return groupsList;
    }

    /**
     * Gets set of strings users logins from a current group.
     * @param groupID current group.
     * @return set of users logins.
     */
    public static Set<String> getUsersListFromGroup(String groupID) {
        Set<String> result = new LinkedHashSet<>();
        for (Group entry : getGroupsList()) {
            if (groupID.equals(entry.getGroupID())) {
                result.addAll(entry.getUsersList());
            }
        }
        return result;
    }
}