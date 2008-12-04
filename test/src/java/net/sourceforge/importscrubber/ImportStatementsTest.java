package test.net.sourceforge.importscrubber;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import net.sourceforge.importscrubber.ImportScrubber;
import net.sourceforge.importscrubber.ImportStatements;
import net.sourceforge.importscrubber.PackageStmt;
import static org.testng.AssertJUnit.*;

public class ImportStatementsTest {
    private static final String BAR_CLASS = "com.foo.Bar";
    private static final String BAZ_CLASS = "com.foo.Baz";
    private static final String BUZ_CLASS = "com.foo.biz.Buz";
  @Test
    public void testBasic() {
        ImportStatements is = new ImportStatements();
        is.add(BAR_CLASS);
    }
  @Test
    public void testDupe() {
        ImportStatements is = new ImportStatements();
        is.add(BAR_CLASS);
        is.add(BAR_CLASS);
        assertEquals(1, is.getCount());
    }
  @Test
    public void testRemoveLocal() {
        ImportStatements is = new ImportStatements();
        is.add(BAR_CLASS);
        is.add(BUZ_CLASS);
        assertEquals(2, is.getCount());
        PackageStmt ps = new PackageStmt("package com.foo.biz;");
        is.removeLocalToPackage(ps);
        assertEquals(1, is.getCount());
    }
  @Test
    public void testRemoveUnreferenced() {
        ImportStatements is = new ImportStatements();
        is.add(BAR_CLASS);
        is.add(BUZ_CLASS);
        assertEquals(2, is.getCount());
        is.removeUnreferenced("Buz");
        assertEquals(1, is.getCount());
    }
}
