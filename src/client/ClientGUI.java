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
	static ClientCanvas canvas;
	private static JList userList;
	private String action = "None";
	private int x1 = 0;
	private int y1 = 0;
	private int x2 = 0;
	private int y2 = 0;
	private Color colour = Color.BLACK;
	private String text;

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
					ClientGUI window = new ClientGUI();
					window.frmWhiteboard.setVisible(true);
					System.out.println(canvas.getGraphics() == null);
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

		JTextArea chatBox = new JTextArea();
		chatBox.setEditable(false);
		chatBox.setBounds(694, 201, 231, 281);
		frmWhiteboard.getContentPane().add(chatBox);

		JButton send = new JButton("Send");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		send.setBounds(832, 580, 93, 23);
		frmWhiteboard.getContentPane().add(send);

		JTextArea typeBox = new JTextArea();
		typeBox.setBounds(694, 490, 231, 113);
		frmWhiteboard.getContentPane().add(typeBox);

		JButton curve = new JButton("Curve");
		curve.setBounds(53, 14, 93, 23);
		frmWhiteboard.getContentPane().add(curve);

		JButton line = new JButton("Line");
		line.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Line";
			}
		});
		line.setBounds(156, 14, 93, 23);
		frmWhiteboard.getContentPane().add(line);

		JButton circle = new JButton("Circle");
		circle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Circle";
			}
		});
		circle.setBounds(259, 14, 93, 23);
		frmWhiteboard.getContentPane().add(circle);

		JButton triangle = new JButton("Triangle");
		triangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Triangle";
			}
		});
		triangle.setBounds(362, 14, 93, 23);
		frmWhiteboard.getContentPane().add(triangle);

		JButton rectangle = new JButton("Rectangle");
		rectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Rectangle";
			}
		});
		rectangle.setBounds(465, 14, 93, 23);
		frmWhiteboard.getContentPane().add(rectangle);

		JButton colour = new JButton("Colour");
		colour.addActionListener(e -> this.colour = JColorChooser.showDialog(frmWhiteboard, "select colour", Color.black));
		colour.setBounds(568, 14, 93, 23);
		frmWhiteboard.getContentPane().add(colour);

		userList = new JList();
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.setBounds(694, 47, 231, 144);
		frmWhiteboard.getContentPane().add(userList);

		JButton text = new JButton("Text");
		text.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Text";
			}
		});
		text.setBounds(671, 14, 93, 23);
		frmWhiteboard.getContentPane().add(text);
	}



//	private JFrame frmWhiteboard;
//	private static JList userList;
//	private int x1 = 0;
//	private int y1 = 0;
//	private int x2 = 0;
//	private int y2 = 0;
//	private String action = "None";
//	private Color colour = Color.BLACK;
//	private Canvas canvas;
//	private String text;
//
//	public Canvas getCanvas() {
//		return canvas;
//	}
//
//	/**
//	 * Launch the application.
//	 */
//	public void run() {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ClientGUI window = new ClientGUI();
//					window.frmWhiteboard.setVisible(true);
//					System.out.println(canvas.getGraphics() == null);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
//
//	/**
//	 * Create the application.
//	 */
//	public ClientGUI() {
//		initialize();
//	}
//
//	/**
//	 * Initialize the contents of the frame.
//	 */
//	private void initialize() {
//		frmWhiteboard = new JFrame();
//		frmWhiteboard.setTitle("WhiteBoard");
//		frmWhiteboard.setBounds(100, 100, 950, 650);
//		frmWhiteboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frmWhiteboard.getContentPane().setLayout(null);
//
//		JButton send = new JButton("Send");
//		send.setBounds(850, 578, 74, 23);
//		frmWhiteboard.getContentPane().add(send);
//
//		canvas = new Canvas();
//		canvas.setBackground(Color.WHITE);
//		canvas.setBounds(10, 49, 661, 552);
//		frmWhiteboard.getContentPane().add(canvas);
//		canvas.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mousePressed(MouseEvent e) {
//				x1 = e.getX();
//				y1 = e.getY();
//				if (action.equals("Text")) {
//					text = JOptionPane.showInputDialog("Please enter some text");
//					if (text != null) {
//						JSONObject msg = new JSONObject();
//						msg.put("action", action);
//						msg.put("x1", x1);
//						msg.put("y1", y1);
//						msg.put("x2", x2);
//						msg.put("y2", y2);
//						msg.put("rgb", colour.getRGB());
//						msg.put("text", text);
//						Client.send(msg);
//					}
//				}
//			}
//			@Override
//			public void mouseReleased(MouseEvent e) {
//				x2 = e.getX();
//				y2 = e.getY();
//				if (!action.equals("None")) {
//					JSONObject msg = new JSONObject();
//					msg.put("action", action);
//					msg.put("x1", x1);
//					msg.put("y1", y1);
//					msg.put("x2", x2);
//					msg.put("y2", y2);
//					msg.put("rgb", colour.getRGB());
//					Client.send(msg);
//				}
//			}
//		});
//
//		JButton line = new JButton("Line");
//		line.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				action = "Line";
//			}
//		});
//		line.setBounds(126, 16, 93, 23);
//		frmWhiteboard.getContentPane().add(line);
//
//		JButton circle = new JButton("Circle");
//		circle.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				action = "Circle";
//			}
//		});
//		circle.setBounds(229, 16, 93, 23);
//		frmWhiteboard.getContentPane().add(circle);
//
//		JButton triangle = new JButton("Triangle");
//		triangle.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				action = "Triangle";
//			}
//		});
//		triangle.setBounds(332, 16, 93, 23);
//		frmWhiteboard.getContentPane().add(triangle);
//
//		JButton rectangle = new JButton("Rectangle");
//		rectangle.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				action = "Rectangle";
//			}
//		});
//		rectangle.setBounds(435, 16, 93, 23);
//		frmWhiteboard.getContentPane().add(rectangle);
//
//		JTextArea typeBox = new JTextArea();
//		typeBox.setBounds(691, 502, 233, 99);
//		frmWhiteboard.getContentPane().add(typeBox);
//
//		JTextArea chatBox = new JTextArea();
//		chatBox.setEditable(false);
//		chatBox.setBounds(691, 147, 233, 345);
//		frmWhiteboard.getContentPane().add(chatBox);
//
//		JButton colour = new JButton("Colour");
//		colour.addActionListener(e -> this.colour = JColorChooser.showDialog(frmWhiteboard, "select colour", Color.black));
//		colour.setBounds(538, 16, 93, 23);
//		frmWhiteboard.getContentPane().add(colour);
//
//		JButton curve = new JButton("Curve");
//		curve.setBounds(23, 16, 93, 23);
//		frmWhiteboard.getContentPane().add(curve);
//
//		JButton text = new JButton("Text");
//		text.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				action = "Text";
//			}
//		});
//		text.setBounds(641, 16, 93, 23);
//		frmWhiteboard.getContentPane().add(text);
//
//		userList = new JList();
//		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		userList.setBounds(691, 49, 233, 92);
//		frmWhiteboard.getContentPane().add(userList);
//	}


}