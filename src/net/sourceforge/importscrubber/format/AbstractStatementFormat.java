package net.sourceforge.importscrubber.format;


import java.util.ResourceBundle;

public abstract class AbstractStatementFormat {

    protected boolean _sortJavaLibsHigh;


    public boolean sortJavaLibsHigh() {

        return _sortJavaLibsHigh;

    }

    public void sortJavaLibsHigh(boolean sortJavaLibsHigh) {

        _sortJavaLibsHigh = sortJavaLibsHigh;

    }


    public String toString(String name) {

        ResourceBundle res = ResourceBundle.getBundle("net.sourceforge.importscrubber.Resources");

        return res.getString(name);

    }


}


