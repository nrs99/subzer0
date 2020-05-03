package edu.lehigh.cse216.slj222.admin;

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
        selectAll = db.Connection.prepareStatement("select * from following");
    }

    public static void followingMenu() {

    }

}