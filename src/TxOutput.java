import java.io.Serializable;
import java.security.*;

public class TxOutput implements Serializable {

    double value;
    PublicKey receiver;

    public TxOutput(String[] strings) throws NoSuchAlgorithmException {

        // First element in array is value
        String value = strings[0].split(":")[1];
        this.value = Double.parseDouble(value);

        // Second element in array is the receiver public key
        String index = strings[1].split(":")[1];
        KeyPair client = Network.peers.getOrDefault(Integer.parseInt(index), null);
        if (client == null) {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024, new SecureRandom(index.getBytes()));
            client = keyPairGen.generateKeyPair();
            Network.peers.put(Integer.parseInt(index), client);
        }
        this.receiver = client.getPublic();


    }

}
