package net.sourceforge.importscrubber;



import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;



/**

 * Encapsulates the "find files" Command (GOF p 233)

 */

public class FindCommand extends Thread

{

   private ImportScrubberGUI receiver;

   public FindCommand(ImportScrubberGUI receiver) {

      this.receiver = receiver;

   }

   public void run()
   {
       receiver.find();
   }
}



