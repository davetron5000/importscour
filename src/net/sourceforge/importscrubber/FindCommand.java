package net.sourceforge.importscrubber;/**

 * Encapsulates the "find files" Command (GOF p 233)

 */public class FindCommand extends Thread {    private ImportScrubberGUI receiver;    public FindCommand(ImportScrubberGUI receiver) {        this.receiver = receiver;    }    public void run() {        receiver.find();    }}