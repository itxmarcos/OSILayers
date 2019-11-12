
//import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;
//import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

public class Layer3 extends Layer{
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
				//	ARPPacket arpPacket =(ARPPacket) p;
					ARP ARPProtocol = new ARP();
					ARP.miSemaforo.acquire();
					ARP.misProtocolos.add(ARPProtocol);
					ARP.miSemaforo.release();
				}
				else if(type==EthernetPacket.ETHERTYPE_IP) {
				//	IPPacket ipPacket =(IPPacket) p;
					Ipv4 IPV4Protocol = new Ipv4();
					IPV.miSemaforo.acquire();
					IPV.misProtocolos.add(IPV4Protocol);
					IPV.miSemaforo.release();
				} else {
					System.out.println("\nThe packet cannot be processed");
				}
			}
			ARP.endTime=true;
			IPV.endTime=true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

