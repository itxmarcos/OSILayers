import java.io.IOException;

public class Principal {

	public static void main(String[] args) throws IOException {
		Layer1 miCapa1=new Layer1();
		Layer2 miCapa2=new Layer2();
		
		miCapa1.setNeighbors(null, miCapa2);
		miCapa2.setNeighbors(miCapa1, null);
		
		miCapa1.configuration();
		miCapa1.run();	
	}
}