import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Network {
    static Map<Integer, KeyPair> peers;
    static ArrayList<String> connectedDevices;
    private Runnable runnableServer;
    private Thread threadServer;
    private ArrayList<Runnable> runnableClients;
    private ArrayList<Thread> threadClients;

    final private Integer SERVER_PORT = 5000;
    final private String NODE1 = "127.0.0.1/5000";
    final private String NODE2 = "127.0.0.1/6000";
    final private String NODE3 = "127.0.0.1/7000";



    public Network(){
        connectedDevices = new ArrayList<>();
        connectedDevices.add(NODE1);
        connectedDevices.add(NODE2);
        connectedDevices.add("127.0.0.1/7000");

        runnableServer = new Server(SERVER_PORT);
        threadServer = new Thread(runnableServer);

        threadServer.start();

        for(int i=0;i<connectedDevices.size();i++){
            runnableClients.add(new Client(connectedDevices.get(i)));
            threadClients.add(new Thread(runnableClients.get(i)));
        }



    }

}
