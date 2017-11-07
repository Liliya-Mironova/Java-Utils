package ru.ncedu.mironova.urldownloader;

import java.net.*;
import java.util.Scanner;
import java.awt.Desktop;
import java.io.*;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class URLDownloader {
	
	/**
	 * Function which prints how to use downloader
	 */
	public static void printHelp() {
		System.out.println("URL Downloader");
		System.out.println("To download files:      url filename(optionally) {open}(optionally)");
	}
	
	/**
	 * Default constructor
	 */
	URLDownloader() {}
	
	/**
	 * Function which downloads a file by URL
	 * @param text - text[0] is URL, text[1] is a new file name (optionally)
	 */
	public void download(String[] text) {
		// select a name of a file
		String normname = null;
    	int indexname = text[0].lastIndexOf("/");
        if (text[0].lastIndexOf("/") == text[0].length() - 1) {
            normname = text[0].substring(0, indexname);
        } else {
        	normname = text[0];
        }
        indexname = normname.lastIndexOf("/");
        normname = normname.substring(indexname + 1, normname.length());
		if (normname.contains("?")) {
			normname = normname.substring(0, normname.lastIndexOf('?'));
		}
		
		String normNamePath = normname;
		String contentType = null;
		int flagToOpen = 0;
		
		// save
		try {
			URL url = new URL(text[0]);
			URLConnection uc = url.openConnection();
			contentType = uc.getContentType();
			
			try {
				InputStream ins = url.openStream();
				if (text.length != 1) {
					for (int i = 1; i < text.length; i++) {						
						if (!text[i].equals("open")) {
							File file = new File(text[i]);	

							if (!file.exists()) { // there is no such file in user.dir, create a new file with default name
								write(ins, new FileOutputStream(normname));

							} else {											
								if (file.isDirectory()) { // our file is a directory, add new file in it
									File newFile = new File(text[i] + File.separator + normname);
									normNamePath = newFile.getCanonicalPath();								
									
									if (!newFile.exists()) { // there is no such file in our directory, create a new file with default name in it
										write(ins, new FileOutputStream(newFile));
									
									} else { // there is a file with the same name in our directory, so:
										System.out.println("Do you want to replace \"" + normname + "\"? Y/N");
								        Scanner input = new Scanner(System.in);
								        String reply = input.nextLine();
								        input.close();
								        
								        if (reply.equals("Y") || reply.equals("y")) { // we want to replace the file
								        	newFile.delete();
								        	newFile = new File(normNamePath);
								        	write(ins, new FileOutputStream(newFile));
								       
								        } else if (reply.equals("N") || reply.equals("n")) { // we want to create a new file with the similar name
								        	String newName = null;
								        	if (!contentType.contains(".")) {
								        		newName = normNamePath + "(1)";
								        	} else {
								        		newName = normNamePath.substring(0, normNamePath.lastIndexOf('.')) + "(1)" + normNamePath.substring(normNamePath.lastIndexOf('.'));
								        	}
								        	write(ins, new FileOutputStream(new File(newName)));
								        }
									}			
								
								} else { // there is a file with the same name to our in user.dir, so:
							        System.out.println("Do you want to replace \"" + text[i] + "\"? Y/N");
							        Scanner input = new Scanner(System.in);
							        String reply = input.nextLine();
							        input.close();
							        
							        if (reply.equals("Y") || reply.equals("y")) { // we want to replace the file
							        	file.delete();
							        	file = new File(text[i]);
							        	write(ins, new FileOutputStream(file));
							        
							        } else if (reply.equals("N") || reply.equals("n")) { // we want to create a new file with the similar name
							        	String newName = null;
							        	if (!contentType.contains(".")) {
							        		newName = text[i] + "(1)";
							        	} else {
							        		newName = text[i].substring(0, text[i].lastIndexOf('.')) + "(1)" + text[i].substring(text[i].lastIndexOf('.'));							        	
							        	}
						        		write(ins, new FileOutputStream(new File(newName)));
							        }
								}
							}
						} else {
							flagToOpen = 1;
						}
					}
				} else { // we didn't enter anything except url, so we will save new file in user.dir
					File file = new File(normname);
					
					if (!file.exists()) {
						write(ins, new FileOutputStream(System.getProperty("user.dir") + File.separator + normname));
					
					} else { // there is a file with the same name in user.dir, so:
						 System.out.println("Do you want to replace \"" + normname + "\"? Y/N");
					     Scanner input = new Scanner(System.in);
					     String reply = input.nextLine();
					     input.close();
					     
					     if (reply.equals("Y") || reply.equals("y")) { // we want to replace the file
					      	file.delete();
					       	file = new File(normname);
					       	write(ins, new FileOutputStream(file));
					     
					     } else if (reply.equals("N") || reply.equals("n")) { // we want to create a new file with the similar name
					        String newName = null;
					        if (!contentType.contains(".")) {
					        	newName = normname + "(1)";
					        } else {
					        	newName = normname.substring(0, normname.lastIndexOf('.')) + "(1)" + normname.substring(normname.lastIndexOf('.'));							        	
					        }
					    	write(ins, new FileOutputStream(new File(newName)));
					     }
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// if it is an html page, save all images and links if a folder called filename_files
		if (contentType.contains("html")) {
			Document doc = null;
			File contentDir = new File(normname + "_files");
			contentDir.mkdir();
		    try {
		    	doc = Jsoup.connect(text[0]).get();
		    	
	            Elements imgs = doc.getElementsByTag("img");
	            for (Element el : imgs) {
	                String src = el.absUrl("src");
	                getContent(src, contentDir);
	                
	            }
	            
	            Elements links = doc.getElementsByTag("link");
	            for (Element el : links) {
	                String src = el.absUrl("href");
	                getContent(src, contentDir);
	                
	            }
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		
		// open file 		
		if (flagToOpen == 1 && Desktop.isDesktopSupported()) {
			try {
				Desktop desktop = Desktop.getDesktop();
				File myFile = new File(normNamePath);
				desktop.open(myFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Function which downloads all resources from an html page
	 * @param src is an url of resource
	 * @param contentDir is a folder to save
	 */

    private void getContent(String src, File contentDir) {
		String normname;
		
		// extract a name from an url
		int indexname = src.lastIndexOf("/");
        if (src.lastIndexOf("/") == src.length() - 1) {
            normname = src.substring(0, indexname);
        } else {
        	normname = src;
        }
        indexname = normname.lastIndexOf("/");
        normname = normname.substring(indexname + 1, normname.length());
        if (normname.contains("?")) {
			normname = normname.substring(0, normname.lastIndexOf('?'));
		}
		
        // download
		URL url;
        InputStream ins = null;
		try {
			url = new URL(src);
			ins = url.openStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

        try {
			write(ins, new FileOutputStream(new File(contentDir + File.separator + normname)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
	
    /** 
     * Function which writes from input stream to output stream
     * @param input is InputStream
     * @param fos is OutputStream
     */
    private static void write(InputStream input, OutputStream fos) {  
        try {
        	byte[] buffer = new byte[1024];
	        int bytesRead;
	        while ((bytesRead = input.read(buffer)) != -1) {
	            fos.write(buffer, 0, bytesRead);
	        }
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
}