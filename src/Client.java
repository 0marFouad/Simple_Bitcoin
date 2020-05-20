import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

public class Client  {
    // initialize socket and input output streams
    private Socket socket ;
    private ObjectOutputStream out ;
    private String addr_ip;
    private Integer addr_port;




    // constructor to put ip address and port
    public  Client(String address) throws IOException {
        this.addr_ip = address.split("/")[0];
        this.addr_port = Integer.parseInt(address.split("/")[1]);
        while(true) {
            try {
                socket = new Socket(addr_ip, addr_port);
                System.out.println("Connected");
                // sends output to the socket
                out = new ObjectOutputStream(socket.getOutputStream());
                break;
            } catch (UnknownHostException u) {
                System.out.println(u);
            } catch (IOException i) {
                System.out.println(i);
            }
        }

    }


    public void send(String token,Object obj) {
        try {
            out.writeUTF(token);
            out.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
