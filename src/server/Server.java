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
import java.util.Objects;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Server {
    private static ArrayList<String> usernames = new ArrayList<>();
    private static DataOutputStream writer;
    private static DataInputStream reader;

    public Server(String host, int port, String username, ServerGUI serverGUI) {
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try (ServerSocket server = factory.createServerSocket(port)) {
            usernames.add(username);
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
            System.exit(1);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(new JLabel("error"), "Unknown error occurred", "Unknown error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private static void serveClient(Socket client){
        try {
            reader = new DataInputStream(client.getInputStream());
            writer = new DataOutputStream(client.getOutputStream());

            while (true) {
                String in = reader.readUTF();

                JSONObject request = new JSONObject();
                JSONParser parser = new JSONParser();
                try {
                    request = (JSONObject) parser.parse(in);
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(new JLabel("error"), "Parse error", "Parse error", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
                JSONObject reply = new JSONObject();

                if (request.get("action").equals("Connection")) {
                    String username = (String) request.get("username");
                    if (usernames.contains(username)) {
                        reply.put("reply", "username exists");
                        writer.writeUTF(reply.toString());
                        writer.flush();
                        client.close();
                    } else {
                        int result = JOptionPane.showConfirmDialog(new JLabel(), username + " wants to share your whiteboard", "New connection", JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.YES_OPTION) {
                            usernames.add(username);
                            reply.put("reply", "approved");
                        } else {
                            reply.put("reply", "denied");
                        }
                        writer.writeUTF(reply.toString());
                        writer.flush();
                    }


                }


            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(new JLabel("error"), "Unknown error occurred", "Unknown error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
