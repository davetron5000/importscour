package net.sourceforge.importscrubber;


/**

 * Encapsulates the "go" Command (GOF p 233)

 */
public class GoCommand extends Thread {

    private ImportScrubberGUI receiver;

    public GoCommand(ImportScrubberGUI receiver) {

        this.receiver = receiver;

    }

    public void run() {

        receiver.go();

    }

}



