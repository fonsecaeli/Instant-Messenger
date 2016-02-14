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
   
   public BigInteger getPublicExponent()
   {
      return publicExponent;
   }
   
   public BigInteger getModulus()
   {
      return modulus;
   }
   
   // Uses public keys (e and n) to encrypt a char: c = m^e mod n
   public BigInteger encryptChar(char inMessage)
   {
      String mString = (int) inMessage + "";
      BigInteger m = new BigInteger(mString);
      BigInteger encrypted = m.modPow(publicExponent, modulus);
      return encrypted;
   }
   
   // Uses private and public keys (d and n) to decrypt a BigInteger: m = c^d mod n
   public char decryptChar(BigInteger cipherMessage)
   {
      BigInteger m = cipherMessage.modPow(privateExponent, modulus);
      char decrypted = (char) m.intValue();
      return decrypted;
   }
   
}
