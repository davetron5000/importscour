package net.sourceforge.importscrubber;


import java.io.File;
import java.io.FilenameFilter;


/**
 * Encapsulates a file filter which accepts directories and (Java source file names only when there is a class file in the same directory).
 */

public class JavaFileFilter implements FilenameFilter {

    public boolean accept(File dir, String name) {

        File file = new File(dir + ImportScrubber.FILE_SEPARATOR + name);

        if (file.isDirectory()) {

            return true;

        }

        if (!file.exists() || !name.endsWith(".java")) {

            return false;

        }


        File classFile = new File(dir + ImportScrubber.FILE_SEPARATOR + name.substring(0, name.length() - 5) + ".class");

        return classFile.exists();

    }

}

