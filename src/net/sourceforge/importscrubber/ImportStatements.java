package net.sourceforge.importscrubber;

import java.util.*;

/**
 * This class encapsulates various operations on a class' import statements
 */
public class ImportStatements {
	
	public static final String MARKER = "import ";

	private Vector _stmts = new Vector();
	
	public void addImport(String className) {
		if (!_stmts.contains(className)) {
			_stmts.addElement(className);
		}
	}
	
	public StringBuffer getOutput() {
		boolean swap = true;
		while (swap) {
			swap = false;
			for (int i=0; i<_stmts.size(); i++) {
				String firstStmt = (String)_stmts.elementAt(i);
				for (int j=i; j<_stmts.size(); j++) {
					String secondStmt = (String)_stmts.elementAt(j);
					if (firstStmt.compareTo(secondStmt) > 0) {
						_stmts.removeElementAt(j);
						_stmts.insertElementAt(firstStmt, j);
						_stmts.removeElementAt(i);
						_stmts.insertElementAt(secondStmt, i);
						swap = true;
						break;
					} 
				}
			}
		}
		StringBuffer result = new StringBuffer(_stmts.size() * 64);
		for (Enumeration e = _stmts.elements(); e.hasMoreElements();) {
			result.append(ImportStatements.MARKER);
			result.append((String)e.nextElement());
			result.append(";");
			result.append(System.getProperty("line.separator"));
		}
		return result;
	}
	
	/**
	 * Remove imports for classes in the same package as the current class
	 */
	public void removeLocalToPackage(PackageStmt packageStmt) {
		for (int i=0; i<_stmts.size(); i++) {
			String importStmt = (String)_stmts.elementAt(i);
			// get the package of this import
			int lastDot = importStmt.lastIndexOf(".");
			String importPkg = importStmt.substring(0, lastDot);
			if (packageStmt.isInSamePackageAs(importPkg)) {
			if (ImportScrubber.DEBUG) System.out.println("Removing import from same package:" + importStmt);
				_stmts.removeElementAt(i);
				i--;
			}
		}
	}

	/**
	 * Remove those imports which appear in the class file 
	 * byte code but are not directly referenced in the source code
	 */
	public void removeUnreferenced(String classBody) {
		for (int i=0; i<_stmts.size(); i++) {
			// get each imported class
			String importStmt = (String)_stmts.elementAt(i);
			// get the class name
			int lastDot = importStmt.lastIndexOf(".");
			String className = importStmt.substring(lastDot + 1, importStmt.length());
			// is that class name mentioned somewhere in the class?
			if (classBody.indexOf(className) == -1) {
				// nope, so remove that import
				if (ImportScrubber.DEBUG) System.out.println("Removing unnecessary import:" + className);
				_stmts.removeElementAt(i);
				i--;
			}
		}
	}
	
	
}
