package test.net.sourceforge.importscrubber;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.*;
import net.sourceforge.importscrubber.*;
import static org.testng.AssertJUnit.*;

public class ImportStatementTest {
    private static final String BAR_CLASS = "com.foo.Bar";
    private static final String BAZ_CLASS = "com.foo.Baz";
    private static final String BUZ_CLASS = "com.foo.biz.Buz";
    private static final String STRING_CLASS = "java.lang.String";
    private static final String SQL_DATE_CLASS = "javax.sql.Date";
    @Test
    public void testBasic() {
        ImportStatement stmt = new ImportStatement(BAR_CLASS);
        assertEquals(BAR_CLASS, stmt.getFullyQualifiedClassName());
    }
    @Test
    public void testStdLib() {
        ImportStatement stmt = new ImportStatement(BAR_CLASS);
        assertTrue(!stmt.isInStdJavaLibrary());
        stmt = new ImportStatement(STRING_CLASS);
        assertTrue(stmt.isInStdJavaLibrary());
    }
    @Test
    public void testStdExtLib() {
        ImportStatement stmt = new ImportStatement(BAR_CLASS);
        assertTrue(!stmt.isInStdJavaExtensionLibrary());
        stmt = new ImportStatement(SQL_DATE_CLASS);
        assertTrue(stmt.isInStdJavaExtensionLibrary());
    }
    @Test
    public void testCompare() {
        ImportStatement stmtBar = new ImportStatement(BAR_CLASS);
        ImportStatement stmtBaz = new ImportStatement(BUZ_CLASS);
        assertEquals(-1, stmtBar.compareTo(stmtBaz));
        assertEquals(1, stmtBaz.compareTo(stmtBar));
        assertEquals(0, stmtBaz.compareTo(stmtBaz));
    }
    @Test
    public void testIsInDefaultPackage() {
        ImportStatement stmtBar = new ImportStatement(BAR_CLASS);
		assertNotNull(stmtBar.getPackage());
        ImportStatement defaultStmt = new ImportStatement("Worble");
        assertNull(defaultStmt.getPackage());
    }
    @Test
    public void testGetPackage() {
        ImportStatement stmtBar = new ImportStatement(BAR_CLASS);
        assertEquals("com.foo", stmtBar.getPackage());
    }
    @Test
    public void testCompareBad() {
        try {
            ImportStatement stmtBar = new ImportStatement(BAR_CLASS);
            stmtBar.compareTo("hello");
            fail("Should have gotten a ClassCastException");
        } catch (ClassCastException ex) {
            // cool
        }
    }
    @Test
    public void testCompareNull() {
        try {
            ImportStatement stmtBar = new ImportStatement(BAR_CLASS);
            stmtBar.compareTo(null);
            fail("Should have gotten an IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // cool
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHashcode() {
        ImportStatement stmtBar = new ImportStatement(BAR_CLASS);
        ImportStatement stmtBaz = new ImportStatement(BUZ_CLASS);
        assertTrue(stmtBar.hashCode() != stmtBaz.hashCode());
        HashMap map = new HashMap();
        map.put(stmtBar, "hello");
        assertEquals("hello", map.get(stmtBar));
    }
    @Test
    public void testEquals() {
        ImportStatement stmtBar = new ImportStatement(BAR_CLASS);
        ImportStatement stmtBaz = new ImportStatement(BUZ_CLASS);
        assertTrue(!stmtBar.equals(stmtBaz));
        assertTrue(stmtBar.equals(stmtBar));
    }
}
