package test.net.sourceforge.importscrubber.format;

import java.util.ArrayList;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sourceforge.importscrubber.format.NoBreaks;
import net.sourceforge.importscrubber.*;

public class NoBreaksTest extends TestCase
{
   public NoBreaksTest(String aName)
   {
      super(aName);
   }

   public static Test suite()
   {
      return new TestSuite(NoBreaksTest.class);
   }

   public void testBasic()
   {
      List l = new ArrayList();
      l.add(new ImportStatement("com.foo.Bar"));
      l.add(new ImportStatement("java.util.Buz"));
      
      NoBreaks n = new NoBreaks();

      StringBuffer res = n.applyFormat(l.iterator());

      int count = 0;
      for (int i=0; i<res.length(); i++)
      {
         if (res.charAt(i) == '\n') {
            count++;
         }
      }

      assertEquals(2, count);
   }
}
