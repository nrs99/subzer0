package edu.lehigh.cse216.slj222.admin;

import java.io.BufferedReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Users {

    /**
     * Inherit the database object
     */
    Database db;

    /**
     * Inherit the BufferedReader
     */
    BufferedReader br;

    /**
     * Create users table
     */
    PreparedStatement createTable;
    /**
     * Select all rows from users table
     */
    PreparedStatement selectAll;
    /**
     * Select one row from users table
     */
    PreparedStatement selectOne;
    /**
     * Delete a row from users table
     */
    PreparedStatement deleteOne;
    /**
     * Drop users table
     */
    PreparedStatement dropTable;

    /**
     * Options that a user has to interact with messages table
     */
    final String options = "C*1-D?";

    public Users(Database db, BufferedReader br) {
        this.db = db;
        this.br = br;
        try {
        createTable = db.Connection.prepareStatement(
                "CREATE TABLE users(userid varchar(30) PRIMARY KEY, displayname varchar(30), photourl varchar(200), email varchar(50))");
        selectAll = db.Connection.prepareStatement("SELECT * from users");
        selectOne = db.Connection.prepareStatement("SELECT * from users WHERE userid = ?");
        deleteOne = db.Connection.prepareStatement("DELETE FROM users WHERE userid = ?");
        dropTable = db.Connection.prepareStatement("DROP TABLE if exists users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create users table
     */
    public void createTable() {
        try {
            createTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Select all row from users
     */
    public void selectAll() {
        try {
            ResultSet rs = selectAll.executeQuery();
            System.out.println("  Current Users Table Contents (minus Photo URL)");
            System.out.printf("%-30s %-30s %s\n", "User ID", "Display Name", "Email");
            System.out.println("  -------------------------");
            while (rs.next()) {
                System.out.printf("%-30s %-30s %s\n", rs.getString(1), rs.getString(2), rs.getString(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Select one row from users
     */
    public void selectOne(String userid) {
        try {
            selectOne.setString(1, userid);
            ResultSet rs = selectOne.executeQuery();
            System.out.println("  Current Users Table Contents (minus Photo URL)");
            System.out.printf("%-30s %-30s %s\n", "User ID", "Display Name", "Email");
            System.out.println("  -------------------------");
            while (rs.next()) {
                System.out.printf("%-30s %-30s %s\n", rs.getString(1), rs.getString(2), rs.getString(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete one row from users
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
     * Drop users table
     */
    public void dropTable() {
        try {
            dropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Print menu for users table
     */
    public void menu() {
        System.out.println("Users Menu");
        System.out.println("  [C] Create messages table");
        System.out.println("  [*] Select all rows");
        System.out.println("  [1] Select a specific row");
        System.out.println("  [-] Delete a message");
        System.out.println("  [D] Drop messages table");
        System.out.println("  [?] Help - this menu");
    }

    /**
     * User interface for Users Menu
     */
    public void execute() {
        char selection = App.prompt(br, options);
        String userID;
        switch (selection) {
            case 'C':
                createTable();
                break;
            case '*':
                selectAll();
                break;
            case '1':
                userID = App.getString(br, "Enter the userID");
                selectOne(userID);
                break;
            case '-':
                userID = App.getString(br, "Enter the userID");
                deleteOne(userID);
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