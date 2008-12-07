package net.sourceforge.importscrubber;

public class ConsoleProgressMonitor implements IProgressMonitor
{
    private int filesProcessed;

    public void taskStarted(ScrubTask task)
    {
        System.out.print(".");
    }

    public void taskComplete(ScrubTask task)
    {
        filesProcessed++;
        System.out.flush();
    }

    public int getFilesProcessed() { return filesProcessed; }
}














