package net.sourceforge.importscrubber;

import java.util.Enumeration;

/**
 * This interface defines the behavior of something that can pick files
 */
public interface IFileChooser {
	
	/**
	 * @return an Enumeration of FilePair objects
	 */
	public Enumeration getFiles();
	
	/**
	 * @param root the root of the file tree
	 */
	public void setRoot(String root);
}
