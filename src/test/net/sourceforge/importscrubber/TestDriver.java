package test.net.sourceforge.importscrubber;

import junit.framework.*;

public class TestDriver extends TestCase {
	public static void main(String[] args) {
		Test test = suite();
		TestResult tr = new TestResult();
		test.run(tr);
	}
	
	public TestDriver(String name) {
		super(name);
	}
	
	public static Test suite() {
		TestSuite t = new TestSuite(PackageStmtTest.class);
		t.addTest(new TestSuite(FilePairTest.class));
		return t;
	}
}
