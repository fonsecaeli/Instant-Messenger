public class LoginTester {
   private static String userName = "";
   private static String IP = "";
   private static int bitLength = 0;
   public static void main(String[] args) throws InterruptedException {
      Login log = new Login(300, 300, true);
      getUserName(log);
      System.out.println(userName);
      System.out.println(IP);
      System.out.println(bitLength);
   }
   
   private static void getUserName(Login log) throws InterruptedException {
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
