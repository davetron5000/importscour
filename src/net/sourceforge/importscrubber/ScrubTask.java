package net.sourceforge.importscrubber;


import net.sourceforge.importscrubber.format.IStatementFormat;

import java.io.File;
import java.io.IOException;
import java.util.ListIterator;


/**
 * Encapsulates the data needed to clean up the import statements of one file
 */

public class ScrubTask implements IReferenceFoundListener {

    private SourceFile _sourceFile;

    private final FilePair _pair;

    private IStatementFormat _format;

    private String _encoding;

    public ScrubTask(FilePair pair, IStatementFormat format) throws IOException {
        this(pair, format, null);
    }
    public ScrubTask(FilePair pair, IStatementFormat format, String encoding)
        throws IOException {
        _pair = pair;

        _format = format;

        _encoding = encoding;
    }


    public void run() throws IOException {

        _sourceFile = new SourceFile(_pair.getSourceFile(), _encoding);

        for (ListIterator iter = _pair.getClassFiles(); iter.hasNext();) {

            ClassParserWrapper.parse((File) iter.next(), this);

        }

        _sourceFile.save(_format);

    }


    public void referenceFound(String className) {

        _sourceFile.addImport(className);

    }


    public String toString() {

        return getSourcePath();

    }


    public String getSourcePath() {

        return _pair.getSourceFile().getAbsolutePath();

    }


    public static void main(String[] args) {

        FilePair pair = new FilePair(new File("d:\\importscrubber\\tmp\\NodeListener.java"), new File("d:\\importscrubber\\tmp\\NodeListener.class"));

        for (ListIterator iter = pair.getClassFiles(); iter.hasNext();) {

            try {

                ClassParserWrapper.parse((File) iter.next(), new PrintListener());

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    }

}
