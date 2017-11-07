package ru.ncedu.mironova.filesearcher;

/**
 * Class FileSearcher - searching for files by regular expression
 * @author Liliya Mironova <theflower86@mail.ru>
 */

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileSearcher {

	/**
	 * Function which prints how to use File Searcher
	 */
	public static void printHelp() {
		System.out.println("File Searcher");
		System.out.println("To search files:   regexp date(optionally) size(optionally)");
	}
	
	/**
	 * Default constructor
	 */
	FileSearcher() {}
	
	/**
	 * Searches for files in the entered folder and its subfolders which names correspond to the entered regular expression
	 * @param folderPath is the path to the folder (user.dir default)
	 * @param args: arg[0] is a regexp, arg[1] and arg[2] may be date and/or size
	 */
	public void search(String folderPath, String[] args) {
		
		/**
		 * Class which describes a filter
		 */
		class MyFilter implements FileFilter {
			public String text;
			
			MyFilter(String text_) {
				text = text_;
			}
			
			@Override
			public boolean accept(File filename) {
				if (text.equals("*")) {
					return true;
				}
				if (!filename.isDirectory()) {
					return filename.getName().matches(text);					
				} else {
					search(filename.getPath(), args);
					return false;
				}
			}
		}
		
		File dir = new File(folderPath);
		File[] files = dir.listFiles(new MyFilter(args[0]));
		if (files == null) {
			return;
		}
		
		for (File f : files) {
			if (f.isDirectory()) {
				System.out.print("dir:   ");
			} else {
				System.out.print("file:  ");
			}
			Formatter fmt = new Formatter();
			System.out.print(fmt.format("%15s %80s           ", f.getName(), f.getPath()));

			for (int i = 1; i < args.length; i++) {
				if (args[i].equals("date")) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
					System.out.print(sdf.format(f.lastModified()));
				}
			
				if (args[i].equals("size")) {
					Formatter fmt2 = new Formatter();
					System.out.print(fmt2.format("%10d bytes          ", f.length()));
				}
			}
			System.out.println();
		}
	}
	
//------------------------------------------------------------------------------------		
//Second variant of decision, without FileFilter
//
//	public void search(String folderPath, String[] args) {
//		File dir = new File(folderPath);
//		File[] files = dir.listFiles();
//		if (files.length != 0) {
//			for (File f : files) {
//				if (f.isDirectory()) {
//					search(f.getPath(), args);
//				}
//				if ((args[0].equals("*")) || f.getName().matches(args[0])) {
//					if (f.isDirectory()) {
//						System.out.print("dir:   ");
//					} else {
//						System.out.print("file:  ");
//					}
//					Formatter fmt = new Formatter();
//					System.out.print(fmt.format("%15s %80s           ", f.getName(), f.getPath()));
//					
//					for (int i = 1; i < args.length; i++) {
//						if (args[i].equals("date")) {
//							SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
//							System.out.print(sdf.format(f.lastModified()));
//						}
//						
//						if (args[i].equals("size")) {
//							Formatter fmt2 = new Formatter();
//							System.out.print(fmt2.format("%10d bytes          ", f.length()));
//						}
//					}
//					System.out.println();
//				}
//			}
//		}
//	}
}
