package ru.ncedu.mironova.archiver;

public class Main {
	
	public static void main(String[] args) {
	    if (args.length == 0) {
			Archiver.printHelp();
			return;
		}
	    
	    Archiver arch = new Archiver();
	    if (args[0].equals("p") || args[0].equals("pack")) {
	    	arch.packZip(args);
	    } else if (args[0].equals("u") || args[0].equals("unpack")) {
	    	arch.unpackZip(args[1]);
	    } else if (args[0].equals("a") || args[0].equals("add")) {
	    	arch.addToZip(args);
	    } else if (args[0].equals("w") || args[0].equals("writecomment")) {
	    	arch.writeComment(args[1], args[2]);
	    } else if (args[0].equals("r") || args[0].equals("readcomment")) {
	    	String com = arch.readComment(args[1]);
	    	if (com != null) {
		    	System.out.println(com);
	    	} else {
	    		System.out.println("no comment");
	    	}
		} else {
			Archiver.printHelp();
		}
	}
}