import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;

public class TxOutput {

    double value;
    PublicKey receiver;

    public TxOutput(String[] strings) throws NoSuchAlgorithmException {

        // First element in array is value
        String value = strings[0].split(":")[1];
        this.value = Double.parseDouble(value);

        // Second element in array is the receiver public key
        String index = strings[1].split(":")[1];
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRand = new SecureRandom(index.getBytes());
        keyPairGen.initialize(1024, secureRand);
        this.receiver = keyPairGen.generateKeyPair().getPublic();


    }

}
