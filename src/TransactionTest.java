import org.junit.jupiter.api.Test;

import java.security.*;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {
    @Test
    public void testGenerateTransaction() throws NoSuchAlgorithmException {
        String transactionLine = "50\tintput:41\tprevioustx:41\toutputindex:1\tvalue1:0.6444474\toutput1:47\tvalue2:10.64253\toutput2:46\tvalue3:5.213972\toutput3:41";
        String[] splits = transactionLine.split("\t");
        Transaction transaction = new Transaction(splits);
        assertNotNull(transaction);
        assertEquals(transaction.getId(), 50);

        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRand = new SecureRandom("41".getBytes());
        keyPairGen.initialize(1024, secureRand);
        assertEquals(transaction.sender.getFormat(), keyPairGen.generateKeyPair().getPublic().getFormat());

        assertEquals(transaction.inputCount, 1);
        assertEquals(transaction.outputCount, 3);
        assertEquals(transaction.inputs.get(0).getPrevTransactionId(), 41);
        assertEquals(transaction.inputs.get(0).getOutputIndex(), 1);
        secureRand = new SecureRandom("47".getBytes());
        keyPairGen.initialize(1024, secureRand);
        assertEquals(transaction.outputs.get(0).value, 0.6444474);
        assertEquals(SHA256.bytesToHex(transaction.outputs.get(0).receiver.getEncoded()), SHA256.bytesToHex(Network.peers.get(47).getPublic().getEncoded()));

        secureRand = new SecureRandom("46".getBytes());
        keyPairGen.initialize(1024, secureRand);
        assertEquals(transaction.outputs.get(1).value, 10.64253);
        assertEquals(SHA256.bytesToHex(transaction.outputs.get(1).receiver.getEncoded()), SHA256.bytesToHex(Network.peers.get(46).getPublic().getEncoded()));

        assertEquals(transaction.outputs.get(2).value, 5.213972);
        assertEquals(SHA256.bytesToHex(transaction.outputs.get(2).receiver.getEncoded()), SHA256.bytesToHex(Network.peers.get(41).getPublic().getEncoded()));

    }

    @Test
    public void testGenerateTransaction2() throws NoSuchAlgorithmException {

        String transactionLine = "71\tintput:26\tprevioustx:68\toutputindex:1\tvalue1:3.4163618\toutput1:47\tvalue2:3.652896\toutput2:27\tvalue3:3.347314\toutput3:26";
        String[] splits = transactionLine.split("\t");
        Transaction transaction = new Transaction(splits);
        assertNotNull(transaction);
        assertEquals(transaction.getId(), 71);

        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRand = new SecureRandom("26".getBytes());
        keyPairGen.initialize(1024, secureRand);
        assertEquals(transaction.sender.getFormat(), keyPairGen.generateKeyPair().getPublic().getFormat());

        assertEquals(transaction.inputCount, 1);
        assertEquals(transaction.outputCount, 3);
        assertEquals(transaction.inputs.get(0).getPrevTransactionId(), 68);
        assertEquals(transaction.inputs.get(0).getOutputIndex(), 1);

        assertEquals(transaction.outputs.get(0).value, 3.4163618);
        assertEquals(SHA256.bytesToHex(transaction.outputs.get(0).receiver.getEncoded()), SHA256.bytesToHex(Network.peers.get(47).getPublic().getEncoded()));

        assertEquals(transaction.outputs.get(1).value, 3.652896);
        assertEquals(SHA256.bytesToHex(transaction.outputs.get(1).receiver.getEncoded()), SHA256.bytesToHex(Network.peers.get(27).getPublic().getEncoded()));

        assertEquals(transaction.outputs.get(2).value, 3.347314);
        assertEquals(SHA256.bytesToHex(transaction.outputs.get(2).receiver.getEncoded()), SHA256.bytesToHex(Network.peers.get(26).getPublic().getEncoded()));

    }

    @Test
    public void testTransactionValidateInput() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        String transactionLine = "50\tintput:0\tprevioustx:41\toutputindex:1\tvalue1:0.6444474\toutput1:47\tvalue2:10.64253\toutput2:46\tvalue3:5.213972\toutput3:41";
        String[] splits = transactionLine.split("\t");
        Transaction transaction = new Transaction(splits);
        transaction.setSignature();
        assertTrue(transaction.verifySignature());

    }

    @Test
    public void testGenerateTransaction2InputInitial() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        String transactionLine = "12\tintput:0\tvalue:56.163067\toutput:12";
        String[] splits = transactionLine.split("\t");
        Transaction transaction = new Transaction(splits);
        assertTrue(transaction.isValidTransaction(new LinkedHashMap<>()));


    }

}