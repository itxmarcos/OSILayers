import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;
import jpcap.packet.Packet;

//import java.util.HashMap;
//import java.util.Map;

public class ARP extends Protocol{
    //Map<String, Integer> numberMapping = new HashMap<>(); //Esto se encargaria de traducir de IP a MAC
	@Override
	public void configuration() {
		endTime = false;
	}
	@Override
	public void run() {
		try {
			while(!endTime || !misPaquetes.isEmpty()) {
				miSemaforo.acquire();
				CustomPacket paquete = misPaquetes.poll();
				miSemaforo.release();
			
				if(paquete != null)	{
					ARPPacket ap= (ARPPacket)paquete.packet;
					EthernetPacket ep = (EthernetPacket) ap.datalink;
					if(compareMACs(ap.target_hardaddr)) {
						ARPPacket a = new ARPPacket();
						EthernetPacket e = (EthernetPacket) a.datalink;
						a.hardtype = ARPPacket.HARDTYPE_ETHER;
						a.prototype = ARPPacket.PROTOTYPE_IP;
						a.hlen =(short) e.src_mac.length;
						//a.plen = IP.IP_LENGTH;
						a.sender_hardaddr = e.src_mac;
						//a.sender_protoaddr = a.src_ip;
						a.target_hardaddr = hexStringToByteArray("000000000000");
						//a.target_protoaddr = destination_ip_address;
					}
					//System.out.println("ARP packet processed: "+ (ARPPacket) paquete.packet);
				}
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean compareMACs(byte[] mac_addr) {	
		boolean condition = false;
		if(mac_addr[0] == 0 &&
				mac_addr[1] == 0 &&
				mac_addr[2] == 0 &&
				mac_addr[3] == 0 &&
				mac_addr[4] == 0 &&
				mac_addr[5] == 0) condition = true;
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
}