package net.sourceforge.importscrubber;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

/**
 * This class encapsulates a simple method of picking a file to scrub
 */
public class SingleFileChooser implements IFileChooser {
	
	private String _root;
	
	public void setRoot(String root) {
		File file = new File(root);
		if (file.isDirectory()) throw new IllegalArgumentException("File is directory!");
		if (!(new File(root + ".java").exists())) throw new IllegalArgumentException("Source file doesn't exist!");
		if (!(new File(root + ".class").exists())) throw new IllegalArgumentException("Class file doesn't exist!");
		_root = root;
	}
	
	public Enumeration getFiles() {
		FilePair pair = new FilePair(new File(_root + ".java"), new File(_root + ".class"));
		Vector v = new Vector();
		v.addElement(pair);
		return v.elements();
	}
}
