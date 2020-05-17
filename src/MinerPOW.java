import java.math.BigInteger;

public class MinerPOW {
    private int difficulty;
    private Block block;

    MinerPOW(int difficulty){
        this.difficulty = difficulty;
        mine();
    }

    private void mine(){
        String hash = block.getHash();
        String bits =  new BigInteger(hash, 16).toString(2);
        String target = "";
        for(int i=0;i<difficulty;i++){
            target += '0';
        }
        while(bits.substring(0,difficulty).equals(target)){
            block.incrementNonce();
            hash = block.getHash();
            bits =  new BigInteger(hash, 16).toString(2);
        }
        //We need to check every loop that this BLOCK was not successfully mined by another peer
        //Here we will broadcast BLOCK [SUCCESS CASE]
    }

}
