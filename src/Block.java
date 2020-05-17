import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.List;

public class Block {
    private String hashPrevBlock ;
    private String hashMerkleRoot;
    private Timestamp timestamp;
    private String headerHash;
    private int nonce = 0;
    boolean state = false;

    public Block(Block previous, List<Transaction> txList){
        if(previous != null){
            hashPrevBlock = previous.getHash();
        }else{
            hashPrevBlock = "";
        }
        String transactionHashAccu = "";
        for(int i =0;i<txList.size();i++){
            transactionHashAccu += txList.get(i).getHash();
        }
        hashMerkleRoot = SHA256.hash(transactionHashAccu);
        headerHash = SHA256.hash(hashPrevBlock + hashMerkleRoot + timestamp.toString() + nonce);
    }


    public Block(List<Transaction> txList){
        this(null , txList);
    }

    public void incrementNonce(){
        this.nonce += 1;
        headerHash = SHA256.hash(hashPrevBlock + hashMerkleRoot + timestamp.toString() + nonce);
    }

    public int getNonce(){
        return nonce;
    }

    public String getHash(){
       return this.headerHash;
    }
}
