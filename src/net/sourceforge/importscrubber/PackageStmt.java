package net.sourceforge.importscrubber;

/**
 * This class encapsulates some operations on the package statement of a class
 */
public class PackageStmt {
	
	public static final String MARKER = "package ";
	
	private String _stmt;
	private boolean _isInDefaultPackage;
	
	public PackageStmt(String stmt) {
		_stmt = stmt;
	}
	
	public PackageStmt() {
		_isInDefaultPackage = true;
	}
	
	public String getStmt() {
		return _stmt;
	}
	
	public boolean isInSamePackageAs(String other) {
		if (_isInDefaultPackage) {
			return other.indexOf(".") == -1;
		}
		return _stmt.substring(_stmt.indexOf(" ") + 1, _stmt.indexOf(";")).equals(other);	
	}
	
	public StringBuffer getOutput() {
		StringBuffer buffer = new StringBuffer();
		if (_isInDefaultPackage) {
			return buffer;
		}
		buffer.append(_stmt);
		buffer.append(SourceFile.LINE_SEPARATOR);
		buffer.append(SourceFile.LINE_SEPARATOR);
		return buffer;
	}
}
