package net.sourceforge.importscrubber;



import java.awt.Component;

import java.awt.Font;

import javax.swing.BorderFactory;

import javax.swing.DefaultListCellRenderer;

import javax.swing.JList;

import javax.swing.ListCellRenderer;

import javax.swing.border.Border;



/**

 * Encapsulates the appearance of an item in the file list.

 */

public class WorkingCellRenderer extends DefaultListCellRenderer implements ListCellRenderer {



   private final Font _bold = new Font("Arial", Font.BOLD, 10);

   private final Font _normal = new Font("Arial", Font.PLAIN, 10);



   public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

      super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);

      if (isSelected) {

         setBorder(BorderFactory.createRaisedBevelBorder());

         setFont(_bold);

      } else {

         setBorder(BorderFactory.createEmptyBorder());

         setFont(_normal);

      }

      return this;

   }

}



