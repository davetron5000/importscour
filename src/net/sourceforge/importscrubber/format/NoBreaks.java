package net.sourceforge.importscrubber.format;


import net.sourceforge.importscrubber.ImportStatement;
import net.sourceforge.importscrubber.Resources;

import java.util.Iterator;


public class NoBreaks extends AbstractStatementFormat implements IStatementFormat {

    public StringBuffer applyFormat(Iterator it) {

        StringBuffer result = new StringBuffer();

        while (it.hasNext()) {

            result.append(((ImportStatement) it.next()).getFormattedStmt());

        }

        return result;

    }


    public String toString() {

        return super.toString(Resources.BREAK_NONE);

    }

}
