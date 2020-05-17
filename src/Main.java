import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

public class Main {
    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    private static byte [] convToHash(String message) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(message.getBytes(StandardCharsets.UTF_8));
        return encodedhash;
    }
    public static void main(String[] args) throws NoSuchAlgorithmException {
        Timestamp t = new Timestamp(System.currentTimeMillis());
        System.out.println(t.toString());
        String bg =  new BigInteger(SHA256.hash("1234567890abcdefo"), 16).toString(2);
        System.out.println(bg.length());

    }
}
