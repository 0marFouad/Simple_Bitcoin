import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Network implements Runnable {
    static Map<Integer, KeyPair> peers = new HashMap<>();
    static ArrayList<String> connectedDevices;
    private ArrayList<Runnable> runnableServer;
    private ArrayList<Thread> threadServer;
    private ArrayList<Client> clients;
    private static Network instance;
    private static Integer SERVER_PORT;
    private static String CLIENT1_ADDR;
    private ServerSocket server;

    public static Network getInstance(int port) {
        if (instance == null) {
            SERVER_PORT = port;
            if (port == 5000) {
                CLIENT1_ADDR = "127.0.0.1/6000";
            } else {
                CLIENT1_ADDR = "127.0.0.1/5000";
            }
            instance = new Network();
            return instance;
        } else {
            return instance;
        }
    }

    public static Network getInstance() {
        if (instance == null) {
            return new Network();
        } else {
            return instance;
        }
    }


    private Network() {
        peers = new HashMap<>();
        connectedDevices = new ArrayList<>();
        connectedDevices.add(CLIENT1_ADDR);

        server = null;
        try {
            server = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        Socket socket;
        runnableServer = new ArrayList();
        threadServer = new ArrayList();
        while (true) {
            try {
                socket = server.accept();
                Runnable s = new Server(socket);
                runnableServer.add(s);
                Thread t = new Thread(s);
                threadServer.add(t);
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void intiateClientConnection() {
        clients = new ArrayList();
        for (int i = 0; i < connectedDevices.size(); i++) {
            try {
                clients.add(new Client(connectedDevices.get(i)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcast(String token, Object obj) {
        for (int i = 0; i < clients.size(); i++) {
            clients.get(i).send(token, obj);
        }
    }

}
