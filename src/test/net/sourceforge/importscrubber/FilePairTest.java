package test.net.sourceforge.importscrubber;

import java.io.File;
import java.util.Iterator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sourceforge.importscrubber.FilePair;

public class FilePairTest extends TestCase
{
   public FilePairTest(String aName)
   {
      super(aName);
   }

   public static Test suite()
   {
      return new TestSuite(FilePairTest.class);
   }

   public void testBasic()
   {
      File a = new File("c:\\");
      File b = new File("d:\\");
      FilePair pair = new FilePair(a, b);
      assert(pair.getSourceFile().equals(a));

      Iterator i = pair.getClassFiles();
      assert(i.hasNext());
      i.next();
      assert(!i.hasNext());
   }
}
