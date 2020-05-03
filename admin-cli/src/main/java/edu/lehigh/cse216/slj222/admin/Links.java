package edu.lehigh.cse216.slj222.admin;

import java.io.BufferedReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Links {
    /**
     * Inherit the database object
     */
    Database db;

    /**
     * Inherit the BufferedReader
     */
    BufferedReader br;
    /**
     * Create links table
     */
    PreparedStatement createTable;
    /**
     * Select all rows from links table
     */
    PreparedStatement selectAll;
    /**
     * Select one row from links table
     */
    PreparedStatement selectOne;
    /**
     * Add one link to table
     */
    PreparedStatement addOne;
    /**
     * Update one link
     */
    PreparedStatement updateOne;
    /**
     * Delete link from table
     */
    PreparedStatement deleteOne;
    /**
     * Drop links table
     */
    PreparedStatement dropTable;
    /**
     * Options that a user has to interact with links table
     */
    final String options = "C*1+-~D?";

    public Links(Database db, BufferedReader br) {
        this.db = db;
        this.br = br;
        try {
            createTable = db.Connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS links(msgid int primary key, url varchar(100), foreign key(msgid) references users ON DELETE CASCADE)");
            selectAll = db.Connection.prepareStatement("SELECT * from links");
            selectOne = db.Connection.prepareStatement("SELECT * from links WHERE msgid = ?");
            addOne = db.Connection.prepareStatement("INSERT INTO links VALUES(?, ?");
            updateOne = db.Connection.prepareStatement("UPDATE links SET url = ? WHERE msgid = ?");
            deleteOne = db.Connection.prepareStatement("DELETE FROM links WHERE msgid = ?");
            dropTable = db.Connection.prepareStatement("DROP TABLE IF EXISTS links");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Print menu for messages table
     */
    public void menu() {
        System.out.println("Links Menu");
        System.out.println("  [C] Create links table");
        System.out.println("  [*] Select all rows");
        System.out.println("  [1] Select a specific row");
        System.out.println("  [+] Add a link");
        System.out.println("  [-] Delete a link");
        System.out.println("  [~] Update a link row");
        System.out.println("  [D] Drop links table");
        System.out.println("  [?] Help - this menu");
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
            System.out.println("  Current Link Table Contents");
            System.out.printf("%-5s %-10s\n", "ID", "URL");
            System.out.println("  -------------------------");
            while (rs.next()) {
                System.out.printf("[%3d] %s\n", rs.getInt(1), rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectOne(int msgid) {
        try {
            selectOne.setInt(1, msgid);
            ResultSet rs = selectOne.executeQuery();
            System.out.printf("%-5s %-10s\n", "ID", "URL");
            while (rs.next()) {
                System.out.printf("[%3d] %s\n", rs.getInt(1), rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addOne(int msgid, String url) {
        try {
            addOne.setInt(1, msgid);
            addOne.setString(2, url);
            addOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteOne(int msgid) {
        try {
            deleteOne.setInt(1, msgid);
            deleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateOne(int msgid, String url) {
        try {
            updateOne.setString(1, url);
            updateOne.setInt(2, msgid);
            updateOne.executeUpdate();
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
     * User interface for Messages Menu
     */
    public void execute() {
        char selection = App.prompt(br, options);
        int msgid;
        String url;
        switch (selection) {
            case 'C':
                createTable();
                break;
            case '*':
                selectAll();
                break;
            case '1':
                msgid = App.getInt(br, "Enter the message ID");
                selectOne(msgid);
                break;
            case '+':
                msgid = App.getInt(br, "Enter the message ID");
                url = App.getString(br, "Enter the URL");
                addOne(msgid, url);
                break;
            case '-':
                msgid = App.getInt(br, "Enter the message ID");
                deleteOne(msgid);
                break;
            case '~':
                msgid = App.getInt(br, "Enter the message ID");
                url = App.getString(br, "Enter the URL");
                updateOne(msgid, url);
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