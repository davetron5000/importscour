package test.net.sourceforge.importscrubber;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sourceforge.importscrubber.*;
import net.sourceforge.importscrubber.format.NoBreaks;
import java.util.*;

public class ImportStatementTest extends TestCase
{

   private static final String BAR_CLASS = "com.foo.Bar";
   private static final String BAZ_CLASS = "com.foo.Baz";
   private static final String BUZ_CLASS = "com.foo.biz.Buz";
   private static final String STRING_CLASS = "java.lang.String";
   private static final String SQL_DATE_CLASS = "javax.sql.Date";


   public ImportStatementTest(String aName)
   {
      super(aName);
   }

   public static Test suite()
   {
      return new TestSuite(ImportStatementTest.class);
   }

   public void testBasic()
   {
      ImportStatement stmt = new ImportStatement(BAR_CLASS);
      assertEquals(BAR_CLASS, stmt.getFullyQualifiedClassName());
   }

   public void testStdLib()
   {
      ImportStatement stmt = new ImportStatement(BAR_CLASS);
      assertTrue(!stmt.isInStdJavaLibrary());
      
      stmt = new ImportStatement(STRING_CLASS);
      assertTrue(stmt.isInStdJavaLibrary());
   }

   public void testStdExtLib()
   {
      ImportStatement stmt = new ImportStatement(BAR_CLASS);
      assertTrue(!stmt.isInStdJavaExtensionLibrary());
      
      stmt = new ImportStatement(SQL_DATE_CLASS);
      assertTrue(stmt.isInStdJavaExtensionLibrary());
   }

   public void testCompare()
   {
      ImportStatement stmtBar = new ImportStatement(BAR_CLASS);
      ImportStatement stmtBaz = new ImportStatement(BUZ_CLASS);
      assertEquals(-1, stmtBar.compareTo(stmtBaz));
      assertEquals(1, stmtBaz.compareTo(stmtBar));
      assertEquals(0, stmtBaz.compareTo(stmtBaz));
   }

   public void testIsInDefaultPackage()
   {
      ImportStatement stmtBar = new ImportStatement(BAR_CLASS);
      assertTrue(!stmtBar.isInDefaultPackage());

      ImportStatement defaultStmt = new ImportStatement("Worble");
      assertTrue(defaultStmt.isInDefaultPackage());
   }

   public void testGetPackage()
   {
      ImportStatement stmtBar = new ImportStatement(BAR_CLASS);
      assertEquals("com.foo", stmtBar.getPackage());
   }

   public void testCompareBad()
   {
      try
      {
         ImportStatement stmtBar = new ImportStatement(BAR_CLASS);
         stmtBar.compareTo("hello");
         fail("Should have gotten a ClassCastException");
      } catch (ClassCastException ex)
      {
         // cool
      }
   }
   public void testCompareNull()
   {
      try
      {
         ImportStatement stmtBar = new ImportStatement(BAR_CLASS);
         stmtBar.compareTo(null);
         fail("Should have gotten an IllegalArgumentException");
      } catch (IllegalArgumentException ex)
      {
         // cool
      }
   }

   public void testHashcode()
   {
      ImportStatement stmtBar = new ImportStatement(BAR_CLASS);
      ImportStatement stmtBaz = new ImportStatement(BUZ_CLASS);
      assertTrue(stmtBar.hashCode() != stmtBaz.hashCode());

      HashMap map = new HashMap();
      map.put(stmtBar, "hello");
      assertEquals("hello", map.get(stmtBar));
   }

   public void testEquals()
   {
      ImportStatement stmtBar = new ImportStatement(BAR_CLASS);
      ImportStatement stmtBaz = new ImportStatement(BUZ_CLASS);
      assertTrue(!stmtBar.equals(stmtBaz));
      assertTrue(stmtBar.equals(stmtBar));
   }

}
