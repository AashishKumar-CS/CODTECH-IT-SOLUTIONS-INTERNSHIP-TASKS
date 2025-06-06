package CODTECHIT;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            System.out.println("Connected to chat server");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            // Get username
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
            out.println(name);

            // Start thread to read server messages
            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        if (message.startsWith("SUBMITNAME")) {
                            out.println(name);
                        } else if (message.startsWith("NAMEACCEPTED")) {
                            System.out.println("Name accepted: " + message.split(" ")[1]);
                        } else if (message.startsWith("MESSAGE")) {
                            System.out.println(message.substring(8));
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Client error: " + e.getMessage());
                }
            }).start();

            // Send messages from user input
            while (scanner.hasNextLine()) {
                String message = scanner.nextLine();
                if (!message.isEmpty()) {
                    out.println(message);
                }
            }
        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }
}