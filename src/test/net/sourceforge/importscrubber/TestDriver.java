package test.net.sourceforge.importscrubber;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import test.net.sourceforge.importscrubber.filechooser.*;
import test.net.sourceforge.importscrubber.format.*;

public class TestDriver extends TestCase
{
   public static void main(String[] args)
   {
      Test test = suite();
      TestResult tr = new TestResult();
      test.run(tr);
   }

   public TestDriver(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      TestSuite t = new TestSuite(PackageStmtTest.class);
      t.addTest(new TestSuite(FilePairTest.class));
      t.addTest(new TestSuite(RecursiveFileChooserTest.class));
      t.addTest(new TestSuite(AllInDirTest.class));  
      t.addTest(new TestSuite(JavaFileFilterTest.class));  
      t.addTest(new TestSuite(ImportStatementsTest.class));  
      t.addTest(new TestSuite(ImportStatementTest.class));  
      t.addTest(new TestSuite(NoBreaksTest.class));  
      return t;
   }
}
