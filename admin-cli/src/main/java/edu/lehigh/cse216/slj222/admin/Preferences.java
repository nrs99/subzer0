package edu.lehigh.cse216.slj222.admin;

import java.io.BufferedReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Preferences {

    /**
     * Inherit the database object
     */
    Database db;

    /**
     * Inherit the BufferedReader
     */
    BufferedReader br;

    /**
     * Create preferences table
     */
    PreparedStatement createTable;
    /**
     * Show all entries in preferences table
     */
    PreparedStatement selectAll;
    /**
     * Show one entry in preferences table
     */
    PreparedStatement selectOne;
    /**
     * Add a user to preferences table, set their preferences
     */
    PreparedStatement addOne;
    /**
     * Update a user's preferences
     */
    PreparedStatement updateOne;
    /**
     * Delete a user's preferences
     */
    PreparedStatement deleteOne;
    /**
     * Drop preferences table
     */
    PreparedStatement dropTable;
    /**
     * Options that a user has to interact with preferences table
     */
    final String options = "C*1+-~D?";

    /**
     * Create a new Preferences object with given Database and BufferedReader
     */
    public Preferences(Database db, BufferedReader br) {
        this.db = db;
        this.br = br;

        try {
            createTable = db.Connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS preferences(userid varchar(30) primary key, followsMe boolean, commentsOnPost boolean, followingPosts boolean, FOREIGN KEY (userid) references users (userid) ON DELETE CASCADE)");
            selectAll = db.Connection.prepareStatement("SELECT * from preferences");
            selectOne = db.Connection.prepareStatement("SELECT * from preferences where userid = ?");
            addOne = db.Connection.prepareStatement("INSERT INTO preferences values (?, ?, ?, ?)");
            updateOne = db.Connection.prepareStatement(
                    "UPDATE preferences SET followsMe = ?, commentsOnPost = ?, followingPosts = ? WHERE userid = ?");
            deleteOne = db.Connection.prepareStatement("DELETE from preferences where userid = ?");
            dropTable = db.Connection.prepareStatement("DROP TABLE IF EXISTS preferences");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create preferences table
     */
    public void createTable() {
        try {
            createTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Select all rows from preferences table
     */
    public void selectAll() {
        try {
            ResultSet rs = selectAll.executeQuery();
            System.out.println("  Current Preferences Table Contents");
            System.out.printf("%-30s %-15s %-17s %-15s\n", "User ID", "Follows Me", "Comments on Post",
                    "Following Posts");
            System.out.println("  -------------------------");
            while (rs.next()) {
                System.out.printf("%-30s %-15b %-17b %-15b\n", rs.getString(1), rs.getBoolean(2), rs.getBoolean(3),
                        rs.getBoolean(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Select one row from Preferences table
     * 
     * @param userid User whose preferences you want to see
     */
    public void selectOne(String userid) {
        try {
            selectOne.setString(1, userid);
            ResultSet rs = selectOne.executeQuery();
            System.out.printf("%-30s %-15s %-17s %-15s\n", "User ID", "Follows Me", "Comments on Post",
                    "Following Posts");
            System.out.println("  -------------------------");
            while (rs.next()) {
                System.out.printf("%-30s %-15b %-17b %-15b\n", rs.getString(1), rs.getBoolean(2), rs.getBoolean(3),
                        rs.getBoolean(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a row with a given userID and preferences
     */
    public void addOne(String userid, boolean followsMe, boolean commentsOnPost, boolean followingPosts) {
        try {
            addOne.setString(1, userid);
            addOne.setBoolean(2, followsMe);
            addOne.setBoolean(3, commentsOnPost);
            addOne.setBoolean(4, followingPosts);
            addOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update a row with a given userID and new preferences
     */
    public void updateOne(String userid, boolean followsMe, boolean commentsOnPost, boolean followingPosts) {
        try {
            updateOne.setString(4, userid);
            updateOne.setBoolean(1, followsMe);
            updateOne.setBoolean(2, commentsOnPost);
            updateOne.setBoolean(3, followingPosts);
            updateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete a user from preferences table
     */
    public void deleteOne(String userid) {
        try {
            deleteOne.setString(1, userid);
            deleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Drop preferences table
     */
    public void dropTable() {
        try {
            dropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Print menu for Preferences table
     */
    public void menu() {
        System.out.println("Preferences Menu");
        System.out.println("  [C] Create preferences table");
        System.out.println("  [*] Select all rows");
        System.out.println("  [1] Select a specific row");
        System.out.println("  [+] Add a preferences row");
        System.out.println("  [-] Delete a preferences row");
        System.out.println("  [~] Update a preferences row");
        System.out.println("  [D] Drop preferences table");
        System.out.println("  [?] Help - this menu");
    }

    /**
     * User interface for Preferences Menu
     */
    public void execute() {
        char selection = App.prompt(br, options);
        String userid;
        String response = "";
        boolean valid = false;
        boolean followsMe;
        boolean commentsOnPost;
        boolean followingPosts;
        switch (selection) {
            case 'C':
                createTable();
                break;
            case '*':
                selectAll();
                break;
            case '1':
                userid = App.getString(br, "Enter the userID");
                selectOne(userid);
                break;
            case '+':
                userid = App.getString(br, "Enter the userID");
                while (!valid) {
                    response = App.getString(br, "Notifications for someone following them? (y/n)").toUpperCase();
                    if (response.equals("Y") || response.equals("N")) {
                        valid = true;
                    }
                }
                followsMe = response.equals("Y");
                valid = false;
                while (!valid) {
                    response = App.getString(br, "Notifications for someone commenting on their post? (y/n)")
                            .toUpperCase();
                    if (response.equals("Y") || response.equals("N")) {
                        valid = true;
                    }
                }
                commentsOnPost = response.equals("Y");
                valid = false;
                while (!valid) {
                    response = App.getString(br, "Notifications for someone they follow posting? (y/n)").toUpperCase();
                    if (response.equals("Y") || response.equals("N")) {
                        valid = true;
                    }
                }
                followingPosts = response.equals("Y");
                addOne(userid, followsMe, commentsOnPost, followingPosts);
                break;
            case '-':
                userid = App.getString(br, "Enter the userID");
                deleteOne(userid);
                break;
            case '~':
                userid = App.getString(br, "Enter the userID");
                while (!valid) {
                    response = App.getString(br, "Notifications for someone following them? (y/n)").toUpperCase();
                    if (response.equals("Y") || response.equals("N")) {
                        valid = true;
                    }
                }
                followsMe = response.equals("Y");
                valid = false;
                while (!valid) {
                    response = App.getString(br, "Notifications for someone commenting on their post? (y/n)")
                            .toUpperCase();
                    if (response.equals("Y") || response.equals("N")) {
                        valid = true;
                    }
                }
                commentsOnPost = response.equals("Y");
                valid = false;
                while (!valid) {
                    response = App.getString(br, "Notifications for someone they follow posting? (y/n)").toUpperCase();
                    if (response.equals("Y") || response.equals("N")) {
                        valid = true;
                    }
                }
                followingPosts = response.equals("Y");
                updateOne(userid, followsMe, commentsOnPost, followingPosts);
                break;
            case 'D':
                dropTable();
                break;
            case '?':
                menu();
                execute();
                break;
            default:
                System.out.println("Invalid");
        }
    }

}