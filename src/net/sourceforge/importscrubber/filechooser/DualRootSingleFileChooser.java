package net.sourceforge.importscrubber.filechooser;


import net.sourceforge.importscrubber.FilePair;
import net.sourceforge.importscrubber.ImportScrubber;
import net.sourceforge.importscrubber.Resources;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Encapsulates a file chooser that picks one file, but it takes the class and source file from different directories
 */

public class DualRootSingleFileChooser implements IFileChooser {

    private String _sourcesRoot;

    private String _classesRoot;

    private String _sourceFilename;


    public DualRootSingleFileChooser(String sourcesRoot,

                                     String classesRoot,

                                     String sourceFilename) {

        _sourcesRoot = sourcesRoot;

        _classesRoot = classesRoot;

        _sourceFilename = sourceFilename;


        ResourceBundle res = ResourceBundle.getBundle("net.sourceforge.importscrubber.Resources");
        // Sanity check inputs
        File sourcesRootFile = new File(_sourcesRoot);

        if (!sourcesRootFile.isDirectory())

            throw new IllegalArgumentException(res.getString(Resources.ERR_NOT_DIR) + ":" + _sourcesRoot);

        File classesRootFile = new File(_classesRoot);

        if (!classesRootFile.isDirectory())

            throw new IllegalArgumentException(res.getString(Resources.ERR_NOT_DIR) + ":" + _classesRoot);

        File sourceFilenameFile = new File(_sourceFilename);

        if (sourceFilenameFile.isDirectory())

            throw new IllegalArgumentException(res.getString(Resources.ERR_MUST_NOT_BE_DIR) + _sourceFilename);

        try {

            _sourceFilename = sourceFilenameFile.getCanonicalPath();

            _sourcesRoot = sourcesRootFile.getCanonicalPath();

            if (!_sourceFilename.startsWith(_sourcesRoot))

                throw new IllegalArgumentException("Input file '" + _sourceFilename + "' must be in a subdirectory of sourcesRoot: " + _sourcesRoot);

        } catch (java.io.IOException e) {

            throw new IllegalArgumentException(e.toString());

        }

    }


    public void setRoot(String root) {

        throw new IllegalArgumentException("setRoot() is unused in this implementation of IFileChooser");

    }


    public List getFiles() {

        File sourceFile = null;


        try {

            sourceFile = new File(_sourceFilename).getCanonicalFile();

        } catch (java.io.IOException e) {

            throw new IllegalArgumentException(e.toString());

        }


        int relStart = _sourcesRoot.length();

        String relativeSourceFilename = sourceFile.getParent().substring(relStart);


        String cfilename = sourceFile.getName().substring(0, sourceFile.getName().length() - 5) + ".class";

        String classfilename = _classesRoot + relativeSourceFilename + ImportScrubber.FILE_SEPARATOR + cfilename;

        File classfile = new File(classfilename);

        if (!classfile.exists())

            throw new IllegalArgumentException(Resources.ERR_CLASS_FILE_MUST_EXIST + classfilename);


        FilePair pair = new FilePair(sourceFile, classfile);

        List files = new ArrayList();

        files.add(pair);

        return files;

    }


}
