import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


public class Transaction {

    List<TxInput> inputs;
    List<TxOutput> outputs;
    private final int senderIndex;
    private byte[] signature;
    int id;
    int inputCount;
    int outputCount;
    boolean isValid;
    PublicKey sender;


    public Transaction(String[] transactionStrings) throws NoSuchAlgorithmException {
        inputs = new ArrayList<>();
        outputs = new ArrayList<>();

        this.id = Integer.parseInt(transactionStrings[0]);

        // Public key for the sender and its index
        String index = transactionStrings[1].split(":")[1];
        this.senderIndex = Integer.parseInt(index);

        KeyPair client = Clients.clients.getOrDefault(Integer.parseInt(index), null);
        if (client == null) {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024, new SecureRandom(index.getBytes()));
            client = keyPairGen.generateKeyPair();
            Clients.clients.put(Integer.parseInt(index), client);
        }
        this.sender = client.getPublic();

        for (int i = 2; i < transactionStrings.length; i += 2) {
            if (Pattern.matches("value[0-9]:.*", transactionStrings[i])) {
                TxOutput output = new TxOutput(Arrays.copyOfRange(transactionStrings, i, i + 2));
                outputs.add(output);
            } else {
                TxInput input = new TxInput(Arrays.copyOfRange(transactionStrings, i, i + 2));
                inputs.add(input);
            }
        }
        inputCount = inputs.size();
        outputCount = outputs.size();

    }

    public boolean isValidTransaction(Map<String, TxOutput> txMap) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        for (int i = 0; i < inputCount; i++) {
            boolean verifyInput = this.verifySenderInput(txMap, inputs.get(i));

            if (this.verifyDoublySpent(txMap, inputs.get(i))) {
                if (this.verifySenderInput(txMap, inputs.get(i)))
                    return verifySignature();
            }
        }
        return true;
    }

    private boolean verifyDoublySpent(Map<String, TxOutput> txMap, TxInput txInput) {

        String key = txInput.toString();
        return txMap.containsKey(key);

    }

    private boolean verifySenderInput(Map<String, TxOutput> txMap, TxInput txInput) {

        String key = txInput.toString();
        TxOutput value = txMap.get(key);
        return Utils.bytesToHex(value.receiver.getEncoded()).equals(Utils.bytesToHex(this.sender.getEncoded()));

    }

    public boolean verifySignature() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        PublicKey publicKey = Clients.clients.get(senderIndex).getPublic();

        Signature sign = Signature.getInstance("SHA256withRSA");
        byte[] bytes = this.getHash().getBytes();

        sign.initVerify(publicKey);
        sign.update(bytes);

        //Verifying the signature
        return sign.verify(signature);

    }

    private byte[] generateSignature() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        PrivateKey privateKey = Clients.clients.get(senderIndex).getPrivate();
        Signature sign = Signature.getInstance("SHA256withRSA");

        //Initializing the signature
        sign.initSign(privateKey);
        byte[] bytes = this.getHash().getBytes();

        //Adding data to the signature
        sign.update(bytes);

        //Calculating the signature
        return sign.sign();
    }

    public void setSignature() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        this.signature = generateSignature();
    }

    public String getHash() throws NoSuchAlgorithmException {

        MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
        String input = String.valueOf(id) + inputCount + inputs;
        return Arrays.toString(msgDigest.digest(input.getBytes(StandardCharsets.UTF_8)));

    }


}
