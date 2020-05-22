import java.math.BigInteger;

public class MinerPOW implements Runnable {
    private final int difficulty;
    private Block block;

    MinerPOW(int difficulty, Block block) {
        this.difficulty = difficulty;
        this.block = block;
    }

    private void mine() {
        String hash = block.getHash();
        String bits = new BigInteger(hash, 16).toString(2);
        long startTime = System.nanoTime();

        while (BlockChain.isRunning() && bits.length() + difficulty > 256) {
            block.incrementNonce();
            hash = block.getHash();
            bits = new BigInteger(hash, 16).toString(2);
        }
        long endTime = System.nanoTime();

        // get difference of two nanoTime values
        if (BlockChain.isRunning()) {
            System.out.println("time Elapsed " + (endTime - startTime));
            //TODO broadcast Done
            Network.getInstance().broadcast("Block", block);
            //add to chain
            BlockChain.getInstance().addMyBlock(block);
            System.out.println("Mining completed");
        }
    }


    private boolean isValid() {
        String hash = block.getHash();
        String bits = new BigInteger(hash, 16).toString(2);
        return bits.length() + difficulty > 256;
    }


    @Override
    public void run() {
        this.mine();
    }
}
