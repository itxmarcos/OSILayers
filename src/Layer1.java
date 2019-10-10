import jpcap.*;
import jpcap.packet.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Layer1 extends Layer{
	NetworkInterface[] devices;
	JpcapCaptor captor;
	JpcapSender sender;
	ArrayList <CustomPacket> misPaquetes=new ArrayList<CustomPacket>();
	int number;
		
	public void configuration() {
		//Obtain the list of network interfaces
		devices = JpcapCaptor.getDeviceList();
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
		number = input.nextInt();
		input.close();
		try {
			//open a network interface to send a packet to
			captor=JpcapCaptor.openDevice(devices[number], 65535, true, 20); //boolean promics changed to true	
			sender=JpcapSender.openDevice(devices[number]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		for(int i=0;i<10;i++) {
			Packet p = captor.getPacket();
			//capture a single packet that is different from null
			while(p==null) p = captor.getPacket();
			CustomPacket cp=new CustomPacket(p, true);
			up.misPaquetes.add(cp); //store packet in Layer 2 arraylist
		}
		for(int i=0;i<up.misPaquetes.size();i++) {
			CustomPacket cp= misPaquetes.get(i);
			if (cp.direction==false) {
				Packet p=cp.packet;
				sender.sendPacket(p);
			}
		}
		captor.close();
		sender.close();
	}

}