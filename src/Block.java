import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Block implements Serializable {

    public static final int TransactionSize = BlockChain.getBlockSize();

    private String hashPrevBlock;
    private String hashMerkleRoot;
    private Timestamp timestamp;
    private String headerHash;
    private int nonce = 0;
    boolean state = false;
    private Block parent;
    private ArrayList<Block> children;
    private List<Transaction> txList;
    int level = 0;


    public Block(String hashPrevBlock, String hashMerkleRoot, Timestamp timestamp, int nonce, List<Transaction> txList) {
        this.hashPrevBlock = hashPrevBlock;
        this.hashMerkleRoot = hashMerkleRoot;
        this.timestamp = timestamp;
        this.nonce = nonce;
        this.txList = txList;
        headerHash = SHA256.hash(hashPrevBlock + hashMerkleRoot + timestamp.toString() + nonce);
        children = new ArrayList<>();
    }

    public Block(Block previous, List<Transaction> txList) throws NoSuchAlgorithmException { // transaction list must be validated
        this.txList = txList;
        timestamp = new Timestamp(System.currentTimeMillis());
        if (previous != null) {
            hashPrevBlock = previous.getHash();
            level = previous.level + 1;
        } else {
            hashPrevBlock = "";
            level = 1;
        }
        String transactionHashAccu = "";
        for (int i = 0; i < txList.size(); i++) {
            transactionHashAccu += txList.get(i).getHash();
        }
        hashMerkleRoot = SHA256.hash(transactionHashAccu);
        headerHash = SHA256.hash(hashPrevBlock + hashMerkleRoot + timestamp.toString() + nonce);
        children = new ArrayList<>();

    }

    public Block(List<Transaction> txList) throws NoSuchAlgorithmException {
        this(null, txList);
    }

    public void incrementNonce() {
        headerHash = SHA256.hash(hashPrevBlock + hashMerkleRoot + timestamp.toString() + nonce);
        this.nonce += 1;
    }

    public int getNonce() {
        return nonce;
    }

    public String getHash() {
        return this.headerHash;
    }

    private String makeMerkleHash(List<Transaction> txs) throws NoSuchAlgorithmException {
        List<String> temp1 = new ArrayList();
        for (int i = 0; i < txs.size(); i++) {
            temp1.add(txs.get(i).getHash());
        }
        while (temp1.size() != 1) {
            List<String> temp2 = new ArrayList<>();
            for (int i = 0; i < temp1.size(); i += 2) {
                String txAcc = "";
                txAcc += temp1.get(i);
                if (i + 1 < txs.size()) {
                    txAcc += txs.get(i + 1).getHash();
                } else {
                    txAcc += txs.get(i).getHash();
                }
                temp2.add(SHA256.hash(txAcc));
            }
            temp1 = temp2;
        }
        return temp1.get(0);
    }

    public boolean validateHashMerkle() throws NoSuchAlgorithmException {
        String transactionHashAccu = "";
        for (int i = 0; i < txList.size(); i++) {
            transactionHashAccu += txList.get(i).getHash();
        }
        String hashMerkleRootExpected = SHA256.hash(transactionHashAccu);
        return hashMerkleRootExpected.equals(hashMerkleRoot);
    }

    public void addChild(Block child) {
        for (int i = 0; i < children.size(); i++) {
            if (child != children.get(i)) {
                return;
            }
        }
        children.add(child);
    }

    public boolean validatePrevHeaderHash(HashMap<String, Block> blockChain) { // must be last validation done
        if (!blockChain.containsKey(this.hashPrevBlock)) {
            return false;
        } else {
            Block parent = blockChain.get(this.hashPrevBlock);
            addToTree(parent);
            return true;
        }
    }

    public void addToTree(Block parent) {
        if (parent != null) {
            this.parent = parent;
            parent.addChild(this);
        } else {
            this.parent = parent;
            level = 1;
        }
    }

    public boolean validateTransactionSize() {
        if (txList.size() != TransactionSize)
            return false;
        return true;
    }

    public List<Transaction> getTxList() {
        return txList;
    }

    public boolean isValidBlock(HashMap<String, Block> blockChain) throws NoSuchAlgorithmException {
        if (validateHashMerkle() && validateTransactionSize())
            return validatePrevHeaderHash(blockChain);
        return false;
    }

    public List<Transaction> getNonValidateTransactions(HashSet<Integer> validatedTransactions) {

        List<Transaction> list = new ArrayList<Transaction>();
        for (Transaction tx : txList) {

            if (!validatedTransactions.contains(tx.getId()))
                list.add(tx);

        }
        return list;

    }
}
