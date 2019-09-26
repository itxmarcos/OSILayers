import jpcap.*;
import jpcap.packet.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Layer1 extends Layer{
	NetworkInterface[] devices;
	JpcapCaptor captor;
	JpcapSender sender;
	ArrayList <Packet> misPaquetes=new ArrayList();
	
	public Layer1 () throws IOException{
		devices = JpcapCaptor.getDeviceList();
		int number=configuration();
		//Open an interface with openDevice(NetworkInterface intrface, int snaplen, boolean promics, int to_ms)
		captor=JpcapCaptor.openDevice(devices[number], 65535, true, 20); //boolean promics changed to true	
		//open a network interface to send a packet to
		sender=JpcapSender.openDevice(devices[number]);
		run(number);
	}
	
	public int configuration() throws IOException {
		//Obtain the list of network interfaces
		//for each network interface
		for (int i = 0; i < devices.length; i++) {
		  //print out its name and description
		  System.out.println(i+": "+devices[i].name + "(" + devices[i].description+")");

		  //print out its datalink name and description
		  System.out.println(" datalink: "+devices[i].datalink_name + "(" + devices[i].datalink_description+")");

		  //print out its MAC address
		  System.out.print(" MAC address:");
		  for (byte b : devices[i].mac_address)
		    System.out.print(Integer.toHexString(b&0xff) + ":");
		  System.out.println();

		  //print out its IP address, subnet mask and broadcast address
		  for (NetworkInterfaceAddress a : devices[i].addresses)
		    System.out.println(" address:"+a.address + " " + a.subnet + " "+ a.broadcast);
		}
		
		//Ask the user which interface to use
		System.out.println("\n\nSelect an interface number from before: ");
		Scanner input = new Scanner(System.in);
		int number = input.nextInt();
		input.close();
		
		return number;
	}
	
	public void run(int number) throws UnknownHostException {
		//call processPacket() to let Jpcap call PacketPrinter.receivePacket() for every packet capture.
		//captor.processPacket(10,new PacketPrinter());
		for(int i=0;i<10;i++) {
			Packet p = captor.getPacket();
			while(p==null) p = captor.getPacket();
			//capture a single packet and print it out
			System.out.println(p);
			//send the packet p
			sender.sendPacket(p);
			//store packet in an arraylist
			misPaquetes.add(p);
		}
		captor.close();
		sender.close();
	}
}