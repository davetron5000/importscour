package net.sourceforge.importscrubber;


import net.sourceforge.importscrubber.format.IStatementFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * Encapsulates various operations on a class' import statements
 */

public class ImportStatements {

    public static final String MARKER = "import";

    private List _stmts = new ArrayList();


    public void add(String candidateString) {

        ImportStatement candidateImport = new ImportStatement(candidateString);


        if (candidateImport.isInDefaultPackage() || _stmts.contains(candidateImport)) {

            return;

        }


        _stmts.add(candidateImport);

    }


    public StringBuffer getOutput(IStatementFormat format) {

        Collections.sort(_stmts, new ImportStatementComparator(format.sortJavaLibsHigh()));

        return format.applyFormat(_stmts.iterator());

    }


    public int getCount() {

        return _stmts.size();

    }


    /**
     * Remove imports for classes in the same package as the current class
     */

    public void removeLocalToPackage(PackageStmt packageStmt) {

        for (Iterator i = _stmts.iterator(); i.hasNext();) {

            if (packageStmt.isInSamePackageAs((ImportStatement) i.next())) {

                i.remove();

            }

        }

    }


    /**
     * Remove those imports which appear in the class file
     * byte code but are not directly referenced in the source code
     */

    public void removeUnreferenced(String classBody) {

        for (Iterator i = _stmts.iterator(); i.hasNext();) {

            ImportStatement importStmt = (ImportStatement) i.next();
            // is that class name mentioned somewhere in the class?
            if (classBody.indexOf(importStmt.getClassName()) == -1) {

                // nope, so remove it
                if (ImportScrubber.DEBUG) System.out.println("Removing unreferenced import:" + importStmt.getClassName());

                i.remove();

            }

        }


        for (Iterator i = _stmts.iterator(); i.hasNext();) {

            ImportStatement importStmt = (ImportStatement) i.next();
            // is that class used as a fully qualified name in the class body somewhere?
            if (classBody.indexOf(importStmt.getFullyQualifiedClassName()) != -1) {

                // nope, so remove it
                if (ImportScrubber.DEBUG) System.out.println("Removing fully qualified import:" + importStmt.getClassName());

                i.remove();

            }

        }

    }

}
