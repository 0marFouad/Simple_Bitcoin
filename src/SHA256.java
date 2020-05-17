import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class SHA256 {

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String hash(ArrayList<String> data){
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        StringBuilder originalString = new StringBuilder();
        for(int i=0;i<data.size();i++){
            originalString.append(data.get(i));
        }
        byte[] encodedhash = digest.digest(originalString.toString().getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedhash);
    }

    public static String hash(String data){
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] encodedhash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedhash);
    }

    public static String hash(Block block){
        return block.getHash();
    }

    public static String hash(Transaction transaction){
        return transaction.getHash();
    }
}
