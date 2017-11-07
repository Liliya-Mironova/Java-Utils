package ru.ncedu.mironova.portscanner;

/**
 * Class PortScanner - scanning the ports of the specified server to 
 * determine if each port is open to receive incoming connections (TCP).
 * @author Liliya Mironova
 */

import java.io.IOException;
import java.net.*;

public class PortScanner {
	/**
	 * Function which prints how to use Port Scanner
	 */
	public static void printHelp() {
		System.out.println("Port Scanner");
		System.out.println("To scan ports of domain in range of begin to end enter:     address begin end");
	}
	
	/**
	 * Default constructor
	 */
	PortScanner() {}
	
	/**
	 * scanning the ports to determine if each port is open.
	 * @param address is IP or domain
	 * @param beg is a begin port to scan
	 * @param end is an end port to scan
	 */
	public void scan(String address, int beg, int end) {
		
		for (int i = beg; i <= end; i++) {
			try {
				Socket ServerSock = new Socket(address, i);
				System.out.println("Port in use:" + i);
				ServerSock.close();
			} catch (IOException e) {
				System.out.println("Port not in use:" + i);
			}
		}
	}
		
// Second decision
//	public void scan(String address, int beg, int end) {
//		for (int i = beg; i <= end; i++) {
//			try {
//				Socket socket = new Socket();
//				socket.connect(new InetSocketAddress(address, i));
//				socket.close();
//				System.out.println("Port in use:" + i);
//			} catch (Exception e) {
//				System.out.println("Port not in use:" + i);
//			}
//		}
//	}
}
