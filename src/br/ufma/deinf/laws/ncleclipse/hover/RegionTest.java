package br.ufma.deinf.laws.ncleclipse.hover;

import java.awt.AlphaComposite;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

public class RegionTest{
	// private JFrame j;
	private int WIDTH = 300;
	private int HEIGHT = 300;
	private Vector<RegionValues> values;
	private Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	public int bgwidth;
	public int bgheight;

	public RegionTest(Vector<RegionValues> values) {
		
		this.values = values;
		

		double proporcao;
		if (d.width > d.height) {
			proporcao = (double) d.height / d.width;
			WIDTH = 300;
			HEIGHT = (int) Math.floor(proporcao * WIDTH);
		} else {
			proporcao = (double) d.width / d.height;
			HEIGHT = 300;
			WIDTH = (int) Math.floor(proporcao * HEIGHT);
		}

		RegionValues v = new RegionValues();
		v.setWidth("100%");
		v.setHeight("100%");
		this.values.add(0, v);
		
	}

	public boolean ispercent(String value) {
		if (value.contains("%")) {
			return true;
		}
		return false;
	}

	public double percentToint(String value) {

		String[] vector = null;
		if (value.endsWith("px")) {
			String tmp[] = value.split("px");
			return Double.parseDouble(tmp[0]);
		}
		if (value != null) {
			vector = value.split(Pattern.quote("%"));
		}
		double aux = Double.parseDouble(vector[0]);

		return aux;

	}

	
	
	public Point paintRegions(final Composite parent) {
		
		int top = 0;
		int left = 0;
		int width = 0;
		int heght = 0;
		int rigth = 0;
		int bottom = 0;

		int bgwidth;
		int bgheight;
		int w, h;

		if (ispercent(values.get(0).getWidth())) {
			w = (int) Math.ceil(percentToint(values.get(0).getWidth()) / 100
					* d.width);
		} else
			w = (int) Math.ceil(percentToint(values.get(0).getWidth()));

		if (ispercent(values.get(0).getHeight()))
			h = (int) Math.ceil(percentToint(values.get(0).getHeight()) / 100
					* d.height);
		else
			h = (int) Math.ceil(percentToint(values.get(0).getHeight()));

		if (ispercent(values.get(0).getLeft()))
			left = (int) Math.ceil(percentToint(values.get(0).getLeft()) / 100
					* d.width);
		else
			left = (int) Math.ceil(percentToint(values.get(0).getLeft()));

		if (ispercent(values.get(0).getRigth()))
			rigth = (int) Math.ceil(percentToint(values.get(0).getRigth())/ 100
					* d.width);
		else
			rigth = (int) Math.ceil(percentToint(values.get(0).getRigth()));

		if (ispercent(values.get(0).getBottom()))
			bottom = (int) Math.ceil(percentToint(values.get(0).getBottom())/ 100 
					* d.height);
		else
			bottom = (int) Math.ceil(percentToint(values.get(0).getBottom()));

		if (ispercent(values.get(0).getTop()))
			top = (int) Math.ceil(percentToint(values.get(0).getTop()) / 100
					* d.height);
		else
			top = (int) Math.ceil(percentToint(values.get(0).getTop()));

		if (w == -1)
			w = d.width;
		if (h == -1)
			h = d.height;
		if (top == -1)
			top = 0;
		if (bottom == -1)
			bottom = 0;
		if (rigth == -1)
			rigth = 0;
		if (left == -1)
			left = 0;
		

		double proporcao;

		if (w > h) {
			proporcao = (double) h / w;
			bgwidth = WIDTH;
			bgheight = (int) Math.floor(bgwidth * proporcao);
		} else {
			proporcao = (double) w / h;
			bgheight = HEIGHT;
			bgwidth = (int) Math.floor(bgheight * proporcao);
		}

		top *= HEIGHT;
		bottom *= HEIGHT;
		left *= WIDTH;
		rigth *= WIDTH;

		if (top == 0 ) 
			if ( bottom > 0)
				top = bgheight - bottom - heght;
			else if (bottom == 0 && !values.get(0).getRigth().equals("-1"))
				top = bgheight;
				
		if (left == 0)
			if ( rigth > 0)
				left = bgwidth - rigth - width;
			else if (rigth == 0 && !values.get(0).getRigth().equals("-1"))
				left = bgwidth;		

	
		final int bgw=bgwidth;
		final int bgh=bgheight;
		parent.addPaintListener(new PaintListener(){

			@Override
			public void paintControl(PaintEvent e) {
				Color blue = new Color(e.display, 0, 0, 255);

				e.gc.setBackground(blue);
				
				e.gc.setAlpha(80);
				e.gc.drawRectangle(0, 0, bgw, bgh);
				e.gc.fillRectangle(0, 0, bgw, bgh);
				
				
			}
			
		});
		
		
		int paiheight = bgheight;
		int paiwidth = bgwidth;
		int paileft = left;
		int paitop = top;

		
		for (int i = 1; i < values.size(); i++) {
			
			if (ispercent(values.get(i).getTop()))
				top = (int) Math.ceil(percentToint(values.get(i).getTop())
						/ 100 * paiheight);
			else
				top = (int) Math
						.ceil((percentToint(values.get(i).getTop()) / h)
								* HEIGHT);

			if (ispercent(values.get(i).getLeft()))
				left = (int) Math.ceil(percentToint(values.get(i).getLeft())
						/ 100 * paiwidth);
			else
				left = (int) Math
						.ceil((percentToint(values.get(i).getLeft()) / w)
								* WIDTH);

			if (ispercent(values.get(i).getWidth()))
				width = (int) Math.ceil(percentToint(values.get(i).getWidth())
						/ 100 * paiwidth);
			else
				width = (int) Math
						.ceil((percentToint(values.get(i).getWidth()) / w)
								* WIDTH);

			if (ispercent(values.get(i).getHeight()))
				heght = (int) Math.ceil(percentToint(values.get(i).getHeight())
						/ 100 * paiheight);
			else
				heght = (int) Math
						.ceil((percentToint(values.get(i).getHeight()) / h)
								* HEIGHT);

			if (ispercent(values.get(i).getBottom()))
				bottom = (int) Math
						.ceil(percentToint(values.get(i).getBottom()) / 100
								* paiheight);
			else
				bottom = (int) Math.ceil((percentToint(values.get(i).getBottom()) / h)
						* HEIGHT);

			if (ispercent(values.get(i).getRigth()))
				rigth = (int) (percentToint(values.get(i).getRigth()) / 100 
						* paiwidth);
			else
				rigth = (int) Math
						.ceil((percentToint(values.get(i).getRigth()) / h)
								* WIDTH);
			
			
			if (values.get(i).getWidth().equals("-1")){
				width = paiwidth;
			}
			if (values.get(i).getHeight().equals("-1")){
				heght = paiheight;
			}
			
			
			if (top == 0 && values.get(i).getTop().equals("-1"))
				if ( bottom > 0)
					top = paiheight - heght - bottom;
				else if (bottom == 0 && !values.get(i).getBottom().equals("-1")) top = paiheight - heght;
			
			if (left == 0 && values.get(i).getLeft().equals("-1"))
				if (rigth > 0)
					left = paiwidth - width - rigth;
				else if (rigth == 0 && !values.get(i).getRigth().equals("-1")) left = paiwidth - width;
			
		
			if (heght + top >= paiheight)
				heght = paiheight - top; 
			
			if (width + left >= paiwidth)
				width = paiwidth - left;
			
			
			if (top < 0){
				heght += top;
				if (heght < 0) heght = 0;
				top = 0;
			}
			
			if (left < 0){
				width += left;
				if (width < 0) width = 0;
				left = 0;
			}
			
			top += paitop;
			left += paileft;

		//	g2.drawRect(left, top, width, heght);
			//g2.fillRect(left, top, width, heght);
			final int l=left;
			final int t=top;
			final int w1=width;
			final int h1=heght;
			parent.addPaintListener(new PaintListener(){

				@Override
				public void paintControl(PaintEvent e) {
					Color blue = new Color(e.display, 0, 0, 255);

					e.gc.setBackground(blue);
					e.gc.setAlpha(40);
					e.gc.drawRectangle(l, t, w1, h1);
					e.gc.fillRectangle(l, t, w1, h1);
					
				}
				
			});

			paiheight = heght;
			paiwidth = width;
			paileft = left;
			paitop = top;
		

		}
		
		return new Point (bgw, bgh);

	}

	public String toString() {
	
		return "";
	}

}