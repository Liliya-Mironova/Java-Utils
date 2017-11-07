package ru.ncedu.mironova.urldownloader;

public class Main {

	public static void main(String[] args) {
		if (args.length == 0) {
			URLDownloader.printHelp();
			return;
		}
		
		URLDownloader ud = new URLDownloader();
		ud.download(args);
	}
}