package net.sourceforge.importscrubber;

import java.awt.BorderLayout;
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

/**
 * Enacapsulates the Importscrubber GUI.  Serves as a Mediator (GOF, p 273) for all GUI components.
 */
public class ImportScrubberGUI implements IProgressMonitor {

   private class BrowseListener implements ActionListener {
      public void actionPerformed(ActionEvent e) {
         JFileChooser chooser = new JFileChooser(_settings.get(ImportScrubber.START_DIRECTORY_KEY));

         chooser.setDialogTitle(ResourceBundle.getBundle("net.sourceforge.importscrubber.Resources").getString(Resources.FILE_BROWSER_TITLE));
         chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
         chooser.showOpenDialog(_mainFrame);
         if (chooser.getSelectedFile() != null) {
            _fileSpecField.setText(chooser.getSelectedFile().getAbsolutePath());
         }
         _settings.put(ImportScrubber.START_DIRECTORY_KEY, _fileSpecField.getText());
         try {
            _settings.save();
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


   private JTextField _fileSpecField;
   private JFrame _mainFrame;
   private JCheckBox _recurseCheckbox;
   private JList _fileList;
   private ImportScrubber _scrubber;
   private JComboBox _formats;
   private ImportScrubberMenu _menu;
   private Settings _settings;

   public static void main(String[] args) {
      ImportScrubberGUI gui = new ImportScrubberGUI();
   }

   public ImportScrubberGUI() {
      
      _settings = new Settings("importscrubber");
      if(_settings.get(ImportScrubber.START_DIRECTORY_KEY) == null)
      {
         _settings.put(ImportScrubber.START_DIRECTORY_KEY, System.getProperty("user.home"));
         try {
            _settings.save();
         } catch (IOException ioe) {
            ioe.printStackTrace();
         }
      }

      ResourceBundle res = ResourceBundle.getBundle("net.sourceforge.importscrubber.Resources");
      
      _mainFrame = new JFrame(res.getString(Resources.VERSION_ID));
      _mainFrame.setLocation(200, 150);
      _mainFrame.setSize(600,450);
      _mainFrame.addWindowListener(new BasicWindowMonitor());
      _mainFrame.getContentPane().setLayout(new FlowLayout());
      _fileSpecField = new JTextField(30);
      JButton browseButton = new JButton(res.getString(Resources.BROWSE_LABEL));
      browseButton.setMnemonic('B');
      browseButton.addActionListener(new BrowseListener());
      _recurseCheckbox = new JCheckBox();

      JPanel browsePanel = new JPanel(new FlowLayout());
      browsePanel.add(new JLabel(res.getString(Resources.RECURSE_LABEL)));
      browsePanel.add(_recurseCheckbox);
      browsePanel.add(_fileSpecField);
      browsePanel.add(browseButton);

      _formats = new JComboBox(new StatementFormatModel());
      JPanel formatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      formatPanel.add(_formats);

      _fileList = new JList(new DefaultListModel());
      _fileList.setCellRenderer(new WorkingCellRenderer());

      JButton findFilesButton = new JButton(res.getString(Resources.FIND_FILES_LABEL));
      findFilesButton.setMnemonic('i');
      findFilesButton.addActionListener(new FindCommand(this));
      JPanel findButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      findButtonPanel.add(findFilesButton);

      JPanel settingsTopPanel = new JPanel(new BorderLayout());
      settingsTopPanel.add(findButtonPanel, BorderLayout.NORTH);
      settingsTopPanel.add(formatPanel, BorderLayout.SOUTH);
      
      JPanel goButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      JButton goButton = new JButton(res.getString(Resources.GO_LABEL));
      goButton.setMnemonic('G');
      goButton.addActionListener(new GoCommand(this));
      goButtonPanel.add(goButton, BorderLayout.NORTH);

      JPanel allSettingsPanel = new JPanel(new BorderLayout());
      allSettingsPanel.add(settingsTopPanel, BorderLayout.NORTH);
      allSettingsPanel.add(goButtonPanel, BorderLayout.SOUTH);

      JPanel resultsPanel = new JPanel(new BorderLayout());
      JScrollPane listPane = new JScrollPane(_fileList);
      listPane.setSize(resultsPanel.getWidth()-40, resultsPanel.getHeight()-10);
      resultsPanel.add(listPane);

      JPanel bottom = new JPanel(new BorderLayout());
      bottom.add(allSettingsPanel, BorderLayout.NORTH);
      bottom.add(resultsPanel, BorderLayout.SOUTH);

      _menu = new ImportScrubberMenu(this);
      _mainFrame.setJMenuBar(_menu);
      _mainFrame.getContentPane().add(browsePanel);
      _mainFrame.getContentPane().add(bottom);

      _scrubber = new ImportScrubber();
      _mainFrame.setVisible(true);
   }

   public void go() {
      if (_fileList.getModel().getSize() == 0) {
         return;
      }
      ResourceBundle res = ResourceBundle.getBundle("net.sourceforge.importscrubber.Resources");
      try {
         IStatementFormat format = (IStatementFormat)_formats.getSelectedItem();
         format.sortJavaLibsHigh(_menu.areLibsSortedHigh());
         _scrubber.setFormat(format);
         _scrubber.buildTasks();
         _scrubber.runTasks(this);
         JOptionPane.showMessageDialog(null, res.getString(Resources.ALL_DONE), res.getString(Resources.APP_NAME), JOptionPane.INFORMATION_MESSAGE);
      } catch (Exception e) {
         JOptionPane.showMessageDialog(null, res.getString(Resources.ERR_UNABLE_TO_FINISH) + e.getMessage(), res.getString(Resources.APP_NAME), JOptionPane.ERROR_MESSAGE);
         e.printStackTrace();
      }
   }

   public void find() {
      _scrubber.setFileRoot(_fileSpecField.getText(), _recurseCheckbox.isSelected());
      DefaultListModel model = (DefaultListModel)_fileList.getModel();
      model.removeAllElements();
      for (ListIterator iter = _scrubber.getFiles().listIterator(); iter.hasNext();) {
         model.addElement(iter.next());
      }
      _mainFrame.paintAll(_mainFrame.getGraphics());
   }

   public void destroy() {
      _mainFrame.setVisible(false);
      _mainFrame.dispose();
      System.exit(0);
   }

   // ITaskListener
   public void taskStarted(ScrubTask task) {
      DefaultListModel model = (DefaultListModel)_fileList.getModel();
      for (Enumeration e = model.elements(); e.hasMoreElements();) {
         FilePair pair = (FilePair)e.nextElement();
         if (pair.getSourceFile().getAbsolutePath().equals(task.getSourcePath())) {
            _fileList.setSelectedValue(pair, true);   
         }
      }
      _fileList.paint(_fileList.getGraphics());
   }

   public void taskComplete(ScrubTask task) {
      _fileList.clearSelection();
   }
   // ITaskListener

}
