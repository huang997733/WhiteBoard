/**
 * author: Ziyang Huang 1067800
 */
package client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import server.Server;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Client {
    private Socket socket;
    private static DataInputStream reader;
    private static DataOutputStream writer;
    private static ClientGUI clientGUI;
    public static ArrayList<String> actionOnCanvas = new ArrayList<>();

    public Client(String host, int port, String username) {
        actionOnCanvas.add("Line");
        actionOnCanvas.add("Circle");
        actionOnCanvas.add("Rectangle");
        actionOnCanvas.add("Triangle");
        actionOnCanvas.add("Text");
        try {
            socket = new Socket(host, port);
            JSONObject msg = new JSONObject();
            reader = new DataInputStream(socket.getInputStream());
            writer = new DataOutputStream(socket.getOutputStream());
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
                while (clientGUI.getCanvas().getGraphics()==null) {
                    sleep(10);
                }
                while (true) {
                    try {
                        reply = (JSONObject) parser.parse(reader.readUTF());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (actionOnCanvas.contains((String) reply.get("action"))) {
                        clientGUI.getCanvas().update(reply);
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
            JOptionPane.showMessageDialog(new JLabel("error"), "Unknown Error", "Unknown Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void send(JSONObject msg) {
        try {
            writer.writeUTF(msg.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
