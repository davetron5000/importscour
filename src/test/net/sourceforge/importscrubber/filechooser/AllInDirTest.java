package test.net.sourceforge.importscrubber.filechooser;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sourceforge.importscrubber.filechooser.AllInDirectoryFileChooser;

public class AllInDirTest extends TestCase
{
   public AllInDirTest(String aName)
   {
      super(aName);
   }

   public static Test suite()
   {
      return new TestSuite(AllInDirTest.class);
   }

   public void testBasic()
   {
      AllInDirectoryFileChooser fc = new AllInDirectoryFileChooser();
      fc.setRoot("d:\\data\\importscrubber\\etc");
      assertEquals(2, fc.getFiles().size());
   }
}
