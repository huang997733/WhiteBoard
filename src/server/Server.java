package server;

import javax.net.ServerSocketFactory;
import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

public class Server {
    private ArrayList<String> usernames = new ArrayList<>();


    public Server(String host, int port, String username, ServerGUI serverGUI) {
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try (ServerSocket server = factory.createServerSocket(port)) {
            usernames.add(username);
            System.out.println("Waiting for user connection..");
            serverGUI.run();
            // Wait for connections.
            while (true) {
                Socket client = server.accept();

                // Start a new thread for a connection
                Thread t = new Thread(() -> serveClient(client));
                t.start();
            }

        } catch (BindException e) {
            JOptionPane.showMessageDialog(new JLabel("error"), "Port occupied", "Port occupied", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
        }
    }

    private static void serveClient(Socket client){

    }
}
