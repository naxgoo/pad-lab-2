import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Node extends Thread{
    private boolean isMaster;
    private Payload payload;
    private List<Node> nodes = new ArrayList<>();
    private int PORT;

    public Node(boolean isMaster, Payload payload, int port){
        this.isMaster = isMaster;
        this.payload = payload;
        this.PORT = port;
    }

    public void addNode(Node node){
        nodes.add(node);
    }

    public String getPayload(){
        return payload.getId() + " " + payload.getMessage();
    }

    public String getSlavePayload(int i){
        return nodes.get(i).payload.getId() + " " +nodes.get(i).payload.getMessage();
    }

    public int returnNodes(){
        return nodes.size();
    }

    @Override
    public void run() {

        Thread nodeTCP = new Thread(() -> {
            try {

                Thread.sleep(1000);

                ServerSocket socket = new ServerSocket(PORT);

                Socket s = socket.accept();

                PrintWriter out = new PrintWriter(s.getOutputStream(), true);

                out.println(returnNodes());

                for (int i = 0; i < returnNodes(); i++) {
                    out.println(getSlavePayload(i));
                }

                out.println(getPayload());

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
