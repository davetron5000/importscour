package net.sourceforge.importscrubber;


public class ImportStatement implements Comparable {

    private String className;


    public ImportStatement(String className) {

        this.className = className;

    }


    public String getFullyQualifiedClassName() {

        return className;

    }


    public int hashCode() {

        return className.hashCode();

    }


    public boolean isInDefaultPackage() {

        return className.lastIndexOf(".") == -1;

    }


    public String getPackage() {

        return className.substring(0, className.lastIndexOf("."));

    }


    public boolean equals(Object other) {

        if (!(other instanceof ImportStatement) || other == null) {

            return false;

        }

        ImportStatement otherStmt = (ImportStatement) other;

        return otherStmt.getFullyQualifiedClassName().equals(className);

    }


    public String getClassName() {

        int lastDot = className.lastIndexOf(".");

        return className.substring(lastDot + 1, className.length());

    }


    public boolean isInStdJavaLibrary() {

        return className.startsWith("java.");

    }


    public boolean isInStdJavaExtensionLibrary() {

        return className.startsWith("javax.");

    }


    public StringBuffer getFormattedStmt() {

        StringBuffer result = new StringBuffer();

        result.append(ImportStatements.MARKER);

        result.append(" ");

        result.append(className);

        result.append(";");

        result.append(ImportScrubber.LINE_SEPARATOR);

        return result;

    }

    // Comparable
    public int compareTo(Object in) {

        if (in == null) {

            throw new IllegalArgumentException("Can't compare this to null!");

        }
        // If they are the same then simple comparison will suffice.  If not,
        // we'd like to put something like java.awt.Toolkit before
        // java.awt.print.Pageable which a simple comparison won't do.
        ImportStatement otherImport = (ImportStatement) in;


        if (!getPackage().equals(otherImport.getPackage())) {

            if (getPackage().startsWith(otherImport.getPackage())) {

                return 1;


            } else if (otherImport.getPackage().startsWith(getPackage())) {

                return -1;

            }

        }

        // same package, so just do simple comparison
        return className.toLowerCase().compareTo(otherImport.getFullyQualifiedClassName().toLowerCase());

    }
    // Comparable

}


