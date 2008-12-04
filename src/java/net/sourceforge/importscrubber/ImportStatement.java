package net.sourceforge.importscrubber;

public class ImportStatement implements Comparable
{
    private String fqClassName;
    // pre-compute these for efficiency
    private String packageName;
    private String className;

    public ImportStatement(String fqClassName)
    {
        this.fqClassName = fqClassName;
        int lastDot = fqClassName.lastIndexOf(".");
		className = fqClassName.substring(lastDot + 1);
		// ImportStatements.add() will check for null package.
		// everyone else can assume it's non-null by the time they see it.
		packageName = (lastDot == -1) ? null : fqClassName.substring(0, lastDot);
    }

    public String getFullyQualifiedClassName()
    {
        return fqClassName;
    }

    public int hashCode()
    {
        return fqClassName.hashCode();
    }

    public String getPackage()
    {
        return packageName;
    }

    public String getClassName()
    {
        return className;
    }

    public boolean equals(Object other)
    {
        if(!(other instanceof ImportStatement) || other == null) {
            return false;
        }
        ImportStatement otherStmt = (ImportStatement)other;
        return otherStmt.getFullyQualifiedClassName().equals(fqClassName);
    }

    public boolean isInStdJavaLibrary()
    {
        return fqClassName.startsWith("java.");
    }

    public boolean isInStdJavaExtensionLibrary()
    {
        return fqClassName.startsWith("javax.");
    }

    public StringBuffer getFormattedStmt()
    {
        StringBuffer result = new StringBuffer();
        result.append(ImportStatements.MARKER);
        result.append(" ");
        result.append(fqClassName);
        result.append(";");
        result.append(ImportScrubber.LINE_SEPARATOR);
        return result;
    }

    // Comparable
    public int compareTo(Object in)
    {
        if(in == null) {
            throw new IllegalArgumentException("Can't compare this to null!");
        }
        // If they are the same then simple comparison will suffice.  If not,
        // we'd like to put something like java.awt.Toolkit before
        // java.awt.print.Pageable which a simple comparison won't do.
        ImportStatement otherImport = (ImportStatement)in;

        if (!getPackage().equals(otherImport.getPackage())) {
            if (getPackage().startsWith(otherImport.getPackage())) {
                return 1;

            } else if (otherImport.getPackage().startsWith(getPackage())) {
                return -1;
            }
        }

        // same package, so just do simple comparison
        return fqClassName.toLowerCase().compareTo(otherImport.getFullyQualifiedClassName().toLowerCase());
    }
}

