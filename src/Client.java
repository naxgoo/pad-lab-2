import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {

        Scanner in = new Scanner(System.in);

        Socket socket = new Socket("localhost", 6781);

        Scanner fromProxy = new Scanner(socket.getInputStream());
        PrintWriter toProxy = new PrintWriter(socket.getOutputStream(), true);

        String line = fromProxy.nextLine();

        System.out.println(line);

        toProxy.println(in.nextLine());

        System.out.println(fromProxy.nextLine());

        socket.close();
    }
}


