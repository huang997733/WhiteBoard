/**
 * author: Ziyang Huang 1067800
 */
package client;

import org.json.simple.JSONObject;
import server.Server;

import java.awt.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * client side GUI
 */
public class ClientGUI {
	private final Client client;
	private final String username;
	private JFrame frmWhiteboard;
	static ClientCanvas canvas;
	private String action = "None";
	private int x1 = 0;
	private int y1 = 0;
	private int x2 = 0;
	private int y2 = 0;
	private Color colour = Color.BLACK;
	private String text;
	static JTextArea chatBox;
	private JTextArea typeBox;

	public ClientCanvas getCanvas() {
		return canvas;
	}

	/**
	 * Launch the application.
	 */
	public void run() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI window = new ClientGUI(client, username);
					window.frmWhiteboard.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientGUI(Client client, String name) {
		this.client = client;
		this.username = name;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWhiteboard = new JFrame();
		frmWhiteboard.setResizable(false);
		frmWhiteboard.setTitle("WhiteBoard");
		frmWhiteboard.setBounds(100, 100, 951, 652);
		frmWhiteboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmWhiteboard.getContentPane().setLayout(null);

		canvas = new ClientCanvas();
		canvas.setBackground(Color.WHITE);
		canvas.setBounds(10, 47, 664, 556);
		frmWhiteboard.getContentPane().add(canvas);
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				x1 = e.getX();
				y1 = e.getY();
				if (action.equals("Text")) {
					text = JOptionPane.showInputDialog("Please enter some text");
					if (text != null) {
						JSONObject msg = new JSONObject();
						msg.put("action", action);
						msg.put("x1", x1);
						msg.put("y1", y1);
						msg.put("x2", x2);
						msg.put("y2", y2);
						msg.put("rgb", colour.getRGB());
						msg.put("text", text);
						Client.send(msg);
					}
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				x2 = e.getX();
				y2 = e.getY();
				if (!action.equals("None")) {
					JSONObject msg = new JSONObject();
					msg.put("action", action);
					msg.put("x1", x1);
					msg.put("y1", y1);
					msg.put("x2", x2);
					msg.put("y2", y2);
					msg.put("rgb", colour.getRGB());
					Client.send(msg);
				}
			}
		});

		chatBox = new JTextArea();
		chatBox.setEditable(false);
		chatBox.setBounds(694, 47, 231, 435);
		frmWhiteboard.getContentPane().add(chatBox);

		JButton send = new JButton("Send");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = typeBox.getText();
				if (!text.equals("")) {
					JSONObject msg = new JSONObject();
					msg.put("action", "chat");
					msg.put("username", username);
					msg.put("text", text);
					Client.send(msg);
					typeBox.setText("");
				}
			}
		});
		send.setBounds(832, 580, 93, 23);
		frmWhiteboard.getContentPane().add(send);

		typeBox = new JTextArea();
		typeBox.setBounds(694, 490, 231, 113);
		frmWhiteboard.getContentPane().add(typeBox);

		JButton line = new JButton("Line");
		line.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Line";
			}
		});
		line.setBounds(53, 14, 93, 23);
		frmWhiteboard.getContentPane().add(line);

		JButton circle = new JButton("Circle");
		circle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Circle";
			}
		});
		circle.setBounds(156, 14, 93, 23);
		frmWhiteboard.getContentPane().add(circle);

		JButton triangle = new JButton("Triangle");
		triangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Triangle";
			}
		});
		triangle.setBounds(259, 14, 93, 23);
		frmWhiteboard.getContentPane().add(triangle);

		JButton rectangle = new JButton("Rectangle");
		rectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Rectangle";
			}
		});
		rectangle.setBounds(362, 14, 93, 23);
		frmWhiteboard.getContentPane().add(rectangle);

		JButton colour = new JButton("Colour");
		colour.addActionListener(e -> {
			this.colour = JColorChooser.showDialog(frmWhiteboard, "select colour", this.colour);
			if (this.colour == null) {
				this.colour = Color.BLACK;
			}
		});
		colour.setBounds(465, 14, 93, 23);
		frmWhiteboard.getContentPane().add(colour);

		JButton text = new JButton("Text");
		text.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Text";
			}
		});
		text.setBounds(568, 14, 93, 23);
		frmWhiteboard.getContentPane().add(text);
	}
}