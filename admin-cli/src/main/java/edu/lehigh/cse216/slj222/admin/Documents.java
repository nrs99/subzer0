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
    final String options = "C*-D?";

    public Documents(Database db, BufferedReader br) {
        this.db = db;
        this.br = br;
        try {
            createTable = db.Connection.prepareStatement(
                    "create table if not exists documents(msgid int primary key, fileid varchar(50), mime varchar(20), foreign key (msgid) references messages on delete cascade)");
            selectAll = db.Connection.prepareStatement("SELECT * FROM documents");
            deleteOne = db.Connection.prepareStatement("DELETE FROM documents where msgid = ?");
            dropTable = db.Connection.prepareStatement("DROP TABLE if exists documents");
        } catch (SQLException e) {

        }
    }

    /**
     * Print menu for documents table
     */
    public void menu() {
        System.out.println("Documents Menu");
        System.out.println("  [C] Create documents table");
        System.out.println("  [*] Select all rows");
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