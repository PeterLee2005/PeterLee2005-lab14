import java.io.*;
import java.net.*;

public class Client {
    private Socket socket;

    public Client(String host, int port) throws IOException {
        socket = new Socket (host, port);
    }

    public Socket getSocket() {
        return socket;
    }

    public void handshake() {
        try {
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            pw.println("12345");
        } catch (Exception e) {
            System.err.print("IOException");
            System.exit(1);
        }
    }

    public String request (String input) {
        try {
            PrintWriter pw = new PrintWriter (socket.getOutputStream(), true);
            BufferedReader br = new BufferedReader (new InputStreamReader(socket.getInputStream()));
            pw.println(input);
            return br.readLine();
        } catch (Exception e) {
            return "IOException";
        }
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {

        }
    }
}