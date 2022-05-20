package client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Socket socket;
    private DataInputStream reader;
    private DataOutputStream writer;


    public Client(String host, int port, String username, ClientGUI clientGUI) {

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

    public JSONObject send(JSONObject msg) {
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
        return reply;
    }

}
