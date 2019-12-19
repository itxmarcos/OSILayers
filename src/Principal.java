import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Principal {

	public static void main(String[] args) throws IOException {

		try {
			Layer1 miCapa1 = new Layer1();
			Layer2 miCapa2 = new Layer2();
			Layer3 miCapa3 = new Layer3();

			Ipv4 IPVProtocol = new Ipv4();
			ARP ARPProtocol = new ARP();

			miCapa1.setLayersNeighbors(null, miCapa2);
			miCapa2.setLayersNeighbors(miCapa1, miCapa3);
			miCapa3.setLayersNeighbors(miCapa2, null);

			miCapa3.ARP = ARPProtocol;
			miCapa3.IPV = IPVProtocol;

			ARPProtocol.miCapa = miCapa3;

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

			boolean exit = false;
			@SuppressWarnings("resource")
			Scanner input = new Scanner(System.in);

			while(!exit) {
				System.out.println("Do you want to check for an IP? [1=YES, 2=NO]");
				int response = input.nextInt();
				if(response == 2) exit = true;
				else ARPProtocol.translator(askIP());
			}
			System.out.println("Terminating....");
			miCapa1.endTime = true;


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static byte[] askIP() {
		try {
			System.out.println("\nIntroduce an IP address separated by '.' :");
			@SuppressWarnings("resource")
			Scanner input = new Scanner(System.in);
			String stringAux = input.next();
			if(!isValidIP(stringAux)) { //Check if IP is correct
				while (!isValidIP(stringAux)){
					System.out.println("Invalid IP, introduce it again please separated by '.': ");
					stringAux = input.next();
				}
			}
			return stringToByteArray(stringAux);
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] stringToByteArray(String s) {
		String[] ipArr = s.split("\\.");
		byte[] ipAddr = new byte[4];
		for (int i = 0; i < 4; i++) {
			int digit = Integer.parseInt(ipArr[i]);
			ipAddr[i] = (byte) digit;
		}

		return ipAddr;
	}
	public static boolean isValidIP(String ipAddr){
		Pattern ptn = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
		Matcher mtch = ptn.matcher(ipAddr);
		return mtch.find();
	}
}