package edu.lehigh.cse216.slj222.admin;

import java.io.BufferedReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Documents {
    /**
     * Inherit the database object
     */
    Database db;

    /**
     * Inherit the BufferedReader
     */
    BufferedReader br;

    /**
     * Create documents table
     */
    PreparedStatement createTable;
    /**
     * Show all rows from documents table
     */
    PreparedStatement selectAll;
    /**
     * Select one row from documents table
     */
    PreparedStatement selectOne;
    /**
     * Delete a row from documents table
     */
    PreparedStatement deleteOne;
    /**
     * Drop documents table
     */
    PreparedStatement dropTable;

    /**
     * Options that a user has to interact with messages table
     */
    final String options = "C*1-D?";

    public Documents(Database db, BufferedReader br) {
        this.db = db;
        this.br = br;
        try {
            createTable = db.Connection.prepareStatement(
                    "create table if not exists documents(msgid int primary key, fileid varchar(50), mime varchar(20), foreign key (msgid) references messages on delete cascade)");
            selectAll = db.Connection.prepareStatement("SELECT * FROM documents");
            selectOne = db.Connection.prepareStatement("SELECT * FROM documents where msgid = ?");
            deleteOne = db.Connection.prepareStatement("DELETE FROM documents where msgid = ?");
            dropTable = db.Connection.prepareStatement("DROP TABLE if exists documents");
        } catch (SQLException e) {

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
            System.out.println("  Current Documents Table Contents");
            System.out.printf("%-5s %-50s %-20s\n", "ID", "File ID (from Google Drive)", "MIME Type");
            System.out.println("  -------------------------");
            while (rs.next()) {
                System.out.printf("[%3d] %-50s %-20s\n", rs.getInt(1), rs.getString(2), rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectOne(int msgid) {
        try {
            selectOne.setInt(1, msgid);
            ResultSet rs = selectOne.executeQuery();
            System.out.printf("%-10s %-50s %-20s\n", "Message ID", "File ID (from Google Drive)", "MIME Type");
            while (rs.next()) {
                System.out.printf("%10d %-50s %-20s\n", rs.getInt(1), rs.getString(2), rs.getString(3));
            }
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

    public void dropTable() {
        try {
            dropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Print menu for documents table
     */
    public void menu() {
        System.out.println("Documents Menu");
        System.out.println("  [C] Create documents table");
        System.out.println("  [*] Select all rows");
        System.out.println("  [1] Select one row");
        System.out.println("  [-] Delete a message");
        System.out.println("  [D] Drop documents table");
        System.out.println("  [?] Help - this menu");
    }

    /**
     * User interface for Documents Menu
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
                msgid = App.getInt(br, "Enter the message ID");
                selectOne(msgid);
            case '-':
                msgid = App.getInt(br, "Enter the message ID");
                deleteOne(msgid);
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