package test.net.sourceforge.importscrubber.filechooser;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sourceforge.importscrubber.filechooser.RecursiveFileChooser;

public class RecursiveFileChooserTest extends TestCase
{
   public RecursiveFileChooserTest(String aName)
   {
      super(aName);
   }

   public static Test suite()
   {
      return new TestSuite(RecursiveFileChooserTest.class);
   }

   public void testBasic()
   {
      RecursiveFileChooser fc = new RecursiveFileChooser();
      fc.setRoot("d:\\data\\importscrubber\\etc");
      assertEquals(4, fc.getFiles().size());
   }
}
