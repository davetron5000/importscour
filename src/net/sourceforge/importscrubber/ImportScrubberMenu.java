package net.sourceforge.importscrubber;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;


/**
 * Encapsulates the ImportScrubber GUI menu.
 */

public class ImportScrubberMenu extends JMenuBar {


    private class MyAboutWindow implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            JOptionPane.showMessageDialog(ImportScrubberMenu.this, _helpMsg);

        }

    }


    private class ExitActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            _frame.destroy();

        }

    }


    private ImportScrubberGUI _frame;

    private String _helpMsg;

    private JCheckBoxMenuItem _areLibsSortedHighItem;


    public ImportScrubberMenu(ImportScrubberGUI frame) {

        _frame = frame;


        ResourceBundle res = ResourceBundle.getBundle("net.sourceforge.importscrubber.Resources");


        _helpMsg = res.getString(Resources.HELP_MESSAGE);


        JMenu lFileMenu = new JMenu(res.getString(Resources.FILE_LABEL));

        lFileMenu.setMnemonic('f');

        JMenuItem lExitItem = new JMenuItem(res.getString(Resources.EXIT_LABEL));

        lExitItem.setMnemonic('x');

        lExitItem.addActionListener(new ExitActionListener());

        lFileMenu.add(lExitItem);


        JMenu lOptionsMenu = new JMenu(res.getString(Resources.OPTIONS_LABEL));

        lOptionsMenu.setMnemonic('o');

        _areLibsSortedHighItem = new JCheckBoxMenuItem(res.getString(Resources.SORT_JAVA_LIBS_LABEL));

        _areLibsSortedHighItem.setMnemonic('s');

        _areLibsSortedHighItem.setSelected(true);

        lOptionsMenu.add(_areLibsSortedHighItem);


        JMenu lHelpMenu = new JMenu(res.getString(Resources.HELP_LABEL));

        lHelpMenu.setMnemonic('h');

        JMenuItem lAboutItem = new JMenuItem(res.getString(Resources.ABOUT_LABEL));

        lAboutItem.setMnemonic('a');

        lAboutItem.addActionListener(new MyAboutWindow());

        lHelpMenu.add(lAboutItem);


        add(lFileMenu);

        add(lOptionsMenu);

        add(lHelpMenu);

    }


    public boolean areLibsSortedHigh() {

        return _areLibsSortedHighItem.isSelected();

    }


}
