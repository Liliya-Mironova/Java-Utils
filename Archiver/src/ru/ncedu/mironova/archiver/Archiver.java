package ru.ncedu.mironova.archiver;

/**
 * Class Archiver - packing, unpacking and adding files to an existing archive 
 * @author Liliya Mironova
 */

import java.io.*;
import java.util.zip.*;
import java.util.*;

public class Archiver {
	private List<File> list = null;
	
	/**
	 * Function which prints how to use Archiver
	 */
	public static void printHelp() {
		System.out.println("ZIP archiver");
		System.out.println("To pack files:      {p|pack} zipname file1/folder1 file2/folder2...");
		System.out.println("To unpack ZIP:      {u|unpack} zipname");
		System.out.println("To add files:       {a|add} zipname folder file1/folder1 file2/folder2...");
		System.out.println("To write a comment: {w|writecomment} zipname comment");
		System.out.println("To read a comment:  {r|readcomment} zipname");
	}
	
	/**
	 * Function which packs files to ZIP archive
	 * @param output is a name of archive in which we pack files
	 * @param sources is a list of files
	 */
    public void packZip(String[] filenames) {
		List<File> sources = new ArrayList<File>();
	    for (int i = 2; i < filenames.length; i++) {
		   sources.add(new File(filenames[i]));
	    }
    	
        try {
	    	ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(new File(filenames[1])));
	        zout.setLevel(Deflater.DEFAULT_COMPRESSION);
	
	        for (File source : sources) {
	            if (source.isDirectory()) {
	                zipDir(zout, "", source);
	            } else {
	                zipFile(zout, "", source);
	            }
	        }
	
	        zout.flush();
	        zout.close();
        } catch(IOException e) {
        	e.printStackTrace();
        }
    }
    
    /**
     * Utility function which makes a path to create a file in a folder
     * @param path is a foldername
     * @param file is a filename
     * @return
     */
    private String buildPath(String path, String file) {
        if (path == null || path.isEmpty()) {
            return file;
        } else {
            return path + File.separator + file;
        }
    }
    
    /**
     * Recursive function to pack a folder to an archive
     * @param zout
     * @param path
     * @param dir
     */

    private void zipDir(ZipOutputStream zout, String path, File dir) {
        try {
        	if (!dir.canRead()) {
	            System.out.println("Cannot read " + dir.getCanonicalPath());
	            return;
	        }
	
	        File[] files = dir.listFiles();
	        path = buildPath(path, dir.getName());
	
	        for (File source : files) {
	            if (source.isDirectory()) {
	                zipDir(zout, path, source);
	            } else {
	                zipFile(zout, path, source);
	            }
	        }
        } catch(IOException e) {
        	e.printStackTrace();
        }
    }

    /**
     * Function which packs a file to an archive, an end of probable recursion
     * @param zout
     * @param path
     * @param file
     */
    private void zipFile(ZipOutputStream zout, String path, File file) {
        try {
        	if (!file.canRead()) {
	            System.out.println("Cannot read " + file.getCanonicalPath());
	            return;
	        }
	
	        zout.putNextEntry(new ZipEntry(buildPath(path, file.getName()))); // add file to archive
	        FileInputStream fis = new FileInputStream(file); // закинем файл в поток
	        write(fis, zout); // copy the contents of the stream to the entry in the archive
	
	        zout.closeEntry();
	
	        fis.close();
	        zout.closeEntry();
        } catch(IOException e) {
        	e.printStackTrace();
        }
    }
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------

    /**
     * Function which unpacks ZIP archive
     * @param input: input[0] is a name of zip to unpack
     */
    public void unpackZip(String input) {
        try {
	        ZipFile file = new ZipFile(input);
	        list = new ArrayList<File>();
	
	        Enumeration<? extends ZipEntry> entries = file.entries(); 
	        while (entries.hasMoreElements()) {
	            ZipEntry entry = entries.nextElement();
	            write(file.getInputStream(entry), new FileOutputStream(entry.getName()));
	            list.add(new File(entry.getName()));
	        }
	        file.close();
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }

    /** 
     * Function which writes from input stream to output stream
     * @param input is InputStream
     * @param fos is OutputStream
     */
    private void write(InputStream input, OutputStream fos) {  
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

    /**
     * Function which adds files to an existing zip archive
     * @param newFiles is an array of files which we want to add 
     */
    public void addToZip(String[] newFiles) {
        try {
	    	File tmp = new File("tmp.zip");
	        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(tmp));
	
	        unpackZip(newFiles[1]);
	        
	        List<File> newList = new ArrayList<File>();
	        for (int i = 2; i < newFiles.length; i++) {
	        	newList.add(new File(newFiles[i]));
	        }
	
	        for (File source : list) {
	            if (source.isDirectory()) {
	                zipDir(zout, "", source);
	            } else {
	                zipFile(zout, "", source);
	            }
	        }
	
	        for (File source : newList) {
	            if (source.isDirectory()) {
	                zipDir(zout, "", source);
	            } else {
	                zipFile(zout, "", source);
	            }
	        }
	
	        File out = new File(newFiles[1]);
	        out.delete();
	        tmp.renameTo(out);
	
	        zout.flush();
	        zout.close();
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }

	/**
	 * Function which sets the ZIP file comment
	 * @param input is a ZIP file name
	 * @param comment is a comment which we write
	 */
    public void writeComment (String input, String comment) {
        ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(new FileOutputStream(input));
			zos.setComment(comment);
			try {
				zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Function gets a comment
     * @param input is a name of ZIP archive
     * @return comment
     */
    public String readComment (String input) {
        ZipFile file = null;
        String comment = null;
		try {
			file = new ZipFile(input);
	        comment = file.getComment();
	        file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return comment;
    }
}
