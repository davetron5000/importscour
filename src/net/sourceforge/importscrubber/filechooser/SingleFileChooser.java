package net.sourceforge.importscrubber.filechooser;

import net.sourceforge.importscrubber.Resources;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import net.sourceforge.importscrubber.FilePair;

/**
 * Encapsulates a file chooser that only picks one file.
 */
public class SingleFileChooser implements IFileChooser
{
   private String _root;

   public void setRoot(String root)
   {
      ResourceBundle res = ResourceBundle.getBundle("net.sourceforge.importscrubber.Resources");
      File file = new File(root);
      if (file.isDirectory()) throw new IllegalArgumentException(res.getString(Resources.ERR_MUST_NOT_BE_DIR));

      File test = null;
      if (file.getName().endsWith(".java"))
      {
         test = new File(root);
      } else
      {
         test = new File(root + ".java");
      }

      if (!test.exists()) throw new IllegalArgumentException("Source file " + test.getAbsoluteFile()  + " doesn't exist!");

      if (file.getName().endsWith(".java"))
      {
         test = new File(root.substring(0, root.indexOf(".java")) + ".class");
      } else
      {
         test = new File(root + ".class");
      }
      if (!test.exists()) throw new IllegalArgumentException("Class file " + test.getAbsoluteFile()  + " doesn't exist!");
      

      if (file.getName().endsWith(".java"))
      {
         _root = root.substring(0, root.indexOf(".java"));
      } else
      {
         _root = root;
      }
   }

   public List getFiles()
   {
      FilePair pair = new FilePair(new File(_root + ".java"), new File(_root + ".class"));
      List files = new ArrayList();
      files.add(pair);
      return files;
   }
}
