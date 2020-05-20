import java.awt.*;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Timestamp;
import java.util.Base64;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        //Network n = Network.getInstance(Integer.parseInt(args[0]));
        Network n = Network.getInstance(5000);
        Thread t = new Thread(n);
        t.start();
        Thread.sleep(2000);
        n.intiateClientConnection();
        TransactionSender txSender = new TransactionSender("txdataset_v2.txt");
        Thread tx = new Thread(txSender);
        tx.start();

//        Test test = new Test();
//        test.value = 1;
//        n.broadcast("test", test);
    }
}
