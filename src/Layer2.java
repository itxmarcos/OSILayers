import jpcap.packet.*;

public class Layer2 extends Layer{
	EthernetPacket ep;
	Packet p;
	CustomPacket cp;
	byte[] broadcastMAC = hexStringToByteArray("FFFFFFFFFFFF");
	int number;

	public void configuration() {
		//Aquí se configura la MAC adress
		for(int number=0;number<misPaquetes.size();number++) {
			cp= misPaquetes.get(number);
			if (cp.direction==true) {
				p=cp.packet;
				ep = (EthernetPacket) p.datalink;
				ep.dst_mac = broadcastMAC; //Address to send this packet to all
				ep.src_mac = ((Layer1)down).getMacAdress(); //My address (I’m the gossip)
			}
		}
		
	}

	public void run() {
		for(int number=0;number<misPaquetes.size();number++) {
			if (cp.direction==true) {
				try {
					p.datalink = ep; // p is the new packet to send
					down.miSemaforo.acquire();
					down.misPaquetes.add(new CustomPacket(p, false));
					down.miSemaforo.release();
					misPaquetes.remove(number);
					//set a filter to only capture TCP/IPv4 packets
					//captor.setFilter("ip and tcp", true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
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