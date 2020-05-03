package edu.lehigh.cse216.slj222.admin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    public void testAddUpdatePreferences() { // This test makes sure that a user can update their preferences with the Preferences.updateOne() method
        Database db = Database.getDatabase(
                "postgres://wbobgqxniofljr:0feb75c4741735e14f18ab72f07b94562d59741b2db3aae7ffbddbf2d4dd3e43@ec2-52-203-160-194.compute-1.amazonaws.com:5432/d7uf5dueelngct");
        Preferences prefs = new Preferences(db, null);
        String userid = "103530917496859003701"; // Our subzer0.cse216 email  
        PreparedStatement selectUser; 
        boolean one = false;
        boolean two = false;
        boolean three = false;

        try {
            selectUser = db.Connection.prepareStatement("SELECT * FROM PREFERENCES WHERE userid = ?");
            selectUser.setString(1, userid);
            ResultSet rs = selectUser.executeQuery();
            if (!rs.next()) {
                prefs.addOne(userid, false, false, false);
            }
            prefs.updateOne(userid, false, true, true);
            rs = selectUser.executeQuery();
            rs.next();
            one = rs.getBoolean(2);
            two = rs.getBoolean(3);
            three = rs.getBoolean(4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals(one, false);
        assertEquals(two, true);
        assertEquals(three, true);
    }

    /**
     * These tests no longer work with the refactoring from Phase 4
     */
    /*
     * public void test_Insert_Comment() { // test insert comment int count = 7;
     * Database db1 = Database.getDatabase(
     * "postgres://wbobgqxniofljr:0feb75c4741735e14f18ab72f07b94562d59741b2db3aae7ffbddbf2d4dd3e43@ec2-52-203-160-194.compute-1.amazonaws.com:5432/d7uf5dueelngct"
     * ); int res = db1.insertRowComments("1", "mama"); Database.RowData rs =
     * db1.selectOneComments("mama");
     * 
     * System.out.println(res + " row added");
     * 
     * System.out.println("value of cComment:" + rs.cComment);
     * 
     * System.out.println("-----------------------------------------------------");
     * // Database.RowData res = db.selectOneComments(1); assertEquals("mama",
     * rs.cComment); }
     */

    /*
     * public void test_insert_Like() { Database db = Database.getDatabase(
     * "postgres://wbobgqxniofljr:0feb75c4741735e14f18ab72f07b94562d59741b2db3aae7ffbddbf2d4dd3e43@ec2-52-203-160-194.compute-1.amazonaws.com:5432/d7uf5dueelngct"
     * ); // int c = db.insertRow(count, 1, "mama"); Database.RowData res =
     * db.selectOne(77); assertEquals("override check", res.mMessage);
     * assertEquals(0, res.mUserid); assertEquals(0, res.mLikes); assertEquals(6,
     * res.mDislikes); assertEquals("2020-02-23 21:17:36.039", res.mDatecreated);
     * 
     * assertEquals("2", res2.lUserid); assertEquals(1, res2.lLikes);
     * 
     * }
     */

}
