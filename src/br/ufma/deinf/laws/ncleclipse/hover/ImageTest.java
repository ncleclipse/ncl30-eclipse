package br.ufma.deinf.laws.ncleclipse.hover;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class ImageTest extends JComponent {
	private Image img;
	private int WIDTH;
	private int HEIGHT;
	private JFrame j;
	private boolean valido;

	public ImageTest(String filename) {
		try {
			img = ImageIO.read(new File(filename));
			j = new JFrame();
			j.addMouseMotionListener(new Mouseout());
			valido = true;
			double proporcao;
			int height = img.getHeight(null);
			int width = img.getWidth(null);
			if (height > width){
				proporcao = (double)width/height;
				HEIGHT = 300;
				WIDTH = (int) Math.floor(HEIGHT * proporcao);
			}
			else{
				proporcao = height/width;
				WIDTH = 300;
				HEIGHT = (int) Math.floor(WIDTH * proporcao);
			}
			
		} catch (IOException e) {
			valido = false;

		}
	}

	public boolean arquivoValido() {
		return valido;
	}

	private class Mouseout implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseMoved(MouseEvent e) {
			j.dispose();

		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
	}

	public String toString() {

		j.add(this);
		j.setUndecorated(true);
		j.setSize(WIDTH, HEIGHT);
		Point p = MouseInfo.getPointerInfo().getLocation();
		j.setLocation(p.x - 5, p.y - 5);
		j.setVisible(true);

		return "";
	}
}
