package presentation;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

class GifPanel extends JPanel {
    private final ImageIcon gifIcon;

    public GifPanel(String resourcePath) {
        this.gifIcon = new ImageIcon(getClass().getResource(resourcePath));
        Timer timer = new Timer(100, e -> repaint());
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image image = gifIcon.getImage();
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        g.drawImage(image, 0, 0, panelWidth, panelHeight, this);
    }
}
