package edu.lehigh.cse216.slj222.admin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

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
    public void test_Insert_Comment()
    {
       // test insert comment
        int count = 7;
        Database db1 = Database.getDatabase("postgres://wbobgqxniofljr:0feb75c4741735e14f18ab72f07b94562d59741b2db3aae7ffbddbf2d4dd3e43@ec2-52-203-160-194.compute-1.amazonaws.com:5432/d7uf5dueelngct");
        int res = db1.insertRowComments("1" ,"mama");
        Database.RowData rs = db1.selectOneComments("mama");
        

        System.out.println(res + " row added");

        System.out.println("value of cComment:"+rs.cComment);

        System.out.println("-----------------------------------------------------");
        // Database.RowData res = db.selectOneComments(1);
        assertEquals("mama", rs.cComment);
    }

        public void test_insert_Like(){
        Database db = Database.getDatabase("postgres://wbobgqxniofljr:0feb75c4741735e14f18ab72f07b94562d59741b2db3aae7ffbddbf2d4dd3e43@ec2-52-203-160-194.compute-1.amazonaws.com:5432/d7uf5dueelngct");
        int c = db.insertRowLikes("2", 1);
        Database.RowData res2 = db.selectOneLikes("2");

        assertEquals("2", res2.lUserid);
        assertEquals(1, res2.lLikes);

        }
        //test update
       //count ++;

    }
