package edu.lehigh.cse216.slj222.backend;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.Map;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    /**
     * Test that a message can be inserted correctly
     * Hardcoding in database URL
     */
    public void testInsertMessage() {
        Database db = Database.getDatabase("postgres://wbobgqxniofljr:0feb75c4741735e14f18ab72f07b94562d59741b2db3aae7ffbddbf2d4dd3e43@ec2-52-203-160-194.compute-1.amazonaws.com:5432/d7uf5dueelngct");
        int insertedRow = db.insertRow("Hello", 1865);
        Message testMessage = db.selectOne(insertedRow);
        assertEquals(testMessage.message, "Hello");
        assertEquals(testMessage.userId, 1865);
        assertEquals(testMessage.likes, 0);
        assertEquals(testMessage.dislikes, 0);
        try {
            PreparedStatement deleteTest = db.getConnection().prepareStatement("DELETE from messages where msgid=?");
            deleteTest.setInt(1, insertedRow);
            deleteTest.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            db.disconnect();
        }
    }

    /**
     * Test that liking and disliking work correctly
     * Hardcoding in database URL
     */
    public void testLikeDislikeMessage() {
        Database db = Database.getDatabase("postgres://wbobgqxniofljr:0feb75c4741735e14f18ab72f07b94562d59741b2db3aae7ffbddbf2d4dd3e43@ec2-52-203-160-194.compute-1.amazonaws.com:5432/d7uf5dueelngct");
        int insertedRow = db.insertRow("Hello", 1865);
        Message testMessage = db.selectOne(insertedRow);
        assertEquals(testMessage.message, "Hello");
        assertEquals(testMessage.userId, 1865);
        assertEquals(testMessage.likes, 0);
        assertEquals(testMessage.dislikes, 0);
        db.likeOne(insertedRow);
        db.likeOne(insertedRow);
        db.likeOne(insertedRow);
        db.dislikeOne(insertedRow);
        db.dislikeOne(insertedRow);
        assertEquals(testMessage.likes, 3);
        assertEquals(testMessage.dislikes, 2);
        try {
            PreparedStatement deleteTest = db.getConnection().prepareStatement("DELETE from messages where msgid=?");
            deleteTest.setInt(1, insertedRow);
            deleteTest.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            db.disconnect();
        }
    }

}
