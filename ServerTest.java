import javax.swing.JFrame;

public class ServerTest {

   private static String userName = "";
   private static int bitLength = 0;

	public static void main(String[] args) throws InterruptedException {
      Login log = new Login(300, 300, false);
      getUserInfo(log);
		Server server = new Server(1000, 1000, 6789, userName, bitLength);
		server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		server.startRunning();

	}
   
   private static void getUserInfo(Login log) throws InterruptedException {
   do {
      if(log.forumSubmitted()) {
         if(!log.getUserName().equals("") && !log.getBitNumber().equals("")) {
            userName = log.getUserName();
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