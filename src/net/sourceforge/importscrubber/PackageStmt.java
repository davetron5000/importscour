package net.sourceforge.importscrubber;

/**
 * Encapsulates operations on a class' package statement.
 */
public class PackageStmt
{
   public static final String MARKER = "package ";

   private final String _stmt;
   private final boolean _isInDefaultPackage;
   private final String _myPkg;

   /**
    * Creates a new PackageStatement that's not in the default package.
    * 
    * @param stmt
    */
   public PackageStmt(String stmt)
   {
      this(stmt, false);
   }

   /**
    * Creates a new PackageStatement that's in the default package.
    */
   public PackageStmt()
   {
      this(null, true);
   }

   private PackageStmt(String stmt, boolean inDefaultPackage)
   {
      _stmt = stmt;
      _isInDefaultPackage = inDefaultPackage;
      if (!_isInDefaultPackage) {
         int firstSpace = _stmt.indexOf(" ") + 1;
         int semiColon = _stmt.indexOf(";");
         _myPkg = _stmt.substring(firstSpace, semiColon);
      } else {
         _myPkg = null;
      }
   }

   public boolean isInSamePackageAs(ImportStatement importStmt)
   {
      if (_isInDefaultPackage)
      {
         return importStmt.isInDefaultPackage();
      }

      if (dotCount(_myPkg) != dotCount(importStmt.getPackage()))
      {
         // now check to see if this is an inner class
         int lastDot = importStmt.getPackage().lastIndexOf('.');
         if (lastDot == -1)
         {
            return false;
         }
         String uptoLastDot = importStmt.getPackage().substring(0, lastDot);
         if (!_myPkg.equals(uptoLastDot))
         {
            return false;
         }
         return Character.isUpperCase(importStmt.getPackage().substring(lastDot+1, lastDot+2).charAt(0));
      }

      return _myPkg.equals(importStmt.getPackage()) || importStmt.getPackage().startsWith(_myPkg);
   }

   private int dotCount(String pkg)
   {
      int total = 0;
      for (int i=0; i<pkg.length(); i++)
      {
         if (pkg.charAt(i) == '.')
         {
            total++;
         }
      }
      return total;
   }

   public StringBuffer getOutput()
   {
      StringBuffer buffer = new StringBuffer(64);
      if (_isInDefaultPackage)
      {
         return buffer;
      }
      buffer.append(_stmt);
      buffer.append(ImportScrubber.LINE_SEPARATOR);
      buffer.append(ImportScrubber.LINE_SEPARATOR);
      return buffer;
   }
}



