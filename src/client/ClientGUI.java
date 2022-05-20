package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

public class ClientGUI {

	private JFrame frmWhiteboard;

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

		JButton btnNewButton_6 = new JButton("Send");
		btnNewButton_6.setBounds(850, 578, 74, 23);
		frmWhiteboard.getContentPane().add(btnNewButton_6);

		JPanel canvas = new JPanel();
		canvas.setBackground(Color.WHITE);
		canvas.setBounds(10, 49, 661, 552);
		frmWhiteboard.getContentPane().add(canvas);

		JButton line = new JButton("Line");
		line.setBounds(126, 16, 93, 23);
		frmWhiteboard.getContentPane().add(line);

		JButton circle = new JButton("Circle");
		circle.setBounds(229, 16, 93, 23);
		frmWhiteboard.getContentPane().add(circle);

		JButton triangle = new JButton("Triangle");
		triangle.setBounds(332, 16, 93, 23);
		frmWhiteboard.getContentPane().add(triangle);

		JButton rectangle = new JButton("Rectangle");
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
		colour.setBounds(538, 16, 93, 23);
		frmWhiteboard.getContentPane().add(colour);

		JButton curve = new JButton("Curve");
		curve.setBounds(23, 16, 93, 23);
		frmWhiteboard.getContentPane().add(curve);

		JButton text = new JButton("Text");
		text.setBounds(641, 16, 93, 23);
		frmWhiteboard.getContentPane().add(text);

		JList userList = new JList();
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.setBounds(691, 49, 233, 92);
		frmWhiteboard.getContentPane().add(userList);
	}
}