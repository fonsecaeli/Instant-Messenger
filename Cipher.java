import java.math.*;

public class Cipher
{
   private BigInteger privateExponent; // d
   private BigInteger publicExponent; // e
   private BigInteger modulus; // n

   // Default values
   public Cipher()
   {
      privateExponent = new BigInteger("17");
      publicExponent = new BigInteger("2753");
      modulus = new BigInteger("3233");
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

   public String[] encryptString(String messageString)
   {
      String[] cipherString = new String[messageString.length()];
      
      for (int ii = 0; ii < messageString.length(); ii++)
      {
         cipherString[ii] = encryptChar(messageString.charAt(ii));
      }
      
      return cipherString;
   }
   
   public String decryptString(String[] cipherString)
   {
      String decryptString = "";
      
      for (int ii = 0; ii < cipherString.length; ii++)
      {
         decryptString += decryptChar(cipherString[ii]);
      }
      
      return decryptString;
   }
   
}
