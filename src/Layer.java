import java.io.IOException;
import jpcap.*;

public abstract class Layer extends Thread {

	public int configuration() throws IOException{
		return 0;
	}
	public void run() {
		
	}

}