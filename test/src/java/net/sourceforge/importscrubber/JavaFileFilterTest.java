package test.net.sourceforge.importscrubber;
import java.io.File;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sourceforge.importscrubber.JavaFileFilter;

public class JavaFileFilterTest extends TestCase {
    public JavaFileFilterTest(String aName) {
        super(aName);
    }
    public static Test suite() {
        return new TestSuite(JavaFileFilterTest.class);
    }
    public void testBasic() {
        JavaFileFilter f = new JavaFileFilter();
        assertTrue(f.accept(new File("d:\\data\\importscrubber\\etc"), 
                            "FunctionalTest.java"));
        assertTrue(!f.accept(new File("d:\\data\\importscrubber\\etc"), 
                             "Foo.java"));
    }
}