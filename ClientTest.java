import javax.swing.JFrame;

public class ClientTest {
	public static void main(String[] args) {
		Client client; 
		client = new Client("127.0.0.1", 1000, 1000, 6789); //means local host, so will connect to this computer
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.startRunning();
	}
}
