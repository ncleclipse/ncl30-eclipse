package br.ufma.deinf.laws.ncleclipse.hover;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JFrame;




public class RegionTest extends JComponent{
	private JFrame j;
	private  int WIDTH = 300;
	private  int HEIGHT = 300;
	private final int X = MouseInfo.getPointerInfo().getLocation().x-5;
	private final int Y = MouseInfo.getPointerInfo().getLocation().y-5;
	private Vector<RegionValues> values;
	private Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	
	
	public RegionTest (Vector<RegionValues> values){
		 
		 j = new JFrame();
		 this.values = values;
		 j.addMouseMotionListener(new Mouseout());
		 
		 double proporcao;
		 if (d.width > d.height){
			 proporcao = (double) d.height/d.width;
			 WIDTH = 300;
			 HEIGHT = (int) Math.floor(proporcao*WIDTH);
		 }
		 else{
			 proporcao = (double) d.width/d.height;
			 HEIGHT = 300;
			 WIDTH = (int) Math.floor(proporcao*HEIGHT);
		 } 
	}
	private class Mouseout implements MouseMotionListener{
		

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			j.dispose();
			
		}
	}
	
	
	public boolean ispercent(String value){
		if(value.contains("%")){
			return true;
		}
		return false;
	}
	
	public double percentToint(String value){
		
			String[] vector = null;
			if(value !=null){
				vector= value.split(Pattern.quote("%"));
			}
			double aux = Double.parseDouble(vector[0]);
			
			return aux;
		
			
	}
	public void paintComponent (Graphics g){
		super.paintComponent (g);
		int top;
		int left;
		int width;
		int heght;
		
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		float alpha = .3f;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g2.setColor(Color.blue);
		
		int bgwidth;
		int bgheight;
		int w, h;
		
		if (ispercent (values.get(values.size()-1).getWidth()))
			w = (int) percentToint (values.get (values.size()-1).getWidth())/100 * d.width;
		else
			w = (int) percentToint (values.get (values.size()-1).getWidth());
		
		if (ispercent (values.get(values.size()-1).getHeight()))
			h = (int) percentToint (values.get (values.size()-1).getHeight())/100 * d.height;
		else
			h = (int) percentToint (values.get (values.size()-1).getHeight());
		

		double proporcao;
		

		if (w > h){
			 proporcao = (double) h/w;
			 bgwidth = WIDTH;
			 bgheight = (int) Math.floor (bgwidth * proporcao);
		}
		else{
			 proporcao = (double) w/h;
			 bgheight = HEIGHT;
			 bgwidth = (int) Math.floor (bgheight * proporcao);
		}
		
		
		j.setSize (bgwidth, bgheight);
		
		g2.drawRect(0, 0, bgwidth, bgheight);
		g2.fillRect(0, 0, bgwidth, bgheight);
		
	
		for(int i = 0; i < this.values.size()-1;i++){
			
			if (ispercent(values.get(i).getTop()))
				top = (int) (percentToint(values.get(i).getTop())/100*bgheight);
			else 
				top = (int) Math.floor((percentToint(values.get(i).getTop())/h)*bgheight);
			
			if (ispercent(values.get(i).getLeft()))
				left = (int) (percentToint(values.get(i).getLeft())/100*bgwidth);
			else
				left = (int) Math.floor((percentToint(values.get(i).getLeft())/w)*bgwidth);
				
			
			if (ispercent(values.get(i).getWidth()))
				width = (int) (percentToint(values.get(i).getWidth())/100*bgwidth);
			else
				width = (int) Math.floor((percentToint(values.get(i).getWidth())/w)*bgwidth);
			
			if (ispercent(values.get(i).getHeight()))
				heght = (int) (percentToint(values.get(i).getHeight())/100*bgheight);
			else
				heght = (int) Math.floor((percentToint(values.get(i).getHeight())/h)*bgheight);
				
			
			g2.drawRect(left,top,width,heght);
			g2.fillRect(left,top,width,heght);
		}
	
	}
	
	public String toString (){
		j.add(this);
		j.setUndecorated(true);
		j.setLocation(X, Y);
		j.setVisible(true);
				
		return "";
	}
}
