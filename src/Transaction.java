import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


public class Transaction {

    List<TxInput> inputs;
    List<TxOutput> outputs;
    private final String senderIndex;
    private byte[] signature;
    int id;
    int inputCount;
    int outputCount;
    boolean isValid;
    PublicKey sender;


    public Transaction(String[] transactionStrings) throws NoSuchAlgorithmException {
        this.id = Integer.parseInt(transactionStrings[0]);

        // Public key for the sender and its index
        String index = transactionStrings[1].split(":")[1];
        this.senderIndex = index;

        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRand = new SecureRandom(index.getBytes());
        keyPairGen.initialize(1024, secureRand);
        this.sender = keyPairGen.generateKeyPair().getPublic();

        for (int i = 2; i < transactionStrings.length; i++) {
            boolean b3 = Pattern.matches(".s", "as");
            if (Pattern.matches("output[0-9][a-zA-Z0-9]*", transactionStrings[i])) {
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
        return value.receiver.equals(this.sender);

    }

    private boolean verifySignature() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        byte[] hash = this.getHash();
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initVerify(this.sender);
        sign.update(hash);
        return sign.verify(this.signature);

    }

    private byte[] generateSignature() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRand = new SecureRandom(this.senderIndex.getBytes());
        keyPairGen.initialize(1024, secureRand);
        PrivateKey privateKey = keyPairGen.generateKeyPair().getPrivate();
        byte[] hash = this.getHash();
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(privateKey);
        byte[] bytes = this.getHash();
        //Adding data to the signature
        sign.update(bytes);
        //Calculating the signature
        return sign.sign();

    }

    public void setSignature() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        this.signature = generateSignature();
    }

    public byte[] getHash() throws NoSuchAlgorithmException {

        MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
        String input = String.valueOf(id) + inputCount + inputs;
        return msgDigest.digest(input.getBytes(StandardCharsets.UTF_8));

    }


}
