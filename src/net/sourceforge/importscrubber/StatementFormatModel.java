package net.sourceforge.importscrubber;


import net.sourceforge.importscrubber.format.IStatementFormat;
import net.sourceforge.importscrubber.format.StatementFormatFactory;

import javax.swing.*;
import javax.swing.event.ListDataListener;


public class StatementFormatModel implements ComboBoxModel {

    private IStatementFormat _format;


    public StatementFormatModel() {

        setSelectedItem(StatementFormatFactory.getInstance().createStatementFormat(StatementFormatFactory.DEFAULT));

    }


    public void setSelectedItem(Object anItem) {

        _format = (IStatementFormat) anItem;

    }

    public Object getSelectedItem() {

        return _format;

    }

    public int getSize() {

        return StatementFormatFactory.getInstance().getFormatsList().size();

    }

    public Object getElementAt(int index) {

        return StatementFormatFactory.getInstance().getFormatsList().get(index);

    }

    public void addListDataListener(ListDataListener l) {
    }

    public void removeListDataListener(ListDataListener l) {
    }

}
