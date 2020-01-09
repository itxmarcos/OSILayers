import java.net.InetAddress;
import java.util.Stack;

import jpcap.packet.ICMPPacket;
import jpcap.packet.IPPacket;

public class Ipv4 extends Protocol {
	@Override
	public void configuration() {
		endTime = false;
	}
	@Override
	public void run() {
		try {
			while(!endTime || !misPaquetes.isEmpty()) {
				miSemaforo.acquire();
				CustomPacket paquete = misPaquetes.poll();
				miSemaforo.release();
				if(paquete != null) {
					ICMPPacket icmp = (ICMPPacket) paquete.packet;
					if(icmp.type == ICMPPacket.ICMP_ECHO) {
						ICMPPacket icmpprocesado = new ICMPPacket();
						icmpprocesado.type=ICMPPacket.ICMP_ECHOREPLY;
						icmpprocesado.seq=100;
						icmpprocesado.id=100;
						icmpprocesado.orig_timestamp=100;
						icmpprocesado.trans_timestamp=100;
						icmpprocesado.recv_timestamp=100;
						icmpprocesado.data="ping".getBytes();
						icmpprocesado.setIPv4Parameter(0, false,false,false,0,
								false,false,false,0,100,100,IPPacket.IPPROTO_ICMP,
								InetAddress.getByAddress(((Layer2) ((Layer3) miCapa).down).sourceMAC),
								icmp.src_ip);
						CustomPacket cpProcesado = new CustomPacket(icmpprocesado,false);
						miCapa.miSemaforo.acquire();
						miCapa.misPaquetes.add(cpProcesado);
						miCapa.miSemaforo.release();
					}
					else if(icmp.type == ICMPPacket.ICMP_ECHOREPLY) {
						System.out.println("A ping from: "+icmp.src_ip+" just arrive in response");
					}
				}
			}
		}
			catch (Exception e) {
				e.printStackTrace();
			}
	}
	public void pingResponse(byte[] pingToResponse) {
		//if(Subenet mask with our IP == Subnet mask with pingToResponse){
		//((Layer3) miCapa).ARP).translator}
		//else{Create ICMP_ECHO just like in the run method to ping other from other networks
	}
	// cls returns class of given IP address 
	public static char cls(String[] str) { 
	    int a = Integer.parseInt(str[0]); 
	    if (a >= 0 && a <= 127) 
	        return ('A'); 
	    else if (a >= 128 && a <= 191) 
	        return ('B'); 
	    else if (a >= 192 && a <= 223) 
	        return ('C'); 
	    else if (a >= 224 && a <= 239) 
	        return ('D'); 
	    else
	        return ('E'); 
	}
	
	// Converts IP address to the binary form 
	public static int[] bina(String[] str) {
	    int re[] = new int[32]; 
	    int a, b, c, d, i, rem; 
	    a = b = c = d = 1; 
	    Stack<Integer> st = new Stack<Integer>(); 
	    // Separate each number of the IP address 
	    if (str != null)  { 
	        a = Integer.parseInt(str[0]); 
	        b = Integer.parseInt(str[1]); 
	        c = Integer.parseInt(str[2]); 
	        d = Integer.parseInt(str[3]); 
	    } 
	    // convert first number to binary 
	    for (i = 0; i <= 7; i++)  { 
	        rem = a % 2; 
	        st.push(rem); 
	        a = a / 2; 
	    } 
	    // Obtain First octet 
	    for (i = 0; i <= 7; i++) { 
	        re[i] = st.pop(); 
	    } 
	    // convert second number to binary 
	    for (i = 8; i <= 15; i++) { 
	        rem = b % 2; 
	        st.push(rem); 
	        b = b / 2; 
	    } 
	    // Obtain Second octet 
	    for (i = 8; i <= 15; i++) { 
	        re[i] = st.pop(); 
	    } 
	    // convert Third number to binary 
	    for (i = 16; i <= 23; i++) { 
	        rem = c % 2; 
	        st.push(rem); 
	        c = c / 2; 
	    }  
	    // Obtain Third octet 
	    for (i = 16; i <= 23; i++) { 
	        re[i] = st.pop(); 
	    } 
	    // convert fourth number to binary 
	    for (i = 24; i <= 31; i++) { 
	        rem = d % 2; 
	        st.push(rem); 
	        d = d / 2; 
	    } 
	    // Obtain Fourth octet 
	    for (i = 24; i <= 31; i++) { 
	        re[i] = st.pop(); 
	    }  
	    return (re); 
	}
	
	// Converts IP address  
	// from binary to decimal form 
	public static int[] deci(int[] bi) 
	{ 
	      
	    int[] arr = new int[4]; 
	    int a, b, c, d, i, j; 
	    a = b = c = d = 0; 
	    j = 7; 
	      
	    for (i = 0; i < 8; i++) { 
	          
	        a = a + (int)(Math.pow(2, j)) * bi[i]; 
	        j--; 
	    } 
	      
	    j = 7; 
	    for (i = 8; i < 16; i++) { 
	          
	        b = b + bi[i] * (int)(Math.pow(2, j)); 
	        j--; 
	    } 
	      
	    j = 7; 
	    for (i = 16; i < 24; i++) { 
	          
	        c = c + bi[i] * (int)(Math.pow(2, j)); 
	        j--; 
	    } 
	      
	    j = 7; 
	    for (i = 24; i < 32; i++) { 
	          
	        d = d + bi[i] * (int)(Math.pow(2, j)); 
	        j--; 
	    } 
	      
	    arr[0] = a; 
	    arr[1] = b; 
	    arr[2] = c; 
	    arr[3] = d; 
	    return arr; 
	}
}
	