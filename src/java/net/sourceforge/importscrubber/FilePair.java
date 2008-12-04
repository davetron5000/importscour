package net.sourceforge.importscrubber;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/**
 * This class encapsulates the source & class files
 */
public class FilePair
{

    /**
     * A filter which only accepts inner classes.
     */
    private class InnerClassFilter implements FilenameFilter
    {
        public boolean accept(File dir, String name)
        {
            // make sure we only get inner classes
            if (name.indexOf("$") == -1) {
                return false;
            }
            // make sure we get this class's inner classes
            String className = _classFile.getName();
            className = className.substring(0, className.indexOf("."));
            return name.startsWith(className);
        }
    }
    private File _sourceFile;
    private File _classFile;

    /**
     * 
     * @param sourceFile
     * @param classFile
     */
    public FilePair(File sourceFile, File classFile)
    {
        _sourceFile = sourceFile;
        _classFile = classFile;
    }

    public File getSourceFile()
    {
        return _sourceFile;
    }

    @SuppressWarnings("unchecked")
    public ListIterator getClassFiles()
    {
        List files = new ArrayList();
        files.add(_classFile);
        if (_classFile.getParent() != null) {
            File dir = new File(_classFile.getParent());
            files.addAll(Arrays.asList(dir.listFiles(new InnerClassFilter())));
        }
        return files.listIterator();
    }

    public int hashCode()
    {
        return _sourceFile.hashCode();
    }

    public boolean equals(Object o)
    {
        return o instanceof FilePair
               && ((FilePair)o).getSourceFile().compareTo(_sourceFile) == 0;
    }

    public String toString()
    {
        return _sourceFile.getAbsolutePath();
    }
}
