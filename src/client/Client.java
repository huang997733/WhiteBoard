package client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {
    private Socket socket;
    private static DataInputStream reader;
    private static DataOutputStream writer;
    private static ClientGUI clientGUI;
    private static ArrayList<String> shapes = new ArrayList<>();

    public Client(String host, int port, String username) {
        shapes.add("Line");
        shapes.add("Circle");
        shapes.add("Rectangle");
        shapes.add("Triangle");
        shapes.add("Text");
        try {
            socket = new Socket(host, port);
            JSONObject msg = new JSONObject();
            reader = new DataInputStream(socket.getInputStream());
            writer = new DataOutputStream(socket.getOutputStream());
            msg.put("action", "Connection");
            msg.put("username", username);
            JSONObject reply = send(msg);
            if (reply.get("reply").equals("username exists")) {
                JOptionPane.showMessageDialog(new JLabel("error"), "Username exists, please choose another one", "Username exists", JOptionPane.ERROR_MESSAGE);
                socket.close();
                System.exit(1);
            } else if (reply.get("reply").equals("denied")) {
                JOptionPane.showMessageDialog(new JLabel("error"), "Manager denied your connection request", "Connection denied", JOptionPane.ERROR_MESSAGE);
                socket.close();
                System.exit(1);
            } else if (reply.get("reply").equals("approved")) {
                clientGUI = new ClientGUI();
                clientGUI.run();
            }

        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(new JLabel("error"), "Unknown host", "Unknown host", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (ConnectException e) {
            JOptionPane.showMessageDialog(new JLabel("error"), "Cannot connect to server", "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }catch (IOException e) {
            JOptionPane.showMessageDialog(new JLabel("error"), "Unknown Error", "Unknown Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public static JSONObject send(JSONObject msg) {
        JSONObject reply = new JSONObject();
        try {
            writer.writeUTF(msg.toString());
            writer.flush();

            JSONParser parser = new JSONParser();
            try {
                reply = (JSONObject) parser.parse(reader.readUTF());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (shapes.contains((String) reply.get("action"))) {
            draw(reply);
        }



        return reply;
    }

    public static void draw(JSONObject reply) {
        Graphics g = ClientGUI.canvas.getGraphics();
        int x1 = ((Long) reply.get("x1")).intValue();
        int y1 = ((Long) reply.get("y1")).intValue();
        int x2 = ((Long) reply.get("x2")).intValue();
        int y2 = ((Long) reply.get("y2")).intValue();
        int rgb = ((Long) reply.get("rgb")).intValue();
        Color c = new Color(rgb);
        g.setColor(c);
        switch ((String) reply.get("action")) {

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
                String text = (String) reply.get("text");
                g.drawString(text, x1, y1);
            }
        }
    }
}
