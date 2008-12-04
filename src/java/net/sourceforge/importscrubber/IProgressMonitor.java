package net.sourceforge.importscrubber;

/**
 
 * Defines the behavior of by an object which wishes 
 
 * to be notified when a task is started or completed.
 
 */

public interface IProgressMonitor

{

    public void taskStarted(ScrubTask task);

    public void taskComplete(ScrubTask task);

}














