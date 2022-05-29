/**
 * author: Ziyang Huang 1067800
 */
package server;

import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ServerCanvas extends JPanel {
    int x1;
    int y1;
    int x2;
    int y2;
    String action = "None";
    String text;
    private ArrayList<JSONObject> history = new ArrayList<>();

    public ServerCanvas() {
        super();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
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

    public void update(JSONObject msg) {
        this.history.add(msg);
    }

    public void clear() {
        this.history.clear();
    }
}
