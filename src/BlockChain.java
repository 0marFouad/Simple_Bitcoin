import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.*;

public class BlockChain {

    int blockSize;
    LinkedList<Block> chain;
    List<Transaction> pendingTransactions;
    List<Transaction> transactionPool;
    Map<String, TxOutput> prevTransactions;
    int length;
    private Thread miningThread;

    public BlockChain(int blockSize) {
        this.blockSize = blockSize;
        chain = new LinkedList<>();
        transactionPool = new LinkedList<>();
        prevTransactions = new HashMap<>();
        length = 0;
    }

    public boolean addTransaction(Transaction transaction) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        boolean isValid = transaction.isValidTransaction(prevTransactions);
        if (isValid) {
            updatePrevTransactions(prevTransactions, transaction);
            addOutputsToMap(prevTransactions, transaction);
            transactionPool.add(transaction);
            if (transactionPool.size() >= blockSize)
                this.startMiningNewBlock();
        }
        return isValid;
    }

    private void updatePrevTransactions(Map<String, TxOutput> prevTransactions, Transaction transaction) {

        for (TxInput input : transaction.inputs) {
            prevTransactions.remove(input.toString());
        }
    }

    private void startMiningNewBlock() throws NoSuchAlgorithmException {
        pendingTransactions = transactionPool.subList(0, blockSize);
        if (transactionPool.size() > blockSize) {
            transactionPool = transactionPool.subList(blockSize - 1, transactionPool.size());
        } else {
            transactionPool = new ArrayList<>();
        }
        Block newBlock = new Block(chain.getLast(), pendingTransactions);
        miningThread = new Thread(new MinerPOW(3, newBlock));
        miningThread.start();

    }

    public void stopMining() {
        if (miningThread != null)
            miningThread.interrupt();
    }

    private void addOutputsToMap(Map<String, TxOutput> prevTransactions, Transaction transaction) {

        String key = transaction.getId() + ",";
        for (int i = 1; i <= transaction.outputs.size(); i++) {
            prevTransactions.put(key + i, transaction.outputs.get(i - 1));
        }

    }

    private void addMyBlock(Block newBlock) {
        pendingTransactions = new ArrayList<>();
        chain.add(newBlock);
    }

    private void addReceivedBlock(Block newBlock) {
        if (newBlock.isValidBlock()) {
            stopMining();
            checkDifferentTransaction(newBlock);
            chain.add(newBlock);
        }
    }

    private void checkDifferentTransaction(Block newBlock) {

//        for (Transaction :
//             ) {
//
//        }
//

    }

}
