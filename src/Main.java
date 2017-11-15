import java.io.IOException;
import java.io.PrintWriter;

public class Main {

    public static void main(String[] args) {

        try {
            Process p = new ProcessBuilder("notepad.exe").start();
            Thread.sleep(5000);
            p.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }
}
