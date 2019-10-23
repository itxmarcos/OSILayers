import jpcap.packet.*;

public class Layer2 extends Layer{
	
	byte[] sourceMAC;
	byte[] broadcastMAC = hexStringToByteArray("FFFFFFFFFFFF");

	public void configuration() {
		
		// Solicitar (o coger) una MAC
		this.sourceMAC = ((Layer1) down).getMacAdress();
		
		// Solicitar: scanner entrada, coger una cadena FF:AD:CA:19:42:C7, dividir, calcular bytes	
		

	}

	public void run() {

		try {

			while(true) {

				miSemaforo.acquire();
				CustomPacket cpProcesado = misPaquetes.poll();
				miSemaforo.release();

				if(cpProcesado!=null) {
					
					Packet p = cpProcesado.packet;
					EthernetPacket ep = (EthernetPacket) p.datalink;
					ep.dst_mac = broadcastMAC;
					ep.src_mac = sourceMAC;
					p.datalink = ep;
					cpProcesado.packet = p;
					cpProcesado.direction = false;


					down.miSemaforo.acquire();
					down.misPaquetes.add(cpProcesado);
					down.miSemaforo.release();
					System.out.println("Packet sent to Layer 1: \n"+p);
				}
				
			}
			
		} catch (Exception e) {
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
