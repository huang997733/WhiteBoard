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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Server {
    private static ArrayList<String> usernames = new ArrayList<>();
    private static DataOutputStream writer;
    private static DataInputStream reader;
    private static ServerGUI serverGUI;
    private static ArrayList<JSONObject> history = new ArrayList<>();
    private static HashMap<String, Socket> connections = new HashMap<>();

    public Server(String host, int port, String username) {
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        serverGUI = new ServerGUI();
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
                            connections.put(username,client);
                            reply.put("reply", "approved");
                        } else {
                            reply.put("reply", "denied");
                        }
                        writer.writeUTF(reply.toString());
                        writer.flush();
                    }
                } else if (request.get("action").equals("Line")) {
                    int x1 = ((Long) request.get("x1")).intValue();
                    int y1 = ((Long) request.get("y1")).intValue();
                    int x2 = ((Long) request.get("x2")).intValue();
                    int y2 = ((Long) request.get("y2")).intValue();
                    int rgb = ((Long) request.get("rgb")).intValue();
                    Color c = new Color(rgb);
                    Graphics g = ServerGUI.canvas.getGraphics();
                    g.setColor(c);
                    g.drawLine(x1,y1,x2,y2);
                    writer.writeUTF(request.toString());
                    writer.flush();
                    history.add(request);
                } else if (request.get("action").equals("Circle")) {
                    int x1 = ((Long) request.get("x1")).intValue();
                    int y1 = ((Long) request.get("y1")).intValue();
                    int x2 = ((Long) request.get("x2")).intValue();
                    int y2 = ((Long) request.get("y2")).intValue();
                    int rgb = ((Long) request.get("rgb")).intValue();
                    Color c = new Color(rgb);
                    Graphics g = ServerGUI.canvas.getGraphics();
                    g.setColor(c);
                    int max = Math.max(Math.abs(x1 - x2), Math.abs(x1 - x2));
                    g.drawOval(Math.min(x1, x2), Math.min(y1, y2), max, max);
                    writer.writeUTF(request.toString());
                    writer.flush();
                    history.add(request);
                } else if (request.get("action").equals("Triangle")) {
                    int x1 = ((Long) request.get("x1")).intValue();
                    int y1 = ((Long) request.get("y1")).intValue();
                    int x2 = ((Long) request.get("x2")).intValue();
                    int y2 = ((Long) request.get("y2")).intValue();
                    int rgb = ((Long) request.get("rgb")).intValue();
                    Color c = new Color(rgb);
                    Graphics g = ServerGUI.canvas.getGraphics();
                    g.setColor(c);
                    int[] x = {(x2+x1)/2,x2,x1};
                    int[] y = {y1,y2,y2};
                    g.drawPolygon(x,y,3);
                    writer.writeUTF(request.toString());
                    writer.flush();
                    history.add(request);
                } else if (request.get("action").equals("Rectangle")) {
                    int x1 = ((Long) request.get("x1")).intValue();
                    int y1 = ((Long) request.get("y1")).intValue();
                    int x2 = ((Long) request.get("x2")).intValue();
                    int y2 = ((Long) request.get("y2")).intValue();
                    int rgb = ((Long) request.get("rgb")).intValue();
                    Color c = new Color(rgb);
                    Graphics g = ServerGUI.canvas.getGraphics();
                    g.setColor(c);
                    g.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
                    writer.writeUTF(request.toString());
                    writer.flush();
                    history.add(request);
                } else if (request.get("action").equals("Text")) {
                    int x1 = ((Long) request.get("x1")).intValue();
                    int y1 = ((Long) request.get("y1")).intValue();
                    int rgb = ((Long) request.get("rgb")).intValue();
                    String text = (String) request.get("text");
                    Color c = new Color(rgb);
                    Graphics g = ServerGUI.canvas.getGraphics();
                    g.setColor(c);
                    g.drawString(text, x1, y1);
                    writer.writeUTF(request.toString());
                    writer.flush();
                    history.add(request);
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(new JLabel("error"), "Unknown error occurred", "Unknown error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void share(Socket client) {
        try {
            writer = new DataOutputStream(client.getOutputStream());
            int i = 0;
            while (i < history.size()) {
                JSONObject msg = history.get(i);
                writer.writeUTF(msg.toString());
                writer.flush();
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
