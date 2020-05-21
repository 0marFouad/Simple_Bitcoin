import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class Server implements Runnable {

    //initialize socket and input stream
    private Socket socket = null;
    //    private ServerSocket server = null;
    private ObjectInputStream in = null;
//    private Integer port;


    // constructor with port
    public Server(Socket socket) {
        // starts server and waits for a connection
        try {
            this.socket = socket;
            // takes input from the client socket
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    @Override
    public void run() {
        System.out.println("STARTING SOCKET THREAD");
        while (true) {
            try {
                String token = in.readUTF();
                if (token.equals("tx")) {
                    Transaction tx = (Transaction) in.readObject();
                    System.out.println(tx.getId());
                    // add to transaction pool
                    boolean accepted = BlockChain.getInstance().addTransaction(tx);
                    System.out.println(tx.getId() + " " + accepted);
                } else if (token.equals("Block")) {
                    Block block = (Block) in.readObject();
                    System.out.println(block.level);
                    // add to object pool
                    BlockChain.getInstance().addReceivedBlock(block);
                } else {
                    Test test = (Test) in.readObject();
                    System.out.println(test.value);
                }
            } catch (IOException i) {
                System.out.println(i);
            } catch (ClassNotFoundException | NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
                e.printStackTrace();
            }
        }
    }
}
