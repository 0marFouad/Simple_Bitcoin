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
    List <Transaction > txList ;
    public Block(String hashPrevBlock , String hashMerkleRoot, Timestamp timestamp, int nonce, List <Transaction> txList){
        this.hashPrevBlock = hashPrevBlock;
        this.hashMerkleRoot = hashMerkleRoot;
        this.timestamp = timestamp;
        this.nonce = nonce;
        this.txList = txList;
        headerHash = SHA256.hash(hashPrevBlock + hashMerkleRoot + timestamp.toString() + nonce);
    }
    public Block(Block previous, List<Transaction> txList){
        this.txList = txList;
        timestamp =  new Timestamp(System.currentTimeMillis());
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
    }


    public Block(List<Transaction> txList){
        this(null , txList);
    }

    public void incrementNonce(){
        headerHash = SHA256.hash(hashPrevBlock + hashMerkleRoot + timestamp.toString() + nonce);
        this.nonce += 1;
    }

    public int getNonce(){
        return nonce;
    }

    public String getHash(){
       return this.headerHash;
    }

    public boolean validateHashMerkle (){
        String transactionHashAccu = "";
        for(int i =0;i<txList.size();i++){
            transactionHashAccu += txList.get(i).getHash();
        }
        String hashMerkleRootExpected = SHA256.hash(transactionHashAccu);
        return hashMerkleRootExpected.equals(hashMerkleRoot);
    }


}
