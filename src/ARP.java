//import java.util.HashMap;
//import java.util.Map;


public class ARP extends Protocol{
    //Map<String, Integer> numberMapping = new HashMap<>(); //Esto se encargaría de traducir de IP a MAC
	@Override
	public void configuration() {
			
	}
	@Override
	public void run() {
		try {
		while(!endTime && !misProtocolos.isEmpty()) {
			miSemaforo.acquire();
			Protocol ProtocoloProcesado = misProtocolos.poll();
			miSemaforo.release();
			System.out.println("El Protocolo ARP ha sido procesado: \n"+ProtocoloProcesado+"\n");
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
