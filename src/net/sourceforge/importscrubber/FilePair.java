package net.sourceforge.importscrubber;

import java.io.File;

/**
 * This class encapsulates the source & class files
 */
public class FilePair {
	
	private final File _sourceFile;
	private final File _classFile;
	
	public FilePair(File sourceFile, File classFile) {
		_sourceFile = sourceFile;
		_classFile = classFile;
	}
	
	public File getSourceFile() {
		return _sourceFile;
	}

	public File getClassFile() {
		return _classFile;
	}
}
