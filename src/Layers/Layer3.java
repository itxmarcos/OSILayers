package Layers;

import CustomPacket.CustomPacket;
import Protocols.Protocol;
import jpcap.packet.EthernetPacket;
//import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

public class Layer3 extends Layer{
	Packet p;
	int type;
	EthernetPacket ep;
	
	public Protocol ARP;
	public Protocol IPV;
	
	@Override
	public void configuration() {
		endTime = false;
	}

	@Override
	public void run() {
		try {
			while(!endTime || !misPaquetes.isEmpty()) {
				miSemaforo.acquire();
				CustomPacket cpProcesado = misPaquetes.poll();
				miSemaforo.release();
				
				if(cpProcesado != null) {
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
			}
			ARP.endTime=true;
			IPV.endTime=true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

