package net.sourceforge.importscrubber;


import net.sourceforge.importscrubber.filechooser.*;
import net.sourceforge.importscrubber.format.IStatementFormat;
import net.sourceforge.importscrubber.format.StatementFormatFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


/**

 * This class encapsulates the import scrubber controller

 */

public class ImportScrubber {

    public static final String START_DIRECTORY_KEY = "importscrubber.startDir";
    public static final String RECURSE = "importscrubber.recurse";
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static boolean DEBUG = false;
    private IFileChooser _fileChooser;
    private List _tasks = new ArrayList();
    private IStatementFormat _format;

    public void setFileRoot(String filename, boolean recurse) {
        File file = new File(filename);
        if (file.isDirectory()) {
            if (recurse) {
                _fileChooser = new RecursiveFileChooser();
            } else {
                _fileChooser = new AllInDirectoryFileChooser();
            }
        } else {
            _fileChooser = new SingleFileChooser();
        }
        _fileChooser.setRoot(filename);
    }

   public void setFileRoot(String sourceRoot, String classRoot, String fileName) {
        _fileChooser = new DualRootSingleFileChooser(sourceRoot, classRoot, fileName);
    }

   public void setFormat(IStatementFormat format) {
        _format = format;
    }

   public void debugOff() {
        DEBUG = false;
    }

   public void debug() {
        DEBUG = true;
    }

   public int getTaskCount() {
        return _tasks.size();
    }

   public List getFiles() {
        return _fileChooser.getFiles();
    }

   // Returns number of files to work on, allows getFiles to be called just once.
   public int buildTasks() throws IOException {
       return buildTasks(null);
   }

   public int buildTasks(String encoding) throws IOException {
        List list = _fileChooser.getFiles();
        for (ListIterator iter = list.listIterator(); iter.hasNext();) {
            FilePair pair = (FilePair) iter.next();
            _tasks.add(new ScrubTask(pair, _format, encoding));
        }
        return list.size();
    }

   public void runTasks(IProgressMonitor monitor) throws IOException {
        for (ListIterator iter = _tasks.listIterator(); iter.hasNext();) {
            ScrubTask task = (ScrubTask) iter.next();
            monitor.taskStarted(task);
            task.run();
            monitor.taskComplete(task);
        }
        _tasks.clear();
    }


    public static void main(String[] args) {
        if (argExists("g", args)) {
            new ImportScrubberGUI();
            return;

        }

        if (!argExists("root", args)) {
            usage();
            System.exit(0);
        }

        String root = findArg("root", args);

        if (!(new File(root).exists())) {
            System.out.println("Root: " + root + " does not exist");
            usage();
            System.exit(0);
        }

        boolean recurse = argExists("recurse", args);
        IStatementFormat format = null;
        if (!argExists("format", args)) {
            format = StatementFormatFactory.getInstance().createStatementFormat(StatementFormatFactory.DEFAULT);
        } else {
            format = StatementFormatFactory.getInstance().createStatementFormat(findArg("format", args));
        }

        format.sortJavaLibsHigh(argExists("sortjavalibshigh", args));

        String encoding = null;
        if (argExists("encoding", args)) {
            encoding = findArg("encoding", args);
        }

        try {
            ImportScrubber scrubber = new ImportScrubber();
            if (argExists("classesRoot", args)) {
                String sourcesRootStr = findArg("sourcesRoot", args);
                String classesRootStr = findArg("classesRoot", args);
                String sourceFilenameStr = root;
                scrubber.setFileRoot(sourcesRootStr, classesRootStr, sourceFilenameStr);
            } else {
                scrubber.setFileRoot(root, recurse);
            }


            scrubber.setFormat(format);
            System.out.println("Building file list");
            List files = scrubber.getFiles();
            System.out.println("Building tasks");
            scrubber.buildTasks(encoding);
            System.out.println("Processing " + files.size() + " files");
            scrubber.runTasks(new ConsoleProgressMonitor());
            System.out.println(LINE_SEPARATOR + "All done!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean argExists(String argFlag, String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-" + argFlag)) {
                return true;
            }
        }
        return false;
    }

    private static String findArg(String argFlag, String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-" + argFlag)) {
                return args[i + 1];
            }
        }
        throw new IllegalArgumentException("Couldn't find " + argFlag);
    }

    private static void usage() {
        System.out.println("Usage: java net.sourceforge.importscrubber.ImportScrubber -root [rootDir | file] [-recurse] [-format each|top|nobreaks] [-encoding charsetname] [-g]");
        System.out.println("Ex: java net.sourceforge.importscrubber.ImportScrubber -root /home/me/myproject/src -recurse -format nobreaks -sortjavalibshigh");
        System.out.println("Ex: java net.sourceforge.importscrubber.ImportScrubber -root d:\\project\\src\\Foo.java");
        System.out.println("Ex: java net.sourceforge.importscrubber.ImportScrubber -root d:\\importscrubber\\etc\\FunctionalTest.java -classesRoot d:\\importscrubber\\build -sourcesRoot d:\\importscrubber\\etc\\ ");
        System.out.println("\r\nOR, TO USE THE GUI:\r\n");
        System.out.println("java net.sourceforge.importscrubber.ImportScrubber -g");
    }
}



