package net.sourceforge.importscrubber.format;

import java.util.*;
import net.sourceforge.importscrubber.*;

public class BreakForTopPackage extends AbstractStatementFormat implements IStatementFormat
{
   public StringBuffer applyFormat(Iterator it) 
   {
      StringBuffer result = new StringBuffer();
      ImportStatement last = null;
      while (it.hasNext())
      {
         ImportStatement next = (ImportStatement) it.next();
         if (last != null && !last.getPackage().equals(next.getPackage()))
         {
            result.append(ImportScrubber.LINE_SEPARATOR);
         }
         last = next;
         result.append(next.getFormattedStmt());
      }
      return result;
   }

   public String toString() {
      return super.toString(Resources.BREAK_TOP_PACKAGE);
   }

}
