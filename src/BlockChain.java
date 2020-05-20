import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.*;

public class BlockChain {

    private static final int DIFFICULTY = 3;
    //TODO hash with block here
    private HashMap<String, Block> blockChain;
    private List<Transaction> transactionPool;
    private Map<String, TxOutput> prevTransactions;
    private HashSet<Integer> validatedTransactions;
    private Block maxLevelBlock;
    private Thread miningThread;
    private final int blockSize;
    int maxLevel;

    public BlockChain(int blockSize) {
        this.blockSize = blockSize;
        blockChain = new HashMap<>();
        transactionPool = new LinkedList<>();
        prevTransactions = new HashMap<>();
        validatedTransactions = new HashSet<>();
        maxLevel = 0;
    }

    public boolean addTransaction(Transaction transaction) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        boolean isValid = transaction.isValidTransaction(prevTransactions);
        if (isValid) {
            processTransaction(transaction);
            transactionPool.add(transaction);
            if (miningThread == null && transactionPool.size() >= blockSize)
                this.startMiningNewBlock();
        }
        return isValid;
    }

    private void updatePrevTransactions(Transaction transaction) {

        for (TxInput input : transaction.inputs) {
            prevTransactions.remove(input.toString());
        }
    }

    private void startMiningNewBlock() throws NoSuchAlgorithmException {
        List<Transaction> list = transactionPool.subList(0, blockSize);
        if (transactionPool.size() > blockSize) {
            transactionPool = transactionPool.subList(blockSize - 1, transactionPool.size());
        } else {
            transactionPool = new ArrayList<>();
        }
        Block newBlock = new Block(maxLevelBlock, list);
        miningThread = new Thread(new MinerPOW(DIFFICULTY, newBlock));
        miningThread.start();

    }

    public void stopMining() {
        if (miningThread != null)
            miningThread.interrupt();
    }

    private void addOutputsToMap(Transaction transaction) {

        String key = transaction.getId() + ",";
        for (int i = 1; i <= transaction.outputs.size(); i++) {
            this.prevTransactions.put(key + i, transaction.outputs.get(i - 1));
        }

    }


    private void addReceivedBlock(Block newBlock) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        List<Transaction> nonValidated = newBlock.getNonValidateTransactions(validatedTransactions);
        if (nonValidated.size() > 0)
            if (!validateTransactions(nonValidated))
                return;
        if (newBlock.isValidBlock(blockChain)) {
            if (newBlock.level > maxLevel) {
                stopMining();
                maxLevel = newBlock.level;
                maxLevelBlock = newBlock;
            }
        }
        removeTxFromPool(newBlock.txList);
    }

    private void removeTxFromPool(List<Transaction> txList) {

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


}
