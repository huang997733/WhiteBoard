/**
 * author: Ziyang Huang 1067800
 */
package server;

import org.json.simple.JSONObject;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * server side GUI
 */
public class ServerGUI {
	private final Server server;
	private JFrame frmWhiteboardmanager;
	static ServerCanvas canvas;
	private static JList userList;
	private String action = "None";
	private int x1 = 0;
	private int y1 = 0;
	private int x2 = 0;
	private int y2 = 0;
	private Color colour = Color.BLACK;
	private String text;
	private JTextArea typeBox;
	static JTextArea chatBox;
	/**
	 * Launch the application.
	 */
	public void run() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI window = new ServerGUI(server);
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
	public ServerGUI(Server server) {
		this.server = server;
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
				int i = userList.getSelectedIndex();
				if (i > 0) {
					JSONObject msg = new JSONObject();
					String name = Server.getUsernames()[i];
					msg.put("action", "kick");
					msg.put("username", name);
					server.share(msg);
					Server.usernames.remove(name);
					Server.connections.remove(name);
					setUserList();
				}
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
		newFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JSONObject msg = new JSONObject();
				msg.put("action", "new");
				server.share(msg);
				canvas.clear();
				canvas.paintComponent(canvas.getGraphics());
			}
		});

		JMenuItem openFile = new JMenuItem("Open");
		fileMenu.add(openFile);
		openFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);
				File file = chooser.getSelectedFile();
				if (file != null) {
					try {
						BufferedImage image = ImageIO.read(file);
						JSONObject msg = new JSONObject();
						msg.put("action", "open");
						msg.put("image", file.getAbsolutePath());
						server.share(msg);
						canvas.clear();
						canvas.loadImage(image);
						canvas.paintComponent(canvas.getGraphics());
					} catch (IOException ex) {
						JOptionPane.showMessageDialog(new JLabel("error"), "Can't read image", "Can't read image", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		JMenuItem saveFile = new JMenuItem("Save");
		fileMenu.add(saveFile);
		saveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save("canvas.png");
			}
		});

		JMenuItem saveAs = new JMenuItem("Save as");
		fileMenu.add(saveAs);
		saveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.showSaveDialog(null);
				File file = chooser.getSelectedFile();
				if (file != null) {
					String path = file.getAbsolutePath();
					if (!(path.endsWith(".png"))){
						path += ".png";
					}
					save(path);
				}
			}
		});

		JMenuItem closeFile = new JMenuItem("Close");
		fileMenu.add(closeFile);
		closeFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(new JLabel(), "Do you want to close your whiteboard", "Close", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					JSONObject msg = new JSONObject();
					msg.put("action", "close");
					server.share(msg);
					System.exit(1);
				}
			}
		});

		canvas = new ServerCanvas();
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
						canvas.update(msg);
						canvas.paintComponent(canvas.getGraphics());
						server.share(msg);
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
					canvas.update(msg);
					canvas.paintComponent(canvas.getGraphics());
					server.share(msg);
				}
			}
		});

		chatBox = new JTextArea();
		chatBox.setEditable(false);
		chatBox.setBounds(694, 201, 231, 281);
		frmWhiteboardmanager.getContentPane().add(chatBox);

		JButton send = new JButton("Send");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = typeBox.getText();
				if (!text.equals("")) {
					JSONObject msg = new JSONObject();
					msg.put("action", "chat");
					msg.put("username", Server.name);
					msg.put("text", text);
					server.share(msg);
					chatBox.setText(chatBox.getText() + Server.name + ": " + text + "\n");
					typeBox.setText("");
				}
			}
		});
		send.setBounds(832, 580, 93, 23);
		frmWhiteboardmanager.getContentPane().add(send);

		typeBox = new JTextArea();
		typeBox.setBounds(694, 490, 231, 113);
		frmWhiteboardmanager.getContentPane().add(typeBox);

		JButton line = new JButton("Line");
		line.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Line";
			}
		});
		line.setBounds(53, 14, 93, 23);
		frmWhiteboardmanager.getContentPane().add(line);

		JButton circle = new JButton("Circle");
		circle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Circle";
			}
		});
		circle.setBounds(156, 14, 93, 23);
		frmWhiteboardmanager.getContentPane().add(circle);

		JButton triangle = new JButton("Triangle");
		triangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Triangle";
			}
		});
		triangle.setBounds(259, 14, 93, 23);
		frmWhiteboardmanager.getContentPane().add(triangle);

		JButton rectangle = new JButton("Rectangle");
		rectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Rectangle";
			}
		});
		rectangle.setBounds(362, 14, 93, 23);
		frmWhiteboardmanager.getContentPane().add(rectangle);

		JButton colour = new JButton("Colour");
		colour.addActionListener(e -> {
			this.colour = JColorChooser.showDialog(frmWhiteboardmanager, "select colour", this.colour);
			if (this.colour == null) {
				this.colour = Color.BLACK;
			}
		});
		colour.setBounds(465, 14, 93, 23);
		frmWhiteboardmanager.getContentPane().add(colour);

		userList = new JList();
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.setBounds(694, 47, 231, 144);
		frmWhiteboardmanager.getContentPane().add(userList);
		setUserList();

		JButton text = new JButton("Text");
		text.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = "Text";
			}
		});
		text.setBounds(568, 14, 93, 23);
		frmWhiteboardmanager.getContentPane().add(text);
	}

	/**
	 * update the user list
	 */
	public static void setUserList() {
		userList.setListData(Server.getUsernames());
	}

	/**
	 * save the canvas to the given path
	 * @param path the path manager wants to save to
	 */
	private void save(String path){
		BufferedImage pic = new BufferedImage(1024, 768, BufferedImage.TYPE_INT_RGB);
		Graphics g = pic.getGraphics();
		g.fillRect(0, 0, 1024, 768);
		g.drawImage(canvas.getImage(),0,0,null);
		for (JSONObject h : Server.history) {
			String a = (String) h.get("action");
			int x1 = (int) h.get("x1");
			int y1 = (int) h.get("y1");
			int x2 = (int) h.get("x2");
			int y2 = (int) h.get("y2");
			Color color = new Color((Integer) h.get("rgb"));
			g.setColor(color);

			switch (a) {
				case "Line" -> g.drawLine(x1, y1, x2, y2);
				case "Circle" -> {
					int max = Math.max(Math.abs(x1 - x2), Math.abs(x1 - x2));
					g.drawOval(Math.min(x1, x2), Math.min(y1, y2), max, max);
				}
				case "Triangle" -> {
					int[] x = {(x1+x2)/2,x2,x1};
					int[] y = {y1,y2,y2};
					g.drawPolygon(x,y,3);
				}
				case "Rectangle" -> g.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
				case "Text" -> g.drawString((String) h.get("text"), x1, y1);
			}
		}
		try {
			ImageIO.write(pic, "PNG", new File(path));
			JOptionPane.showMessageDialog(new JLabel(), "Image saved", "Image saved", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JLabel(), "Can't save the image", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}