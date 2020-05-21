import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class MinerBFT {

    private static MinerBFT instance;
    private String state;
    private int myId;
    private int prepareMsgsReceived;
    private int commitMsgsReceived;

    public static MinerBFT getInstance(int id) {
        if (instance == null) {
            instance = new MinerBFT(id);
            return instance;
        } else {
            return instance;
        }
    }

    public static MinerBFT getInstance() {
        if (instance == null) {
            return null;
        } else {
            return instance;
        }
    }


    private MinerBFT(int id) {
        myId = id;
        state = "NEW_ROUND";
        prepareMsgsReceived = 0;
        commitMsgsReceived = 0;
    }

    public void handleMsg(String token, Block block) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        if (state.equals("NEW_ROUND")) {
            handleNewRound(block);
        } else if (state.equals("PRE_PREPARE")) {
            handlePrePrepared(block);
        } else if (state.equals("PREPARE")) {
            handlePrepared(block);
        }
    }

    public void handleNewRound(Block block) {
        int numOfBlocks = BlockChain.getInstance().getBlockChain().size();
        if (numOfBlocks % 3 == myId) {
            //I will Broadcast the block then
            Network network = Network.getInstance();
            network.broadcast("NEW_ROUND", block);
            state = "PRE_PREPARED";
        } else {
            state = "PRE_PREPARED";
            BlockChain b = BlockChain.getInstance();
            if (block.validateTransactionSize() && block.getNonValidateTransactions(b.getValidatedTransactions()).size() == 0 && block.validatePrevHeaderHash(b.getBlockChain())) {
                Network network = Network.getInstance();
                network.broadcast("PREPARE", block);
            }
        }
    }

    public void handlePrePrepared(Block block) {
        prepareMsgsReceived++;
        if (prepareMsgsReceived == 3) {
            state = "PREPARED";
            Network network = Network.getInstance();
            network.broadcast("COMMIT", block);
        }
    }

    public void handlePrepared(Block block) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        commitMsgsReceived++;
        if (commitMsgsReceived == 3) {
            //Done with the previous round
            state = "NEW_ROUND";
            commitMsgsReceived = 0;
            prepareMsgsReceived = 0;
            BlockChain b = BlockChain.getInstance();
            b.addReceivedBlock(block);
        }
    }
}
