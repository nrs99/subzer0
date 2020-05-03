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
                System.out.printf("%-30s %-30s\n", rs.getString(0), rs.getString(1));
            }
        } catch (SQLException e) {

        }
    }

}