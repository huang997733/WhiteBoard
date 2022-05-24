package server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectionThread extends Thread{
    private final Socket client;
    private DataInputStream reader;
    private DataOutputStream writer;

    public ConnectionThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
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
                    if (Server.usernames.contains(username)) {
                        reply.put("reply", "username exists");
                        writer.writeUTF(reply.toString());
                        writer.flush();
                        client.close();
                    } else {
                        int result = JOptionPane.showConfirmDialog(new JLabel(), username + " wants to share your whiteboard", "New connection", JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.YES_OPTION) {
                            Server.usernames.add(username);
                            Server.connections.put(username,client);
                            reply.put("reply", "approved");
                        } else {
                            reply.put("reply", "denied");
                        }
                        writer.writeUTF(reply.toString());
                        writer.flush();
                    }
                } else if (Server.actionOnCanvas.contains((String) request.get("action"))) {
                    System.out.println("received1");
                    update(request);
                    share(request);
                    Server.history.add(request);
                }

            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(new JLabel("error"), "Unknown error occurred", "Unknown error", JOptionPane.ERROR_MESSAGE);
        }
    }


    static void update(JSONObject msg) {
        int x1 = ((Long) msg.get("x1")).intValue();
        int y1 = ((Long) msg.get("y1")).intValue();
        int x2 = ((Long) msg.get("x2")).intValue();
        int y2 = ((Long) msg.get("y2")).intValue();
        int rgb = ((Long) msg.get("rgb")).intValue();
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



    public void share(JSONObject msg) {
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
}

