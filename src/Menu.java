import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Menu extends JPanel {

	Image Menu;

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		ImageIcon i = new ImageIcon("menu.png");
		Image img = i.getImage();
		g2d.drawImage(img, 0, 0, null);
	}

}
