package net.sourceforge.importscrubber;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Encapsulates the "go" Command (GOF p 233)
 */
public class GoCommand implements ActionListener
{
   private ImportScrubberGUI receiver;
   public GoCommand(ImportScrubberGUI receiver) {
      this.receiver = receiver;
   }
   public void actionPerformed(ActionEvent e) 
   {
      receiver.go();
   }
}

