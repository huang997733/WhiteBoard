package server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

public class ServerGUI {

	private JFrame frmWhiteboardmanager;
	static JPanel canvas;
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
			}
		});
		line.setBounds(156, 14, 93, 23);
		frmWhiteboardmanager.getContentPane().add(line);

		JButton circle = new JButton("Circle");
		circle.setBounds(259, 14, 93, 23);
		frmWhiteboardmanager.getContentPane().add(circle);

		JButton triangle = new JButton("Triangle");
		triangle.setBounds(362, 14, 93, 23);
		frmWhiteboardmanager.getContentPane().add(triangle);

		JButton rectangle = new JButton("Rectangle");
		rectangle.setBounds(465, 14, 93, 23);
		frmWhiteboardmanager.getContentPane().add(rectangle);

		JButton colour = new JButton("Colour");
		colour.setBounds(568, 14, 93, 23);
		frmWhiteboardmanager.getContentPane().add(colour);

		JList userList = new JList();
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.setBounds(694, 47, 231, 144);
		frmWhiteboardmanager.getContentPane().add(userList);

		JButton text = new JButton("Text");
		text.setBounds(671, 14, 93, 23);
		frmWhiteboardmanager.getContentPane().add(text);
	}
}