package net.sourceforge.importscrubber;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;

/**
 * This class encapsulates operations on the Java source code file
 */
public class SourceFile {

	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private File _file;
	private PackageStmt _package;
	private String _classBody;
	private ImportStatements _imports;
	
	public SourceFile(File file) throws IOException {
		// hang on to the file handle
		_file = file;
		
		_imports = new ImportStatements();
		
		// read in the source
		if (ImportScrubber.DEBUG) System.out.println("Beginning to parse source code file " + file.getAbsolutePath());
		FileReader reader = new FileReader(_file);
		BufferedReader buff = new BufferedReader(reader);
		StringBuffer classBodyBuffer = new StringBuffer((int)file.length());
		String currentLine = null;
		while ((currentLine = buff.readLine()) != null) {
			// check for package stmt
			if (currentLine.startsWith(PackageStmt.MARKER)) {
				_package = new PackageStmt(currentLine);
				if (ImportScrubber.DEBUG) System.out.println("Found package stmt: " + _package);
				continue;
			}
			// discard imports
			if (currentLine.startsWith(ImportStatements.MARKER)) {
				if (ImportScrubber.DEBUG) System.out.println("Skipping import stmt: " + currentLine);
				continue;
			}
			// accept everything else
			classBodyBuffer.append(currentLine + LINE_SEPARATOR);
		}
		if (_package == null) {
			_package = new PackageStmt();
		}
		buff.close();
		reader.close();
		if (ImportScrubber.DEBUG) System.out.println("Done parsing source code file " + file.getAbsolutePath());
		
		_classBody = classBodyBuffer.toString();
	}
	
	public void addImport(String className) {
		_imports.addImport(className);
	}
	
	public void writeSourceToDisk() throws IOException {
		if (ImportScrubber.DEBUG) System.out.println("Writing source file to disk");
		_imports.removeUnreferenced(_classBody);

		if (ImportScrubber.DEBUG) System.out.println("Removing imports local to this package");
		_imports.removeLocalToPackage(_package);
		
		// push everything together
		if (ImportScrubber.DEBUG) System.out.println("Concatenating source code elements");
		StringBuffer finishedSource = new StringBuffer();
		finishedSource.append(_package.getOutput());
		finishedSource.append(_imports.getOutput());

		// remove blank lines
		while (_classBody.substring(0, 2).equals("\r\n")) {
			_classBody = _classBody.substring(2);
		}
		
		// add a blank line
		_classBody = "\r\n" + _classBody;
		
		// add class body
		finishedSource.append(_classBody);
		
		// write it to disk
		if (ImportScrubber.DEBUG) System.out.println("Writing source code to disk");
		FileWriter writer = new FileWriter(_file);
		writer.write(finishedSource.toString());
		writer.close();
	}
}
