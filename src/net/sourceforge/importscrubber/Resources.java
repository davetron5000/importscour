package net.sourceforge.importscrubber;


import java.util.ListResourceBundle;


public class Resources extends ListResourceBundle {

    public static final String APP_NAME = "Importscrubber";

    public static final String FILE_BROWSER_TITLE = "Note: class files and source code files must be in the same directory";

    public static final String VERSION_ID = APP_NAME + " 1.4.2";

    public static final String BROWSE_LABEL = "Browse";

    public static final String GO_LABEL = "Go";

    public static final String FIND_FILES_LABEL = "Find files";

    public static final String ALL_DONE = "All done!  \r\nYou might want to recompile just to ensure \r\nthat I didn't accidentally remove too many imports\r\n";

    public static final String HELP_LABEL = "Help";

    public static final String FILE_LABEL = "File";

    public static final String ABOUT_LABEL = "About";

    public static final String OPTIONS_LABEL = "Options";

    public static final String RECURSE_LABEL = "Recurse";

    public static final String EXIT_LABEL = "Exit";

    public static final String HELP_MESSAGE = "                     " + APP_NAME + "\nThis is a utility to clean up import statements.  To use it:\n1) Make sure your Java source code file and class file are in the same directory\n2) Select your source code file\n3) Click \"Find files\"\n4) Click \"Go\"\nImportscrubber will crank away for a few seconds and then pop up a box telling you it's done.\nTo process multiple files, just pick a directory and toggle the \"recursive\" checkbox\nQuestions? Comments? Contact tomcopeland@users.sourceforge.net";

    public static final String BREAK_EACH_PACKAGE = "Break for each package";

    public static final String BREAK_TOP_PACKAGE = "Break for top package";

    public static final String BREAK_NONE = "No breaks";


    public static final String ERR_NOT_DIR = "File is not a directory!";

    public static final String ERR_UNABLE_TO_FINISH = "Unable to finish due to:";

    public static final String ERR_DIR_NOT_EXIST = "Directory does not exist!";

    public static final String ERR_CLASS_FILE_MUST_EXIST = "Class file must exist: ";

    public static final String ERR_MUST_NOT_BE_DIR = "Input file cannot be a directory: ";

    public static final String SORT_JAVA_LIBS_LABEL = "Sort standard libraries high";


    private static final Object[][] contents = {

        {FILE_BROWSER_TITLE, FILE_BROWSER_TITLE},

        {VERSION_ID, VERSION_ID},

        {BROWSE_LABEL, BROWSE_LABEL},

        {GO_LABEL, GO_LABEL},

        {OPTIONS_LABEL, OPTIONS_LABEL},

        {HELP_LABEL, HELP_LABEL},

        {EXIT_LABEL, EXIT_LABEL},

        {ABOUT_LABEL, ABOUT_LABEL},

        {RECURSE_LABEL, RECURSE_LABEL},

        {FILE_LABEL, FILE_LABEL},

        {FIND_FILES_LABEL, FIND_FILES_LABEL},

        {ALL_DONE, ALL_DONE},

        {APP_NAME, APP_NAME},

        {HELP_MESSAGE, HELP_MESSAGE},

        {BREAK_EACH_PACKAGE, BREAK_EACH_PACKAGE},

        {BREAK_TOP_PACKAGE, BREAK_TOP_PACKAGE},

        {BREAK_NONE, BREAK_NONE},

        {ERR_NOT_DIR, ERR_NOT_DIR},

        {ERR_DIR_NOT_EXIST, ERR_DIR_NOT_EXIST},

        {ERR_CLASS_FILE_MUST_EXIST, ERR_CLASS_FILE_MUST_EXIST},

        {ERR_MUST_NOT_BE_DIR, ERR_MUST_NOT_BE_DIR},

        {ERR_UNABLE_TO_FINISH, ERR_UNABLE_TO_FINISH},

        {SORT_JAVA_LIBS_LABEL, SORT_JAVA_LIBS_LABEL}

    };


    public Object[][] getContents() {

        return contents;

    }

}
