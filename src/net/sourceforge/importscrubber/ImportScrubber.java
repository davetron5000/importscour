package net.sourceforge.importscrubber;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

/**
 * This class encapsulates the import scrubber controller
 */
public class ImportScrubber {

	public static boolean DEBUG = false;
	private Properties _props = new Properties();
	private IFileChooser _fileChooser;
	
	public ImportScrubber() throws IOException, IllegalAccessException, InstantiationException, ClassNotFoundException {
		InputStream is = getClass().getResourceAsStream("/net/sourceforge/importscrubber/importscrubber.properties");
		_props.load(is);
		is.close();
		DEBUG = Boolean.valueOf(_props.getProperty("DEBUG")).booleanValue();
		_fileChooser = (IFileChooser)Class.forName(_props.getProperty("filechooser")).newInstance();
		_fileChooser.setRoot(_props.getProperty("filechooser.root"));
	}
	
	public ImportScrubber(String filename) {
		_fileChooser = new SingleFileChooser();
		_fileChooser.setRoot(filename);
		DEBUG = false;
	}
	
	public void scrub() throws IOException, InterruptedException {
		long start = System.currentTimeMillis();
		
		Vector tasks = new Vector();
		for (Enumeration e = _fileChooser.getFiles(); e.hasMoreElements();) {
			FilePair pair = (FilePair)e.nextElement();
			tasks.addElement(new ScrubTask(pair));
		}

		for (Enumeration e = tasks.elements(); e.hasMoreElements();) {
			ScrubTask task = (ScrubTask)e.nextElement();
			task.start();
		}
		
		for (Enumeration e = tasks.elements(); e.hasMoreElements();) {
			ScrubTask task = (ScrubTask)e.nextElement();
			task.join();
		}

		long stop = System.currentTimeMillis();
		
		System.out.println("Elapsed time " + String.valueOf(stop-start));
	}

}

