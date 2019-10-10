import java.util.LinkedList;

public abstract class Layer extends Thread {
	LinkedList <CustomPacket> misPaquetes;
	public Layer up;
	public Layer down;
	
	public abstract void configuration();
	@Override
	public abstract void run();
	
	public void setNeighbors(Layer down, Layer up) {
		this.down = down;
		this.up = up;
	}
}