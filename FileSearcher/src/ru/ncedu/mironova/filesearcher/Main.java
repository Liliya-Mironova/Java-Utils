package ru.ncedu.mironova.filesearcher;

public class Main {

	// args format:
	// arg[0] is a regexp
	// date - if you want to show the date of last modification of file
	// size - if you want to show size of file
 	public static void main(String[] args) {
	    if (args.length == 0) {
			FileSearcher.printHelp();
			return;
		}
	    
		FileSearcher fs = new FileSearcher(); 
 		fs.search(System.getProperty("user.dir"), args);
	}
}