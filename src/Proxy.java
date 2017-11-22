import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Logger;

public class Proxy {

    private static final Logger LOGGER = Logger.getLogger( Proxy.class.getName() );

    public static void main(String[] args) {

        byte[] buffer = new byte[1024];

        Thread udp = new Thread(() -> {

            try {
                MulticastSocket socket = new MulticastSocket();

                DatagramPacket dp = new DatagramPacket("PROXY_LOOKING".getBytes(), "PROXY_LOOKING".getBytes().length,
                        InetAddress.getByName("224.0.0.3"), 1234);
                socket.send(dp);

                LOGGER.info("Server sent UDP packet!");


                dp = new DatagramPacket(buffer, buffer.length);
                socket.receive(dp);
                System.out.println(new String(buffer, 0 , buffer.length));

                dp = new DatagramPacket(buffer, buffer.length);
                socket.receive(dp);
                System.out.println(new String(buffer, 0 , buffer.length));


            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Thread tcp = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(6781);
                while (true){

                    Socket client = serverSocket.accept();
                    LOGGER.info("TCP Client successfully connected!");

                    PrintWriter toClient = new PrintWriter(client.getOutputStream(), true);
                    Scanner fromClient = new Scanner(client.getInputStream());

                    toClient.println("Hey! Psh! Want some data? (y / n) ");
                    LOGGER.info("Sent question to client. Waiting for answer!");

                    String line = fromClient.nextLine();
                    if(!line.equals("y")){
                        toClient.println("KTHXBYE!");
                        toClient.close();
                    }

                    toClient.println("Knock-knock! Here's Johnny with his UDP discovery!");
                    udp.start();
                    toClient.close();

                }
            } catch (IOException e) {
                LOGGER.severe("Input/output exception!");
            }


        }); tcp.start();




    }
}
