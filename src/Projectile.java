import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

public class Projectile extends JPanel {
	private int masse = 10; // Masse d'une balle
	public int vY = 1; // vitesse du projectile selon y
	public Color couleur = Color.black; // Couleur noire de la balle
	public double gravity = -9.8;
	public int fenHaut;
	public int fenLarg;
	public int toStop = 0;
	public LinkedList<Terrain> landscapes;

	public double endTime, x, y, vitesse, angle, ay, ax, vx, vy;

	public Projectile(double vitesse, double angle, double departH,
			double departLongitude, int fenHaut, int fenLarg,
			LinkedList<Terrain> landscapes) {
		this.vitesse = vitesse;
		this.x = departLongitude;
		this.y = departH;
		this.ax = 0.0;
		this.angle = angle;
		this.fenHaut = fenHaut;
		this.fenLarg = fenLarg;
		this.landscapes = landscapes;

		this.vx += this.vitesse * Math.cos(this.angle * (Math.PI / 180.0));
		this.vy += this.vitesse * Math.sin(this.angle * (Math.PI / 180.0));
	}

	public void drawBalle(Graphics g, double x, int y) {
	}

	public void collision(int shooter) {
	}

	public int getPosX() {
		return (int) x;
	}

	public void setPosX(double x) {
		this.x = x;
	}

	public int getPosY() {
		return (int) y;
	}
}
