package net.sourceforge.importscrubber.filechooser;


import net.sourceforge.importscrubber.FilePair;
import net.sourceforge.importscrubber.ImportScrubber;
import net.sourceforge.importscrubber.JavaFileFilter;
import net.sourceforge.importscrubber.Resources;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Encapsulates a file chooser that recurses in to subdirectories
 */

public class RecursiveFileChooser implements IFileChooser {

    private String _root;


    public void setRoot(String root) {

        File file = new File(root);

        if (!file.isDirectory()) throw new IllegalArgumentException(Resources.ERR_NOT_DIR);

        if (!file.exists()) throw new IllegalArgumentException(Resources.ERR_DIR_NOT_EXIST);

        _root = root;

    }


    public List getFiles() {

        File root = new File(_root);

        List list = new ArrayList();

        scanDirectory(root, list);

        return list;

    }


    private void scanDirectory(File dir, List list) {

        String[] possibles = dir.list(new JavaFileFilter());

        for (int i = 0; i < possibles.length; i++) {

            File tmp = new File(dir + ImportScrubber.FILE_SEPARATOR + possibles[i]);

            if (tmp.isDirectory()) {

                scanDirectory(tmp, list);

                continue;

            }

            FilePair pair = new FilePair(new File(dir + ImportScrubber.FILE_SEPARATOR + possibles[i]), new File(dir + ImportScrubber.FILE_SEPARATOR + possibles[i].substring(0, possibles[i].length() - 5) + ".class"));

            list.add(pair);

        }

    }

}
