package server;

import client.Client;
import org.json.simple.JSONObject;

import java.awt.EventQueue;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ServerGUI {

	private JFrame frmWhiteboardmanager;
	static JPanel canvas;
	private static JList userList;
	private String action = "None";
	private int x1 = 0;
	private int y1 = 0;
	private int x2 = 0;
	private int y2 = 0;
	private Color colour = Color.BLACK;
	private String text;
	/**
	 * Launch the application.
	 */
	public void run() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI window = new ServerGUI();
					window.frmWhiteboardmanager.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ServerGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWhiteboardmanager = new JFrame();
		frmWhiteboardmanager.setResizable(false);
		frmWhiteboardmanager.setTitle("WhiteBoard(Manager)");
		frmWhiteboardmanager.setBounds(100, 100, 951, 652);
		frmWhiteboardmanager.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmWhiteboardmanager.getContentPane().setLayout(null);

		JButton kick = new JButton("Kick");
		kick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		kick.setBounds(832, 14, 93, 23);
		frmWhiteboardmanager.getContentPane().add(kick);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 43, 23);
		frmWhiteboardmanager.getContentPane().add(menuBar);

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		fileMenu.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));

		JMenuItem newFile = new JMenuItem("New");
		fileMenu.add(newFile);

		JMenuItem openFile = new JMenuItem("Open");
		fileMenu.add(openFile);

		JMenuItem saveFile = new JMenuItem("Save");
		fileMenu.add(saveFile);

		JMenuItem saveAs = new JMenuItem("Save as");
		fileMenu.add(saveAs);

		JMenuItem closeFile = new JMenuItem("Close");
		fileMenu.add(closeFile);

		canvas = new JPanel();
		canvas.setBackground(Color.WHITE);
		canvas.setBounds(10, 47, 664, 556);
		frmWhiteboardmanager.getContentPane().add(canvas);
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
						Server.share(msg);
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
					Server.share(msg);
				}
			}
		});

		JTextArea chatBox = new JTextArea();
		chatBox.setEditable(false);
		chatBox.setBounds(694, 201, 231, 281);
		frmWhiteboardmanager.getContentPane().add(chatBox);

		JButton send = new JButton("Send");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		send.setBounds(832, 580, 93, 23);
		frmWhiteboardmanager.getContentPane().add(send);

		JTextArea typeBox = new JTextArea();
		typeBox.setBounds(694, 490, 231, 113);
		frmWhiteboardmanager.getContentPane().add(typeBox);

		JButton curve = new JButton("Curve");
		curve.setBounds(53, 14, 93, 23);
		frmWhiteboardmanager.getContentPane().add(curve);

		JButton line = new JButton("Line");
		line.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Line";
			}
		});
		line.setBounds(156, 14, 93, 23);
		frmWhiteboardmanager.getContentPane().add(line);

		JButton circle = new JButton("Circle");
		line.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Circle";
			}
		});
		circle.setBounds(259, 14, 93, 23);
		frmWhiteboardmanager.getContentPane().add(circle);

		JButton triangle = new JButton("Triangle");
		line.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Triangle";
			}
		});
		triangle.setBounds(362, 14, 93, 23);
		frmWhiteboardmanager.getContentPane().add(triangle);

		JButton rectangle = new JButton("Rectangle");
		line.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Rectangle";
			}
		});
		rectangle.setBounds(465, 14, 93, 23);
		frmWhiteboardmanager.getContentPane().add(rectangle);

		JButton colour = new JButton("Colour");
		colour.addActionListener(e -> this.colour = JColorChooser.showDialog(frmWhiteboardmanager, "select colour", Color.black));
		colour.setBounds(568, 14, 93, 23);
		frmWhiteboardmanager.getContentPane().add(colour);

		userList = new JList();
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.setBounds(694, 47, 231, 144);
		frmWhiteboardmanager.getContentPane().add(userList);
		setUserList();

		JButton text = new JButton("Text");
		line.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Text";
			}
		});
		text.setBounds(671, 14, 93, 23);
		frmWhiteboardmanager.getContentPane().add(text);
	}

	public static void setUserList() {
		userList.setListData(Server.getUsernames());
	}
}