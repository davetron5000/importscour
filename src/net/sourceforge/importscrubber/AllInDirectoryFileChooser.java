package net.sourceforge.importscrubber;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;
import java.io.FilenameFilter;

/**
 * This class encapsulates a file chooser that gets everything in the indicated directory
 */
public class AllInDirectoryFileChooser implements IFileChooser {
	
	public static class MyFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			File file = new File(dir + System.getProperty("file.separator") + name);
			if (file.isDirectory()) return false;
			if (!file.exists()) return false;
			if (!name.endsWith(".java")) return false;
			
			File classFile = new File(dir + System.getProperty("file.separator") + name.substring(0, name.length()-5) + ".class");
			if (!classFile.exists()) {
				return false;
			}
			return true;
		}
	}
	
	private String _root;
	
	public void setRoot(String root) {
		File file = new File(root);
		if (!file.isDirectory()) throw new IllegalArgumentException("File is not a directory!");
		if (!file.exists()) throw new IllegalArgumentException("Directory does not exist!");
		_root = root;
	}
	
	public Enumeration getFiles() {
		File file = new File(_root);
		String[] sourceFiles = file.list(new MyFilter());
		Vector v = new Vector(sourceFiles.length);
		for (int i=0; i<sourceFiles.length; i++) {
			FilePair pair = new FilePair(new File(_root + System.getProperty("file.separator") + sourceFiles[i]), new File(_root + System.getProperty("file.separator") + sourceFiles[i].substring(0, sourceFiles[i].length()-5) + ".class"));
			v.addElement(pair);
		}
		return v.elements();
	}}
