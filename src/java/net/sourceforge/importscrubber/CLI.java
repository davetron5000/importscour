package net.sourceforge.importscrubber;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/** Command line interface to {@link ImportScrubber}.
 *
 */
public class CLI
{
    private static final String PROPERTIES_ENVIRONMENT_VARIABLE = "IMPORTSCOUR_PROPERTIES";

    private static final String PROPNAME_JAVA_LIBS_HIGH = "importscour.javalibshigh";
    private static final String PROPNAME_BREAK_STYLE = "importscour.breakstyle";
    private static final String PROPNAME_COMBINE_THRESHOLD = "importscour.combinethreshold";
    private static final String PROPNAME_THRESHOLD_STANDARD = "importscour.threasholdstandard";

    private CLI() {}

    /** Runs the CLI version of ImportScrubber.
     * The command line arguments are pretty simple:
     * <ul>
     * <li>The first argument is the full path to the root of the classes directory</li>
     * <li>The second argument is the full path to the root of the source directory</li>
     * <li>If the third argument is "ALL", all classes in the source directory are scheduled for processing</li>
     * <li>Otherwise, all remaining arguments are assumed to be paths to source files for processing, relative to the 
     * second argument</li>
     * </ul>
     */
    public static void main(String args[])
        throws IOException
    {
        if (args.length < 3)
        {
            System.err.println("Usage: " + CLI.class.getName() + " classes_root source_root [relative/path/to/JavaFile.java]*");
            System.err.println("OR");
            System.err.println("Usage: " + CLI.class.getName() + " classes_root source_root ALL");
            System.exit(-1);
        }
        String classesRoot = args[0];
        String sourceRoot = args[1];
        boolean processAll;
        List<String> sources = new ArrayList<String>(args.length - 2);
        if (args[2].equals("ALL"))
        {
            System.out.println("Processing all sources we can in " + sourceRoot);
            processAll = true;
        }
        else
        {
            processAll = false;
            sources = new ArrayList<String>(args.length - 2);
            System.out.println("Processing only:");
            for (int i=2;i<args.length; i++)
            {
                System.out.println("\t" + args[i]);
                sources.add(args[i]);
            }
        }
        StatementFormat format = createStatementFormat();
        System.out.println();
        System.out.println(format.toString());

        ImportScrubber scrubber = new ImportScrubber(null);
        if (processAll)
        {
            scrubber.setFileRoot(sourceRoot,classesRoot,true);
        }
        else
        {
            scrubber.setFilesToProcess(sourceRoot,classesRoot,sources);
        }
        scrubber.setFormat(format);
        scrubber.buildTasks(scrubber.getFilesIterator());
        ConsoleProgressMonitor consoleMonitor = new ConsoleProgressMonitor();
        scrubber.runTasks(consoleMonitor);
        System.out.println();
        System.out.println("All done!");
        System.out.println(consoleMonitor.getFilesProcessed() + " files processed");
    }

    /** Creates the statement format based on the user's environment and configuration */
    private static StatementFormat createStatementFormat()
        throws IOException
    {
        Properties properties = loadProperties();
        return new StatementFormat(
                "true".equals(properties.getProperty(PROPNAME_JAVA_LIBS_HIGH)),
                "package".equals(properties.getProperty(PROPNAME_BREAK_STYLE)) ? StatementFormat.BREAK_EACH_PACKAGE : StatementFormat.BREAK_NONE,
                Integer.parseInt(properties.getProperty(PROPNAME_COMBINE_THRESHOLD)),
                "true".equals(properties.getProperty(PROPNAME_THRESHOLD_STANDARD))
                );
    }

    private static Properties loadProperties()
        throws IOException
    {
        String propertiesFileName;
        boolean failIfNoExist;
        if (System.getenv(PROPERTIES_ENVIRONMENT_VARIABLE) != null)
        {
            propertiesFileName = System.getenv(PROPERTIES_ENVIRONMENT_VARIABLE);
            failIfNoExist = true;
        }
        else
        {
            propertiesFileName = System.getProperty("user.home") + File.separator + ".importscour.properties";
            failIfNoExist = false;
        }

        File propertiesFile = new File(propertiesFileName);

        Properties properties = initializeProperties();

        if (!propertiesFile.exists())
        {
            if (failIfNoExist)
            {
                throw new FileNotFoundException(propertiesFileName + " doesn't exist");
            }
        }
        else
        {
            properties.load(new FileInputStream(propertiesFile));
        }
        return properties;
    }

    /** Initializes the properties file with default values.
     */
    private static Properties initializeProperties()
    {
        Properties properties = new Properties();
        properties.setProperty(PROPNAME_JAVA_LIBS_HIGH,System.getProperty(PROPNAME_JAVA_LIBS_HIGH,"true"));
        properties.setProperty(PROPNAME_BREAK_STYLE,System.getProperty(PROPNAME_BREAK_STYLE,"package"));
        properties.setProperty(PROPNAME_COMBINE_THRESHOLD,System.getProperty(PROPNAME_COMBINE_THRESHOLD,"0"));
        properties.setProperty(PROPNAME_THRESHOLD_STANDARD,System.getProperty(PROPNAME_THRESHOLD_STANDARD,"false"));
        return properties;
    }
}
