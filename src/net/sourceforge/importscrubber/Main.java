package net.sourceforge.importscrubber;

/**
 * This class encapsulates the startup point for importscrubber
 */
public class Main {
	
	public static void main(String[] args) {
		
		try {
			
			ImportScrubber scrubber = null;
			if (args.length == 1) {
				scrubber = new ImportScrubber(args[0]);
			} else {
				scrubber = new ImportScrubber();
			}
			scrubber.scrub();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
