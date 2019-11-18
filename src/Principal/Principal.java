package Principal;
import java.io.IOException;

import Layers.Layer1;
import Layers.Layer2;
import Layers.Layer3;
import Protocols.ARP;
import Protocols.Ipv4;
import Protocols.Protocol;

public class Principal {

	public static void main(String[] args) throws IOException {
		try {
			Layer1 miCapa1=new Layer1();
			Layer2 miCapa2=new Layer2();
			Layer3 miCapa3=new Layer3();
			
			Protocol IPVProtocol= new Ipv4();
			Protocol ARPProtocol= new ARP();
			
			miCapa1.setLayersNeighbors(null, miCapa2);
			miCapa2.setLayersNeighbors(miCapa1, miCapa3);
			miCapa3.setLayersNeighbors(miCapa2, null);

			miCapa3.ARP = ARPProtocol;
			miCapa3.IPV = IPVProtocol;
			
			miCapa1.configuration();
			miCapa2.configuration();
			miCapa3.configuration();
			IPVProtocol.configuration();
			ARPProtocol.configuration();
			
			miCapa1.start();
			miCapa2.start();
			miCapa3.start();
			IPVProtocol.start();
			ARPProtocol.start();
			
			Thread.sleep(20000);
			
			miCapa1.endTime = true;
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}