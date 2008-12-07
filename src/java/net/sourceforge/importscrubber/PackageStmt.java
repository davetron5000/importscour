package net.sourceforge.importscrubber;

/**
 * Encapsulates operations on a class' package statement.
 */
public class PackageStmt
{
    public static final String MARKER = "package ";

    private final String stmt;
    private final boolean isInDefaultPackage;
    private final String myPkg;

    /**
     * Creates a new PackageStatement that's not in the default package.
     * 
     * @param stmt
     */
    public PackageStmt(String stmt)
    {
        this(stmt, false);
    }

    /**
     * Creates a new PackageStatement that's in the default package.
     */
    public PackageStmt()
    {
        this(null, true);
    }

    private PackageStmt(String stmt, boolean inDefaultPackage)
    {
        this.stmt = stmt;
        isInDefaultPackage = inDefaultPackage;
        if (!isInDefaultPackage) {
            int firstSpace = this.stmt.indexOf(" ") + 1;
            int semiColon = this.stmt.indexOf(";");
            myPkg = this.stmt.substring(firstSpace, semiColon);

        } else {
            myPkg = null;
        }
    }

	public String getPkg()
	{
		return myPkg;
	}

    public boolean isInSamePackageAs(ImportStatement importStmt)
    {
        if (isInDefaultPackage) {
            return false; // default-package imports have already been removed
        }
        return importStmt.getPackage().compareTo(myPkg) == 0;
    }

    public StringBuffer getOutput()
    {
        StringBuffer buffer = new StringBuffer(64);
        if (isInDefaultPackage) {
            return buffer;
        }
        buffer.append(stmt);
        buffer.append(ImportScrubber.LINE_SEPARATOR);
        buffer.append(ImportScrubber.LINE_SEPARATOR);
        return buffer;
    }
}






