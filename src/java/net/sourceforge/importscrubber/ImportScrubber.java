package net.sourceforge.importscrubber;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * This class encapsulates the import scrubber controller.
 */
public class ImportScrubber
{
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static boolean DEBUG = false;
    private Iterator<FilePair> _filesIterator;
    private List<ScrubTask> _tasks = new ArrayList<ScrubTask>();
    private StatementFormat _format;
	private String _encoding;

    /** Create an ImportScrubber using the given encoding for reading source files.
     * @param encoding the character encoding string, e.g. "UTF-8".  Null means to use the native encoding for the platform on which this
     * is run.
     */
	public ImportScrubber(String encoding)
	{
		_encoding = encoding;
	}

    public void setFileRoot(String fileName, String classRoot, boolean recurse) throws IOException
    {
        _filesIterator = new FileChooser(fileName, classRoot, recurse);
    }

    /** Sets the files to process explicitly.
     * @param sourceRoot the root of the source tree
     * @param classRoot the root of the class tree
     * @param files the files, relative to sourceRoot representing the source files to process
     * @param skipMissingClasses if true, sources with missing classes are skipped.  If false, a missing class causes an exception to be thrown.
     */
    public void setFilesToProcess(String sourceRoot, String classRoot, List<String> files, boolean skipMissingClasses)
    {
        List<FilePair> filePairs = new ArrayList<FilePair>(files.size());
        for (String sourceFile: files)
        {
            try
            {
                filePairs.add(new FilePair(sourceRoot,classRoot,sourceFile));
            }
            catch (IllegalArgumentException e)
            {
                if (!skipMissingClasses)
                    throw e;
            }
        }
        _filesIterator = filePairs.iterator();
    }

    public void setFormat(StatementFormat format)
    {
        _format = format;
    }

    public void debugOff()
    {
        DEBUG = false;
    }

    public void debug()
    {
        DEBUG = true;
    }

    public int getTaskCount()
    {
        return _tasks.size();
    }

    public Iterator<FilePair> getFilesIterator()
    {
        return _filesIterator;
    }

    /** Returns number of files to work on, allows getFiles to be called just once.
     */
    public int buildTasks(Iterator<FilePair> iter) throws IOException
    {
        while (iter.hasNext()) {
            FilePair pair = iter.next();
            _tasks.add(new ScrubTask(pair, _format, _encoding));
        }

        return _tasks.size();
    }

    public void runTasks(IProgressMonitor monitor) throws IOException
    {
        for (ListIterator iter = _tasks.listIterator(); iter.hasNext();) {
            ScrubTask task = (ScrubTask)iter.next();
            monitor.taskStarted(task);
            task.run();
            monitor.taskComplete(task);
        }
        _tasks.clear();
    }
}
