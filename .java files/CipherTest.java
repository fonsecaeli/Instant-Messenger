import java.math.*;
import java.util.*;

public class CipherTest
{
   public static void main(String[] arg)
   {
   Cipher decryptKey = new Cipher();

      Cipher encryptKey = new Cipher(decryptKey.getPublicExponent(), decryptKey.getModulus());
            
      String messageString = "Fonseca & Ding Inc.";
      ArrayList<String> cipherString = encryptKey.encryptString(messageString);
      String decryptString = decryptKey.decrypt(cipherString);
      
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
