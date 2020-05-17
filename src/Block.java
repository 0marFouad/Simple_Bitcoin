import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.List;

public class Block {
    private String hashPrevBlock ;
    private String hashMerkleRoot;
    private Timestamp timestamp;
    private int nonce = 0;
    boolean state = false;
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
    public Block(Block previous, List<Transaction> txList) throws NoSuchAlgorithmException {
        if(previous != null){
            hashPrevBlock = previous.getHash();
        }else{
            hashPrevBlock = "";
        }
        String transactionHashAccu = "";
        for(int i =0;i<txList.size();i++){
            transactionHashAccu += txList.get(i).getHash();
        }
        byte [] hash = convToHash(transactionHashAccu);
        hashMerkleRoot = bytesToHex(hash);
    }
    public Block(List <Transaction> txList) throws NoSuchAlgorithmException {
        this(null , txList);
    }

    public String getHash() throws NoSuchAlgorithmException {
       byte[] hash = convToHash(hashPrevBlock + hashMerkleRoot + timestamp.toString() + Integer.toString(nonce));
       return bytesToHex(hash);
    }
}
