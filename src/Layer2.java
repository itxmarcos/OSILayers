import java.util.ArrayList;

import jpcap.packet.*;

public class Layer2 extends Layer{
	ArrayList <CustomPacket> misPaquetes=new ArrayList<CustomPacket>();
	
	public void configuration() {
		
	}

	public void run() {
		for(int i=0;i<down.misPaquetes.size();i++) {
			CustomPacket cp= misPaquetes.get(i);
			if (cp.direction==true) {
				Packet p=cp.packet;
				EthernetPacket ep = (EthernetPacket) p.datalink;
				ep.dst_mac = null; //Address to send this packet to all
				ep.src_mac = null; //My address (I’m the gossip)
				p.datalink = ep; // p is the new packet to send
				down.misPaquetes.add(new CustomPacket(p, false));
			}
		}
		//set a filter to only capture TCP/IPv4 packets
		//captor.setFilter("ip and tcp", true);
	}

}
