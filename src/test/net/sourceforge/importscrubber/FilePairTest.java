package test.net.sourceforge.importscrubber;

import junit.framework.*;
import net.sourceforge.importscrubber.*;
import java.io.*;
 
public class FilePairTest extends TestCase {
    public FilePairTest(String aName) {
        super(aName);
    }
         
    public static Test suite() {
        return new TestSuite(FilePairTest.class);
    }

	public void testBasic() {
		File a = new File("c:\\");
		File b = new File("d:\\");
		FilePair pair = new FilePair(a, b);
		assert(pair.getSourceFile().equals(a));
		assert(pair.getClassFile().equals(b));
	}
}
