import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Block {
    private String hashPrevBlock;
    private String hashMerkleRoot;
    private Timestamp timestamp;
    private String headerHash;
    private int nonce = 0;
    boolean state = false;
    private Block parent;
    private ArrayList<Block> children;
    List <Transaction > txList;
    int level = 0;
    public Block(String hashPrevBlock , String hashMerkleRoot, Timestamp timestamp, int nonce, List <Transaction> txList){
        this.hashPrevBlock = hashPrevBlock;
        this.hashMerkleRoot = hashMerkleRoot;
        this.timestamp = timestamp;
        this.nonce = nonce;
        this.txList = txList;
        headerHash = SHA256.hash(hashPrevBlock + hashMerkleRoot + timestamp.toString() + nonce);
    }

    public Block(Block previous, List<Transaction> txList) throws NoSuchAlgorithmException {
        this.txList = txList;
        timestamp =  new Timestamp(System.currentTimeMillis());
        if(previous != null){
            hashPrevBlock = previous.getHash();
            level = previous.level +1;
        }else{
            hashPrevBlock = "";
            level = 1;
        }
        String transactionHashAccu = "";
        for(int i =0;i<txList.size();i++){
            transactionHashAccu += txList.get(i).getHash();
        }
        hashMerkleRoot = SHA256.hash(transactionHashAccu);

    }

    public Block(List<Transaction> txList) throws NoSuchAlgorithmException {
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

    public boolean validateHashMerkle () throws NoSuchAlgorithmException {
        String transactionHashAccu = "";
        for(int i =0;i<txList.size();i++){
            transactionHashAccu += txList.get(i).getHash();
        }
        String hashMerkleRootExpected = SHA256.hash(transactionHashAccu);
        return hashMerkleRootExpected.equals(hashMerkleRoot);
    }

    public void addChild(Block child){
        for(int i=0;i<children.size();i++){
            if(child != children.get(i)){
                return;
            }
        }
        children.add(child);
    }

    public boolean validatePrevHeaderHash (HashMap<String, Block> blockChain){ // must be last validation done
        if(!blockChain.containsKey(this.hashPrevBlock)){
            return false;
        }else{
            Block parent = blockChain.get(this.hashPrevBlock);
            this.parent = parent;
            parent.addChild(this);
            level = parent.level +1;
            return true;
        }
    }
}
