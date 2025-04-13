import java.io.*;
import java.net.*;
import java.util.*;
import java.time.*;

public class Server {
    private ServerSocket serverSock;
    private ArrayList<LocalDateTime> connectedTimes = new ArrayList<>();

    public Server (int port) {
        try {
            serverSock = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("IOException");
            System.exit(1);
        }
    }

    public void serve (int numClients) {
        for (int i=0; i<numClients; i++) {
            try {
                Socket clientSock = serverSock.accept();
                System.out.println ("New connection: " + clientSock.getRemoteSocketAddress());
                new ClientHandler(clientSock).start();
            } catch (Exception e) {
                System.err.println("ERROR: Could not accept client");
            }
            
        }
    }

    private class ClientHandler extends Thread {
        private Socket sock;

        public ClientHandler(Socket sock) {
            this.sock = sock;
        }

        public void run() {
            try {
                PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                String handshake = in.readLine();
                if (!"12345".equals(handshake)) {
                    out.println("couldn't handshake");
                    sock.close();
                    return;
                }

                synchronized (connectedTimes) {
                    connectedTimes.add(LocalDateTime.now());
                }

                String input = in.readLine();
                try {
                    int number = Integer.parseInt(input);
                    int count = countFactors(number);
                    out.println("The number " + number + " has " + count + " factors");
                } catch (Exception e) {
                    out.println("There was an exception on the server");
                }

                out.close();
                in.close();
                sock.close();
            } catch (Exception e) {
                System.err.println("Connection error");
            }
        }
    }

    public void disconnect() {
        try {
            serverSock.close();
        } catch (IOException e) {
            System.err.println ("ERROR: Server socket could not close");
        }
    }

    public ArrayList<LocalDateTime> getConnectedTimes() {
        synchronized (connectedTimes) {
            return new ArrayList<>(connectedTimes);
        }
    }

    private int countFactors(int n) {
        int count = 0;
        for (int i=1; i<=n; i++) {
            if (n % i == 0) count++;
        }
        return count;
    }
}