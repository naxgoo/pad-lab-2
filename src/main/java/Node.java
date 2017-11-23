import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Node extends Thread{

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private Payload payload;
    private List<Node> nodes = new ArrayList<>();
    private int PORT;

    Node(Payload payload, int port){
        this.payload = payload;
        this.PORT = port;
    }

    void addNode(Node node){
        nodes.add(node);
    }

    private String getPayload(){
        return payload.getId() + " " + payload.getMessage();
    }

    private String getSlavePayload(int i){
        return nodes.get(i).payload.getId() + " " +nodes.get(i).payload.getMessage();
    }

    private Payload getSlavePayloadasObject(int i){
        return nodes.get(i).payload;
    }

    private Payload getPayloadasObject(){
        return this.payload;
    }

    private int returnNodes(){
        return nodes.size();
    }

    @Override
    public void run() {

        Thread nodeTCP = new Thread(() -> {
            try {
                //Giving time for UDP to send its packets
                Thread.sleep(1000);

                ServerSocket socket = new ServerSocket(PORT);

                Socket s = socket.accept();

                PrintWriter out = new PrintWriter(s.getOutputStream(), true);

                out.println(returnNodes());

                //Data serialization
                Gson gson;
                String json;

                for (int i = 0; i < returnNodes(); i++) {
                    gson = new Gson();
                    json = gson.toJson(getSlavePayloadasObject(i));
                    LOGGER.info(json);
                    out.println(json);
                }

                gson = new Gson();
                json = gson.toJson(getPayloadasObject());
                LOGGER.info(json);
                out.println(json);

                socket.close();

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });


        Thread nodeUDP = new Thread(() -> {
            byte[] buffer = new byte[1024];
            try {
                MulticastSocket socket = new MulticastSocket(1234);
                socket.joinGroup(InetAddress.getByName("224.0.0.3"));

                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                socket.receive(dp);

                int port = dp.getPort();

                if(new String(dp.getData(), 0, dp.getLength()).equals("PROXY_LOOKING")){
                    String tcpPort = String.valueOf(PORT);
                    buffer = tcpPort.getBytes();
                    dp = new DatagramPacket(buffer, buffer.length,
                            InetAddress.getByName("localhost"), port);
                    socket.send(dp);
                }

                nodeTCP.start();

                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });         nodeUDP.start();


    }
}
