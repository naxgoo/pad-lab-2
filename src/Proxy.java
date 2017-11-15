import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Proxy {

    public static void main(String[] args) throws IOException {
        //local port
        ServerSocket serverSocket = new ServerSocket(14141);

        final byte[] request = new byte[1024];
        byte[] reply = new byte[4096];

        while (true){

            Socket client = null, server = null;

            //accepting connections
            client = serverSocket.accept();

            InputStream fromClient = client.getInputStream();
            OutputStream toClient  = client.getOutputStream();


        }
    }
}
