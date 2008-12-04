package net.sourceforge.importscrubber;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

/**
 * Encapsulates operations on the Java source code file
 * 
 * TODO: refactor this into a parser class - this constructor is getting heinous
 */
public class SourceFile
{
    private File _file;
	private String _encoding;
    private PackageStmt _package;
    private String _classBody;
    private ImportStatements _imports  = new ImportStatements();
    private String _firstCommentHeader;
    private String _secondCommentHeader;

    public SourceFile(File file, String encoding) throws IOException
    {
        _file = file;
		_encoding = encoding;

        // read in the source
		FileInputStream inputstream = new FileInputStream(_file);
		InputStreamReader reader = null;
        if (_encoding != null) {
            reader = new InputStreamReader(inputstream, _encoding);
        } else {
            reader = new InputStreamReader(inputstream);
        }
        BufferedReader buff = new BufferedReader(reader);

        StringBuffer classBodyBuffer = new StringBuffer((int)file.length());
        StringBuffer firstCommentHeaderBuffer = new StringBuffer();
        StringBuffer secondCommentHeaderBuffer = new StringBuffer();

        String currentLine = null;
        boolean passedFirstCommentHeader = false;
        boolean passedSecondCommentHeader = false;

        while ((currentLine = buff.readLine()) != null) {
            // discard imports
            if (currentLine.startsWith(ImportStatements.MARKER)) {
                passedFirstCommentHeader = true;
                passedSecondCommentHeader = true;
                continue;
            }

            // check for package stmt
            if (currentLine.startsWith(PackageStmt.MARKER)) {
                passedFirstCommentHeader = true;
                _package = new PackageStmt(currentLine);
                continue;
            }

            // TODO - what happens if there are no import statements and no package header?
            // preserve comment headers, if any exist
            if(!passedFirstCommentHeader) {
                firstCommentHeaderBuffer.append(currentLine);
                firstCommentHeaderBuffer.append(ImportScrubber.LINE_SEPARATOR);
                continue;
            }

            if(passedFirstCommentHeader && !passedSecondCommentHeader) {
                secondCommentHeaderBuffer.append(currentLine);
                secondCommentHeaderBuffer.append(ImportScrubber.LINE_SEPARATOR);
                continue;
            }

            // everything else is part of the class body
            passedSecondCommentHeader = true;
            classBodyBuffer.append(currentLine);
            classBodyBuffer.append(ImportScrubber.LINE_SEPARATOR);
        }

        if (_package == null) {
            _package = new PackageStmt();
        }

        buff.close();
		inputstream.close();
        reader.close();
        if (ImportScrubber.DEBUG)
            System.out.println("Done parsing source code file " + file.getAbsolutePath());

        _classBody = classBodyBuffer.toString();
        _firstCommentHeader = firstCommentHeaderBuffer.toString();
        _secondCommentHeader = secondCommentHeaderBuffer.toString();
    }

    public void addImport(String className)
    {
        try
		{
			_imports.add(className);
		} catch (Exception e) {
			// funky classnames will result in ImportStatement throwing StringIndexOutOfBoundsException
			System.out.println("Non-fatal, but unexpected error while processing " + className + " in " + _file + " --");
			e.printStackTrace();
		}
    }

    public void save(StatementFormat format) throws IOException
    {
        if (ImportScrubber.DEBUG)
            System.out.println("Imports before pruning:" + _imports.getCount());
        String maybeError = _imports.removeUnreferenced(_classBody);
        if (maybeError != null) {
            System.err.println("Skipping " + _file.getPath() + ": " + maybeError);
            return;
        }
        _imports.removeLocalToPackage(_package);
		int extIndex = _file.getName().length() - ".java".length();
		String thisClass = _file.getName().substring(0, extIndex);
		// doublecheck
		if (_file.getName().substring(extIndex).compareToIgnoreCase(".java") != 0) {
			System.err.println("Warning: not a .java file; can't prune inner classes");
		} else {
			_imports.removeInnerClasses(_package.getPkg() + "." + thisClass);
		}

		// we don't overwrite when DEBUG is on
        if (ImportScrubber.DEBUG) {
            System.out.println("Imports remaining:\n" + _imports.getOutput(format));
        } else {
            // push everything together
            StringBuffer finishedSource = new StringBuffer((int)(_classBody.length() * 1.1));
            finishedSource.append(_firstCommentHeader);
            finishedSource.append(_package.getOutput());

            finishedSource.append(removeMultipleBlankLines(_secondCommentHeader));
            finishedSource.append(_imports.getOutput(format));

            _classBody = ImportScrubber.LINE_SEPARATOR + removeMultipleBlankLines(_classBody);
            finishedSource.append(_classBody);

            // write it to disk
            OutputStreamWriter osw = null;
            if (_encoding != null) {
                osw = new OutputStreamWriter(new FileOutputStream(_file), _encoding);
            } else {
                osw = new OutputStreamWriter(new FileOutputStream(_file));
            }

			BufferedWriter writer = new BufferedWriter(osw);
            writer.write(finishedSource.toString());
            writer.close();
        }
    }

    private String removeMultipleBlankLines(String in)
    {
        while (in.startsWith(ImportScrubber.LINE_SEPARATOR)) {
            in = in.substring(ImportScrubber.LINE_SEPARATOR.length());
        }
        return in;
    }
}
