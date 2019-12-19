import jpcap.packet.ARPPacket; 

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jpcap.packet.EthernetPacket;
import jpcap.packet.Packet;

public class Layer3 extends Layer{
	Packet p;
	int type;
	byte[] sourceIP;
	byte[] broadcastIP = hexStringToByteArray("FFFFFFFF"); //255.255.255.255

	public ARP ARP;
	public Ipv4 IPV;

	@Override
	public void configuration() {
		endTime = false;
		try {
			System.out.println("\nIntroduce your IP address separated by '.':");
			@SuppressWarnings("resource")
			Scanner input = new Scanner(System.in);
			String stringAux = input.next();
			if(!isValidIP(stringAux)) { //Check if IP is correct
				while (!isValidIP(stringAux)){
					System.out.println("Invalid IP, introduce it again please separated by '.': ");
					stringAux = input.next();
				}
			}
			this.sourceIP = stringToByteArray(stringAux);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
					if(cpProcesado.direction){
						p = cpProcesado.packet;
						EthernetPacket ep = (EthernetPacket) p.datalink;
						type = ep.frametype;
						if(type==EthernetPacket.ETHERTYPE_ARP && compareIPs(((ARPPacket) p).target_protoaddr)) {
							ARP.miSemaforo.acquire();
							ARP.misPaquetes.add(cpProcesado);
							ARP.miSemaforo.release();
							//System.out.println("Packet sent to ARP");
						}
						else if(type==EthernetPacket.ETHERTYPE_IP) {
							IPV.miSemaforo.acquire();
							IPV.misPaquetes.add(cpProcesado);
							IPV.miSemaforo.release();
							//System.out.println("Packet sent to IPV");
						} else {
							//System.out.println("The packet cannot be processed");
						}
					}
					else{
						down.miSemaforo.acquire();
						down.misPaquetes.add(cpProcesado);
						down.miSemaforo.release();
						//System.out.println("Packet sent to Layer 2");
					}
				}
			}
			ARP.endTime=true;
			IPV.endTime=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean compareIPs(byte[] IP) {
		boolean condition = false;
		if(IP[0] == sourceIP[0] &&
				IP[1] == sourceIP[1] &&
				IP[2] == sourceIP[2] &&
				IP[3] == sourceIP[3]) condition = true;
		if(IP[0] == broadcastIP[0] &&
				IP[1] == broadcastIP[1] &&
				IP[2] == broadcastIP[2] &&
				IP[3] == broadcastIP[3]) condition = true;
		return condition;
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
	public static boolean isValidIP(String ipAddr){
		Pattern ptn = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
		Matcher mtch = ptn.matcher(ipAddr);
		return mtch.find();
	}
	public static byte[] stringToByteArray(String s) {
		String[] ipArr = s.split("\\.");
		byte[] ipAddr = new byte[4];
		for (int i = 0; i < 4; i++) {
			int digit = Integer.parseInt(ipArr[i]);
			ipAddr[i] = (byte) digit;
		}
		return ipAddr;
	}
}