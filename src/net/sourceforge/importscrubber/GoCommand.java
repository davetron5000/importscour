package net.sourceforge.importscrubber;


import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;


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



