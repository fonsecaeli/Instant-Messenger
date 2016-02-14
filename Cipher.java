import java.math.*;
import java.util.*;

public class Cipher
{
   private BigInteger privateExponent; // d
   private BigInteger publicExponent; // e
   private BigInteger modulus; // n

   // Default values
   public Cipher()
   {
      RSAKeyGenerator generator = new RSAKeyGenerator(512);
      privateExponent = generator.getD();
      publicExponent = generator.getE();
      modulus = generator.getModulus();
   
   }
   
   // Takes only the two public keys, used for encryption only
   public Cipher(String publicExponent1, String modulus1)
   {
      publicExponent = new BigInteger(publicExponent1);
      modulus = new BigInteger(modulus1);
   }
   
   public String getPublicExponent()
   {
      return publicExponent.toString();
   }
   
   public String getModulus()
   {
      return modulus.toString();
   }
   
   // Uses public keys (e and n) to encrypt a char: c = m^e mod n
   public String encryptChar(char messageChar)
   {
      String mString = (int) messageChar + "";
      BigInteger m = new BigInteger(mString);
      BigInteger encryptChar = m.modPow(publicExponent, modulus);
      return encryptChar.toString();
   }
   
   // Uses private and public keys (d and n) to decrypt a BigInteger: m = c^d mod n
   public char decryptChar(String encryptChar)
   {
      BigInteger c = new BigInteger(encryptChar);
      BigInteger m = c.modPow(privateExponent, modulus);
      char decrypted = (char) m.intValue();
      return decrypted;
   }

   public ArrayList<String> encryptString(String messageString)
   {
      ArrayList<String> cipherString = new ArrayList<String>(messageString.length());
      
      for (int ii = 0; ii < messageString.length(); ii++)
      {
         cipherString.add(ii, encryptChar(messageString.charAt(ii)));
      }
      
      return cipherString;
   }
   
   public String decrypt(ArrayList<String> cipherString)
   {
      String decryptString = "";
      
      for (int ii = 0; ii < cipherString.size(); ii++)
      {
         decryptString += decryptChar(cipherString.get(ii));
      }
      
      return decryptString;
   }
   
}
