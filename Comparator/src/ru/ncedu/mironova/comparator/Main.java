package ru.ncedu.mironova.comparator;

import java.io.File;

public class Main {
	
	// arguments: args[0] is a first zip archive name, arg[1] is a second zip archive name
	// no or not sufficient number of args shows filechooser window
    public static void main(String[] args) {
    	if (args.length >= 2) {
    		Comparator.compareZips(new File(args[0]), new File(args[1]));
    	} else {
    		Comparator.select2FromChooser();
		}
    }
}
