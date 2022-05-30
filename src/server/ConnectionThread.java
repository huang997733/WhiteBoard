/**
 * author: Ziyang Huang 1067800
 */
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

/**
 * One thread for one client
 */
public class ConnectionThread extends Thread{
    private final Socket client;
    private DataOutputStream writer;
    private String username;

    public ConnectionThread(Socket client, ServerGUI serverGUI) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            DataInputStream reader = new DataInputStream(client.getInputStream());
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
                // client request for connection
                if (request.get("action").equals("Connection")) {
                    username = (String) request.get("username");
                    // check if the username already exists
                    if (Server.usernames.contains(username)) {
                        reply.put("reply", "username exists");
                        writer.writeUTF(reply.toString());
                        writer.flush();
                        client.close();
                    } else {
                        // pop up a confirm dialog for manager
                        int result = JOptionPane.showConfirmDialog(new JLabel(), username + " wants to share your whiteboard", "New connection", JOptionPane.YES_NO_OPTION);
                        // connection approved
                        if (result == JOptionPane.YES_OPTION) {
                            Server.usernames.add(username);
                            Server.connections.put(username,client);
                            reply.put("reply", "approved");
                            writer.writeUTF(reply.toString());
                            writer.flush();
                            ServerGUI.setUserList();
                            // send action history to the user to synchronize his canvas
                            int i = 0;
                            while (i < Server.history.size()) {
                                writer.writeUTF(Server.history.get(i).toString());
                                writer.flush();
                                i++;
                            }
                        } else {
                            // connection disapproved
                            reply.put("reply", "denied");
                            writer.writeUTF(reply.toString());
                            writer.flush();
                        }
                    }
                } else {
                    share(request);
                }
            }
        } catch (IOException e) {
            try {
                // user disconnects
                Server.usernames.remove(username);
                Server.connections.remove(username);
                this.client.close();
                ServerGUI.setUserList();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    /**
     * share the message to all users
     * @param msg a json object contains the message
     */
    public synchronized void share(JSONObject msg) {
        if (!msg.get("action").equals("chat") && !msg.get("action").equals("kick")) {
            Server.history.add(msg);
            update(msg);
        } else if (msg.get("action").equals("chat")) {
            String name = (String) msg.get("username");
            String text = (String) msg.get("text");
            // update the chat box
            ServerGUI.chatBox.setText(ServerGUI.chatBox.getText() + name + ": " + text + "\n");
        }
        for (Socket c : Server.connections.values()) {
            try {
                writer = new DataOutputStream(c.getOutputStream());
                writer.writeUTF(msg.toString());
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * update the canvas
     * @param msg a json object which tells the canvas what to draw
     */
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
}

