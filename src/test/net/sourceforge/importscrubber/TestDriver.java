package test.net.sourceforge.importscrubber;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
public class TestDriver extends TestCase {
    public TestDriver(String name) {
        super(name);
    }
    public static void main(String[] args) {
        Test test = suite();
        TestResult tr = new TestResult();
        test.run(tr);
    }
    public static Test suite() {
        TestSuite t = new TestSuite(PackageStmtTest.class);
        t.addTest(new TestSuite(FilePairTest.class));
        t.addTest(new TestSuite(JavaFileFilterTest.class));
        t.addTest(new TestSuite(ImportStatementsTest.class));
        t.addTest(new TestSuite(ImportStatementTest.class));
        return t;
    }
}
