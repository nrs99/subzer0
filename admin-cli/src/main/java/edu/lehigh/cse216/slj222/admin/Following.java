package edu.lehigh.cse216.slj222.admin;

import java.io.BufferedReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Following {

    /**
     * Inherit the database object
     */
    Database db;

    /**
     * Inherit the BufferedReader
     */
    BufferedReader br;

    /**
     * Create following table
     */
    PreparedStatement createTable;
    /**
     * Select all info from following table
     */
    PreparedStatement selectAll;
    /**
     * View all followers of a user
     */
    PreparedStatement selectFollowers;
    /**
     * View all users a user is following
     */
    PreparedStatement selectFollowing;
    /**
     * Add a new follow relationship
     */
    PreparedStatement newFollow;
    /**
     * Delete a follow relationship
     */
    PreparedStatement deleteFollow;
    /**
     * Drop the following table
     */
    PreparedStatement dropTable;

    /**
     * Options that a user has to interact with following table
     */
    final String options = "C*SG+-D?";

    /**
     * Create a Following object and set up all PreparedStatements
     */
    public Following(Database db, BufferedReader br) {
        this.db = db;
        this.br = br;
        try {
            createTable = db.Connection.prepareStatement(
                    "CREATE TABLE if not exists following(usera varchar(30), userb varchar(30), FOREIGN KEY (usera) references users (userid) ON DELETE CASCADE, FOREIGN KEY (userb) references users (userid) ON DELETE CASCADE, PRIMARY KEY (usera, userb))");
            selectAll = db.Connection.prepareStatement("select * from following");
            selectFollowers = db.Connection.prepareStatement("select usera from following where userb = ?");
            selectFollowing = db.Connection.prepareStatement("select userb from following where usera = ?");
            newFollow = db.Connection.prepareStatement("insert into following values(?, ?)");
            deleteFollow = db.Connection.prepareStatement("delete from following where usera = ? and userb = ?");
            dropTable = db.Connection.prepareStatement("DROP TABLE IF EXISTS following");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the table
     */
    public void createTable() {
        try {
            createTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Select all rows of the table
     */
    public void selectAll() {
        try {
            ResultSet rs = selectAll.executeQuery();
            System.out.println("  Current Following Table Contents");
            System.out.printf("%-30s %-30s\n", "User A", "User B");
            System.out.println("  -------------------------");

            while (rs.next()) {
                System.out.printf("%-30s %-30s\n", rs.getString(1), rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Select all followers of a particular user
     */
    public void selectFollowers(String user) {
        try {
            selectFollowers.setString(1, user);
            ResultSet rs = selectFollowers.executeQuery();
            System.out.printf("  Current Followers of %s\n", user);
            System.out.println("  -------------------------");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Select all the users a certain user is following
     */
    public void selectFollowing(String user) {
        try {
            selectFollowing.setString(1, user);
            ResultSet rs = selectFollowing.executeQuery();
            System.out.printf("  Current Users %s is Following\n", user);
            System.out.println("  -------------------------");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a new row to the table usera follows userb
     */
    public void newFollow(String usera, String userb) {
        try {
            newFollow.setString(1, usera);
            newFollow.setString(2, userb);
            if (newFollow.executeUpdate() == 0) {
                System.out.println("Invalid new row");
            }
            System.out.println("Row added");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFollow(String usera, String userb) {
        try {
            deleteFollow.setString(1, usera);
            deleteFollow.setString(2, userb);
            if (deleteFollow.executeUpdate() == 0) {
                System.out.println("Invalid deletion");
            }
            System.out.printf("%s is no longer following %s\n", usera, userb);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get rid of the following table
     */
    public void dropTable() {
        try {
            dropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Print menu for following table
     */
    public void menu() {
        System.out.println("Following Menu");
        System.out.println("  [C] Create following table");
        System.out.println("  [*] Select all in following table");
        System.out.println("  [S] View all followers of a particular user");
        System.out.println("  [G] View all users a particular user is following");
        System.out.println("  [+] Add a new follow");
        System.out.println("  [-] Delete a follow");
        System.out.println("  [D] Drop following table");
        System.out.println("  [?] Help - this menu");
    }

    public void execute() {
        char selection = App.prompt(br, options);
        switch (selection) {
            case 'C':
                createTable();
                break;
            case '*':
                selectAll();
                break;
            case 'S':
                String user = App.getString(br, "Enter the user whose followers you want to see");
                selectFollowers(user);
                break;
            case 'G':
                String user2 = App.getString(br, "Enter the user whose followers you want to see");
                selectFollowing(user2);
                break;
            case '+':
                String userA = App.getString(br, "Enter the user who will be following someone");
                String userB = App.getString(br, "Enter the user ID of who they will be following");
                newFollow(userA, userB);
                break;
            case '-':
                String usera = App.getString(br, "Enter the user who will be unfollowing someone");
                String userb = App.getString(br, "Enter the user ID of who they will no longer be following");
                deleteFollow(usera, userb);
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