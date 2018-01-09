package cosmicRayDetector;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Test extends JPanel {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	/*
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame();
		frame = new JFrame ("Foo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0, 0, 200, 200);
		frame.setVisible(true);
		
		Test panel = new Test();
		frame.getContentPane().add(panel);
	}*/
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.RED);
		g.fillRect(20, 20, 100, 100);
	}

}
