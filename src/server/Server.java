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

public class Server {
    static ArrayList<String> usernames = new ArrayList<>();
    private static DataOutputStream writer;
    private static DataInputStream reader;
    private static ServerGUI serverGUI;
    static ArrayList<JSONObject> history = new ArrayList<>();
    static HashMap<String, Socket> connections = new HashMap<>();
    static ArrayList<String> actionOnCanvas = new ArrayList<>();

    public Server(String host, int port, String username) {
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        usernames.add(username);
        actionOnCanvas.add("Line");
        actionOnCanvas.add("Circle");
        actionOnCanvas.add("Rectangle");
        actionOnCanvas.add("Triangle");
        actionOnCanvas.add("Text");
        serverGUI = new ServerGUI();
        try (ServerSocket server = factory.createServerSocket(port)) {
            serverGUI.run();
            // Wait for connections.
            while (true) {
                Socket client = server.accept();

                // Start a new thread for a connection
                ConnectionThread clientThread = new ConnectionThread(client);
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

    public static String[] getUsernames() {
        return usernames.toArray(new String[0]);
    }

    public static void share(JSONObject msg) {
        history.add(msg);
        update(msg);
        int i = 0;
        for (Socket c : Server.connections.values()) {
            System.out.println(i);
            i++;
            try {
                writer = new DataOutputStream(c.getOutputStream());
                writer.writeUTF(msg.toString());
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void update(JSONObject msg) {
        int x1 = (int) msg.get("x1");
        int y1 = (int) msg.get("y1");
        int x2 = (int) msg.get("x2");
        int y2 = (int) msg.get("y2");
        int rgb = (int) msg.get("rgb");
        Color c = new Color(rgb);
        Graphics g = ServerGUI.canvas.getGraphics();
        g.setColor(c);
        switch ((String) msg.get("action")) {
            case "Line" -> {
                g.drawLine(x1,y1,x2,y2);
            }
            case "Circle" -> {
                int max = Math.max(Math.abs(x1 - x2), Math.abs(x1 - x2));
                g.drawOval(Math.min(x1, x2), Math.min(y1, y2), max, max);
            }
            case "Triangle" -> {
                int[] x = {(x1+x2)/2,x2,x1};
                int[] y = {y1,y2,y2};
                g.drawPolygon(x,y,3);
            }
            case "Rectangle" -> {
                g.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
            }
            case "Text" -> {
                String text = (String) msg.get("text");
                g.drawString(text, x1, y1);
            }
        }
    }
}
