package net.sourceforge.importscrubber.format;


import java.util.*;


public class StatementFormatFactory {


    public static final String BREAK_FOR_EACH_PACKAGE = "each";

    public static final String BREAK_FOR_TOP_PACKAGE = "top";

    public static final String NO_BREAKS = "nobreaks";

    public static final String DEFAULT = NO_BREAKS;


    private HashMap _formats;

    private List _formatsList;

    private static StatementFormatFactory _singleton;


    private StatementFormatFactory() {

        _formats = new HashMap();

        _formats.put(BREAK_FOR_EACH_PACKAGE, new BreakForEachPackage());

        _formats.put(BREAK_FOR_TOP_PACKAGE, new BreakForTopPackage());

        _formats.put(NO_BREAKS, new NoBreaks());


        _formatsList = new ArrayList();

        _formatsList.add(_formats.get(BREAK_FOR_EACH_PACKAGE));

        _formatsList.add(_formats.get(BREAK_FOR_TOP_PACKAGE));

        _formatsList.add(_formats.get(NO_BREAKS));

    }


    public static StatementFormatFactory getInstance() {

        if (_singleton == null) {

            _singleton = new StatementFormatFactory();

        }

        return _singleton;

    }


    public IStatementFormat createStatementFormat(String id) {

        if (!_formats.containsKey(id)) {

            System.out.println("Format " + id + " not found!  Available formats:");

            for (Iterator keys = _formats.keySet().iterator(); keys.hasNext();) {

                String key = (String) keys.next();

                System.out.println(key);

            }

            throw new IllegalArgumentException("Format " + id + " not found!  Available formats:");

        }


        return (IStatementFormat) _formats.get(id);

    }


    public Map getFormatsMap() {

        return Collections.unmodifiableMap(_formats);

    }


    public List getFormatsList() {

        return Collections.unmodifiableList(_formatsList);

    }


}
