package net.sourceforge.importscrubber;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import net.sourceforge.importscrubber.format.IStatementFormat;

public class ImportScrubberGUI implements IProgressMonitor, ActionListener {
    
    private JTextField fileSpecField;
    private JFrame mainFrame;
    private JCheckBox recurseCheckbox;
    private JList fileList;
    private ImportScrubber scrubber;
    private JComboBox formats;
    private ImportScrubberMenu menu;
    private Settings settings;
    private JButton goButton;
    
    private class BrowseListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {            
            JFileChooser chooser = new JFileChooser(settings.get(ImportScrubber.START_DIRECTORY_KEY));
            
            chooser.setDialogTitle(ResourceBundle.getBundle("net.sourceforge.importscrubber.Resources").getString(Resources.FILE_BROWSER_TITLE));
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.showOpenDialog(mainFrame);

            if (chooser.getSelectedFile() != null) {
                fileSpecField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
            settings.put(ImportScrubber.START_DIRECTORY_KEY, fileSpecField.getText());
            try {
                settings.save();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
    
    private class BasicWindowMonitor extends WindowAdapter {
        public void windowClosing(WindowEvent we) {
            destroy();
        }
    }
    
    public ImportScrubberGUI() {
        settings = new Settings("importscrubber");
        
        ResourceBundle res = ResourceBundle.getBundle("net.sourceforge.importscrubber.Resources");
        
        mainFrame = new JFrame(res.getString(Resources.VERSION_ID));
        mainFrame.setLocation(200, 150);
        mainFrame.setSize(600,450);
        mainFrame.addWindowListener(new BasicWindowMonitor());
        mainFrame.getContentPane().setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new BorderLayout());
        
        fileSpecField = new JTextField(30);
        JButton browseButton = new JButton(res.getString(Resources.BROWSE_LABEL));
        browseButton.setMnemonic('B');
        browseButton.addActionListener(new BrowseListener());
        
        // needed to stop button being expanded.
        JPanel browseButtonPanel = new JPanel();
        browseButtonPanel.add(browseButton);
        
        recurseCheckbox = new JCheckBox(res.getString(Resources.RECURSE_LABEL));
        
        topPanel.add(recurseCheckbox,BorderLayout.WEST);
        topPanel.add(fileSpecField,BorderLayout.CENTER);
        topPanel.add(browseButtonPanel,BorderLayout.EAST);
        
        JPanel middlePanel = new JPanel(new FlowLayout());
        
        formats = new JComboBox(new StatementFormatModel());

        JButton findFilesButton = new JButton(res.getString(Resources.FIND_FILES_LABEL));
        findFilesButton.setMnemonic('i');
        findFilesButton.addActionListener(this);
        findFilesButton.setActionCommand("FIND"); // Internal String only
        
        goButton = new JButton(res.getString(Resources.GO_LABEL));
        goButton.setMnemonic('G');
        goButton.addActionListener(this);
        goButton.setActionCommand("GO"); // Internal String only
        goButton.setEnabled(false);
        
        middlePanel.add(findFilesButton, BorderLayout.WEST);
        middlePanel.add(goButton, BorderLayout.CENTER);
        middlePanel.add(formats, BorderLayout.EAST);
        
        fileList = new JList(new DefaultListModel());        
        
        JPanel topMainPanel = new JPanel(new  GridLayout(2,1));
        topMainPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Controls"));
        topMainPanel.add(topPanel);
        topMainPanel.add(middlePanel);
        
        JScrollPane listPane = new JScrollPane(fileList);
        JPanel listPanel = new JPanel(new BorderLayout());
        
        listPanel.add(listPane,BorderLayout.CENTER);
        listPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Files"));
        
        menu = new ImportScrubberMenu(this);
        mainFrame.setJMenuBar(menu);
        mainFrame.getContentPane().add(topMainPanel,BorderLayout.NORTH);
        mainFrame.getContentPane().add(listPanel,BorderLayout.CENTER);
        
        scrubber = new ImportScrubber();
        
        if(settings.get(ImportScrubber.START_DIRECTORY_KEY) == null) {
            settings.put(ImportScrubber.START_DIRECTORY_KEY, System.getProperty("user.home"));
            try {
                settings.save();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            fileSpecField.setText(settings.get(ImportScrubber.START_DIRECTORY_KEY));
        }
        
       if(settings.get(ImportScrubber.RECURSE)!=null) {
           recurseCheckbox.setSelected(Boolean.valueOf(settings.get(ImportScrubber.RECURSE)).booleanValue());
       }
        
        mainFrame.setVisible(true);
    }
    
    public void go() {
        if (fileList.getModel().getSize() == 0) {
            return;
        }
        
        setBusy(true);
        
        ResourceBundle res = ResourceBundle.getBundle("net.sourceforge.importscrubber.Resources");
        
        try {
            IStatementFormat format = (IStatementFormat)formats.getSelectedItem();
            format.sortJavaLibsHigh(menu.areLibsSortedHigh());
            scrubber.setFormat(format);
            int count = scrubber.buildTasks();
            scrubber.runTasks(this);
            setBusy(false);
            
            JOptionPane.showMessageDialog(null, res.getString(Resources.ALL_DONE) + " - processed " + count + " files", res.getString(Resources.APP_NAME), JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            setBusy(false);
            JOptionPane.showMessageDialog(null, res.getString(Resources.ERR_UNABLE_TO_FINISH) + e.getMessage(), res.getString(Resources.APP_NAME), JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    public void find() {
        setBusy(true);
        scrubber.setFileRoot(fileSpecField.getText(), recurseCheckbox.isSelected());
        DefaultListModel model = (DefaultListModel)fileList.getModel();
        model.removeAllElements();
        for (ListIterator iter = scrubber.getFiles().listIterator(); iter.hasNext();) {
            model.addElement(iter.next());
        }
        setBusy(false);
        // leave it too swing to repaint.  Model will handle repaints for you.
    }
    
    public void destroy() {
        mainFrame.setVisible(false);
        
        // Save Settings
        settings.put(ImportScrubber.RECURSE,String.valueOf(recurseCheckbox.isSelected()));
        try {
            settings.save();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
        mainFrame.dispose();
        System.exit(0);
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
       if(actionEvent.getActionCommand().equals("FIND")) { // Internal String Only
            FindCommand fc = new FindCommand(this);
            // Keeps Swing UI Usable
            fc.setPriority(Thread.MIN_PRIORITY);
            fc.start();
        } else if(actionEvent.getActionCommand().equals("GO")) { // Internal String Only
            GoCommand go = new GoCommand(this);
            // Keeps Swing UI Usable
            go.setPriority(Thread.MIN_PRIORITY);
            go.start();
        }
    }
    
    public void setBusy(boolean busy) {
        javax.swing.SwingUtilities.invokeLater(new SetBusy(busy));
    }
    
    public class SetBusy implements Runnable {
        boolean busy;
        public SetBusy(boolean busy) {
            this.busy = busy;
        }
        public void run() {
            if(fileList.getModel().getSize()>0) {
                goButton.setEnabled(true);
            } else {
                goButton.setEnabled(false);
            }
            
            if(busy) {
                mainFrame.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
            } else {
                mainFrame.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        }
    }

    // ITaskListener
    public void taskStarted(ScrubTask task) {
        DefaultListModel model = (DefaultListModel)fileList.getModel();
        for (Enumeration e = model.elements(); e.hasMoreElements();) {
            FilePair pair = (FilePair)e.nextElement();
            if (pair.getSourceFile().getAbsolutePath().equals(task.getSourcePath())) {
                fileList.setSelectedValue(pair, true);
            }
        }
    }
    
    public void taskComplete(ScrubTask task) {
        fileList.clearSelection();
    }
    // ITaskListener

    public static void main(String[] args) {
        ImportScrubberGUI gui = new ImportScrubberGUI();
    }
}