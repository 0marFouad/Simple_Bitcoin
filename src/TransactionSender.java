import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
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
            while (scan.hasNext()) {
                String line = scan.nextLine();
                String[] strings = line.split("\t");
                Transaction transaction = new Transaction(strings);
                //Call broadcast from the network instance
                Network.getInstance().broadcast("tx", transaction);
                Thread.sleep(2000);
            }

        } catch (
                FileNotFoundException | NoSuchAlgorithmException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
