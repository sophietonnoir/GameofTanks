import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.util.*;
import java.util.Timer;

public class Tank extends JPanel implements Runnable {

	private double x, y;
	public int BLarg = 1000;
	public int BHaut = 1000;
	private boolean bX, bY;
	public static int tankWidth = 25;
	public static int tankHeight = 20;
	private int fps = 100; // Nombre d'images par seconde du jeu
	public boolean tir = false;
	public double angle;
	public double angleTir;
	public double angleCorrige;
	public double degres;
	public int marge;
	public int ajustCanonX;
	public int ajustCanonY;
	public boolean balle1 = true, balleen3 = false, balleen2 = false; // Boolean
																		// de
																		// type
																		// de
																		// balles
	public int id;
	public int idActif = 1;
	public int hit = 0;
	public int vie = 10;
	public boolean suivilifeBar = false;
	public boolean down = false, angleSelected = false, powerSelected = false;

	public Image tankDroit, tankGauche, canon, explosion, balleEn3, balleSeule,
			balle2;
	public boolean right = true;
	public boolean vise = false;
	public boolean vivant = true;
	public boolean changing = false;
	public boolean canonApp = true; // Canon horizontal apparent
	public boolean rectangle1 = false; // rectangle de choix de projectile
	public boolean rectangle2 = true;
	public boolean rectangle3 = false;// rectangle de choix de projectile

	private boolean spacePause = false;

	private LinkedList<Terrain> landscapes;
	private Board board;

	public JProgressBar lifeBar;

	LinkedList<Projectile> balles = new LinkedList<Projectile>();
	ListIterator<Tank> tanks = board.tanks.listIterator();

	private int posX;
	private int posY;

	public Tank(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;

		// Barres de vie des tanks

		JProgressBar lifeBar = new JProgressBar(JProgressBar.HORIZONTAL);
		lifeBar.setBounds(850, 50 * id / 2, 100, 15);
		this.lifeBar = lifeBar;
		lifeBar.setStringPainted(true);
		lifeBar.setBorderPainted(true);

		// Images du Tank

		Image tankDroit = getToolkit().getImage("tank11.png");
		this.tankDroit = tankDroit;
		Image tankGauche = getToolkit().getImage("tank22.png");
		this.tankGauche = tankGauche;

		// Image du canon
		Image canon = getToolkit().getImage("cannon.png");
		this.canon = canon;

		// Image de l'explosion finale
		Image explosion = getToolkit().getImage("boom.png");
		this.explosion = explosion;

		// Projectile qui se divise en 3
		Image balleEn3 = getToolkit().getImage("balles3.png");
		this.balleEn3 = balleEn3;

		// Projectile 1 balle
		Image balleSeule = getToolkit().getImage("balleseule.png");
		this.balleSeule = balleSeule;

		// Projectile 2 balles
		Image balle2 = getToolkit().getImage("balles2.png");
		this.balle2 = balle2;
	}

	public void run() {
		x = this.getPosX();
		y = this.getPosY();
		while (vivant) {
			while (tanks.hasNext() && vivant) {
				if (!suivilifeBar)
					setLifeBar();
				if (Windows.key.a)
					setIdAnimated(1); // si on appuie sur a, le tank 1 est animé
				if (Windows.key.b)
					setIdAnimated(2); // si on appuie sur b, le tank 1 est animé

				ListIterator<Terrain> landscapeAdded = landscapes
						.listIterator();
				while (landscapeAdded.hasNext()) {

					// Choix de l'arme à utiliser
					if (Windows.key.d) { // d-> projectile 1 balle
						balle1 = true;
						balleen2 = false;
						balleen3 = false;
						rectangle2 = true;
						rectangle1 = false;
						rectangle3 = false;

					}

					if (Windows.key.c) { // c-> projectile 3 balles
						rectangle1 = true;
						rectangle2 = false;
						rectangle3 = false;
						balle1 = false;
						balleen3 = true;
						balleen2 = false;
					}

					if (Windows.key.x) { // x-> projectile 2 balles
						rectangle1 = false;
						rectangle2 = false;
						rectangle3 = true;
						balle1 = false;
						balleen3 = false;
						balleen2 = true;
					}

					Terrain landscape = landscapeAdded.next();
					// Calcul de la pente de la courbe des tanks et des canons à
					// prendre
					if (posY > landscape.maxH - 40
							&& posY < 2 * landscape.minH - landscape.maxH) {

						int y1 = landscape.map.get((int) x);
						int y2 = landscape.map.get((int) x + tankWidth);
						double x1 = x;
						double x2 = x + tankWidth;
						angle = Math.atan((y2 - y1) / (x2 - x1));
						degres = Math.toDegrees(angle);
						if (degres > -45 && degres < 0)
							marge = -6;
						if (degres > 0 && degres < 45)
							marge = 3;
						if (degres == 45) {
							marge = 10;
							ajustCanonX = 0;
							ajustCanonY = 15;
						}
						if (degres == 0)
							marge = 5;
						if (degres == -45) {
							marge = -10;
							ajustCanonX = 0;
							ajustCanonY = 15;
						}
						if (y > y1 - tankWidth + marge)
							bY = true;
					}

					// On fait tomber les tanks jusqu'à la rencontre du terrain
					if (x < 0)
						bX = false;
					if (x > this.getWidth() - 50)
						bX = true;
					if (y < 1)
						bY = false;
					if (posY > landscape.map.get((int) x) - tankHeight) {
						bY = true;
					}
					if (!bY)
						this.setPosY((int) y++);

					// Si le tank est activé, on lance le code
					if (this.id == idActif && vivant && !changing) {

						if (posY > landscape.maxH - 40
								&& posY < 2 * landscape.minH - landscape.maxH) {
							if (!bY) {
								this.setPosY((int) y++);
							} else { // Direction des tanks
								if (Windows.key.left) {
									right = false;
									if (posX > 3)
										this.setPosX((int) x--);
									this.setPosY((landscape.map.get((int) x))
											- tankWidth + marge);
								}
								if (Windows.key.right) {
									right = true;
									if (posX < BLarg - tankWidth - 10)
										this.setPosX((int) x++);
									this.setPosY((landscape.map.get((int) x))
											- tankWidth + marge);
								}
								if (Windows.key.space && !spacePause
										&& !Windows.key.left
										&& !Windows.key.right
										&& !Windows.key.up && !Windows.key.down) {
									vise = true;
									canonApp = false;
									Windows.key.space = false;
									// Selection de l'angle de tir du canon
									// selon que le tank est dirigé à gauche ou
									// à droite
									int i = 1;
									while (!angleSelected) {
										if (Windows.key.down && right) {
											if (getAngleTir() > -1.5707963267949
													&& getAngleTir() < 1.5707963267949)
												angleCorrige = angleCorrige + 0.004453292519943;
											else
												angleCorrige = -1.56;
										}
										if (Windows.key.up && right) {
											if (getAngleTir() > -1.5707963267949
													&& getAngleTir() < 1.5707963267949)
												angleCorrige = angleCorrige - 0.004453292519943;
											else
												angleCorrige = 1.56;
										}

										if (Windows.key.up && !right) {
											if (getAngleTir() > -1.5707963267949
													&& getAngleTir() < 1.5707963267949)
												angleCorrige = angleCorrige + 0.004453292519943;
											else
												angleCorrige = -1.56;
										}
										if (Windows.key.down && !right) {
											if (getAngleTir() > -1.5707963267949
													&& getAngleTir() < 1.5707963267949)
												angleCorrige = angleCorrige - 0.004453292519943;
											else
												angleCorrige = 1.56;
										}
										try {
											Thread.sleep(10);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										if (Windows.key.space) { // Angle
																	// choisi,
																	// on
																	// réinitialise
											angleSelected = true;

											Windows.key.space = false;
										}
									}

									while (!powerSelected) { // on fait
																// apparaitre la
																// barre de tir
										board.shotBar
												.setBounds(50, 50, 15, 100);
										board.add(board.shotBar);
										board.shotBar.setValue(i);
										if (i >= 100)
											down = true;
										if (i <= 0)
											down = false;
										if (down)
											i = i - 2;
										else
											i = i + 2;
										try {
											Thread.sleep(10);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										if (Windows.key.space) { // On tire
											powerSelected = true;
											canonApp = true;
											Audio.play("touche.wav");
											Windows.key.space = false;
										}
									}
									board.remove(board.shotBar); // on retire la
																	// barre de
																	// tir
									angleSelected = false;
									powerSelected = false;
									vise = false;
									if (vivant && !changing)
										balleGenerator(landscapes,
												board.shotBar.getValue()); // generation
																			// de
																			// la
																			// balle
									spacePause = true;
									Timer timer = new Timer();
									timer.schedule(new TimerTask() {

										public void run() {
											spacePause = false;
										}
									}, 180); // Le temps entre deux tirs
								}
							}
						}
					}

				}

				setLifeBarValue();
				board.repaint();
				// Faire une pause de fps secondes
				try {
					Thread.sleep(1000 / fps);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}

	public void setLandscapes(LinkedList<Terrain> landscapes) {
		this.landscapes = landscapes;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIdAnimated(int idActif) {
		this.idActif = idActif;
	}

	public void setHit() {
		hit = 10;
	}

	public void setLifeBar() {
		lifeBar.setBounds(850, 50 * id / 2, 100, 15);
		lifeBar.setValue(100);
		board.add(lifeBar);
		suivilifeBar = true;
	}

	public void setLifeBarValue() {
		lifeBar.setBounds(850, 50 * id / 2, 100, 15);
		lifeBar.setValue(vie * 10);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.white);
		drawTank(g);
	}

	// On dessine les tanks pour qu'ils suivent bien le terrain
	public void drawTank(Graphics g) {
		Graphics2D g2d;
		g2d = (Graphics2D) g.create();

		AffineTransform transform = new AffineTransform();
		transform.rotate(angle, posX + tankWidth / 2, posY + tankHeight / 2); // angle
																				// pour
																				// la
																				// rotation
		transform.translate(posX, posY - 10); // Translation du tank

		// Rotation et translation du canon si le tank est dirigé vers la droite
		AffineTransform transform4 = new AffineTransform();
		transform4.rotate(angle, posX + tankWidth / 2, posY + tankHeight / 2);
		transform4.translate(posX + tankWidth - 5, posY);

		// Rotation et translation du canon si le tank est dirigé vers la gauche
		AffineTransform transform5 = new AffineTransform();
		transform5.rotate(angle, posX + tankWidth / 2, posY + tankHeight / 2);
		transform5.translate(posX + tankWidth / 2, posY);

		if (right) { // Dessin du tank et canon si le tank est dirigé vers la
						// droite
			g2d.drawImage(tankDroit, transform, this);
			if (canonApp) {
				g2d.drawImage(canon, transform4, this);
			}
		}
		if (!right) { // Dessin du tank et canon si le tank est dirigé vers la
						// gauche
			g2d.drawImage(tankGauche, transform, this);
			if (canonApp) {
				g2d.drawImage(canon, transform5, this);
			}
		}

		// Rotation et translation du canon lors du tir vers la droite
		AffineTransform transform2 = new AffineTransform();
		angleTir = angleCorrige;
		transform2.rotate(angle + angleTir, posX + tankWidth / 2, posY
				+ tankHeight / 2);
		transform2.translate(posX + tankWidth - 5, posY);

		// Rotation et translation du canon lors du tir vers la gauche
		AffineTransform transform3 = new AffineTransform();
		angleTir = angleCorrige;
		transform3.rotate(angle + angleTir, posX + tankWidth + 8, posY
				+ tankHeight - 30);
		transform3.translate(posX - tankWidth / 2 + 20, posY);

		if (vise && right)
			g2d.drawImage(canon, transform2, this);
		if (vise && !right)
			g2d.drawImage(canon, transform3, this);

		// On dessine les différents projectiles
		g2d.drawImage(balleEn3, 360, 80, this);
		g2d.drawImage(balleSeule, 400, 80, this);
		g2d.drawImage(balle2, 440, 86, this);

		// On dessine les rectangles de choix autour des projectiles
		if (rectangle1)
			g2d.drawRect(360, 80, 30, 30);
		if (rectangle2)
			g2d.drawRect(400, 80, 30, 30);
		if (rectangle3)
			g2d.drawRect(440, 80, 30, 30);

	}

	// Dessin de l'explosion finale
	public void drawExplosion(Graphics g) {
		Graphics2D g2d;
		g2d = (Graphics2D) g.create();
		g2d.drawImage(explosion, posX, posY, this);

	}

	// Generation des différentes types de balles
	private void balleGenerator(LinkedList<Terrain> landscapes, int power) {
		// Si balle seule et vers la droite
		if (balle1 && right) {
			Destruction balle = new Destruction(power, Math.toDegrees(angle)
					+ Math.toDegrees(angleTir), posY + 5, posX + tankWidth / 2,
					BHaut - 45, BLarg, landscapes);
			balles.add(balle);
		}
		// Si balle seule et vers la gauche
		if (balle1 && !right) {
			Destruction balle = new Destruction(power, Math.toDegrees(angle)
					+ Math.toDegrees(angleTir) - 180, posY + 5, posX
					- tankWidth - 10, BHaut - 45, BLarg, landscapes);
			balles.add(balle);
		}
		// Si 3 balles et vers la gauche
		if (balleen3 && !right) {
			Destruction balle = new Destruction(power, Math.toDegrees(angle)
					+ Math.toDegrees(angleTir) - 180, posY - 35, posX
					- tankWidth - 10, BHaut - 45, BLarg, landscapes);
			balles.add(balle);
			Destruction balle1 = new Destruction(power, Math.toDegrees(angle)
					+ Math.toDegrees(angleTir) - 180, posY, posX - tankWidth
					- 50, BHaut - 45, BLarg, landscapes);
			balles.add(balle1);
			Destruction balle2 = new Destruction(power, Math.toDegrees(angle)
					+ Math.toDegrees(angleTir) - 180, posY - 15, posX
					- tankWidth - 30, BHaut - 45, BLarg, landscapes);
			balles.add(balle2);
		}

		// Si 3 balles et vers la droite

		if (balleen3 && right) {
			Destruction balle = new Destruction(power, Math.toDegrees(angle)
					+ Math.toDegrees(angleTir), posY - 35, posX + tankWidth
					+ 10, BHaut - 45, BLarg, landscapes);
			balles.add(balle);
			Destruction balle1 = new Destruction(power, Math.toDegrees(angle)
					+ Math.toDegrees(angleTir), posY, posX + tankWidth + 50,
					BHaut - 45, BLarg, landscapes);
			balles.add(balle1);
			Destruction balle2 = new Destruction(power, Math.toDegrees(angle)
					+ Math.toDegrees(angleTir), posY - 15, posX + tankWidth
					+ 30, BHaut - 45, BLarg, landscapes);
			balles.add(balle2);
		}

		// Si 2 balles et vers la gauche
		if (balleen2 && !right) {
			Destruction balle = new Destruction(power, Math.toDegrees(angle)
					+ Math.toDegrees(angleTir) - 180, posY - 35, posX
					- tankWidth - 10, BHaut - 45, BLarg, landscapes);
			balles.add(balle);
			Destruction balle1 = new Destruction(power, Math.toDegrees(angle)
					+ Math.toDegrees(angleTir) - 180, posY, posX - tankWidth
					- 50, BHaut - 45, BLarg, landscapes);
			balles.add(balle1);

		}

		// Si 2 balles et vers la droite

		if (balleen2 && right) {
			Destruction balle = new Destruction(power, Math.toDegrees(angle)
					+ Math.toDegrees(angleTir), posY - 35, posX + tankWidth
					+ 10, BHaut - 45, BLarg, landscapes);
			balles.add(balle);
			Destruction balle1 = new Destruction(power, Math.toDegrees(angle)
					+ Math.toDegrees(angleTir), posY, posX + tankWidth + 50,
					BHaut - 45, BLarg, landscapes);
			balles.add(balle1);

		}
		tir = true;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public double getAngleTir() {
		return angleTir;
	}

}
