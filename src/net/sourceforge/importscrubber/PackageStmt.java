package net.sourceforge.importscrubber;

/**
 * Encapsulates operations on a class' package statement.
 */
public class PackageStmt
{
    public static final String MARKER = "package ";

    private final String _stmt;
    private final boolean _isInDefaultPackage;
    private final String _myPkg;

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
        _stmt = stmt;
        _isInDefaultPackage = inDefaultPackage;
        if (!_isInDefaultPackage) {
            int firstSpace = _stmt.indexOf(" ") + 1;
            int semiColon = _stmt.indexOf(";");
            _myPkg = _stmt.substring(firstSpace, semiColon);
        } else {
            _myPkg = null;
        }
    }

	public String getPkg()
	{
		return _myPkg;
	}

    public boolean isInSamePackageAs(ImportStatement importStmt)
    {
        if (_isInDefaultPackage) {
            return false; // default-package imports have already been removed
        }
        return importStmt.getPackage().compareTo(_myPkg) == 0;
    }

    public StringBuffer getOutput()
    {
        StringBuffer buffer = new StringBuffer(64);
        if (_isInDefaultPackage) {
            return buffer;
        }
        buffer.append(_stmt);
        buffer.append(ImportScrubber.LINE_SEPARATOR);
        buffer.append(ImportScrubber.LINE_SEPARATOR);
        return buffer;
    }
}


