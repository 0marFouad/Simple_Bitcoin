import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Network implements Runnable {
    static Map<Integer, KeyPair> peers;
    static ArrayList<String> connectedDevices;
    private ArrayList<Runnable> runnableServer;
    private ArrayList<Thread> threadServer;
    private ArrayList<Client> clients;


    final private Integer SERVER_PORT = 5000;
    ServerSocket server;
    final private String NODE1 = "127.0.0.1/5000";
    final private String NODE2 = "127.0.0.1/6000";
    final private String NODE3 = "127.0.0.1/7000";



    public Network() throws IOException {
        connectedDevices = new ArrayList<>();
        connectedDevices.add(NODE2);
        connectedDevices.add(NODE3);

        server  = null;
        try {
            server = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        Socket socket = null;
        runnableServer = new ArrayList();
        threadServer = new ArrayList();
        while (true){
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

    public void intiateClientConnection(){
        clients = new ArrayList();
        for(int i=0;i<connectedDevices.size();i++){
            try {
                clients.add(new Client(connectedDevices.get(i)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void broadcast(String token, Object obj){
        for(int i = 0; i< clients.size();i++){
            clients.get(i).send(token,obj);
        }
    }

}
