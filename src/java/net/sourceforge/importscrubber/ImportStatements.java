package net.sourceforge.importscrubber;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Encapsulates various operations on a class' import statements
 */
public class ImportStatements
{
    public static final String MARKER = "import";
    private List stmts = new ArrayList();

    @SuppressWarnings("unchecked")
    public void add(String candidateString)
    {
		ImportStatement candidate = new ImportStatement(candidateString);
		if(candidate.getPackage() == null || stmts.contains(candidate)) {
			if (ImportScrubber.DEBUG)
				System.out.println("not adding " + candidate.getFullyQualifiedClassName());
			return;
		}

		stmts.add(candidate);
    }

    public StringBuffer getOutput(StatementFormat format)
    {
        return format.applyFormat(stmts);
    }

    public int getCount()
    {
        return stmts.size();
    }

    /**
     * Remove imports for classes in the same package as the current class
     */
    public void removeLocalToPackage(PackageStmt packageStmt)
    {
        for (Iterator i = stmts.iterator(); i.hasNext();) {
            ImportStatement stmt = (ImportStatement)i.next();
            if (packageStmt.isInSamePackageAs(stmt)) {
                if (ImportScrubber.DEBUG)
                    System.out.println("Removing local import:" + stmt.getClassName());
                i.remove();
            }
        }
    }

	public void removeInnerClasses(String className) {
		if (ImportScrubber.DEBUG)
			System.out.println("Looking for inner classes of " + className);
        for (Iterator i = stmts.iterator(); i.hasNext();) {
            ImportStatement stmt = (ImportStatement)i.next();
            if (stmt.getFullyQualifiedClassName().startsWith(className + ".")) {
                if (ImportScrubber.DEBUG)
                    System.out.println("Removing inner class import:" + stmt.getClassName());
                i.remove();
            }
        }
	}

    /**
     * Remove those imports which appear in the class file 
     * byte code but are not directly referenced in the source code
    * returns non-null on error condition.
     */
    public String removeUnreferenced(String classBody)
    {
        for (Iterator i = stmts.iterator(); i.hasNext();) {
            ImportStatement stmt = (ImportStatement)i.next();
            // is that class name mentioned somewhere in the class?
            if (classBody.indexOf(stmt.getClassName()) == -1) {
                // nope, so remove it
                if (ImportScrubber.DEBUG)
                    System.out.println("Removing unreferenced import:" + stmt.getClassName());
                i.remove();
                continue;
            }

            // is that class used as a fully qualified name in the class body somewhere?
            int j = classBody.indexOf(stmt.getFullyQualifiedClassName());
            if (j != -1) {
                if (ImportScrubber.DEBUG)
                    System.out.println("FQ class found:" + stmt.getClassName());
                boolean bareClass = false;
                while (j != -1) {
                    if (classBody.charAt(j - 1) != '.') {
                        bareClass = true;
                        break;
                    }
                    j = classBody.indexOf(stmt.getFullyQualifiedClassName(), j + 1);
                }
                // if also used as bare class, check to see if leaving import in makes things ambiguous:
                // if multiple classes w/ the same name are used, we bail.
                // (for example, say java.util.Date and java.sql.Date are both specified as FQ classnames.
                //  but user has imported java.util.Date, and uses unqualified Date too.
                //  we can't tell which import to leave out w/o additional parsing of classBody.  Which
                //  is doable, but I'm considering this rare enough to not be worth the effort, and
                //  I'll settle for just being sure not to break things.)
                if (bareClass) {
                    if (ImportScrubber.DEBUG)
                        System.out.println("Bare class also found");
                    for (Iterator k = stmts.iterator(); k.hasNext(); ) {
                        ImportStatement s2 = (ImportStatement)k.next();
                        if (s2 != stmt && stmt.getClassName().compareTo(s2.getClassName()) == 0) {
                            return "ambiguous use of " + stmt.getClassName() + "."
                                   + ImportScrubber.LINE_SEPARATOR +  "\t(" + stmt.getFullyQualifiedClassName() + " and " + s2.getFullyQualifiedClassName() + ")"
                                   + ImportScrubber.LINE_SEPARATOR +  "\tTo scrub, make all references fully qualified, or none.";
                        }
                    }
                    // loop finished so no ambiguity, the unqualified references all refer to this class.  leave the import in.
                } else {
                    if (ImportScrubber.DEBUG)
                        System.out.println("No bare class found; removing");
                    i.remove();
                }
            }
        }

        return null; // no error
    }
}

