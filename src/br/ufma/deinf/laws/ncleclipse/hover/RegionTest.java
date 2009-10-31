package br.ufma.deinf.laws.ncleclipse.hover;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;
import java.util.regex.Pattern;

public class RegionTest{
	// private JFrame j;
	private int WIDTH = 300;
	private int HEIGHT = 300;
	private Vector<RegionValues> values;
	private Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	public int bgwidth;
	public int bgheight;

	public RegionTest(Vector<RegionValues> values) {
		// j = new JFrame();
		this.values = values;
		// j.addMouseListener(new Mouseout());

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
		// j.add(this);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public BufferedImage paintregions() {
		
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

		BufferedImage img = new BufferedImage(bgwidth, bgheight,
				BufferedImage.TYPE_3BYTE_BGR);
		img.createGraphics();
		

		Graphics2D g2 = (Graphics2D) img.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		float alpha = .3f;
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				alpha));
		g2.setColor(Color.blue);
		
		g2.drawRect(0, 0, bgwidth, bgheight);
		g2.fillRect(0, 0, bgwidth, bgheight);
		
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
			
			System.out.println (values.get(i).getRigth());
			
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

			g2.drawRect(left, top, width, heght);
			g2.fillRect(left, top, width, heght);

			paiheight = heght;
			paiwidth = width;
			paileft = left;
			paitop = top;
		

		}
		
		return img;

	}

	public String toString() {
		String path = this.getClass().getProtectionDomain().getCodeSource()
				.getLocation().toString().substring(5)
				+ "icons" + File.separatorChar + "tmp.png";
		return path;
	}

}
