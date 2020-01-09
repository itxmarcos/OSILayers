
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
				if(!isValidMAC(stringAux)) { //Check if MAC is correct
					while (!isValidMAC(stringAux)){
						System.out.println("Invalid MAC introduce it again please: ");
						stringAux = input2.next();
					}
				}
				this.sourceMAC = hexStringToByteArray((stringAux.split("\\:")).toString());
			}
			
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
					
					
					if(cp.direction){
						
						EthernetPacket ep = (EthernetPacket) p.datalink;
						if(compareSourceBroadcastMACs(ep.dst_mac)) {
							
							up.miSemaforo.acquire();
							up.misPaquetes.add(cp);
							up.miSemaforo.release();

							//System.out.println("Packet sent to L3");
						}
					}
					else{
						
						ARPPacket ap = (ARPPacket) p;
						EthernetPacket ep = new EthernetPacket();
						
						ep.src_mac = sourceMAC;
						ep.frametype = EthernetPacket.ETHERTYPE_ARP;
						//codear 2 tipos de paquetes ARP y IP
						if(ap.operation == ARPPacket.ARP_REQUEST) ep.dst_mac = broadcastMAC;
						else if (ap.operation == ARPPacket.ARP_REPLY) ep.dst_mac = ap.target_hardaddr;
						
						p.datalink = ep;
						
						down.miSemaforo.acquire();
						down.misPaquetes.add(cp);
						down.miSemaforo.release();
						
						//System.out.println("Packet sent to L1");
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

	public boolean compareSourceBroadcastMACs(byte[] dst_mac) {

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
	public boolean compareZeros(byte[] mac) {

		boolean condition = false;

		if(mac[0] == 0 &&
				mac[1] == 0 &&
				mac[2] == 0 &&
				mac[3] == 0 &&
				mac[4] == 0 &&
				mac[5] == 0) condition = true;

		return condition;
	}
	public boolean isValidMAC(String mac) {
        Pattern p = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");
		   Matcher m = p.matcher(mac);
		   return m.find();
		}
}