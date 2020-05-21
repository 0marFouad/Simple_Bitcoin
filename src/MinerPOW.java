import java.math.BigInteger;

public class MinerPOW implements Runnable {
    private final int difficulty;
    private Block block;

    MinerPOW(int difficulty, Block block) {
        this.difficulty = difficulty;
        this.block = block;
    }

    private void mine() {
        System.out.println("STARTED MINING " + block.getHash());
        String hash = block.getHash();
        String bits = new BigInteger(hash, 16).toString(2);
        while (!Thread.interrupted() && bits.length() + difficulty > 256) {
            block.incrementNonce();
            hash = block.getHash();
            bits = new BigInteger(hash, 16).toString(2);
        }
        //TODO broadcast Done
        Network.getInstance().broadcast("Block", block);
        //add to chain
        BlockChain.getInstance().addMyBlock(block);
    }


    private boolean isValid() {
        String hash = block.getHash();
        String bits = new BigInteger(hash, 16).toString(2);
        String target = "";
        for (int i = 0; i < difficulty; i++) {
            target += '0';
        }
        return bits.substring(0, difficulty).equals(target);
    }


    @Override
    public void run() {
        this.mine();
    }
}
