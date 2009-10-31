package br.ufma.deinf.laws.ncleclipse.hover;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
	private boolean valido;

	public ImageTest(String filename) {
		try {
			img = ImageIO.read(new File(filename));
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
				proporcao = (double)height/width;
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

}
