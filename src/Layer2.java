import java.util.Scanner;

import jpcap.packet.*;

public class Layer2 extends Layer{
	
	byte[] sourceMAC;
	byte[] broadcastMAC = hexStringToByteArray("FFFFFFFFFFFF");

	public void configuration() {
		try {
			
			endTime = false;
			
			//Ask the user to use his own MAC address or another
			int userRespond= 0;
			System.out.println("\nTo use your own MAC press 1, to indroduce other MAC address press 2");
			@SuppressWarnings("resource")
			Scanner input = new Scanner(System.in);
			userRespond= input.nextInt();
	
			if(userRespond==1) {
				this.sourceMAC = ((Layer1) down).getMacAdress();
			}
			else if(userRespond==2) {
				
				System.out.println("\nIntroduce the MAC address in Hexadecimal separate by ':' :");
				@SuppressWarnings("resource")
				Scanner input2 = new Scanner(System.in);
				String stringAux = input2.next();
				String[] parts = stringAux.split(":");
				String part1 = parts[0]; 
				String part2 = parts[1];
				String part3 = parts[2];
				String part4 = parts[3];
				String part5 = parts[4];
				String part6 = parts[5];
				this.sourceMAC = hexStringToByteArray(parts.toString());
				
			}
			else {
				System.out.println("Invalid Respond");
			}
		}catch (ArrayIndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Invalid MAC Address");
		}
	}
	public void run() {

		try {

			while(!endTime || !misPaquetes.isEmpty()) {
				
				miSemaforo.acquire();
				CustomPacket cp = misPaquetes.poll();
				miSemaforo.release();

				if(cp != null) {
					
					Packet p = cp.packet;
					EthernetPacket ep = (EthernetPacket) p.datalink;
					if(compareMACs(ep.dst_mac)) {
						
						up.miSemaforo.acquire();
						up.misPaquetes.add(cp);
						up.miSemaforo.release();
						
						System.out.println("Mando paquete a L3");
					}
				}
				
			}
			up.endTime=true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	public boolean compareMACs(byte[] dst_mac) {
		
		boolean condition = false;
		
		if(dst_mac[0] == sourceMAC[0] &&
				dst_mac[1] == sourceMAC[1] &&
				dst_mac[2] == sourceMAC[2] &&
				dst_mac[3] == sourceMAC[3] &&
				dst_mac[4] == sourceMAC[4] &&
				dst_mac[5] == sourceMAC[5]) condition = true;
		
		if(dst_mac[0] == broadcastMAC[0] &&
				dst_mac[1] == broadcastMAC[1] &&
				dst_mac[2] == broadcastMAC[2] &&
				dst_mac[3] == broadcastMAC[3] &&
				dst_mac[4] == broadcastMAC[4] &&
				dst_mac[5] == broadcastMAC[5]) condition = true;
		
		return condition;
	}
}