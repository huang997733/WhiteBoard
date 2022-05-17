package client;

import javax.swing.*;

public class JoinWhiteBoard {
    private static String host;
    private static int port;
    private static String username;


    public static void main(String[] args) {
        if (args.length != 3) {
            JOptionPane.showMessageDialog(new JLabel("error"), "Please enter: <host> <port> <username>", "Wrong parameters", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        ClientGUI clientGUI = null;
        try {
            host = args[0];
            port = Integer.parseInt(args[1]);
            username = args[2];
            if (port < 1024 || port > 49151) {
                JOptionPane.showMessageDialog(new JLabel("error"), "Invalid port: please use port between 1024 and 49151", "Illegal port number", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            clientGUI = new ClientGUI();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JLabel("error"), "Port format incorrect", "Incorrect format", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        new Client(host, port, username, clientGUI);
    }
}
