import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

public class Client implements Runnable {
    // initialize socket and input output streams
    private Socket socket = null;
    private DataInputStream input = null;
    private ObjectOutputStream out = null;


    // constructor to put ip address and port
    public void client(String address, int port) throws IOException {
        // establish a connection
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");

            // takes input from terminal
            input = new DataInputStream(System.in);

            // sends output to the socket
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (UnknownHostException u) {
            System.out.println(u);
        } catch (IOException i) {
            System.out.println(i);
        }

        // string to read message from input
        String line = "";

        // keep reading until "Over" is input
        while (!line.equals("Over")) {
            try {
                line = input.readLine();
                out.writeUTF(line);
                line = input.readLine();
                Transaction transaction = new Transaction(line.split("\t"));
                out.writeObject(transaction);
            } catch (IOException | NoSuchAlgorithmException i) {
                System.out.println(i);
            }
        }

        // close the connection
        try {
            input.close();
            out.close();
            socket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    @Override
    public void run() {
        try {
            this.client("127.0.0.1",5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
