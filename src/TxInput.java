import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;

public class TxInput {

    private final int prevTransactionId;
    private final int outputIndex;


    public TxInput(String[] strings) throws NoSuchAlgorithmException {

        // First element in array is previous transaction id
        this.prevTransactionId = Integer.parseInt(strings[0].split(":")[1]);

        // Second element in array is the index of the used output in the transaction
        this.outputIndex = Integer.parseInt(strings[1].split(":")[1]);

    }


    public int getPrevTransactionId() {
        return prevTransactionId;
    }

    public int getOutputIndex() {
        return outputIndex;
    }

    @Override
    public String toString() {
        return prevTransactionId + "," + outputIndex;
    }
}
