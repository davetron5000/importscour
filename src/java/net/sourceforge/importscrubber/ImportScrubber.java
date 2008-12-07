package net.sourceforge.importscrubber;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * This class encapsulates the import scrubber controller
 */
public class ImportScrubber
{
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static boolean DEBUG = false;
    private FileChooser _fileChooser;
    private List<ScrubTask> _tasks = new ArrayList<ScrubTask>();
    private StatementFormat _format;
	private String _encoding;

	public ImportScrubber(String encoding)
	{
		_encoding = encoding;
	}

    public void setFileRoot(String fileName, String classRoot, boolean recurse) throws IOException
    {
        _fileChooser = new FileChooser(fileName, classRoot, recurse);
    }

    /*
    public static String getDirectory(String fileName)
    {
        File f = new File(fileName);
        if (f.isDirectory()) {
            return fileName;
        } else {
            return f.getParent();
        }
    }
    */

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
        return _fileChooser;
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

    /*
    public static void main(String[] args)
    {
		String encoding = null;
		if (argExists("encoding", args)) {
			encoding = findArg("encoding", args);
		} else {
			encoding = System.getProperty("file.encoding");
		}

        if (!argExists("root", args)) {
            usage();
            System.exit(0);
        }
        String root = findArg("root",args);
        if (!(new File(root).exists())) {
            System.out.println("Root: " + root + " does not exist");
            usage();
            System.exit(0);
        }
        boolean recurse = argExists("recurse", args);

        StatementFormat format = StatementFormat.getFormat(args);

        try {
            ImportScrubber scrubber = new ImportScrubber(encoding);

            if(argExists("classRoot", args) ) {
                String classesRootStr= findArg( "classRoot", args );
                scrubber.setFileRoot(root, classesRootStr, recurse);
            }  else {
                System.out.println("No classRoot specified; assuming " + getDirectory(root));
                scrubber.setFileRoot(root, getDirectory(root), recurse);
            }

            scrubber.setFormat(format);
            System.out.println("Building tasks");
            scrubber.buildTasks(scrubber.getFilesIterator());
            ConsoleProgressMonitor consoleMonitor = new ConsoleProgressMonitor();
            scrubber.runTasks(consoleMonitor);
            System.out.println(LINE_SEPARATOR + "All done!");
            System.out.println(consoleMonitor.getFilesProcessed() + " files processed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
}
