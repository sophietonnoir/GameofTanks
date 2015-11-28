import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Board extends JPanel {
	public static int BLarg = 1000;
	public static int BHaut = 700;
	private int i = 0, k = 0;

	Image ciel, soleil, bird, oiseau, logo;

	LinkedList<Terrain> landscapes = new LinkedList<Terrain>();
	public static LinkedList<Tank> tanks = new LinkedList<Tank>();

	public Terrain landscape = new Terrain(400, 871, 300);

	public static JProgressBar shotBar;

	public Board() {
		// Images de fond et décoration
		try {
			Image ciel = ImageIO.read(new File("ciel.jpg"));
			this.ciel = ciel;

			Image soleil = ImageIO.read(new File("soleil.gif"));
			this.soleil = soleil;

			/*
			 * Image bird = ImageIO.read(new File("bird.png")); this.bird=bird;
			 */

			Image oiseau = ImageIO.read(new File("oiseau.gif"));
			this.oiseau = oiseau;

			/*
			 * Image logo = ImageIO.read(new File("logo.jpg")); this.logo=logo;
			 */

		} catch (IOException e) {
			e.printStackTrace();
		}

		// Bar de tir
		JProgressBar shotBar = new JProgressBar(JProgressBar.VERTICAL);
		this.shotBar = shotBar;

	}

	public void addLandscapes(LinkedList<Terrain> landscapes) {
		this.landscapes = landscapes;
		landscapes.add(landscape);
	}

	public void addTank(Tank tank) {
		tanks.add(tank);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Dessine le fond d'écran et les décorations
		g.drawImage(ciel, 0, 0, BLarg, BHaut, null);
		g.drawImage(soleil, 10, 10, null);
		g.drawImage(oiseau, 600, 140, null);
		// g.drawImage(bird, 600, 140, null);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Century Schoolbook", Font.BOLD, 20));
		g.drawString("Projectiles", 359, 60);
		g.setColor(Color.white);
		g.setFont(new Font("Harrington", Font.BOLD, 20));
		g.drawString("C     D      X", 368, 135);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Century Schoolbook", Font.BOLD, 16));
		g.drawString("Tank 1", 789, 39);
		g.drawString("Tank 2", 789, 65);

		g.setColor(Color.YELLOW);
		g.setFont(new Font("Harrington", Font.BOLD, 20));
		g.drawString("Espace pour tirer", 230, 30);
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Harrington", Font.BOLD, 20));
		g.drawString("Gauche/droite pour se déplacer", 480, 30);
		g.setFont(new Font("Harrington", Font.BOLD, 19));
		g.drawString("Haut/Bas pour l'orientation du canon", 684, 100);
		g.setFont(new Font("Harrington", Font.BOLD, 20));
		g.drawString("A-> Tank 1", 230, 70);
		g.setFont(new Font("Harrington", Font.BOLD, 20));
		g.drawString("B-> Tank 2", 790, 160);
		// g.drawImage(logo, 100, 50, null);
		/*
		 * g.setColor(Color.orange); g.setFont(new Font("Arial", Font.BOLD,
		 * 20)); g.drawString("TankWars",100,50);
		 */

		ListIterator<Terrain> landscapeAdded = landscapes.listIterator();
		while (landscapeAdded.hasNext()) {
			Terrain landscape = landscapeAdded.next();
			landscape.paintMap(g);
		}

		ListIterator<Tank> tankAdded = tanks.listIterator();
		while (tankAdded.hasNext()) {
			Tank tankX = tankAdded.next();
			if (tankX.vie != 0) { // Tant que le tank est vivant, on le dessine
				tankX.drawTank(g);
				if (tankX.tir) { // Quand on tire, on dessine la balle
					ListIterator<Projectile> tirBalle = tankX.balles
							.listIterator();
					while (tirBalle.hasNext()) {
						Projectile balle = tirBalle.next();
						if (balle.toStop != 1) {
							balle.drawBalle(g, balle.getPosX(), balle.getPosY());
							balle.collision(tankX.id);
							balle.repaint();

						}
					}
				}
			} else {
				Audio.play("victoire.wav");
				tankX.drawExplosion(g); // Dessine l'explosion de la fin de
										// partie
				// tankX.drawVictoire(g);

			}
		}
	}

	public void go() {
		addLandscapes(landscapes);
		for (int j = 0; j < 2; j++) {
			Tank tank = new Tank(landscape.departPosAll.get(k + 7),
					landscape.map.get(landscape.departPosAll.get(k)) - 77);
			addTank(tank);
			k++;
		}
		ListIterator<Tank> tankAdded = tanks.listIterator();
		while (tankAdded.hasNext()) {
			i++;
			Tank tankX = tankAdded.next();
			tankX.setId(i);
			tankX.setBoard(this);
			tankX.setLandscapes(landscapes);
			new Thread(tankX).start();

		}
	}

}
