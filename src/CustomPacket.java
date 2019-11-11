import jpcap.packet.Packet;

public class CustomPacket {
	Packet packet;
	boolean direction;
	
	public CustomPacket(Packet p, boolean dir) {
		packet=p;
		direction=dir;
	}
	
	public void setDirection (boolean dir) {
		direction=dir;
	}
}
