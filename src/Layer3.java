
//import jpcap.packet.ARPPacket;
import java.util.Scanner;

import jpcap.packet.EthernetPacket;
//import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

public class Layer3 extends Layer{
	Packet p;
	int type;
	EthernetPacket ep;
	byte[] my_IP;
	
	public Protocol ARP;
	public Protocol IPV;

	
	@Override
	public void configuration() {
		endTime = false;
		//Aquí habría que pedirle la IP al usuario y pasarla a ARP
		try {
			System.out.println("\nIntroduce your IP address in Hexadecimal separate by ':' :");
			@SuppressWarnings("resource")
			Scanner input = new Scanner(System.in);
			String stringAux = input.next();
			String[] parts = stringAux.split(":");
		/*	String part1 = parts[0]; 
			String part2 = parts[1];
			String part3 = parts[2];
			String part4 = parts[3];
		*/	my_IP = hexStringToByteArray(parts.toString());
		}catch (ArrayIndexOutOfBoundsException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Invalid IP Address");
		}
	}

	@Override
	public void run() {
		try {
			while(!endTime || !misPaquetes.isEmpty()) {
				miSemaforo.acquire();
				CustomPacket cpProcesado = misPaquetes.poll();
				miSemaforo.release();
				
				if(cpProcesado != null) {
					if(cpProcesado.direction==true){
						p = cpProcesado.packet;
						ep = (EthernetPacket) p.datalink;
						type = ep.frametype;

						if(type==EthernetPacket.ETHERTYPE_ARP) {

							ARP.miSemaforo.acquire();
							ARP.misPaquetes.add(cpProcesado);
							ARP.miSemaforo.release();
							
							System.out.println("Packet sent to ARP");
						}
						else if(type==EthernetPacket.ETHERTYPE_IP) {
							IPV.miSemaforo.acquire();
							IPV.misPaquetes.add(cpProcesado);
							IPV.miSemaforo.release();
							
							System.out.println("Packet sent to IPV");
						} else {
							System.out.println("The packet cannot be processed");
						}
					}
					else{
						down.miSemaforo.acquire();
						down.misPaquetes.add(cpProcesado);
						down.miSemaforo.release();
						System.out.println("Packet sent to Layer 2");
					}
				}
			}
			ARP.endTime=true;
			IPV.endTime=true;
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
