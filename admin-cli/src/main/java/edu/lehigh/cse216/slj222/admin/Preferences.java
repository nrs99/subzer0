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
            deleteOne = db.Connection.prepareStatement("DELETE from preferences where user id = ?");
            dropTable = db.Connection.prepareStatement("DROP TABLE IF EXISTS preferences");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        try {
            createTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectAll() {
        try {
            ResultSet rs = selectAll.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectOne(String userid) {
        try {
            ResultSet rs = selectOne.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addOne(String userid, boolean followsMe, boolean commentsOnPost, boolean followingPosts) {
        try {
            addOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateOne(String userid, boolean followsMe, boolean commentsOnPost, boolean followingPosts) {
        try {
            updateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteOne(String userid) {
        try {
            deleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropTable() {
        try {
            dropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Print menu for messages table
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
     * User interface for Messages Menu
     */
    public void execute() {
        char selection = App.prompt(br, options);
        int msgid;
        switch (selection) {
            case 'C':
                createTable();
                break;
            case '*':
                selectAll();
                break;
            case '1':
                break;
            case '+':
                break;
            case '-':
                break;
            case '~':
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