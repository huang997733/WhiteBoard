/**
 * author: Ziyang Huang 1067800
 */
package client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import server.Server;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 * client which is the user
 */
public class Client {
    private static DataOutputStream writer;
    public static ArrayList<String> actionOnCanvas = new ArrayList<>();

    /**
     * client instance constructor
     * @param host ip
     * @param port port
     * @param username username
     */
    public Client(String host, int port, String username) {
        actionOnCanvas.add("Line");
        actionOnCanvas.add("Circle");
        actionOnCanvas.add("Rectangle");
        actionOnCanvas.add("Triangle");
        actionOnCanvas.add("Text");
        try {
            // establish connection
            Socket socket = new Socket(host, port);
            JSONObject msg = new JSONObject();
            DataInputStream reader = new DataInputStream(socket.getInputStream());
            writer = new DataOutputStream(socket.getOutputStream());
            // send connection request
            msg.put("action", "Connection");
            msg.put("username", username);
            send(msg);
            JSONObject reply = new JSONObject();
            JSONParser parser = new JSONParser();
            try {
                reply = (JSONObject) parser.parse(reader.readUTF());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (reply.get("reply").equals("username exists")) {
                // username already exists
                JOptionPane.showMessageDialog(new JLabel("error"), "Username exists, please choose another one", "Username exists", JOptionPane.ERROR_MESSAGE);
                socket.close();
                System.exit(1);
            } else if (reply.get("reply").equals("denied")) {
                // manager denied the connection request
                JOptionPane.showMessageDialog(new JLabel("error"), "Manager denied your connection request", "Connection denied", JOptionPane.ERROR_MESSAGE);
                socket.close();
                System.exit(1);
            } else if (reply.get("reply").equals("approved")) {
                // manager approved the connection request
                ClientGUI clientGUI = new ClientGUI(this, username);
                clientGUI.run();
                while (clientGUI.getCanvas().getGraphics()==null) {
                    sleep(10);
                }
                while (true) {
                    // receive messages from the server
                    try {
                        reply = (JSONObject) parser.parse(reader.readUTF());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (actionOnCanvas.contains((String) reply.get("action"))) {
                        clientGUI.getCanvas().update(reply);
                        clientGUI.getCanvas().paintComponent(clientGUI.getCanvas().getGraphics());
                    } else if (reply.get("action").equals("kick") && reply.get("username").equals(username)) {
                        JOptionPane.showMessageDialog(new JLabel("kick"), "Manager has kicked you out", "Kicked out", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    } else if (reply.get("action").equals("chat")) {
                        String name = (String) reply.get("username");
                        String text = (String) reply.get("text");
                        ClientGUI.chatBox.setText(ClientGUI.chatBox.getText() + name + ": " + text + "\n");
                    } else if (reply.get("action").equals("new")) {
                        clientGUI.getCanvas().clear();
                        clientGUI.getCanvas().paintComponent(clientGUI.getCanvas().getGraphics());
                        JOptionPane.showMessageDialog(new JLabel("new"), "Manager created a new whiteboard", "New", JOptionPane.INFORMATION_MESSAGE);
                    } else if (reply.get("action").equals("close")) {
                        JOptionPane.showMessageDialog(new JLabel("close"), "Manager closed the whiteboard", "Close", JOptionPane.INFORMATION_MESSAGE);
                        socket.close();
                        System.exit(1);
                    } else if (reply.get("action").equals("open")) {
                        String path = (String) reply.get("image");
                        File file = new File(path);
                        BufferedImage image = ImageIO.read(file);
                        clientGUI.getCanvas().clear();
                        clientGUI.getCanvas().loadImage(image);
                        clientGUI.getCanvas().paintComponent(clientGUI.getCanvas().getGraphics());
                    }
                }
            }

        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(new JLabel("error"), "Unknown host", "Unknown host", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (ConnectException e) {
            JOptionPane.showMessageDialog(new JLabel("error"), "Cannot connect to server", "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }catch (IOException e) {
            JOptionPane.showMessageDialog(new JLabel("error"), "Manager offline, closing application", "Manager offline", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * send message to the server
     * @param msg the request
     */
    public static void send(JSONObject msg) {
        try {
            writer.writeUTF(msg.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
