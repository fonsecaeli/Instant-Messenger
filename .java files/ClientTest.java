import javax.swing.JFrame;

public class ClientTest {

   private static String userName = "";
   private static String IP = "";
   private static int bitLength = 0;

public static void main(String[] args) throws InterruptedException {
      Login log = new Login(300, 300, true);
      getUserInfo(log);
		Client client; 
		client = new Client(IP, 1000, 1000, 6789, userName, bitLength); //means local host, so will connect to this computer
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.startRunning();
	}
   
   private static void getUserInfo(Login log) throws InterruptedException {
   do {
      if(log.forumSubmitted()) {
         if(!log.getUserName().equals("") && !log.getIP().equals("") && !log.getBitNumber().equals("")) {
            userName = log.getUserName();
            IP = log.getIP();
            bitLength = Integer.valueOf(log.getBitNumber()); 
            Thread.sleep(1000);
            log.close();
            break;
            //log.close();
         }
      }
      Thread.sleep(100);
      }
      while(true);
   }

}
