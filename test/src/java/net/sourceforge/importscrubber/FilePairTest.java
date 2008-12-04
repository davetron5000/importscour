package test.net.sourceforge.importscrubber;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import net.sourceforge.importscrubber.FilePair;

import static org.testng.AssertJUnit.*;

public class FilePairTest {

    @Test
    public void testBasic() {
        File a = new File("c:\\");
        File b = new File("d:\\");
        FilePair pair = new FilePair(a, b);
        assertTrue(pair.getSourceFile().equals(a));
        Iterator i = pair.getClassFiles();
        assertTrue(i.hasNext());
        i.next();
        assertTrue(!i.hasNext());
    }

    @Test
    public void testEquals()
        throws IOException
    {
        File tmpFile1 = File.createTempFile("is-test",".java");
        File tmpClass1 = File.createTempFile("is-test",".class");
        File tmpFile2 = File.createTempFile("is-test",".java");
        File tmpClass2 = File.createTempFile("is-test",".class");
        FilePair pair1 = new FilePair(tmpFile1,tmpClass1);
        FilePair pair2 = new FilePair(tmpFile2,tmpClass2);
        FilePair pair3 = new FilePair(tmpFile1,tmpClass1);
        FilePair pair4 = new FilePair(tmpFile1,tmpClass2);
        FilePair pair5 = new FilePair(tmpFile2,tmpClass1);

        assert !pair1.equals(pair2) : "pair1 and pair2 shouldn't be equal";
        assert pair1.equals(pair1) : "pair1 should equal itself";

        assert pair1.equals(pair3) : "pair1 and pair3 should be equal";
        assert pair3.equals(pair1) : "pair3 and pair1 should be equal";
        assert pair1.hashCode() == pair3.hashCode() : "pair3 and pair1 should have equal hashcodes";

        assert pair1.equals(pair4) : "pair1 and pair4 should be equal";
        assert pair4.equals(pair1) : "pair4 and pair1 should be equal";
        assert pair1.hashCode() == pair4.hashCode() : "pair4 and pair1 should have equal hashcodes";

        assert !pair1.equals(pair5) : "pair1 and pair4 shouldn't be equal";
        assert !pair5.equals(pair1) : "pair4 and pair1 shouldn't be equal";
    }
}
