package net.sourceforge.importscrubber;


import java.util.Comparator;


public class ImportStatementComparator implements Comparator {

    private boolean _sortStdLibsHigh;


    public ImportStatementComparator(boolean sortStdLibsHigh) {

        _sortStdLibsHigh = sortStdLibsHigh;

    }


    public int compare(Object first, Object second) {

        if (first == null || second == null) {

            throw new IllegalArgumentException("Either " + first + " or " + second + " is null");

        }


        ImportStatement firstImport = (ImportStatement) first;

        ImportStatement secondImport = (ImportStatement) second;


        if (_sortStdLibsHigh) {

            if (firstImport.isInStdJavaLibrary() && !secondImport.isInStdJavaLibrary()) {

                return -1;

            }


            if (!firstImport.isInStdJavaLibrary() && secondImport.isInStdJavaLibrary()) {

                return 1;

            }


            if (firstImport.isInStdJavaExtensionLibrary() && !secondImport.isInStdJavaExtensionLibrary()) {

                return -1;

            }


            if (!firstImport.isInStdJavaExtensionLibrary() && secondImport.isInStdJavaExtensionLibrary()) {

                return 1;

            }

        }


        return firstImport.compareTo(secondImport);

    }

}

