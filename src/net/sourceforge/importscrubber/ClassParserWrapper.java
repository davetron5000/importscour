package net.sourceforge.importscrubber;

import com.ibm.toad.cfparse.ClassFile;
import com.ibm.toad.cfparse.ConstantPool;
import com.ibm.toad.cfparse.ConstantPoolEntry;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

/**
 * This class encapsulates whatever utility we are using to parse the class file
 */
public class ClassParserWrapper {
	
	public static void parseWithCFParse(File file, IReferenceFoundListener aListener) throws IOException, FileNotFoundException {
		// read in and parse the class file
		FileInputStream fis = new FileInputStream(file);
		ClassFile cf = new ClassFile(fis);
		fis.close();
		
		// get the referenced classes
		ConstantPool cp = cf.getCP();
		for(int i = 0; i<cp.length(); i++) {
			ConstantPoolEntry cpe = cp.get(i);
			if (cpe == null) {
				continue;
			}
			String ref = cpe.getAsJava();
			int type = cp.getType(i);

			//if (ref.indexOf("OutputStream") != -1) System.out.println("tyep = " + type + " ref = " + ref);
			
			if (type == ConstantPool.CONSTANT_Class && !ref.startsWith("java.lang")) {
				if (ImportScrubber.DEBUG) System.out.println("Class file parser found class " + ref);
				aListener.referenceFound(ref);
				continue;
			} 

			// CFParse doesn't seem to put interface references in the CONSTANT_Class list; why?
			// anyhow, here's where we parse them out
			if ((type == ConstantPool.CONSTANT_Fieldref || type == ConstantPool.CONSTANT_Methodref || type == ConstantPool.CONSTANT_InterfaceMethodref) && !ref.startsWith("java.lang") && ref.indexOf(" ") != -1) {
				String name = ref.substring(0, ref.indexOf(" "));
				if (name.endsWith("[]")) {
					name = name.substring(0, name.length()-2);
				}
				if (name.equalsIgnoreCase("char") || name.equalsIgnoreCase("float") || name.equalsIgnoreCase("byte") || name.equalsIgnoreCase("boolean") || name.equalsIgnoreCase("long") || name.equalsIgnoreCase("void") || name.equalsIgnoreCase("int") || name.equalsIgnoreCase("double")) {
					continue;
				}
				if (ImportScrubber.DEBUG) System.out.println("Class file parser found other class " + name);
				aListener.referenceFound(name);
			}
			
			if (type == 1/*ConstantPool.Utf8Entry*/) {
				// here we're parsing something like this: "Ljava/io/File;" 
				// or
				// "class$com$rokutech$esd$shared$UpdateInfoResponse"
				if (ref.indexOf("class$") != -1 ) {
					int firstDol = ref.indexOf("$");
					String name = ref.substring(firstDol + 1, ref.length()-1);
					// remove $
					StringBuffer buf = new StringBuffer(name);
					for (int j=0; j< buf.length(); j++) {
						char c = buf.charAt(j);
						if (c == '$') {
							buf.deleteCharAt(j);
							buf.insert(j, '.');
						}
					}
					if (buf.toString().trim().equals("")) {
						continue;
					}
					aListener.referenceFound(buf.toString());
					continue;														
				}
				if (ref.indexOf("Ljava/lang") != -1) {
					continue;
				}
				int firstL = ref.indexOf("L");
				if (firstL == -1) {
					continue;
				}
				String name = ref.substring(firstL + 1);
				int semi = name.indexOf(";");
				if (semi == -1) {
					continue;
				}
				name = name.substring(0, semi);
				// remove /
				StringBuffer buf = new StringBuffer(name);
				for (int j=0; j< buf.length(); j++) {
					char c = buf.charAt(j);
					if (c == '/') {
						buf.deleteCharAt(j);
						buf.insert(j, '.');
					}
				}
				if (ImportScrubber.DEBUG) System.out.println("Class file parser found utf8 entry " + buf.toString());
				aListener.referenceFound(buf.toString());
				continue;
			}
			
		}
	}
}
