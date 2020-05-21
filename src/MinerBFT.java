public class MinerBFT {

    private static MinerBFT instance;
    private String state;
    private int myId;
    private int

    public static MinerBFT getInstance(int port) {
        if (instance == null) {
            SERVER_PORT = port;
            if (port == 5000) {
                CLIENT1_ADDR = "127.0.0.1/6000";
            } else {
                CLIENT1_ADDR = "127.0.0.1/5000";
            }
            instance = new MinerBFT();
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
        startMining(block);
    }
    private void startMining(Block block){

    }

    public void handleNewRound(int numOfBlocks, Block block) {
        if (numOfBlocks % 3 == myId) {
            //I will Broadcast the block then
            Network network = Network.getInstance();
            network.broadcast("NEW_ROUND", block);
            state = "PRE_PREPARED";
        } else {
            state = "PRE_PREPARED";
            if (block && block.validatePrevHeaderHash(BlockChain.getBlockChain())) {

            }
        }
    }
}
