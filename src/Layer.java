import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public abstract class Layer extends Thread {
	LinkedList <CustomPacket> misPaquetes=new LinkedList<CustomPacket>();
	public Layer up;
	public Layer down;

	Semaphore miSemaforo=new Semaphore(1); //Only one access per element.
	public boolean endTime;
	
	public abstract void configuration();
	@Override
	public abstract void run();
	
	public void setLayersNeighbors(Layer down, Layer up) {
		this.down = down;
		this.up = up;
	}
}