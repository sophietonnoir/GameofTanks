import java.awt.*;
import java.util.LinkedList;
import java.util.ListIterator;

public class Destruction extends Projectile {

	public Destruction(double vitesse, double angle, double initHauteur,
			double departLongitude, int fenHaut, int fenLarg,
			LinkedList<Terrain> landscapes) {
		super(vitesse, angle, initHauteur, departLongitude, fenHaut, fenLarg,
				landscapes);
	}

	public void drawBalle(Graphics g, double x, int y) {
		double Time = 0.05;
		this.endTime = 100.0;

		this.ay = super.gravity;
		if (x > fenLarg - 35) {
			this.setPosX(fenLarg - 35);
			this.vx = -vx / 2;
			this.ax = -ax / 2;
		}
		if (x < -20) {
			this.setPosX(-20);
			this.vx = -vx / 2;
			this.ax = -ax / 2;
		}

		this.vx += this.ax * Time;
		this.vy -= this.ay * Time;
		ListIterator<Terrain> landscapeAdded = super.landscapes.listIterator();
		while (landscapeAdded.hasNext()) {
			Terrain landscape = landscapeAdded.next();
			if (y > landscape.maxH && y < landscape.minH) { // Pour les portions
															// de terrains
															// verticales en
															// forme de
															// rectangle
				if (y > landscape.map.get((int) this.x + 20) - 10
						&& y < landscape.map.get((int) this.x + 20) + 10) { // Si
																			// la
																			// balle
																			// touche
																			// le
																			// sol
					if (this.x > -1) {
						for (int j = 0; j < 40; j++) { // Formation d'un trou
														// dans le sol
							if ((landscape.map.get((int) this.x + 20 + j - 5) + (200 - (j - 5)
									* (j - 35)) / 25) < landscape.minH
									&& landscape.mapminH.get((int) this.x + 20
											+ j - 5) > landscape.map
												.get((int) this.x + 20 + j - 5))
								landscape.map.set(
										(int) this.x + 20 + j - 5,
										landscape.map.get((int) this.x + 20 + j
												- 5)
												+ (200 - (j - 5) * (j - 35))
												/ 25);

							else
								landscape.map.set((int) this.x + 20 + j - 5,
										fenHaut);

						}
					}
					this.toStop = 1;
					this.vy = 0;
					this.ay = 0;
					this.vx = 0;
					this.ax = 0;
				}

			}
		}

		this.x += this.vx * Time;
		this.y += this.vy * Time;

		// Couleur et dessin de la balle : coordonnées en x, y et de hauteur et
		// largeur 18
		g.setColor(super.couleur);
		g.fillOval((int) x + 20, y - 10, 18, 18);
	}

	public void collision(int shooter) { // Collision
		ListIterator<Tank> tankAdded = Board.tanks.listIterator();
		while (tankAdded.hasNext()) {
			Tank tank = tankAdded.next();
			if (y > tank.getPosY() && y < tank.getPosY() + tank.tankHeight
					&& x > tank.getPosX() - tank.tankWidth - 10
					&& x < tank.getPosX() + 10) {
				this.toStop = 1;
				this.vy = 0;
				this.ay = 0;
				this.vx = 0;
				this.ax = 0;
				tank.setHit(); // Si tank touché on lui retire de la vie
				tank.vie--;
				Audio.play("tir.wav");
				if (tank.vie == 0) { // Tank mort
					tank.setLifeBarValue();
					tank.vivant = false;

				}
			}
		}
	}
}
