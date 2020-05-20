import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Timestamp;
import java.util.Base64;

public class Main {
    public static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static byte[] convToHash(String message) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(message.getBytes(StandardCharsets.UTF_8));
        return encodedhash;
    }

    public static void main(String[] args) throws InterruptedException {

//        Runnable runnable1 = new Server(4000);
//        Thread threadServer = new Thread(runnable1);
//        Runnable runnable2 = new Client("127.0.0.1/5000");
//        Thread threadClient = new Thread(runnable2);
//
//        threadServer.start();
//        Thread.sleep(4000);
//        threadClient.start();

        try {
            Network n = new Network();
            Thread t = new Thread(n);
            t.start();
            n.intiateClientConnection();
            Test test = new Test();
            test.value = 1;
            n.broadcast("test", test);
        } catch (IOException  e) {
            e.printStackTrace();
        }


    }
}
