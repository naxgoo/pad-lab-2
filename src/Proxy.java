import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Logger;

public class Proxy {

    private static final Logger LOGGER = Logger.getLogger(Proxy.class.getName());

    public static void main(String[] args) {

        byte[] buffer = new byte[1024];

        List<String> portsTCP = new ArrayList<>();

        Thread udp = new Thread(() -> {

            try {
                MulticastSocket socket = new MulticastSocket();

                DatagramPacket dp = new DatagramPacket("PROXY_LOOKING".getBytes(), "PROXY_LOOKING".getBytes().length,
                        InetAddress.getByName("224.0.0.3"), 1234);
                socket.send(dp);

                LOGGER.info("Server sent UDP multicast packet!");

                for (int i = 0; i < 6; i++) {
                    dp = new DatagramPacket(buffer, buffer.length);
                    socket.receive(dp);
                    portsTCP.add(new String(buffer, 0, buffer.length));
                }

                LOGGER.info("Received TCP ports for nodes. Hurray! ");

                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        });


        List<String> payloads = new ArrayList<>();
        List<Integer> ports = new ArrayList<>();
        Set<String> tempPayloads = new HashSet<>();
        Thread tcp = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(6781);
                while (true) {

                    Socket client = serverSocket.accept();
                    LOGGER.info("TCP Client successfully connected!");

                    PrintWriter toClient = new PrintWriter(client.getOutputStream(), true);
                    Scanner fromClient = new Scanner(client.getInputStream());

                    toClient.println("Hey! Psh! Want some data? (y / n) ");
                    LOGGER.info("Sent question to client. Waiting for answer!");

                    String line = fromClient.nextLine();
                    if (!line.equals("y")) {
                        toClient.println("K THX BYE!");
                        toClient.close();
                    }

                    toClient.println("Knock-knock! Here's Johnny with his UDP discovery!");
                    udp.start();

                    LOGGER.info("Waiting for TCP ports :(");
                    Thread.sleep(1000);
                    LOGGER.info("Waiting is done :)");

                    for (String port: portsTCP) {
                        LOGGER.info("I've got this port: " + port);
                    }
                    LOGGER.info("Alright! That's enough ports for a day ;). Let's explore them!");

                    for (String p : portsTCP){
                        ports.add(Integer.parseInt(p.trim()));
                    }

                    for (int i = 0; i < portsTCP.size(); i++) {
                        Socket socket = new Socket("localhost", ports.get(i));

                        Scanner fromNode = new Scanner(socket.getInputStream());

                        LOGGER.info("Node " + i + " says the following: ");
                        String nrNodes = fromNode.nextLine();
                        for (int j = 0; j < Integer.valueOf(nrNodes); j++) {
                            payloads.add(fromNode.nextLine());
                        }
                        payloads.add(fromNode.nextLine());
                    }

                    LOGGER.info("That's what yo bro found bout those nodzz: ");
                    for (String payload : payloads) {
                        LOGGER.info(payload);
                    }

                    tempPayloads.addAll(payloads);
                    payloads.clear();
                    payloads.addAll(tempPayloads);

                    LOGGER.info("Sending dat to ma brother from an other mother.");

                    toClient.println("Oh look what i've found:");
                    toClient.println(payloads.size());

                    for (int i = 0; i < payloads.size(); i++) {
                        toClient.println(payloads.get(i));
                    }

                    toClient.println("K THX BYE!");

                    toClient.close();

                }
            } catch (IOException e) {
                LOGGER.severe("Input/output exception!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        tcp.start();


    }
}
