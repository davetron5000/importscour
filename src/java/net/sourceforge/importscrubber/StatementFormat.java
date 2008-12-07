package net.sourceforge.importscrubber;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/** Controls how the block of import statements should be formatted */
@SuppressWarnings("unchecked")
public class StatementFormat
{
    /** Insert a break after a group of imports from the same package */
    public static final int BREAK_EACH_PACKAGE = 0;
    /** Insert no additional breaks in the import statement section */
    public static final int BREAK_NONE = 1;

    private boolean _sortJavaLibsHigh;
    private int _breakStyle;
    private int _combineThreshold;
    private boolean _thresholdStandardOnly; 
    private List identical; // for applyFormat -- stored here so we avoid re-allocating each time

    /** Create a new StatementFormat.
     * @param sortJavaLibsHigh if true, java.* packages will be force-sorted to the top of the import statments.
     * @param breakStyle if {@link #BREAK_EACH_PACKAGE}, an additional linebreak will appear between groups of imports from the same package.  If {@link #BREAK_NONE}, no
     * additional breaking.
     * @param combineThreshold if non-zero, the number of imports from a package until a star import is used.  0 means to never use star imports.
     * @param thresholdStandardOnly if ture, combineThreshold only applies to <tt>java.*</tt> packages
     */
    public StatementFormat(boolean sortJavaLibsHigh, int breakStyle, int combineThreshold, boolean thresholdStandardOnly)
    {
        _sortJavaLibsHigh = sortJavaLibsHigh;
        _breakStyle = breakStyle;
        _combineThreshold = combineThreshold;
        _thresholdStandardOnly = thresholdStandardOnly;

        identical = new ArrayList(_combineThreshold);
    }

    public String toString()
    {
        StringBuilder b = new StringBuilder();
        b.append(getClass().getName());
        b.append(" {\n");

        b.append("\tsortJavaLibsHigh = ");
        b.append(_sortJavaLibsHigh);
        b.append(",\n");

        b.append("\tbreakStyle = ");
        b.append(_breakStyle);
        b.append(",\n");

        b.append("\tcombineThreshold = ");
        b.append(_combineThreshold);
        b.append(",\n");

        b.append("\tthresholdStandardOnly = ");
        b.append(_thresholdStandardOnly);
        b.append("\n");

        b.append("};");

        return b.toString();
    }

    public StringBuffer applyFormat(List<ImportStatement> list)
    {
        Collections.sort(list, new ImportStatementComparator(_sortJavaLibsHigh));

        // pre-process the threshold requirement
        if (_combineThreshold > 0) {
            List<ImportStatement> oldList = list;
            list = new ArrayList<ImportStatement>();
            String lastPackage = null;
            identical.clear();
            for (Iterator i = oldList.iterator(); i.hasNext(); ) {
                ImportStatement stmt = (ImportStatement)i.next();
                if (lastPackage == null || lastPackage.compareTo(stmt.getPackage()) != 0) {
                    if (identical.size() > _combineThreshold) {
                        list.add(new ImportStatement(lastPackage + ".*"));
                    } else {
                        list.addAll(identical);
                    }
                    identical.clear();
                    lastPackage = stmt.getPackage();
                }
                if (_thresholdStandardOnly
                        && !(stmt.isInStdJavaLibrary() || stmt.isInStdJavaExtensionLibrary())) {
                    list.add(stmt);
                } else {
                    identical.add(stmt);
                }
            }
            if (identical.size() > _combineThreshold) {
                list.add(new ImportStatement(lastPackage + ".*"));
            } else {
                list.addAll(identical);
            }
        }

        StringBuffer result = new StringBuffer();
        ImportStatement last = null;
        for (Iterator i = list.iterator(); i.hasNext(); ) {
            ImportStatement next = (ImportStatement)i.next();
            if (_breakStyle == BREAK_EACH_PACKAGE) {
                if (last != null && !last.getPackage().equals(next.getPackage())) {
                    result.append(ImportScrubber.LINE_SEPARATOR);
                }
            }
            last = next;
            result.append(next.getFormattedStmt());
        }
        return result;
    }
}
