package net.sourceforge.importscrubber;

import java.io.File;
import java.io.IOException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

// note: could have used threads rather than the iterator approach used here,
// but that would have bought us nothing in the command line version (we don't WANT
// a thread scrubbing while this finds files, because most disk systems handle
// that kind of load very poorly).  so since our only concern is allowing the GUI
// to update as files are found, this is just as effective -- and simpler.

/** Selects files to process based on the ability to find the source file and it's associated class file.
 */
public class FileChooser implements Iterator
{
    private String _sourceRoot, _classRoot;
    private boolean _recurse;
    // pre-computed for efficiency
    int sourceFileDirectoryNameLength;

    private LinkedList<String> possibles = new LinkedList<String>();
    private FilePair nextFp;

    /** Create a new FileChooser for the given source and class, possibly recursing.
     * @param sourceRoot a directory or file
     * @param classRoot the analagous directory for the sourceRoot; if sourceRoot is a directory, this should be the directory at the same level of depth.  If sourceRoot
     * is a file, this should point to the directory containing that file's class.
     * @param recurse if true, directories inside sourceRoot are recursed into
     * @throws IOException if there was some I/O problem
     */
    public FileChooser(String sourceRoot, String classRoot, boolean recurse) 
        throws IOException
    {
        _recurse = recurse;
        _sourceRoot = getCanonical(sourceRoot,false);
        _classRoot = getCanonical(classRoot,true);

        sourceFileDirectoryNameLength = ImportScrubber.getDirectory(_sourceRoot).length();

        possibles.add(_sourceRoot);
    }

    public void remove() { throw new UnsupportedOperationException(); }

    /** True if there is another file/class pair to process */
    public boolean hasNext()
    {
        if (nextFp == null) {
            try {
                nextFp = (FilePair)next();
            } catch (NoSuchElementException e) {}
        }
        return nextFp != null;
    }

    /** Returns the next FilePair.
     */
    public Object next() throws NoSuchElementException
    {
        if (nextFp != null) {
            FilePair f = nextFp;
            nextFp = null;
            return f;
        }

        FilePair fp = null;
        while (fp == null) {
            fp = nextPossible();
        }
        return fp;
    }

    private String getCanonical(String path, boolean shouldBeDirectory)
        throws IOException
    {
        File file = new File(path);
        if (!file.exists())
            throw new IllegalArgumentException(file.toString() + Resources.ERR_DIR_NOT_EXIST);
        if (shouldBeDirectory && !file.isDirectory())
            throw new IllegalArgumentException(file.toString() + Resources.ERR_NOT_DIR);

        return file.getCanonicalPath();
    }


    // returns null if the sourcefile didn't have a matching classfile.
    private FilePair nextPossible() throws NoSuchElementException
    {
        String s = (String)possibles.removeFirst();
        File tmp = new File(s);
        if (tmp.isDirectory()) {
            if (_recurse) {
                String[] entries = tmp.list(new JavaFileFilter());
                for (int i = 0; i < entries.length; i++) {
                    possibles.add(s + ImportScrubber.FILE_SEPARATOR + entries[i]);
                }
            }
            return nextPossible();
        } else {
            return getPair(tmp);
        }
    }

    private FilePair getPair(File source)
    {
        if (!(source.canRead() && source.canWrite())) {
            return null;
        }
        String relativeSourceFilename = source.getParent().substring(sourceFileDirectoryNameLength);

        String cfilename = source.getName().substring(0, source.getName().length() - 5) + ".class";
        String classfilename = _classRoot + relativeSourceFilename + ImportScrubber.FILE_SEPARATOR + cfilename;
        File classfile = new File(classfilename);
        if(classfile.exists()) {
            return new FilePair(source, classfile);
        }

        return null;
    }
}
