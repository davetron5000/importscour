package net.sourceforge.importscrubber.ant;


import net.sourceforge.importscrubber.IProgressMonitor;
import net.sourceforge.importscrubber.ScrubTask;
import net.sourceforge.importscrubber.format.IStatementFormat;
import net.sourceforge.importscrubber.format.StatementFormatFactory;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.io.File;


/**
 * Example:
 *
 * <PRE>
 * <importscrubber root="/home/tom/project/src" recurse="true" verbose="true"/>
 * </PRE>
 */

public class ImportScrubberTask extends Task {

    private boolean _verbose;

    private boolean _recurse;

    private String _rootString;

    private String _formatID;

    private boolean _sortjavalibshigh;

    private String _encoding;

    public void setVerbose(boolean verbose) {

        _verbose = verbose;

    }


    public void setSortjavalibshigh(boolean sortjavalibshigh) {

        _sortjavalibshigh = sortjavalibshigh;

    }


    public void setRecurse(boolean recurse) {

        _recurse = recurse;

    }

    public void setRoot(String rootString) {

        _rootString = rootString;

    }


    public void setFormat(String format) {

        _formatID = format;

    }

    public void setEncoding(String encoding) {
        _encoding = encoding;
    }

    public void execute() throws BuildException {

        if (_rootString == null || _rootString.length() == 0) {

            throw new BuildException("You must set a root for the ImportScrubber task to work");

        }


        if (_formatID == null) {

            _formatID = StatementFormatFactory.DEFAULT;

        }


        File root = new File(_rootString);

        if (!root.exists()) {

            throw new BuildException("The root " + _rootString + " does not exist");

        }


        try {

            net.sourceforge.importscrubber.ImportScrubber scrubber = new net.sourceforge.importscrubber.ImportScrubber();

            IStatementFormat format = StatementFormatFactory.getInstance().createStatementFormat(_formatID);

            format.sortJavaLibsHigh(_sortjavalibshigh);

            scrubber.setFormat(format);

            scrubber.setFileRoot(_rootString, _recurse);

            log("Building file list");

            scrubber.getFiles();

            log("Processing " + scrubber.getFiles().size() + " files");

            scrubber.buildTasks(_encoding);

            scrubber.runTasks(new IProgressMonitor() {

                public void taskStarted(ScrubTask task) {
                }

                public void taskComplete(ScrubTask task) {
                }

            });

        } catch (Exception ex) {

            throw new BuildException(ex);

        }

    }


}
