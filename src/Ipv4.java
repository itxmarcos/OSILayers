import jpcap.packet.IPPacket;

public class Ipv4 extends Protocol {
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
				
				if(paquete != null)	System.out.println("El paquete p ha sido procesado: "+ (IPPacket) paquete.packet);
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
