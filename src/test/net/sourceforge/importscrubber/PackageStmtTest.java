package test.net.sourceforge.importscrubber;

import junit.framework.*;
import net.sourceforge.importscrubber.*;
 
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
		assert(stmt.isInSamePackageAs("net.sourceforge.importscrubber"));
		assert(!stmt.isInSamePackageAs("net.sourceforge.osage"));
	}
	
	public void testDefaultPkg() {
		PackageStmt stmt = new PackageStmt();
		assert(!stmt.isInSamePackageAs("net.sourceforge.importscrubber"));
		assert(stmt.isInSamePackageAs(""));
	}
}
