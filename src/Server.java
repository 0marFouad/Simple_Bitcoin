import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    //initialize socket and input stream
    private Socket socket = null;
    private ServerSocket server = null;
    private ObjectInputStream in = null;

    // constructor with port
    public void server(int port) {
        // starts server and waits for a connection
        try {
            server = new ServerSocket(port);
            System.out.println("Server started");

            System.out.println("Waiting for a client ...");

            socket = server.accept();
            System.out.println("Client accepted");

            // takes input from the client socket
            in = new ObjectInputStream(socket.getInputStream());

            Transaction line = null;

            // reads message from client until "Over" is sent
            while (true) {
                try {
                    String token = in.readUTF();
                    if (!token.equals("tx")) {
                        break;
                    }
                    line = (Transaction) in.readObject();
                    System.out.println(line.getId());
                    System.out.println(line.inputs);

                } catch (IOException i) {
                    System.out.println(i);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Closing connection");

            // close connection
            socket.close();
            in.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    @Override
    public void run() {
        this.server(4000);
    }
}
