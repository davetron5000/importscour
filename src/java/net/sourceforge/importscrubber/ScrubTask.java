package net.sourceforge.importscrubber;

import java.io.File;
import java.io.IOException;

import java.util.Iterator;

/**
 * Encapsulates the data needed to clean up the import statements of one file
 */
public class ScrubTask implements IReferenceFoundListener
{
    private SourceFile _sourceFile;
    private final FilePair _pair;
    private StatementFormat _format;
	private String _encoding;

    public ScrubTask(FilePair pair, StatementFormat format, String encoding) throws IOException
    {
        _pair = pair;
        _format = format;
		_encoding = encoding;
    }

    public void run() throws IOException
    {
        _sourceFile = new SourceFile(_pair.getSourceFile(), _encoding);
        for (Iterator iter = _pair.getClassFiles(); iter.hasNext();) {
            ClassParserWrapper.parse((File)iter.next(), this);
        }
        _sourceFile.save(_format);
    }

    public void referenceFound(String className)
    {
        _sourceFile.addImport(className);
    }

    public String toString()
    {
        return getSourcePath();
    }

    public String getSourcePath()
    {
        return _pair.getSourceFile().getAbsolutePath();
    }

    public static void main(String[] args)
    {
        FilePair pair = new FilePair(new File("d:\\importscrubber\\tmp\\NodeListener.java"), new File("d:\\importscrubber\\tmp\\NodeListener.class"));
        for (Iterator iter = pair.getClassFiles(); iter.hasNext();) {
            try {
                ClassParserWrapper.parse((File)iter.next(), new PrintListener());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
