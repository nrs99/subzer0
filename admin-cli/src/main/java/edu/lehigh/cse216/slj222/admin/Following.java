package edu.lehigh.cse216.slj222.admin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Following {

    final Database db;

    PreparedStatement createTable;
    PreparedStatement selectAll;
    PreparedStatement selectFollowers;
    PreparedStatement selectFollowing;
    PreparedStatement newFollow;
    PreparedStatement dropTable;

    public Following(Database db) {
        this.db = db;
        try {
            createTable = db.Connection.prepareStatement(
                    "CREATE TABLE if not exists following(usera varchar(30), userb varchar(30), FOREIGN KEY (usera) references users (userid), FOREIGN KEY (userb) references users (userid)) ");
            selectAll = db.Connection.prepareStatement("select * from following");
            selectFollowers = db.Connection.prepareStatement("select usera from following where userb = ?");
            selectFollowing = db.Connection.prepareStatement("select userb from following where usera = ?");
            newFollow = db.Connection.prepareStatement("insert into following values(?, ?)");
            dropTable = db.Connection.prepareStatement("drop table following");
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
            System.out.println("  Current Message Table Contents");
            System.out.printf("%-30s %-30s\n", "User A", "User B");
            System.out.println("  -------------------------");

            while (rs.next()) {
                System.out.printf("%-30s %-30s\n", rs.getString(1), rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    public void newFollow(String usera, String userb) { // TODO
        try {
            newFollow.setString(1, usera);
            newFollow.setString(2, userb);
            ResultSet rs = selectAll.executeQuery();
            System.out.println("Row added");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropTable() {
        try {
            createTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String options() {
        return "C*SG+D?";
    }

    public void help() {
        System.out.println("Following Menu");
        System.out.println("  [C] Create following table");
        System.out.println("  [*] Select all in following table");
        System.out.println("  [S] View all followers of a particular user");
        System.out.println("  [G] View all users a particular user is following");
        System.out.println("  [+] Add a new follow");
        System.out.println("  [D] Drop following table");
        System.out.println("  [?] Help - this menu");
    }

}