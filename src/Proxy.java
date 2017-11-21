import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

public class Proxy {

    private static final Logger LOGGER = Logger.getLogger( Proxy.class.getName() );

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(6781);

        while (true){

            Socket client = serverSocket.accept();
            LOGGER.info("Client successfully connected!");

            PrintWriter toClient = new PrintWriter(client.getOutputStream(), true);
            Scanner fromClient = new Scanner(client.getInputStream());

            toClient.println("Hey! Psh! Want some data? (y / n) ");
            LOGGER.info("Sent question to client. Waiting for answer!");

            String line = fromClient.nextLine();
            if(!line.equals("y")){
                toClient.println("KTHXBYE!");
                toClient.close();
            }

            toClient.close();

        }
    }
}
