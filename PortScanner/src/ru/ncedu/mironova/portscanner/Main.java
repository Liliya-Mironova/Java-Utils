package ru.ncedu.mironova.portscanner;

public class Main {

	public static void main(String[] args) {
		if (args.length == 0) {
			PortScanner.printHelp();
			return;
		}
		
		PortScanner ps = new PortScanner();
		ps.scan(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
	}
}
