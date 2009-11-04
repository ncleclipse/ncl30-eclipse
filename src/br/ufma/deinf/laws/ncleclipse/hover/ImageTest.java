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
	private JFrame j;
	private boolean valido;
	private String path;
	public ImageTest(String filename) {
		this.path=filename;
		

		
	}

	

	
	
	


	public String toString() {

		
		return this.path;
	}
}