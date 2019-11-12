import java.io.IOException;

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
			miCapa3.setProtocolLinked(ARPProtocol, IPVProtocol);
			
			miCapa1.configuration();
			miCapa2.configuration();
			miCapa3.configuration();
			
			
			miCapa1.start();
			miCapa2.start();
			miCapa3.start();
			IPVProtocol.start();
			ARPProtocol.start();
			
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}