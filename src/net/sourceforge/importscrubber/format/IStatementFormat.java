package net.sourceforge.importscrubber.format;

import java.util.Iterator;

public interface IStatementFormat
{
   public StringBuffer applyFormat(Iterator it);
   public boolean sortJavaLibsHigh();
   public void sortJavaLibsHigh(boolean sortJavaLibsHigh);
}
