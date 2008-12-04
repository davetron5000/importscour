package net.sourceforge.importscrubber;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ResourceBundle;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * Encapsulates the ImportScrubber GUI menu.
 */

public class ImportScrubberMenu extends JMenuBar
{
    private class MyAboutWindow implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            JOptionPane.showMessageDialog(ImportScrubberMenu.this, _helpMsg);
        }
    }

    private class ExitActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            _frame.destroy();
        }
    }

    private ImportScrubberGUI _frame;
    private String _helpMsg;

    public ImportScrubberMenu(ImportScrubberGUI frame)
    {
        _frame = frame;

        ResourceBundle res = ResourceBundle.getBundle("net.sourceforge.importscrubber.Resources");

        _helpMsg = res.getString(Resources.HELP_MESSAGE);

        JMenu lFileMenu = new JMenu(res.getString(Resources.FILE_LABEL));
        lFileMenu.setMnemonic('f');
        JMenuItem lExitItem = new JMenuItem(res.getString(Resources.EXIT_LABEL));
        lExitItem.setMnemonic('x');
        lExitItem.addActionListener(new ExitActionListener());
        lFileMenu.add(lExitItem);

        JMenu lHelpMenu = new JMenu(res.getString(Resources.HELP_LABEL));
        lHelpMenu.setMnemonic('h');
        JMenuItem lAboutItem = new JMenuItem(res.getString(Resources.ABOUT_LABEL));
        lAboutItem.setMnemonic('a');
        lAboutItem.addActionListener(new MyAboutWindow());
        lHelpMenu.add(lAboutItem);

        add
            (lFileMenu);
        add
            (lHelpMenu);
    }

}
