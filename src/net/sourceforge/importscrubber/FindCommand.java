package net.sourceforge.importscrubber;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Encapsulates the "find files" Command (GOF p 233)
 */
public class FindCommand implements ActionListener
{
   private ImportScrubberGUI receiver;
   public FindCommand(ImportScrubberGUI receiver) {
      this.receiver = receiver;
   }
   public void actionPerformed(ActionEvent e) 
   {
      receiver.find();
   }
}

