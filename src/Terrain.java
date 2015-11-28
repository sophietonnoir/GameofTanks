import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Terrain extends JPanel {

	private int depart;
	private double nouveau;
	private int f;
	private int fin;
	private int i = 0, k = 0;
	public int departH;
	public int minH;
	public int maxH;

	private int tailleterrain = 1000;
	Random rand = new Random();
	public Color[] colors;
	ArrayList<Integer> map = new ArrayList<Integer>();
	ArrayList<Integer> departPosAll = new ArrayList<Integer>();
	ArrayList<Integer> mapminH = new ArrayList<Integer>();

	public Terrain(int departH, int minH, int maxH) {
		this.departH = departH;
		this.minH = minH;
		this.maxH = maxH;
		f = 1;
		colors = new Color[tailleterrain];
		creationTerrain();
	}

	// Dessin du ciel
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			Image img = ImageIO.read(new File("ciel.jpg"));
			g.drawImage(img, 0, 0, this);
		} catch (IOException e) {
			e.printStackTrace();
		}

		paintMap(g);
	}

	public void creationTerrain() {
		depart = Math.abs(departH + (rand.nextInt() % 40));
		map.add(depart);
		mapminH.add(minH);

		while (i < tailleterrain) {
			fin = map.get(map.size() - 1);

			nouveau = rand.nextInt(100);
			if (nouveau > 93) {
				departPosAll.add(k);
				for (int j = 0; j < 30; j++) {
					map.add(fin);

					mapminH.add(minH);
					k++;
				}
				f = -(f);
			}

			map.add(fin + (f));
			mapminH.add(minH);
			colors[i] = new Color(0, 255, 0);
			i++;
			k++;

		}
	}

	public void paintMap(Graphics g) {
		for (int a = 0; a < tailleterrain; a++) {
			g.setColor(colors[a]);
			if (map.get(a) < minH + 10) {
				g.drawLine(a, map.get(a), a, mapminH.get(a));

			}
		}
	}
}