/**
 * This is a place where people put copyright headers - these need to be preserved
 */
import foo.Buz;

import java.awt.Component;

import java.io.File;
import java.io.*;
import java.io.IOException;

import java.lang.reflect.Modifier;

import java.net.*;
import java.net.URL;
import java.net.URLConnection;

import java.util.*;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * This class encapsulates an piece of code on which to run importscrubber
 */
public class FunctionalTest implements Runnable
{
   public static class Bar extends JTextField
   {
      private static final FilePermission BIF = new FilePermission("/etc/passwd", "foo"); 
      private static final FilePermission BIF2 = new FilePermission("Hello world", "bar"); 
   }

   public class Foo extends JLabel {
      public String getText() {
         return super.getText();
      }
   }

   private static final ArrayList[] STATIC_FOO = new ArrayList[0];
   private HashSet set = new HashSet();

   public FunctionalTest() throws IOException {
      Vector v = new Vector();
      for (Enumeration e = v.elements(); e.hasMoreElements();)
      {
      }
      IOException e = new IOException();

      // this is to test excluding literals
      String buz = "text/plain";
      
      // this is to test array handling
      ArrayList[] foo = new ArrayList[0];
      ArrayList bar = new ArrayList();
      bar.add(HashSet.class);
      bar.add(Integer.class);
      File[] files = (File[])bar.toArray(new File[0]);

      // this is to test imports of inner classes
      HashMap biv = new HashMap();
      Set set = biv.entrySet();
      Iterator iter = set.iterator();
      bar.add((Entry)iter.next());

      // this is to test inclusion of classes from java.lang.reflect
      Modifier m = new Modifier();

      // This next one can't be picked up by importscrubber because the compiler inlines it
      // System.out.println("A JOptionPane thingy " + JOptionPane.CANCEL_OPTION);
      // bummer!

      // this is to test importing a class and only calling a static method on it
      Buz.doSomething();

      // this is to test NOT importing classes which are fully qualified in the class body
      java.sql.Date sqlDate = new java.sql.Date(20);
      Date javaDate = new Date();

      throw new IllegalArgumentException();
   }

   // this is to test method argument/return value types
   public SocketOptions bar(URL aURL, URLConnection aConn) {
      return null;
   }

   public Component getComp() {
      return null;
   }

   public void run() {}
}
