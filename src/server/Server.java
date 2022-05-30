/**
 * author: Ziyang Huang 1067800
 */
package server;

import javax.net.ServerSocketFactory;
import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static java.lang.Thread.sleep;

/**
 * server which is the manager
 */
public class Server {
    public static ArrayList<String> usernames = new ArrayList<>();       // store all the usernames
    public static String name;
    public static ArrayList<JSONObject> history = new ArrayList<>();     // store all the actions
    public static HashMap<String, Socket> connections = new HashMap<>(); // store all the connections
    public static ArrayList<String> actionOnCanvas = new ArrayList<>();  // store all actions which change the canvas

    /**
     * Server instance constructor
     * @param host ip
     * @param port port
     * @param username username of the manager
     */
    public Server(String host, int port, String username) {
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        name = username;
        usernames.add(username);
        actionOnCanvas.add("Line");
        actionOnCanvas.add("Circle");
        actionOnCanvas.add("Rectangle");
        actionOnCanvas.add("Triangle");
        actionOnCanvas.add("Text");
        ServerGUI serverGUI = new ServerGUI(this);
        try (ServerSocket server = factory.createServerSocket(port)) {
            serverGUI.run();
            // Wait for connections.
            while (true) {
                Socket client = server.accept();

                // Start a new thread for a connection
                ConnectionThread clientThread = new ConnectionThread(client, serverGUI);
                clientThread.start();
            }

        } catch (BindException e) {
            JOptionPane.showMessageDialog(new JLabel("error"), "Port occupied", "Port occupied", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(new JLabel("error"), "Unknown error occurred", "Unknown error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    /**
     * get the current usernames
     * @return a String list
     */
    public static String[] getUsernames() {
        return usernames.toArray(new String[0]);
    }


    /**
     * share the message to all the users of the system
     * @param msg a json object which contains the message
     */
    public synchronized void share(JSONObject msg) {
        if (actionOnCanvas.contains((String) msg.get("action"))) {
            history.add(msg);
        }
        for (Socket c : Server.connections.values()) {
            try {
                DataOutputStream writer = new DataOutputStream(c.getOutputStream());
                writer.writeUTF(msg.toString());
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
