package net.sourceforge.importscrubber.filechooser;import java.util.List;/**
 * This interface defines the behavior of something that can pick files
 */public interface IFileChooser {    /**        
 * @return a List of FilePair objects        
 */    public List getFiles();    /**        
 * @param root the root of the file tree        
 */    public void setRoot(String root);}