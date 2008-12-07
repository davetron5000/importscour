package net.sourceforge.importscrubber;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

@SuppressWarnings("unchecked")
public class StatementFormat
{
    public static final int BREAK_EACH_PACKAGE = 0;
    public static final int BREAK_NONE = 1;

    private static List _formats = new ArrayList(2);

    static {
        ResourceBundle res = ResourceBundle.getBundle("net.sourceforge.importscrubber.Resources");
        _formats.add(res.getString(Resources.BREAK_EACH_PACKAGE));
        _formats.add(res.getString(Resources.BREAK_NONE));
    }

    public static Object keyToValue(int key)
    {
        return _formats.get(key);
    }
    public static int valueToKey(Object value)
    {
        return _formats.indexOf(value);
    }

    public static Iterator formatIterator()
    {
        return _formats.iterator();
    }

    // get a description of args understood by getFormat
    public static String getUsage()
    {
        return   "  -sortJavaLibsHigh       : place 'import java.*' statements at the\n"  
               + "                            top [default: off]\n"  
               + "  -combineThreshold <int> : import * from a package when MORE THAN this\n"  
               + "                            number of imports are found [default: 0 = off]\n"  
               + "  -thresholdStandardOnly  : apply combineThreshold only to java standard\n"   
               + "                            library; other packages will continue to import\n"  
               + "                            individual classes no matter how many are\n"  
               + "                            used [default: off]\n"  
               + "  -breakEachPackage       : linebreak after the imports from each\n"  
               + "                            package [default: off]\n"  ;
    }

    // arg parsing done here to make it easier for others to add new features
    public static StatementFormat getFormat(String[] args)
    {
        int breakStyle = BREAK_NONE;
        int combineThreshold = 0;
        boolean sortJavaLibsHigh = ImportScrubber.argExists("sortjavalibshigh", args);
        boolean thresholdStandardOnly = ImportScrubber.argExists("thresholdStandardOnly", args);
        if (ImportScrubber.argExists("combineThreshold", args)) {
            combineThreshold = new Integer(ImportScrubber.findArg("combineThreshold", args)).intValue();
        }
        if (ImportScrubber.argExists("breakEachPackage", args)) {
            breakStyle = BREAK_EACH_PACKAGE;
        }

        return new StatementFormat(sortJavaLibsHigh, breakStyle, combineThreshold, thresholdStandardOnly);
    }

    //
    // the actual format stuff

    private boolean _sortJavaLibsHigh;
    private int _breakStyle;
    private int _combineThreshold;
    private boolean _thresholdStandardOnly; // true if threshold should only apply to standard libraries
    private List identical; // for applyFormat -- stored here so we avoid re-allocating each time

    public StatementFormat(boolean sortJavaLibsHigh, int breakStyle, int combineThreshold, boolean thresholdStandardOnly)
    {
        _sortJavaLibsHigh = sortJavaLibsHigh;
        _breakStyle = breakStyle;
        _combineThreshold = combineThreshold;
        _thresholdStandardOnly = thresholdStandardOnly;
        identical = new ArrayList(_combineThreshold);
    }

    @SuppressWarnings("unchecked")
    public StringBuffer applyFormat(List list)
    {
        Collections.sort(list, new ImportStatementComparator(_sortJavaLibsHigh));

        // pre-process the threshold requirement
        if (_combineThreshold > 0) {
            List oldList = list;
            list = new ArrayList();
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
