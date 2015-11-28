import com.sun.org.apache.bcel.internal.generic.LAND;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class Windows extends JFrame {

	public JPanel container = new JPanel();
	public Board board = new Board();
	public int id_activated = 1;

	public static Keyboard key = new Keyboard();

	public Windows() {

		this.setSize(board.BLarg, board.BHaut);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		container.setBackground(Color.white);
		container.setLayout(new BorderLayout());

		container.add(key);

		container.add(board, BorderLayout.CENTER);
		this.setContentPane(container);
		this.setVisible(true);

		board.go();
	}
}
