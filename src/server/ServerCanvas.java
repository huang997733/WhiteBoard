/**
 * author: Ziyang Huang 1067800
 */
package server;

import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Server canvas extends JPanel
 */
public class ServerCanvas extends JPanel {
    int x1;
    int y1;
    int x2;
    int y2;
    String action = "None";
    String text;
    private final ArrayList<JSONObject> history = new ArrayList<>();
    private BufferedImage image;

    public BufferedImage getImage() {
        return image;
    }

    public ServerCanvas() {
        super();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // draw the preloaded image
        g.drawImage(image, 0, 0, null);
        // draw everything in the history
        for (JSONObject msg : history) {
            action = (String) msg.get("action");
            x1 = (int) msg.get("x1");
            y1 = (int) msg.get("y1");
            x2 = (int) msg.get("x2");
            y2 = (int) msg.get("y2");
            int rgb = (int) msg.get("rgb");
            Color c = new Color(rgb);
            g.setColor(c);
            switch (action) {
                case "Line" -> {
                    g.drawLine(x1,y1,x2,y2);
                }
                case "Circle" -> {
                    int max = Math.max(Math.abs(x1 - x2), Math.abs(x1 - x2));
                    g.drawOval(Math.min(x1, x2), Math.min(y1, y2), max, max);
                }
                case "Triangle" -> {
                    int[] x = {(x1+x2)/2,x2,x1};
                    int[] y = {y1,y2,y2};
                    g.drawPolygon(x,y,3);
                }
                case "Rectangle" -> {
                    g.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
                }
                case "Text" -> {
                    text = (String) msg.get("text");
                    g.drawString(text, x1, y1);
                }
            }
        }

    }

    /**
     * add the action to history which is used to paint the canvas
     * @param msg the message which contains the action on the canvas
     */
    public void update(JSONObject msg) {
        this.history.add(msg);
    }

    /**
     * save the image which is used to paint the canvas
     * @param image the image which we want to draw on the canvas
     */
    public void loadImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * clear the canvas
     */
    public void clear() {
        this.history.clear();
        this.image = null;
    }
}
