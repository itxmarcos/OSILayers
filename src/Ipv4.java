
public class Ipv4 extends Protocol {
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
			System.out.println("El Protocolo Ipv4 ha sido procesado: \n"+ProtocoloProcesado+"\n");
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
