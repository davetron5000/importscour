package net.sourceforge.importscrubber;

public class ConsoleProgressMonitor implements IProgressMonitor

{

    public void taskStarted(ScrubTask task)
    {

        System.out.print(".");

    }

    public void taskComplete(ScrubTask task)
    {}

}












