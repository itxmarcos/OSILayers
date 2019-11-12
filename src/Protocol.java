import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public abstract class Protocol extends Thread {
	public Layer LinkedLayer;
	LinkedList <Protocol> misProtocolos=new LinkedList<Protocol>();
	public abstract void configuration();
	public abstract void run();
	//public void setLayersLinked(Layer LinkedLayer) {
	//	this.LinkedLayer= LinkedLayer;
	//}
	Semaphore miSemaforo=new Semaphore(1); //Only one access per element.
	public boolean endTime;
}
