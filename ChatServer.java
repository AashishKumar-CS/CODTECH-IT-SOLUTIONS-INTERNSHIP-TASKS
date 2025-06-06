package CODTECHIT;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class ChatServer {
    private static final int PORT = 5000;
    private static HashSet<PrintWriter> writers = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("Chat Server started on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (Exception e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String name;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Request and store client name
                while (true) {
                    out.println("SUBMITNAME");
                    name = in.readLine();
                    if (name == null || name.trim().isEmpty()) continue;
                    synchronized (writers) {
                        if (!writers.contains(out)) {
                            writers.add(out);
                            break;
                        }
                    }
                }

                // Welcome message
                out.println("NAMEACCEPTED " + name);
                broadcast(name + " joined the chat!");

                // Handle messages
                String message;
                while ((message = in.readLine()) != null) {
                    if (!message.isEmpty()) {
                        broadcast(name + ": " + message);
                    }
                }
            } catch (Exception e) {
                System.out.println("Client error: " + e.getMessage());
            } finally {
                if (name != null) {
                    broadcast(name + " left the chat!");
                }
                if (out != null) {
                    synchronized (writers) {
                        writers.remove(out);
                    }
                }
                try {
                    socket.close();
                } catch (Exception e) {
                    System.out.println("Error closing socket: " + e.getMessage());
                }
            }
        }

        private void broadcast(String message) {
            synchronized (writers) {
                for (PrintWriter writer : writers) {
                    writer.println("MESSAGE " + message);
                }
            }
        }
    }
}