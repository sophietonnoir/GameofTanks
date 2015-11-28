import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Keyboard extends JPanel implements KeyListener {

	public static boolean left, right, up, down, space, f, a, b, c, d, v,
			enter, x; // les touches que l'on va utiliser

	public Keyboard() {

		this.addKeyListener(this);
		this.setFocusable(true);
		this.left = false;
		this.right = false;
		this.up = false;
		this.down = false;
		this.space = false;
		this.f = false;
		this.a = false;
		this.b = false;
		this.c = false;
		this.d = false;
		this.v = false;
		this.enter = false;
		this.x = false;
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) { // quand on appuie

		switch (e.getKeyCode()) {
		case 37:
			left = true;
			break;
		case 39:
			right = true;
			break;
		case 38:
			up = true;
			break;
		case 40:
			down = true;
			break;
		case 32:
			space = true;
			break;
		case 65:
			a = true;
			break;
		case 66:
			b = true;
			break;
		case 67:
			c = true;
			break;
		case 68:
			d = true;
			break;
		case 88:
			x = true;
			break;
		case 86:
			v = true;
			break;
		case 70:
			f = true;
			break;
		case 13:
			enter = true;
			break;
		}
	}

	public void keyReleased(KeyEvent e) { // quand on relache
		switch (e.getKeyCode()) {
		case 37:
			left = false;
			break;
		case 39:
			right = false;
			break;
		case 38:
			up = false;
			break;
		case 40:
			down = false;
			break;
		case 32:
			space = false;
			break;
		case 65:
			a = false;
			break;
		case 66:
			b = false;
			break;
		case 67:
			c = false;
			break;
		case 68:
			d = false;
			break;
		case 86:
			v = false;
			break;
		case 70:
			f = false;
			break;
		case 13:
			enter = false;
			break;
		case 88:
			x = false;
			break;
		}
	}
}