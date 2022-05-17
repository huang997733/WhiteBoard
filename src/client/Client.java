package client;

import javax.swing.*;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Socket socket;



    public Client(String host, int port, String username, ClientGUI clientGUI) {

        try {
            socket = new Socket(host, port);
            clientGUI.run();
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

}
