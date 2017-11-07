package ru.ncedu.mironova.comparator;
/**
 * Class Comparator - comparison of two ZIP archives
 * @author Liliya Mironova
 */

import java.io.*;
import java.util.zip.*;
import java.util.*;
import java.io.IOException;
import javax.swing.*;
import java.security.*;

public class Comparator {
	final private static String ALGO = "MD5"; // algorithm of encoding with which we will find hash of a file
	
	/**
	 * Function which compares two ZIP archives
	 * @param lhs is a first archive
	 * @param rhs is a second archive 
	 */
	public static void compareZips(File lhs, File rhs) {
		try { 
			PrintWriter pw = new PrintWriter(new File("output.txt")); // a file in which we will write a report

			ZipFile lZip = new ZipFile(lhs);
			ZipFile rZip = new ZipFile(rhs);

			pw.println(lZip.getName().substring(lZip.getName().lastIndexOf(File.separator) + 1) + " | " + rZip.getName().substring(rZip.getName().lastIndexOf(File.separator) + 1));
			pw.println("--------------------------------------------------");
			int flag;
		    
		    ArrayList<ArrayList<Integer>> used = new ArrayList<>();
		    Enumeration<? extends ZipEntry> rEntries5 = rZip.entries();
		    while (rEntries5.hasMoreElements()) {
		       	ZipEntry rEntry = rEntries5.nextElement();
		       	used.add(new ArrayList<Integer>());
		    }

		    Enumeration<? extends ZipEntry> lEntries = lZip.entries();  
		    while (lEntries.hasMoreElements()) {
			    flag = 0;
		        ZipEntry lEntry = lEntries.nextElement();
				String lHash = getMessageDigest(lZip.getInputStream(lEntry), ALGO);

		        Enumeration<? extends ZipEntry> rEntries = rZip.entries();
		        for (int i = 0; rEntries.hasMoreElements() && flag == 0; i++) {
		       		ZipEntry rEntry = rEntries.nextElement();
					String rHash = getMessageDigest(rZip.getInputStream(rEntry), ALGO);
		        	if (lEntry.getName().equals(rEntry.getName())) {
		        		if (lEntry.getSize() == rEntry.getSize() && lHash.equals(rHash)) {
		        		   	pw.println("  " + lEntry.getName() + "    not changed");
		        		} else {
		        			pw.println("* " + lEntry.getName() + "   | * " + rEntry.getName());
		        		}
			        	used.get(i).add(1);
			        	flag = 1;
			        }
		        }

		    	Enumeration<? extends ZipEntry> rEntries2 = rZip.entries();
			    for (int i = 0; rEntries2.hasMoreElements() && flag == 0; i++) {
			        ZipEntry rEntry = rEntries2.nextElement();
				    if (lEntry.getSize() == rEntry.getSize()) {
			        	pw.println("? " + lEntry.getName() + "   | ? " + rEntry.getName());
			        	used.get(i).add(1);
			        	flag = 1;
				    }
			    }
			    if (flag == 0) {
			       	pw.println("- " + lEntry.getName() + "   |");
			    }
			}

			Enumeration<? extends ZipEntry> rEntries3 = rZip.entries();
		    for (int i = 0; rEntries3.hasMoreElements(); i++) {
		    	 ZipEntry rEntry = rEntries3.nextElement();
			    if (used.get(i).isEmpty()) {
			    	pw.println("          | + " + rEntry.getName());
			    }
		    }

			pw.close();
		    lZip.close();
		    rZip.close();
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function which finds a hash of a file
	 * @param input is a stream of a file to hash
	 * @param algo is an algorithm of encoding
	 * @return hash
	 */
	public static String getMessageDigest(InputStream input, String algo) {
		if (algo == null || algo.trim().length() == 0 || input == null) {
			return null;
		}

		MessageDigest md = null;
		StringBuilder sb = null;
		try {
			md = MessageDigest.getInstance(algo);
			byte[] dataBytes = new byte[input.available()];
		    int bytesRead = 0;
			while ((bytesRead = input.read(dataBytes)) != -1) {
		        md.update(dataBytes, 0, bytesRead);
		    }

		    byte[] mdBytes = md.digest();
		    sb = new StringBuilder();
		    for (int i = 0; i < mdBytes.length; i++) {
		    	sb.append(Integer.toString((mdBytes[i] & 0xfff) + 0x100, 16).substring(1));
		    }

		    input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		if (sb != null) {
			return sb.toString();
		} else {
			return null;
		}
	}

	/**
	 * Function which shows JFileChooser window to help user to choose two ZIP archives one by one
	 */
	public static void select2FromChooser() {
		File file1 = null;
		JFileChooser fileOpen1 = new JFileChooser();
		fileOpen1.setMultiSelectionEnabled(false);
		int ret1 = fileOpen1.showDialog(null, "Open first file");
		while (ret1 != JFileChooser.APPROVE_OPTION) {
			fileOpen1.showDialog(null, "Open first file again");
		}
		file1 = fileOpen1.getSelectedFile();
		
		File file2 = null;
		JFileChooser fileOpen2 = new JFileChooser();
		fileOpen2.setMultiSelectionEnabled(false);
		int ret2 = fileOpen2.showDialog(null, "Open second file");
		while (ret2 != JFileChooser.APPROVE_OPTION) {
			fileOpen2.showDialog(null, "Open second file again");
		}
		file2 = fileOpen2.getSelectedFile();
		
		compareZips(file1, file2);
	}
}