package net.sourceforge.importscrubber;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import net.sourceforge.importscrubber.format.IStatementFormat;

/**
 * Encapsulates operations on the Java source code file
 * 
 * TODO: refactor this into a parser class - this constructor is getting heinous
 */
public class SourceFile
{
   private File _file;
   private PackageStmt _package;
   private String _classBody;
   private ImportStatements _imports;
   private String _firstCommentHeader;
   private String _secondCommentHeader;

   public SourceFile(File file) throws IOException {
      _file = file;
      _imports = new ImportStatements();

      // read in the source
      FileReader reader = new FileReader(_file);
      BufferedReader buff = new BufferedReader(reader);

      StringBuffer classBodyBuffer = new StringBuffer((int)file.length());
      StringBuffer firstCommentHeaderBuffer = new StringBuffer();
      StringBuffer secondCommentHeaderBuffer = new StringBuffer();

      String currentLine = null;
      boolean passedFirstCommentHeader = false;
      boolean passedSecondCommentHeader = false;

      while ((currentLine = buff.readLine()) != null)
      {
         // discard imports
         if (currentLine.startsWith(ImportStatements.MARKER))
         {
            passedFirstCommentHeader = true;
            passedSecondCommentHeader = true;
            continue;
         }

         // check for package stmt
         if (currentLine.startsWith(PackageStmt.MARKER))
         {
            passedFirstCommentHeader = true;
            _package = new PackageStmt(currentLine);
            continue;
         }

         // TODO - what happens if there are no import statements and no package header?
         // preserve comment headers, if any exist
         if(!passedFirstCommentHeader)
         {
            firstCommentHeaderBuffer.append(currentLine);
            firstCommentHeaderBuffer.append(ImportScrubber.LINE_SEPARATOR);
            continue;
         } 
         
         if(passedFirstCommentHeader && !passedSecondCommentHeader)
         {
            secondCommentHeaderBuffer.append(currentLine);
            secondCommentHeaderBuffer.append(ImportScrubber.LINE_SEPARATOR);
            continue;
         }
         
         // everything else is part of the class body
         passedSecondCommentHeader = true;
         classBodyBuffer.append(currentLine);
         classBodyBuffer.append(ImportScrubber.LINE_SEPARATOR);
      }

      if (_package == null)
      {
         _package = new PackageStmt();
      }

      buff.close();
      reader.close();
      if (ImportScrubber.DEBUG) System.out.println("Done parsing source code file " + file.getAbsolutePath());

      _classBody = classBodyBuffer.toString();
      _firstCommentHeader = firstCommentHeaderBuffer.toString();
      _secondCommentHeader = secondCommentHeaderBuffer.toString();
   }

   public void addImport(String className)
   {
      _imports.add(className);
   }

   public void save(IStatementFormat format) throws IOException {
      _imports.removeUnreferenced(_classBody);
      _imports.removeLocalToPackage(_package);

      // push everything together
      StringBuffer finishedSource = new StringBuffer((int)(_classBody.length() * 1.1));
      finishedSource.append(_firstCommentHeader);
      finishedSource.append(_package.getOutput());
      
      finishedSource.append(removeMultipleBlankLines(_secondCommentHeader));
      finishedSource.append(_imports.getOutput(format));

      _classBody = ImportScrubber.LINE_SEPARATOR + removeMultipleBlankLines(_classBody);
      finishedSource.append(_classBody);

      // write it to disk
      BufferedWriter writer = new BufferedWriter(new FileWriter(_file));
      writer.write(finishedSource.toString());
      writer.close();
   }

   private String removeMultipleBlankLines(String in)
   {
      while (in.startsWith(ImportScrubber.LINE_SEPARATOR))
      {
         in = in.substring(ImportScrubber.LINE_SEPARATOR.length());
      }
      return in;
   }
}
