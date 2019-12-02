import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;

import java.util.HashMap;
import java.util.Map;

public class ARP extends Protocol{
    public Map<byte[], byte[]> translatorIPtoMAC = new HashMap<>();
    public Layer miCapa;
    byte[] sourceIP;
    byte[] broadcastIP = hexStringToByteArray("FFFFFFFF"); //255.255.255.255
   
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
					if(ap.operation==ARPPacket.ARP_REQUEST) { //Hacer paquete de replay
						ARPPacket arp = new ARPPacket();
						EthernetPacket e = (EthernetPacket) arp.datalink;
						arp.hardtype = ARPPacket.HARDTYPE_ETHER;
						arp.prototype = ARPPacket.PROTOTYPE_IP;
						arp.hlen =(short) e.src_mac.length;
						arp.plen =(short) sourceIP.length;
						arp.sender_hardaddr = e.src_mac;
						arp.sender_protoaddr = sourceIP;
						arp.target_hardaddr = hexStringToByteArray("000000000000");
						arp.target_protoaddr = ap.sender_protoaddr;
						arp.operation=ARPPacket.ARP_REPLY;
					}
					else if(ap.operation==ARPPacket.ARP_REPLY){
						translatorIPtoMAC.put(ap.sender_protoaddr,ep.src_mac);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void Translator(byte[] IPtranslate) {
		if(translatorIPtoMAC.containsKey(IPtranslate)) {
			System.out.println("The MAC address that you are looking for is: "+translatorIPtoMAC.get(IPtranslate));
		}
		else {
			try {
				ARPPacket arpProcesado = new ARPPacket();
				EthernetPacket e = (EthernetPacket) arpProcesado.datalink;
				arpProcesado.hardtype = ARPPacket.HARDTYPE_ETHER;
				arpProcesado.prototype = ARPPacket.PROTOTYPE_IP;
				arpProcesado.hlen =(short) e.src_mac.length;
				arpProcesado.plen =(short) sourceIP.length;
				arpProcesado.sender_hardaddr = e.src_mac;
				arpProcesado.sender_protoaddr = sourceIP;
				arpProcesado.target_hardaddr = hexStringToByteArray("000000000000");
				arpProcesado.target_protoaddr = broadcastIP;
				arpProcesado.operation=ARPPacket.ARP_REQUEST;
				CustomPacket cpProcesado = new CustomPacket(arpProcesado,false);
				miCapa.miSemaforo.acquire();
				miCapa.misPaquetes.add(cpProcesado);
				miCapa.miSemaforo.release();
				System.out.println("Awaiting destination response. Please wait...");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
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
	public byte[] getIPUser() {
		return Principal.userIP;
	}
}