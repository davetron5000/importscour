package net.sourceforge.importscrubber.ant;


import net.sourceforge.importscrubber.IProgressMonitor;
import net.sourceforge.importscrubber.ScrubTask;
import net.sourceforge.importscrubber.NullProgressMonitor;
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
    private boolean verbose;
    private boolean recurse;
    private String rootString;
    private String formatID;
    private boolean sortjavalibshigh;
    private String encoding;
    public void setVerbose(boolean verbose) {
       verbose = verbose;
    }

    public void setSortjavalibshigh(boolean sortjavalibshigh) {
        sortjavalibshigh = sortjavalibshigh;
    }

   public void setRecurse(boolean recurse) {
        recurse = recurse;
    }
    public void setRoot(String rootString) {
        rootString = rootString;
    }

   public void setFormat(String format) {
        formatID = format;
    }
    public void setEncoding(String encoding) {
       encoding = encoding;
   }
    public void execute() throws BuildException {
        if (rootString == null || rootString.length() == 0) {
            throw new BuildException("You must set a root for the ImportScrubber task to work");
        }

       if (formatID == null) {
            formatID = StatementFormatFactory.DEFAULT;
        }

       File root = new File(rootString);
        if (!root.exists()) {
            throw new BuildException("The root " + rootString + " does not exist");
        }

       try {
            net.sourceforge.importscrubber.ImportScrubber scrubber = new net.sourceforge.importscrubber.ImportScrubber();
            IStatementFormat format = StatementFormatFactory.getInstance().createStatementFormat(formatID);
            format.sortJavaLibsHigh(sortjavalibshigh);
            scrubber.setFormat(format);
            scrubber.setFileRoot(rootString, recurse);
            log("Building file list");
            scrubber.getFiles();
            log("Processing " + scrubber.getFiles().size() + " files");
            scrubber.buildTasks(encoding);
            scrubber.runTasks(new NullProgressMonitor());
        } catch (Exception ex) {
            throw new BuildException(ex);
        }
    }
}
