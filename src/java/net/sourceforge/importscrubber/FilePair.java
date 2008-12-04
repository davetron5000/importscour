package net.sourceforge.importscrubber;

import java.io.File;
import java.io.FilenameFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

/**
 * This class encapsulates the source and class files
 */
public class FilePair
{

    private File _sourceFile;
    private File _classFile;

    /**
     * @param sourceFile the source file
     * @param classFile the matching class file
     */
    public FilePair(File sourceFile, File classFile)
    {
        _sourceFile = sourceFile;
        _classFile = classFile;
    }

    public File getSourceFile() { return _sourceFile; }

    /** Returns all class files relevant to the source file of this pair
     */
    public Iterator<File> getClassFiles()
    {
        List<File> files = new ArrayList<File>();
        files.add(_classFile);
        if (_classFile.getParent() != null) {
            File dir = new File(_classFile.getParent());
            files.addAll(Arrays.asList(dir.listFiles(new InnerClassFilter())));
        }
        return files.iterator();
    }

    public int hashCode() { return getSourceFile().hashCode(); }

    public boolean equals(Object o)
    {
        if (o == this)
            return true;
        if (o == null)
            return false;
        if (!o.getClass().getName().equals(getClass().getName()))
            return false;

        return ((FilePair)o).getSourceFile().compareTo(getSourceFile()) == 0;
    }

    public String toString() { return getSourceFile().getAbsolutePath(); }

    /**
     * A filter which only accepts inner classes of {@link #_className}.
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
}
