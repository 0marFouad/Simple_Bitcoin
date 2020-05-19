import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.*;

public class BlockChain {

    int blockSize;
    LinkedList<Block> chain;
    //TODO discuss this
    LinkedList<List<Transaction>> transactionPool;
    Map<String, TxOutput> prevTransactions;
    int length;
    //TODO discuss this
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
            addIntoPool(transactionPool, transaction);
            if (transactionPool.getFirst().size() == blockSize)
                this.startMiningNewBlock();
        }
        return isValid;
    }

    private void addIntoPool(LinkedList<List<Transaction>> transactionPool, Transaction transaction) {
        if (transactionPool.getLast().size() < blockSize)
            transactionPool.getLast().add(transaction);
        else {
            List<Transaction> newPool = new ArrayList<>();
            newPool.add(transaction);
            transactionPool.addLast(newPool);
        }
    }

    private void updatePrevTransactions(Map<String, TxOutput> prevTransactions, Transaction transaction) {

        for (TxInput input : transaction.inputs) {
            prevTransactions.remove(input.toString());
        }
    }

    private void startMiningNewBlock() throws NoSuchAlgorithmException {
        List<Transaction> list = transactionPool.removeFirst();
        Block newBlock = new Block(chain.getLast(), list);
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

}
