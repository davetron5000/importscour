package net.sourceforge.importscrubber;

import java.io.File;
import java.io.IOException;
import java.util.ListIterator;
import net.sourceforge.importscrubber.format.IStatementFormat;

/**
 * Encapsulates the data needed to clean up the import statements of one file
 */
public class ScrubTask implements IReferenceFoundListener
{
   private SourceFile _sourceFile;
   private final FilePair _pair;
   private IStatementFormat _format;

   public ScrubTask(FilePair pair, IStatementFormat format) throws IOException {
      _pair = pair;
      _format = format;
   }

   public void run() throws IOException
   {
      _sourceFile = new SourceFile(_pair.getSourceFile());
      for (ListIterator iter = _pair.getClassFiles(); iter.hasNext();)
      {
         ClassParserWrapper.parse((File)iter.next(), this);
      }
      _sourceFile.save(_format);
   }

   public void referenceFound(String className)
   {
      _sourceFile.addImport(className);
   }

   public String toString()
   {
      return getSourcePath();
   }

   public String getSourcePath()
   {
      return _pair.getSourceFile().getAbsolutePath();
   }

   public static void main(String[] args)
   {
      FilePair pair = new FilePair(new File("d:\\importscrubber\\tmp\\NodeListener.java"), new File("d:\\importscrubber\\tmp\\NodeListener.class"));
      for (ListIterator iter = pair.getClassFiles(); iter.hasNext();)
      {
         try
         {
            ClassParserWrapper.parse((File)iter.next(), new PrintListener());
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
   }
}
