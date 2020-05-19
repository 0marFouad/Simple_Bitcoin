import java.math.BigInteger;

public class MinerPOW implements Runnable {
    private final int difficulty;
    private Block block;

    MinerPOW(int difficulty, Block block) {
        this.difficulty = difficulty;
        this.block = block;
        mine();
    }

    private void mine() {
        String hash = block.getHash();
        String bits = new BigInteger(hash, 16).toString(2);
        String target = "";
        for (int i = 0; i < difficulty; i++) {
            target += '0';
        }
        // interrupted means received a block and this block is valid
        //We need to check every loop that this BLOCK was not successfully mined by another peer
        while (!Thread.interrupted() && bits.substring(0, difficulty).equals(target)) {
            block.incrementNonce();
            hash = block.getHash();
            bits = new BigInteger(hash, 16).toString(2);
        }
        //Here we will broadcast BLOCK [SUCCESS CASE]
        //TODO broadcast
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
        //TODO complete this
        this.mine();
    }
}
