import java.util.ArrayList;

import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

public class Layer3 extends Layer{
	ArrayList <Protocol> misProtocolos=new ArrayList<Protocol>();
	Packet p;
	int type;
	EthernetPacket ep;
	
	@Override
	public void configuration() {
		
	}

	@Override
	public void run() {
		try {
			while(!endTime && !misPaquetes.isEmpty()) {
				miSemaforo.acquire();
				CustomPacket cpProcesado = misPaquetes.poll();
				miSemaforo.release();
				
				p = cpProcesado.packet;
				ep = (EthernetPacket) p.datalink;
				type = ep.frametype;
				
				if(type==EthernetPacket.ETHERTYPE_ARP) {
					ARPPacket arpPacket =(ARPPacket) p;
				}
				else if(type==EthernetPacket.ETHERTYPE_IP) {
					IPPacket ipPacket =(IPPacket) p;
				} else {
					System.out.println("\nThe packet cannot be processed");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

