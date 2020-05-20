//import org.junit.jupiter.api.Test;
//
//import java.security.*;
//
//import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {
//    @Test
//    public void testGenerateTransaction() throws NoSuchAlgorithmException {
//        String transactionLine = "50\tintput:41\tprevioustx:41\toutputindex:1\tvalue1:0.6444474\toutput1:47\tvalue2:10.64253\toutput2:46\tvalue3:5.213972\toutput3:41";
//        String[] splits = transactionLine.split("\t");
//        Transaction transaction = new Transaction(splits);
//        assertNotNull(transaction);
//        assertEquals(transaction.getId(), 50);
//
//        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
//        SecureRandom secureRand = new SecureRandom("41".getBytes());
//        keyPairGen.initialize(1024, secureRand);
//        assertEquals(transaction.sender.getFormat(), keyPairGen.generateKeyPair().getPublic().getFormat());
//
//        assertEquals(transaction.inputCount, 1);
//        assertEquals(transaction.outputCount, 3);
//        assertEquals(transaction.inputs.get(0).getPrevTransactionId(), 41);
//        assertEquals(transaction.inputs.get(0).getOutputIndex(), 1);
//        secureRand = new SecureRandom("47".getBytes());
//        keyPairGen.initialize(1024, secureRand);
//        assertEquals(transaction.outputs.get(0).value, 0.6444474);
//        assertEquals(Utils.bytesToHex(transaction.outputs.get(0).receiver.getEncoded()), Utils.bytesToHex(Network.clients.get(47).getPublic().getEncoded()));
//
//        secureRand = new SecureRandom("46".getBytes());
//        keyPairGen.initialize(1024, secureRand);
//        assertEquals(transaction.outputs.get(1).value, 10.64253);
//        assertEquals(Utils.bytesToHex(transaction.outputs.get(1).receiver.getEncoded()), Utils.bytesToHex(Network.clients.get(46).getPublic().getEncoded()));
//
//        assertEquals(transaction.outputs.get(2).value, 5.213972);
//        assertEquals(Utils.bytesToHex(transaction.outputs.get(2).receiver.getEncoded()), Utils.bytesToHex(Network.clients.get(41).getPublic().getEncoded()));
//
//    }
//
//    @Test
//    public void testGenerateTransaction2() throws NoSuchAlgorithmException {
//
//        String transactionLine = "71\tintput:26\tprevioustx:68\toutputindex:1\tvalue1:3.4163618\toutput1:47\tvalue2:3.652896\toutput2:27\tvalue3:3.347314\toutput3:26";
//        String[] splits = transactionLine.split("\t");
//        Transaction transaction = new Transaction(splits);
//        assertNotNull(transaction);
//        assertEquals(transaction.getId(), 71);
//
//        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
//        SecureRandom secureRand = new SecureRandom("26".getBytes());
//        keyPairGen.initialize(1024, secureRand);
//        assertEquals(transaction.sender.getFormat(), keyPairGen.generateKeyPair().getPublic().getFormat());
//
//        assertEquals(transaction.inputCount, 1);
//        assertEquals(transaction.outputCount, 3);
//        assertEquals(transaction.inputs.get(0).getPrevTransactionId(), 68);
//        assertEquals(transaction.inputs.get(0).getOutputIndex(), 1);
//
//        assertEquals(transaction.outputs.get(0).value, 3.4163618);
//        assertEquals(Utils.bytesToHex(transaction.outputs.get(0).receiver.getEncoded()), Utils.bytesToHex(Network.clients.get(47).getPublic().getEncoded()));
//
//        assertEquals(transaction.outputs.get(1).value, 3.652896);
//        assertEquals(Utils.bytesToHex(transaction.outputs.get(1).receiver.getEncoded()), Utils.bytesToHex(Network.clients.get(27).getPublic().getEncoded()));
//
//        assertEquals(transaction.outputs.get(2).value, 3.347314);
//        assertEquals(Utils.bytesToHex(transaction.outputs.get(2).receiver.getEncoded()), Utils.bytesToHex(Network.clients.get(26).getPublic().getEncoded()));
//
//    }
//
//    @Test
//    public void testTransactionValidateInput() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
//
//        String transactionLine = "50\tintput:0\tprevioustx:41\toutputindex:1\tvalue1:0.6444474\toutput1:47\tvalue2:10.64253\toutput2:46\tvalue3:5.213972\toutput3:41";
//        String[] splits = transactionLine.split("\t");
//        Transaction transaction = new Transaction(splits);
//        transaction.setSignature();
//        assertTrue(transaction.verifySignature());
//
//    }

}