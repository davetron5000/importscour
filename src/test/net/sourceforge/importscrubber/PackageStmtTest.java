package test.net.sourceforge.importscrubber;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sourceforge.importscrubber.ImportStatement;
import net.sourceforge.importscrubber.PackageStmt;

public class PackageStmtTest extends TestCase {
    private static final String DEFAULT = "package net.sourceforge.importscrubber;";
    public PackageStmtTest(String aName) {
        super(aName);
    }
    public static Test suite() {
        return new TestSuite(PackageStmtTest.class);
    }
    public void testBasic() {
        PackageStmt stmt = new PackageStmt(DEFAULT);
        assertTrue(stmt.isInSamePackageAs(
                           new ImportStatement(
                                   "net.sourceforge.importscrubber.Buz")));
        assertTrue(!stmt.isInSamePackageAs(
                            new ImportStatement("net.sourceforge.osage.Bat")));
    }
    public void testDefaultPkg() {
        PackageStmt stmt = new PackageStmt();
        assertTrue(!stmt.isInSamePackageAs(
                            new ImportStatement(
                                    "net.sourceforge.importscrubber.Foo")));
        assertTrue(stmt.isInSamePackageAs(new ImportStatement("")));
    }
    public void testInnerClass() {
        PackageStmt stmt = new PackageStmt(DEFAULT);
        assertTrue(stmt.isInSamePackageAs(
                           new ImportStatement(
                                   "net.sourceforge.importscrubber.Foo.Fiz")));
    }
    public void testChildPackage() {
        PackageStmt stmt = new PackageStmt(DEFAULT);
        assertTrue(!stmt.isInSamePackageAs(
                            new ImportStatement(
                                    "net.sourceforge.importscrubber.biz.Buz")));
    }
}
