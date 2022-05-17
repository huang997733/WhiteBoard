package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JTextArea;

public class ClientGUI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public void run() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI window = new ClientGUI();
					window.frame.setVisible(true);
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
		frame = new JFrame();
		frame.setBounds(100, 100, 950, 650);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton_6 = new JButton("Send");
		btnNewButton_6.setBounds(850, 578, 74, 23);
		frame.getContentPane().add(btnNewButton_6);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(10, 49, 661, 552);
		frame.getContentPane().add(panel);
		
		JButton btnNewButton = new JButton("Line");
		btnNewButton.setBounds(126, 16, 93, 23);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Circle");
		btnNewButton_1.setBounds(229, 16, 93, 23);
		frame.getContentPane().add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Triangle");
		btnNewButton_2.setBounds(332, 16, 93, 23);
		frame.getContentPane().add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Rectangle");
		btnNewButton_3.setBounds(435, 16, 93, 23);
		frame.getContentPane().add(btnNewButton_3);
		
		JTextArea typeArea = new JTextArea();
		typeArea.setBounds(691, 502, 233, 99);
		frame.getContentPane().add(typeArea);
		
		JTextArea chatBox = new JTextArea();
		chatBox.setEditable(false);
		chatBox.setBounds(691, 147, 233, 345);
		frame.getContentPane().add(chatBox);
		
		JTextArea userList = new JTextArea();
		userList.setEditable(false);
		userList.setBounds(691, 49, 233, 89);
		frame.getContentPane().add(userList);
		
		JButton btnNewButton_4 = new JButton("Colour");
		btnNewButton_4.setBounds(538, 16, 93, 23);
		frame.getContentPane().add(btnNewButton_4);
		
		JButton btnNewButton_5 = new JButton("Curve");
		btnNewButton_5.setBounds(23, 16, 93, 23);
		frame.getContentPane().add(btnNewButton_5);
	}
}
