package net.sourceforge.importscrubber;

import java.io.File;
import java.io.FilenameFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This class encapsulates the source and class files.
 */
public class FilePair
{

    private File _sourceFile;
    private File _classFile;

    /** Create a FilePair from files
     * @param sourceFile the source file
     * @param classFile the matching class file
     */
    public FilePair(File sourceFile, File classFile)
    {
        _sourceFile = sourceFile;
        _classFile = classFile;
    }

    /** Creates a FilePair for a source file given the source and class roots.
     * @param sourceRoot the root of the source directory
     * @param classRoot the root of the classes directory
     * @param sourceFile the name of the source file, relative to sourceRoot; must end in .java and exist in sourceRoot
     */
    public FilePair(String sourceRoot, String classRoot, String sourceFile)
    {
        if (!sourceFile.endsWith(".java"))
            throw new IllegalArgumentException(sourceFile + " isn't a java source");

        _sourceFile = new File(sourceRoot + File.separator + sourceFile);

        if (!_sourceFile.exists())
            throw new IllegalArgumentException(_sourceFile + " doesn't exist [sourceRoot=" + sourceRoot + ",classRoot=" + classRoot + "]");

        String className = sourceFile.substring(0,sourceFile.length() - 5) + ".class";
        _classFile = new File(classRoot + File.separator + className);
        if (!_classFile.exists())
            throw new IllegalArgumentException(_classFile + " doesn't exist [sourceRoot=" + sourceRoot + ",classRoot=" + classRoot + "]");
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
