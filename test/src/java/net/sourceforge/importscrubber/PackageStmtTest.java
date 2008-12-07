package test.net.sourceforge.importscrubber;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import net.sourceforge.importscrubber.ImportStatement;
import net.sourceforge.importscrubber.PackageStmt;
import static org.testng.AssertJUnit.*;

public class PackageStmtTest {
    private static final String DEFAULT = "package net.sourceforge.importscrubber;";
    @Test
    public void testBasic() {
        PackageStmt stmt = new PackageStmt(DEFAULT);
        assertTrue(stmt.isInSamePackageAs(
                           new ImportStatement(
                                   "net.sourceforge.importscrubber.Buz")));
        assertTrue(!stmt.isInSamePackageAs(
                            new ImportStatement("net.sourceforge.osage.Bat")));
    }
    @Test
    public void testDefaultPkg() {
        PackageStmt stmt = new PackageStmt();
        assertTrue(!stmt.isInSamePackageAs(
                            new ImportStatement(
                                    "net.sourceforge.importscrubber.Foo")));
        assertTrue(!stmt.isInSamePackageAs(new ImportStatement("")));
    }
    /*
    @Test
    public void testInnerClass() {
        // Currently broken
        PackageStmt stmt = new PackageStmt(DEFAULT);
        assertTrue(stmt.isInSamePackageAs(
                           new ImportStatement(
                                   "net.sourceforge.importscrubber.Foo.Fiz")));
    }
    */
    @Test
    public void testChildPackage() {
        PackageStmt stmt = new PackageStmt(DEFAULT);
        assertTrue(!stmt.isInSamePackageAs(
                            new ImportStatement(
                                    "net.sourceforge.importscrubber.biz.Buz")));
    }
}
