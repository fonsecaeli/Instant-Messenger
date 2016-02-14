import java.math.*;

public class CipherTest
{
   public static void main(String[] arg)
   {
      Cipher encryptKey = new Cipher("2753", "3233");
      Cipher decryptKey = new Cipher();
      
      String messageString = "Fonseca & Ding Inc.";
      String[] cipherString = encryptKey.encryptString(messageString);
      String decryptString = decryptKey.decryptString(cipherString);
      
      System.out.println(messageString);
      System.out.println(decryptString);
      
      /*
      BigInteger cipherMessage = encryptKey.encryptChar(' ');
      char plainMessage = decryptKey.decryptChar(cipherMessage);
      System.out.println("a" + inMessage + "a");
      System.out.println(outKey.getPublicExponent().toString());
      System.out.println(outKey.getModulus().toString());
      */
   }

}
