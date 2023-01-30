import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Cryptography {

    private static SecretKeySpec secretKey;
    private static byte[] key;
    private static final String ALGORITHM = "AES";

    public static void prepareSecreteKey(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, ALGORITHM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // method for encrypting file data
    public static String Encrypt(String message, String secret) throws Exception {
        String cipherText = "";
        try {
            prepareSecreteKey(secret);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            cipherText = Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes("UTF-8")));
        } catch (Exception ex) {
            System.out.println("Cryptography error - " + ex.getMessage());
        }
        return cipherText;
    }

    // method for decrypting file data
    public static String Decrypt(String cipherText, String secret) throws Exception {
        String message = "";
        try {
            Byzantine byzantine = new Byzantine();
            int error = byzantine.ByzantineError();
            if (error == 2) {
                cipherText = "Data has been changed due to some error in cryptography";
                Methods methods = new Methods();
                methods.clearScreen();
                System.out.println("=======================================");
                System.out.println("Byzantine error occured - CRYPTOGRAPHY!");
                System.out.println("=======================================");
            }

            prepareSecreteKey(secret);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            message = new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
        } catch (Exception ex) {
            System.out.println("Cryptography error - " + ex.getMessage());
        }
        return message;
    }
}
