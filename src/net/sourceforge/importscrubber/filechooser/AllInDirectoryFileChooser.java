package net.sourceforge.importscrubber.filechooser;

import net.sourceforge.importscrubber.Resources;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import net.sourceforge.importscrubber.FilePair;
import net.sourceforge.importscrubber.ImportScrubber;
import net.sourceforge.importscrubber.JavaFileFilter;

/**
 * This class encapsulates a file chooser that gets everything in the indicated directory
 */
public class AllInDirectoryFileChooser implements IFileChooser
{
   private String _root;

   public void setRoot(String root)
   {
      File file = new File(root);
      ResourceBundle res = ResourceBundle.getBundle("net.sourceforge.importscrubber.Resources");
      if (!file.isDirectory()) throw new IllegalArgumentException(res.getString(Resources.ERR_NOT_DIR));
      if (!file.exists()) throw new IllegalArgumentException(Resources.ERR_DIR_NOT_EXIST);
      _root = root;
   }

   public List getFiles()
   {
      File file = new File(_root);
      String[] sourceFiles = file.list(new JavaFileFilter());
      List list = new ArrayList(sourceFiles.length);
      for (int i=0; i<sourceFiles.length; i++)
      {
         File sourceFile = new File(_root + ImportScrubber.FILE_SEPARATOR + sourceFiles[i]);
         if (sourceFile.isDirectory()) {
            continue;
         }
         File destFile = new File(_root + ImportScrubber.FILE_SEPARATOR + sourceFiles[i].substring(0, sourceFiles[i].length()-5) + ".class");
         list.add(new FilePair(sourceFile, destFile));
      }
      return list;
   }
}
