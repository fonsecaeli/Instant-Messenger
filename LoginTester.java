public class LoginTester {
   
   public static void main(String[] args) {
      Login log = new Login(1000, 1000);
      while(true) {
         if(!(log.getUserName().trim().equals(""))) {
            System.out.println(log.getUserName());
         }
      }
   }
}
