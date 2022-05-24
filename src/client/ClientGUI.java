package client;

import org.json.simple.JSONObject;

import java.awt.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClientGUI {

	private JFrame frmWhiteboard;
	private int x1 = 0;
	private int y1 = 0;
	private int x2 = 0;
	private int y2 = 0;
	private String action = "None";
	private Graphics g;
	private Color colour = Color.BLACK;
	static JPanel canvas;
	private String text;
	/**
	 * Launch the application.
	 */
	public void run() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI window = new ClientGUI();
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
	public ClientGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWhiteboard = new JFrame();
		frmWhiteboard.setTitle("WhiteBoard");
		frmWhiteboard.setBounds(100, 100, 950, 650);
		frmWhiteboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmWhiteboard.getContentPane().setLayout(null);

		JButton send = new JButton("Send");
		send.setBounds(850, 578, 74, 23);
		frmWhiteboard.getContentPane().add(send);

		canvas = new JPanel();
		canvas.setBackground(Color.WHITE);
		canvas.setBounds(10, 49, 661, 552);
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
					System.out.println("Sent");
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(action);
			}
		});

		JButton line = new JButton("Line");
		line.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Line";
			}
		});
		line.setBounds(126, 16, 93, 23);
		frmWhiteboard.getContentPane().add(line);

		JButton circle = new JButton("Circle");
		circle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Circle";
			}
		});
		circle.setBounds(229, 16, 93, 23);
		frmWhiteboard.getContentPane().add(circle);

		JButton triangle = new JButton("Triangle");
		triangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Triangle";
			}
		});
		triangle.setBounds(332, 16, 93, 23);
		frmWhiteboard.getContentPane().add(triangle);

		JButton rectangle = new JButton("Rectangle");
		rectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Rectangle";
			}
		});
		rectangle.setBounds(435, 16, 93, 23);
		frmWhiteboard.getContentPane().add(rectangle);

		JTextArea typeBox = new JTextArea();
		typeBox.setBounds(691, 502, 233, 99);
		frmWhiteboard.getContentPane().add(typeBox);

		JTextArea chatBox = new JTextArea();
		chatBox.setEditable(false);
		chatBox.setBounds(691, 147, 233, 345);
		frmWhiteboard.getContentPane().add(chatBox);

		JButton colour = new JButton("Colour");
		colour.addActionListener(e -> this.colour = JColorChooser.showDialog(frmWhiteboard, "select colour", Color.black));
		colour.setBounds(538, 16, 93, 23);
		frmWhiteboard.getContentPane().add(colour);

		JButton curve = new JButton("Curve");
		curve.setBounds(23, 16, 93, 23);
		frmWhiteboard.getContentPane().add(curve);

		JButton text = new JButton("Text");
		text.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Text";
			}
		});
		text.setBounds(641, 16, 93, 23);
		frmWhiteboard.getContentPane().add(text);

		JList userList = new JList();
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.setBounds(691, 49, 233, 92);
		frmWhiteboard.getContentPane().add(userList);
	}
}