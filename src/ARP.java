import jpcap.packet.ARPPacket;

//import java.util.HashMap;
//import java.util.Map;


public class ARP extends Protocol{
    //Map<String, Integer> numberMapping = new HashMap<>(); //Esto se encargaría de traducir de IP a MAC
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
			
			if(paquete != null)	System.out.println("El paquete p ha sido procesado: "+ (ARPPacket) paquete.packet);
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
