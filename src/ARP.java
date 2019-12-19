import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;

import java.util.HashMap;

public class ARP extends Protocol{
    
	public HashMap<byte[], byte[]> arpTable = new HashMap<>();
    byte[] broadcastIP = hexStringToByteArray("FFFFFFFF"); //255.255.255.255
    int contador=0;
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
					ARPPacket ap = (ARPPacket) paquete.packet;
					EthernetPacket ep = (EthernetPacket) ap.datalink;
					if(ap.operation == ARPPacket.ARP_REQUEST) { //Make a reply ARPPacket
						ARPPacket arp = new ARPPacket();
						arp.hardtype = ARPPacket.HARDTYPE_ETHER;
						arp.prototype = ARPPacket.PROTOTYPE_IP;
						arp.hlen = 6;
						arp.plen = 4;
						arp.sender_hardaddr = ((Layer2) ((Layer3) miCapa).down).sourceMAC;
						arp.sender_protoaddr = ((Layer3) miCapa).sourceIP;
						arp.target_hardaddr = ap.sender_hardaddr;
						arp.target_protoaddr = ap.sender_protoaddr;
						arp.operation=ARPPacket.ARP_REPLY;
						CustomPacket cpProcesado = new CustomPacket(arp,false);
						miCapa.miSemaforo.acquire();
						miCapa.misPaquetes.add(cpProcesado);
						miCapa.miSemaforo.release();
					}
					else if(ap.operation == ARPPacket.ARP_REPLY){ //Receive a reply from another device
						miSemaforo.acquire();
						arpTable.put(ap.sender_protoaddr,ep.src_mac);
						miSemaforo.release();
						contador++;
						if(contador==30) {// is the number of ARPPackets until the table is refreshed
							arpTable.clear();
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void translator(byte[] IPtranslate) {
		try {
			if(arpTable.containsKey(IPtranslate)) {
				miSemaforo.acquire();
				System.out.println("The MAC address that you are looking for is: " + arpTable.get(IPtranslate));
				miSemaforo.release();
				}
			else {
				ARPPacket arpRequest = new ARPPacket();
				arpRequest.hardtype = ARPPacket.HARDTYPE_ETHER;
				arpRequest.prototype = ARPPacket.PROTOTYPE_IP;
				arpRequest.hlen = 6;
				arpRequest.plen = 4;
				arpRequest.sender_hardaddr = ((Layer2) ((Layer3) miCapa).down).sourceMAC;
				arpRequest.sender_protoaddr = ((Layer3) miCapa).sourceIP;
				arpRequest.target_hardaddr = hexStringToByteArray("000000000000");
				arpRequest.target_protoaddr = IPtranslate;
				arpRequest.operation = ARPPacket.ARP_REQUEST;
				CustomPacket cpProcesado = new CustomPacket(arpRequest,false);
				miCapa.miSemaforo.acquire();
				miCapa.misPaquetes.add(cpProcesado);
				miCapa.miSemaforo.release();
				System.out.println("Awaiting destination response. Ask again in 60 seconds.");
			}
		}
		catch (Exception e) {
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