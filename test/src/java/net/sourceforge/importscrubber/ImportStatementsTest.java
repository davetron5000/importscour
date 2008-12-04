package test.net.sourceforge.importscrubber;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sourceforge.importscrubber.ImportScrubber;
import net.sourceforge.importscrubber.ImportStatements;
import net.sourceforge.importscrubber.PackageStmt;

public class ImportStatementsTest extends TestCase {
    private static final String BAR_CLASS = "com.foo.Bar";
    private static final String BAZ_CLASS = "com.foo.Baz";
    private static final String BUZ_CLASS = "com.foo.biz.Buz";
    public ImportStatementsTest(String aName) {
        super(aName);
    }
    public static Test suite() {
        return new TestSuite(ImportStatementsTest.class);
    }
    public void testBasic() {
        ImportStatements is = new ImportStatements();
        is.add(BAR_CLASS);
    }
    public void testDupe() {
        ImportStatements is = new ImportStatements();
        is.add(BAR_CLASS);
        is.add(BAR_CLASS);
        assertEquals(1, is.getCount());
    }
    public void testRemoveLocal() {
        ImportStatements is = new ImportStatements();
        is.add(BAR_CLASS);
        is.add(BUZ_CLASS);
        assertEquals(2, is.getCount());
        PackageStmt ps = new PackageStmt("package com.foo.biz;");
        is.removeLocalToPackage(ps);
        assertEquals(1, is.getCount());
    }
    public void testRemoveUnreferenced() {
        ImportStatements is = new ImportStatements();
        is.add(BAR_CLASS);
        is.add(BUZ_CLASS);
        assertEquals(2, is.getCount());
        is.removeUnreferenced("Buz");
        assertEquals(1, is.getCount());
    }
}
