package Protocols;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import CustomPacket.CustomPacket;

public abstract class Protocol extends Thread {

	public abstract void configuration();
	public abstract void run();

	public Semaphore miSemaforo=new Semaphore(1); //Only one access per element.
	public boolean endTime;
	
	public LinkedList <CustomPacket> misPaquetes=new LinkedList<CustomPacket>();
}
