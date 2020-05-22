import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Scanner;

public class TransactionSender implements Runnable {

    private String filePath;

    public TransactionSender(String path) {
        this.filePath = path;
    }

    @Override
    public void run() {

        File file = new File(filePath);

        try {
            FileInputStream inputStream = new FileInputStream(file);
            Scanner scan = new Scanner(inputStream);
            scan.nextLine();
            while (scan.hasNext()) {
                String line = scan.nextLine();
                String[] strings = line.split("\t");
                Transaction transaction = new Transaction(strings);
                transaction.setSignature();
                if (transaction.getId() % 800 == 0)
                    System.out.println(transaction.getId());
                //Call broadcast from the network instance
                Network.getInstance().broadcast("tx", transaction);
                BlockChain.getInstance().addTransaction(transaction);
                Thread.sleep(100);
            }
        } catch (
                FileNotFoundException | NoSuchAlgorithmException | InterruptedException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
    }
}
