import java.io.IOException;
import java.util.Scanner;

public class Principal {
	public static byte[] userIP;
	public static int response;
	public static Layer1 miCapa1;
	public static ARP ARPProtocol;
	public static void main(String[] args) throws IOException {
		try {
			miCapa1=new Layer1();
			Layer2 miCapa2=new Layer2();
			Layer3 miCapa3=new Layer3();
			
			Ipv4 IPVProtocol= new Ipv4();
			ARPProtocol= new ARP();
			
			miCapa1.setLayersNeighbors(null, miCapa2);
			miCapa2.setLayersNeighbors(miCapa1, miCapa3);
			miCapa3.setLayersNeighbors(miCapa2, null);

			miCapa3.ARP = ARPProtocol;
			miCapa3.IPV = IPVProtocol;
			
			ARPProtocol.miCapa=miCapa3;
			
			miCapa1.configuration();
			miCapa2.configuration();
			miCapa3.configuration();
			IPVProtocol.configuration();
			ARPProtocol.configuration();
			
			miCapa1.start();
			miCapa2.start();
			miCapa3.start();
			IPVProtocol.start();
			ARPProtocol.start();
			
			checkIP();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void checkIP() {
		Scanner input = new Scanner(System.in);
		System.out.println("Do you want to check for an IP [1=YES, 2=NO]");
		response = input.nextInt();
		if(response==1) {
			while(response==1) {
				askIP();
				ARPProtocol.Translator(userIP);
				System.out.println("Do you want to check for another IP [1=YES, 2=NO]");
				response = input.nextInt();
			}
		} else if(response==2) {
			miCapa1.endTime = true;
		} else {
			System.out.println("Wrong answer. Exiting...");
		}
	}
	public static void askIP() {
		try {
			System.out.println("\nIntroduce your IP address in Hexadecimal separate by '.' :");
			@SuppressWarnings("resource")
			Scanner input = new Scanner(System.in);
			String stringAux = input.next();
			String[] parts = stringAux.split(".");
			String part1 = parts[0];
			String part2 = parts[1];
			String part3 = parts[2];
			String part4 = parts[3];
			userIP = hexStringToByteArray(parts.toString());
		
		}catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			System.out.println("Invalid IP Address");
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