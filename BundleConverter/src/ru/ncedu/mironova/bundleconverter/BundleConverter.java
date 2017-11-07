package ru.ncedu.mironova.bundleconverter;

/**
 * Class BundleConverter - converting 
 */
import java.util.*;
import java.io.*;

public class BundleConverter {
//	
//	/**
//	 * Default constructor
//	 */
//	BundleConverter() {}
	
	/**
	 * 
	 */
	public static void convert(String text) {
//		ResourceBundle stats = ResourceBundle.getBundle("StatsBundle", currentLocale);
//		Double lit = (Double)stats.getObject("Literacy");
// ResourceBundle.getBundle(имя_набора).getString(ключ_строки);
		
		String name = text.substring(0, text.lastIndexOf('_'));
		String bundleName = "ru.ncedu.mironova.bundleconverter." + name;
		String localeName = text.substring(text.lastIndexOf('_') + 1, text.lastIndexOf('.'));
		System.out.println(bundleName + " " + localeName);
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File(name + ".properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Locale locale = new Locale(localeName);
		ResourceBundle labels = ResourceBundle.getBundle(bundleName, locale);
		Enumeration bundleKeys = labels.getKeys();

		while (bundleKeys.hasMoreElements()) {
		    String key = (String)bundleKeys.nextElement();
		    String value = labels.getString(key);
		    pw.println(key + " = " + value);
		}
		pw.close();
		
	}
}
