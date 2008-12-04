package test.net.sourceforge.importscrubber;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.io.File;
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
}
