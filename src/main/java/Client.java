import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

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

        System.out.println(fromProxy.nextLine());

        String jsonResponse = fromProxy.nextLine();

        Gson gson = new Gson();

        gson.toJson(jsonResponse);
        Schema schema = SchemaLoader.load(
                new JSONObject(
                        new FileInputStream("C:\\Users\\bezua\\IdeaProjects\\lab_2\\json_schema")));
        schema.validate(gson);

        System.out.println(jsonResponse);

        System.out.println(fromProxy.nextLine());

        socket.close();
    }
}


