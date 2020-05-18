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

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        //Creating KeyPair generator object
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");

        //Generate the pair of keys
        KeyPair pair = keyPairGen.generateKeyPair();

        Clients.clients.put(0, pair);

        //Creating a Signature object
        Signature sign = Signature.getInstance("SHA256withRSA");

        //Initializing the signature
        sign.initSign(Clients.clients.get(0).getPrivate());
        byte[] bytes = "Hello how are you".getBytes();

        //Adding data to the signature
        sign.update(bytes);

        //Calculating the signature
        byte[] signature = sign.sign();

        //Initializing the signature
        Signature sign1 = Signature.getInstance("SHA256withRSA");

        sign1.initVerify(Clients.clients.get(0).getPublic());
        sign1.update(bytes);

        //Verifying the signature
        boolean bool = sign1.verify(signature);

        if (bool) {
            System.out.println("Signature verified");
        } else {
            System.out.println("Signature failed");
        }

    }
}
