import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.*;

public class BlockChain {

    private static final int DIFFICULTY = 3;
    private static final int BLOCK_SIZE = 100;

    private static BlockChain instance;


    private volatile static boolean running = false;
    private HashMap<String, Block> blockChain;
    private List<Transaction> transactionPool;
    private Map<String, TxOutput> prevTransactions;
    private static HashSet<Integer> validatedTransactions;
    private Block maxLevelBlock;
    private Thread miningThread;
    private final int blockSize;
    int maxLevel;
    boolean isPOW;

    private BlockChain(int blockSize) {
        this.blockSize = blockSize;
        blockChain = new HashMap<>();
        transactionPool = new LinkedList<>();
        prevTransactions = new HashMap<>();
        validatedTransactions = new HashSet<>();
        maxLevel = 0;
    }

    public static BlockChain getInstance() {
        if (instance == null) {
            instance = new BlockChain(BLOCK_SIZE);
        }
        return instance;

    }

    public boolean addTransaction(Transaction transaction) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        boolean isValid = transaction.isValidTransaction(prevTransactions);
        if (isValid) {
            processTransaction(transaction);
            transactionPool.add(transaction);
            if (!running && transactionPool.size() >= blockSize)
                this.startMiningNewBlock();
        }
        return isValid;
    }


    public static boolean isRunning() {
        return running;
    }

    private void updatePrevTransactions(Transaction transaction) {

        for (TxInput input : transaction.inputs) {
            prevTransactions.remove(input.toString());
        }
    }

    private void startMiningNewBlock() throws NoSuchAlgorithmException {
        List<Transaction> list = new ArrayList<>(transactionPool.subList(0, blockSize));
        if (transactionPool.size() > blockSize) {
            transactionPool = transactionPool.subList(blockSize - 1, transactionPool.size());
        } else {
            transactionPool = new ArrayList<>();
        }
        Block newBlock = new Block(maxLevelBlock, list);
        running = true;
        miningThread = new Thread(new MinerPOW(DIFFICULTY, newBlock));
        miningThread.start();

    }

    public void stopMining() {
        running = false;

    }

    private void addOutputsToMap(Transaction transaction) {

        String key = transaction.getId() + ",";
        for (int i = 1; i <= transaction.outputs.size(); i++) {
            this.prevTransactions.put(key + i, transaction.outputs.get(i - 1));
        }

    }

    public void addReceivedBlock(Block newBlock) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        List<Transaction> nonValidated = newBlock.getNonValidateTransactions(validatedTransactions);
        if (nonValidated.size() > 0) {
            if (!validateTransactions(nonValidated))
                return;
        }
        if (newBlock.isValidBlock(blockChain)) {
            if (newBlock.level > maxLevel) {
                stopMining();
                maxLevel = newBlock.level;
                maxLevelBlock = newBlock;
            }
        }
        addToChainMap(newBlock);
        removeTxFromPool(newBlock.getTxList(), false);

    }

    public void addMyBlock(Block newBlock) {
        running = false;
        newBlock.addToTree(maxLevelBlock);
        maxLevelBlock = newBlock;
        maxLevel = newBlock.level;
        addToChainMap(newBlock);
        removeTxFromPool(newBlock.getTxList(), true);

    }

    private void addToChainMap(Block newBlock) {
        blockChain.put(newBlock.getHash(), newBlock);
    }

    private void removeTxFromPool(List<Transaction> txList, boolean b) {

        // added block from the network , remove its transactions from the pool if it's already existed
        for (Transaction tx : txList) {
            transactionPool.remove(tx);
        }

    }

    private boolean validateTransactions(List<Transaction> nonValidated) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        boolean accepted = true;
        for (Transaction transaction : nonValidated) {
            if (transaction.isValidTransaction(prevTransactions)) {
                processTransaction(transaction);
            } else
                accepted = false;
        }
        return accepted;

    }

    private void processTransaction(Transaction transaction) {
        validatedTransactions.add(transaction.getId());
        updatePrevTransactions(transaction);
        addOutputsToMap(transaction);
    }

    public static int getDIFFICULTY() {
        return DIFFICULTY;
    }

    public static int getBlockSize() {
        return BLOCK_SIZE;
    }

    public HashMap<String, Block> getBlockChain() {
        return blockChain;
    }

    public HashSet<Integer> getValidatedTransactions() {
        return validatedTransactions;
    }

}
