package net.sourceforge.importscrubber;

import net.sourceforge.importscrubber.format.IStatementFormat;
import java.io.*;

/** * Encapsulates operations on the Java source code file * * TODO: refactor this into a parser class - this constructor is getting heinous */

public class SourceFile {
    private File _file;
    private PackageStmt _package;
    private String _classBody;
    private ImportStatements _imports;
    private String _firstCommentHeader;
    private String _secondCommentHeader;
    private String _encoding;

    public SourceFile(File file) throws IOException {
        this(file, null);
    }
    public SourceFile(File file, String encoding) throws IOException {
        _file = file;
        _imports = new ImportStatements();
        _encoding = 
            (encoding == null ? System.getProperty("file.encoding") : encoding);

        // read in the source
        FileInputStream inputstream = new FileInputStream(_file);
        InputStreamReader reader = new InputStreamReader(inputstream, _encoding);
        BufferedReader buff = new BufferedReader(reader);
        StringBuffer classBodyBuffer = new StringBuffer((int) file.length());
        StringBuffer firstCommentHeaderBuffer = new StringBuffer();
        StringBuffer secondCommentHeaderBuffer = new StringBuffer();
        String currentLine = null;
        boolean passedFirstCommentHeader = false;
        boolean passedSecondCommentHeader = false;

        while ((currentLine = buff.readLine()) != null) {

            // discard imports            if (currentLine.startsWith(ImportStatements.MARKER)) {
                passedFirstCommentHeader = true;
                passedSecondCommentHeader = true;
                continue;
            }
            // check for package stmt            if (currentLine.startsWith(PackageStmt.MARKER)) {
                passedFirstCommentHeader = true;
                _package = new PackageStmt(currentLine);
                continue;
            }
            // TODO - what happens if there are no import statements and no package header?            // preserve comment headers, if any exist            if (!passedFirstCommentHeader) {
                firstCommentHeaderBuffer.append(currentLine);
                firstCommentHeaderBuffer.append(ImportScrubber.LINE_SEPARATOR);
                continue;
            }

            if (passedFirstCommentHeader && !passedSecondCommentHeader) {
                secondCommentHeaderBuffer.append(currentLine);
                secondCommentHeaderBuffer.append(ImportScrubber.LINE_SEPARATOR);
                continue;
            }
            // everything else is part of the class body            passedSecondCommentHeader = true;
            classBodyBuffer.append(currentLine);
            classBodyBuffer.append(ImportScrubber.LINE_SEPARATOR);
        }

        if (_package == null) {
            _package = new PackageStmt();
        }

        buff.close();
        reader.close();
        inputstream.close();
        if (ImportScrubber.DEBUG) System.out.println("Done parsing source code file " + file.getAbsolutePath());
        _classBody = classBodyBuffer.toString();
        _firstCommentHeader = firstCommentHeaderBuffer.toString();
        _secondCommentHeader = secondCommentHeaderBuffer.toString();
    }


    public void addImport(String className) {
        _imports.add(className);
    }


    public void save(IStatementFormat format) throws IOException {
        _imports.removeUnreferenced(_classBody);
        _imports.removeLocalToPackage(_package);        // push everything together        StringBuffer finishedSource = new StringBuffer();
        finishedSource.append(_firstCommentHeader);
        finishedSource.append(_package.getOutput());
        finishedSource.append(removeMultipleBlankLines(_secondCommentHeader));
        finishedSource.append(_imports.getOutput(format));
       _classBody = ImportScrubber.LINE_SEPARATOR + removeMultipleBlankLines(_classBody);
        finishedSource.append(_classBody);        // write it to disk
        BufferedWriter writer = 
            new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream(_file),
                    _encoding
                    )
                );
        writer.write(finishedSource.toString());
        writer.close();
    }

    private String removeMultipleBlankLines(String in) {
        while (in.startsWith(ImportScrubber.LINE_SEPARATOR)) {
            in = in.substring(ImportScrubber.LINE_SEPARATOR.length());
        }
        return in;
    }
}
