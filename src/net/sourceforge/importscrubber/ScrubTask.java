package net.sourceforge.importscrubber;

import java.io.File;
import java.io.IOException;

/**
 * This class encapsulates the data needed to clean up the import statements of one file
 */
public class ScrubTask extends Thread implements IReferenceFoundListener  {
	
	private SourceFile _sourceFile;
	private final FilePair _pair;
	
	public ScrubTask(FilePair pair) throws IOException {
		_pair = pair;
	}
	
	public void run() {
		try {
			_sourceFile = new SourceFile(_pair.getSourceFile());
			ClassParserWrapper.parseWithCFParse(_pair.getClassFile(), this);
			_sourceFile.writeSourceToDisk();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void referenceFound(String className) {
		_sourceFile.addImport(className);
	}
	
}
