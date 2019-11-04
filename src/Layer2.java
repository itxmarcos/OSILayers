import java.util.Scanner;

import jpcap.packet.*;

public class Layer2 extends Layer{
	
	byte[] sourceMAC;
	byte[] broadcastMAC = hexStringToByteArray("FFFFFFFFFFFF");

	public void configuration() {
		try {
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

			while(!endTime && !misPaquetes.isEmpty()) {
				miSemaforo.acquire();
				CustomPacket cpProcesado = misPaquetes.poll();
				miSemaforo.release();

				if(cpProcesado!=null) {
					
					Packet p = cpProcesado.packet;
					EthernetPacket ep = (EthernetPacket) p.datalink;
				//	ep.dst_mac = broadcastMAC;
					if(ep.dst_mac == sourceMAC || ep.dst_mac== broadcastMAC) {
						p.datalink = ep;
						cpProcesado.packet = p;
						cpProcesado.direction = true;
					}
					
					up.miSemaforo.acquire();
					up.misPaquetes.add(cpProcesado);
					up.miSemaforo.release();

					/*
					down.miSemaforo.acquire();
					down.misPaquetes.add(cpProcesado);
					down.miSemaforo.release();
					System.out.println("Packet sent to Layer 1: \n"+p);
					*/
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
}
