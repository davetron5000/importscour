package test.net.sourceforge.importscrubber;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.io.File;
import net.sourceforge.importscrubber.JavaFileFilter;
import static org.testng.AssertJUnit.*;

public class JavaFileFilterTest {
    @Test
    public void testFile() {
        JavaFileFilter f = new JavaFileFilter();
        assertTrue(f.accept(new File("src/java/net/sourceforge/importscrubber"), 
                            "ImportScrubber.java"));
        assertTrue(!f.accept(new File("src/java/net/sourceforge/importscrubber"), 
                            "Foobar.txt"));
    }
    @Test
    public void testDirs() {
        JavaFileFilter f = new JavaFileFilter();
        assertTrue(f.accept(new File("src/java/net/sourceforge/importscrubber"), 
                            "ant"));
        assertTrue(!f.accept(new File("src/java/net/sourceforge/importscrubber"), 
                            "foobar"));
    }
}
