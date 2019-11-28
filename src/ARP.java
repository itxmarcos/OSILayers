import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;
//import jpcap.packet.Packet;
import jpcap.packet.IPPacket;

import java.util.HashMap;
import java.util.Map;

public class ARP extends Protocol{
    public Map<String, Integer> numberMapping = new HashMap<>();
    public Layer miCapa;
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
				//	EthernetPacket ep = (EthernetPacket) ap.datalink;
					IPPacket ipPA= new IPPacket();
					if(ap.operation==ARPPacket.ARP_REQUEST) {
						ARPPacket arp = new ARPPacket();
						EthernetPacket e = (EthernetPacket) arp.datalink;
						arp.hardtype = ARPPacket.HARDTYPE_ETHER;
						arp.prototype = ARPPacket.PROTOTYPE_IP;
						arp.hlen =(short) e.src_mac.length;
		//				arp.plen =ipPA.src_ip.;
						arp.sender_hardaddr = e.src_mac;
						//arp.sender_protoaddr = a.src_ip;
						arp.target_hardaddr = hexStringToByteArray("000000000000");
						//arp.target_protoaddr = destination_ip_address;
						arp.operation=ARPPacket.ARP_REPLY;
					}
					else if(ap.operation==ARPPacket.ARP_REPLY){
						
					}
				}
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/*	public boolean compareMACs(byte[] mac_addr) {	
		boolean condition = false;
		if(mac_addr[0] == 0 &&
				mac_addr[1] == 0 &&
				mac_addr[2] == 0 &&
				mac_addr[3] == 0 &&
				mac_addr[4] == 0 &&
				mac_addr[5] == 0) condition = true;
		return condition;
	}
*/	
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