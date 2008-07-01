/******************************************************************************
Este arquivo � parte da implementa��o do ambiente declarativo do middleware 
Ginga (Ginga-NCL).

Direitos Autorais Reservados (c) 1989-2007 PUC-Rio/Laborat�rio TeleM�dia

Este programa � software livre; voc� pode redistribu�-lo e/ou modific�-lo sob 
os termos da Licen�a P�blica Geral GNU vers�o 2 conforme publicada pela Free 
Software Foundation.

Este programa � distribu�do na expectativa de que seja �til, por�m, SEM 
NENHUMA GARANTIA; nem mesmo a garantia impl�cita de COMERCIABILIDADE OU 
ADEQUA��O A UMA FINALIDADE ESPEC�FICA. Consulte a Licen�a P�blica Geral do 
GNU vers�o 2 para mais detalhes. 

Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral do GNU vers�o 2 junto 
com este programa; se n�o, escreva para a Free Software Foundation, Inc., no 
endere�o 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA. 

Para maiores informa��es:
ncl @ telemidia.puc-rio.br
http://www.ncl.org.br
http://www.ginga.org.br
http://www.telemidia.puc-rio.br
******************************************************************************
This file is part of the declarative environment of middleware Ginga (Ginga-NCL)

Copyright: 1989-2007 PUC-RIO/LABORATORIO TELEMIDIA, All Rights Reserved.

This program is free software; you can redistribute it and/or modify it under 
the terms of the GNU General Public License version 2 as published by
the Free Software Foundation.

This program is distributed in the hope that it will be useful, but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License version 2 for more 
details.

You should have received a copy of the GNU General Public License version 2
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA

For further information contact:
ncl @ telemidia.puc-rio.br
http://www.ncl.org.br
http://www.ginga.org.br
http://www.telemidia.puc-rio.br
*******************************************************************************/

package br.pucrio.telemidia.ginga.core.player.procedural.lua.canvas;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.keplerproject.luajava.LuaState;

import br.pucrio.telemidia.ginga.core.player.procedural.lua.LuaPlayer;

public class LuaCanvas extends Drawable{
	private static final String FONTSTYLE_BOLD = "bold";
	private static final String FONTSTYLE_ITALIC = "italic";
	private static final String FONTSTYLE_BOLD_ITALIC = "bold-italic";
	
	private LuaState L;
	
	private int width;
	private int height;
	
	private String imagePath;
	
	private Font currentFont;
	private Color currentColor;
	private Rectangle clip;
    private Image image = null;
	
    //private VolatileImage volImage;
    //private Image origImage;
    
	private List<Drawable> bufferedElements;
	//private List<Drawable> elementsBeingDraw;
	
	private Component awtComponent;
	private Component backgroundComponent;
	
	private BufferedImage buffer; 
	private boolean flushed = false;
	
	protected LuaCanvas(LuaState L, Component backgroundComponent){
		super(null,0,0);
		this.L = L;
		//this.elementsBeingDraw = new Vector<Drawable>();
		this.backgroundComponent = backgroundComponent;
		this.bufferedElements = new Vector<Drawable>();
		currentColor = new Color(0,0,0,255);
		currentFont = new Font(null,Font.PLAIN,16);
		imagePath = null;
		buffer = null;
		clip=null;
	}
	
	public LuaCanvas(Component awtComponent, LuaState L){
		this(awtComponent.getWidth(), awtComponent.getHeight(),L,awtComponent);
		this.awtComponent = awtComponent;
	}
	
	public LuaCanvas(int width, int height, LuaState L, Component backgroundComponent){
		this(L,backgroundComponent);
		this.width = width;
		this.height = height;
	}
	
	public LuaCanvas(String imagePath, LuaState L, Component backgroundComponent){
		this(L,backgroundComponent);
		this.imagePath = imagePath;
		File file = new File(imagePath);
		if(!file.isAbsolute()){
			this.imagePath = LuaPlayer.getPlayer(L).getRelativePath() 
				+ "/" + imagePath;
		}
		file = new File(this.imagePath);
		if(!file.exists())
			System.err.println(this.imagePath +" NOT exists!");
		ImageIcon image = new ImageIcon(this.imagePath);
		this.width=image.getIconWidth();
		this.height=image.getIconHeight();
	}
	
	public LuaCanvas createNew(int width, int height){
		return new LuaCanvas(width,height,L,this.backgroundComponent);
	}
	
	public LuaCanvas createNew(String imagePath){
		return new LuaCanvas(imagePath,L,this.backgroundComponent);
	}
	
	public synchronized void setColor(int r, int g, int b, int a){
		currentColor = new Color(r,g,b,a);
	}
	
	private static String BLACK_COLOR="black";
	private static String RED_COLOR="red";
	private static String GREEN_COLOR="green";
	private static String BLUE_COLOR="blue";
	
	public synchronized void setColor(String color){
		if(color.equals(BLACK_COLOR))
			currentColor = Color.BLACK;
		else if(color.equals(BLUE_COLOR))
			currentColor = Color.BLUE;
		else if(color.equals(RED_COLOR))
			currentColor = Color.RED;
		else if(color.equals(GREEN_COLOR))
			currentColor = Color.GREEN;
	}
	
	public Color getColor(){
		return this.currentColor;
	}
	
	public synchronized void compose(int x, int y, LuaCanvas srcCanvas){
		srcCanvas.setPosition(new Point(x,y));
		srcCanvas.setParentCanvas(this);
		this.bufferedElements.add(srcCanvas);
	}
	
	public synchronized void setSize(int width, int height){
		this.width = width;
		this.height = height;
	}
	
	public synchronized Dimension getSize(){
		if(awtComponent!=null){
			return awtComponent.getSize();
		}else
			return new Dimension(width,height);
	}
	
	public void setFont(String name, double size, String style){
		 if(style == null)
			 this.currentFont = new Font(name,Font.PLAIN,(int) size);
		 else if(style.equals(FONTSTYLE_BOLD))
			 this.currentFont = new Font(name,Font.BOLD,(int) size);
		 else if(style.equals(FONTSTYLE_ITALIC))
			 this.currentFont= new Font(name,Font.ITALIC,(int) size);
		 else  if(style.equals(FONTSTYLE_BOLD_ITALIC))
			 this.currentFont= new Font(name,Font.BOLD+Font.ITALIC,(int) size);
	}
	
	public Font getFont(){
		return currentFont;
	}
	
	public synchronized void flush(){
		
		/*for(Drawable element : bufferedElements){
			elementsBeingDraw.add(element);
			if(element instanceof LuaCanvas)
				((LuaCanvas)element).flush();
		}
		bufferedElements.clear();*/
		for(Drawable element : bufferedElements){
			if(element instanceof LuaCanvas)
				((LuaCanvas)element).flush();
		}
		flushed=true;
		if(awtComponent!=null){
			awtComponent.repaint();
		}
	}
	
	public synchronized void drawText(int x, int y, String text){
		LuaText testToDraw = new LuaText(this,x,y,currentFont,currentColor,text);
		bufferedElements.add(testToDraw);
	}
	
	public synchronized void drawRect(int x, int y, int w, int h){
		LuaRectangle rect = new LuaRectangle(this,x,y,w,h, this.currentColor,false);
		bufferedElements.add(rect);
	}
	
	public synchronized void drawPolygon(int[] pointsX, int[] pointsY, String mode){
		bufferedElements.add(new LuaPolygon(this,this.getColor(),pointsX,pointsY,mode));
	}
	
	public synchronized void drawEllipse(int xc, int yc, int width, 
			int height, int ang_start, int ang_end){	
		bufferedElements.add(new LuaEllipse(this,xc-(width/2),yc-(height/2),width,
				height,ang_start,ang_end, this.getColor(),false));
	}
	
	public synchronized void fillEllipse(int xc, int yc, int width, 
			int height, int ang_start, int ang_end){	
		bufferedElements.add(new LuaEllipse(this,xc-(width/2),yc-(height/2),width,
				height,ang_start,ang_end, this.getColor(),true));
	}
	
	public synchronized void drawLine(int x1, int y1, int x2, int y2){
		LuaLine lineToDraw = new LuaLine(this,x1,y1,x2,y2,currentColor);
		bufferedElements.add(lineToDraw);
	}
	
	public synchronized void drawPixel(int x, int y, int r, int g, int b, int a){
		Color color = new Color(r,g,b,a);
		LuaPixel pixel = new LuaPixel(this,x,y,color);
		bufferedElements.add(pixel);
	}
	
	public synchronized Rectangle measureText(String text){
		/*Rectangle rect;
		Graphics g;
		try{
			if(graphics==null){
				Container cont = new Container();
				g = cont.getGraphics();
			}else
				g = this.graphics;
			rect = g.getFontMetrics(this.currentFont).getStringBounds(text, 0, text.length(), g).getBounds();
		}catch(Exception ex){
			rect = new Rectangle(0,0,0,0);
			ex.printStackTrace();
		}*/
		FontMetrics fontMetrics = this.backgroundComponent.getFontMetrics(this.currentFont);
	    
        int width = fontMetrics.stringWidth(text);
        int height = fontMetrics.getHeight();
		return new Rectangle(0,0,width,height);
	}
	
	public Rectangle getClip(){
		if(clip==null){
			Point point = this.getPosition();
			clip = new Rectangle(point.x,point.y,this.width,this.height);
		}
		return clip;
		/*Graphics g;
		if(graphics==null){
			Container cont = new Container();
			g = cont.getGraphics();
		}else
			g = this.graphics;*/
	}
	
	public void setClip(int x, int y, int w, int h){
		if(this.clip == null)
			this.clip = new Rectangle(x,y,w,h);
		else
			this.clip.setBounds(x,y,w,h);
	}
	
	public synchronized Color getPixelValue(int x, int y){
		/*Graphics2D g;
		if(graphics==null){
			Container cont = new Container();
			g = (Graphics2D) cont.getGraphics();
			g.setColor(this.currentColor);
			g.setFont(this.currentFont);
			g = (Graphics2D)cont.getGraphics();
			this.draw(g);
		}else{
			g = (Graphics2D)this.graphics;
		}*/
		//BufferedImage buffImage =  g.getDeviceConfiguration().createCompatibleImage(this.width, this.height);
		//Graphics buffGraphics=buffImage.getGraphics();
		//this.draw(buffGraphics);
		if(buffer != null){
			int rgb = this.buffer.getRGB(x, y);
			return new Color(rgb);
		}else
			return Color.black;
		//((Graphics2D)graphics).
		//PixelGrabber grabber = new PixelGrabber()
	}
	
	public synchronized void drawImage(Graphics g){
		if(image == null){
			MediaTracker tracker;
			Container c = new Container();
			image = c.getToolkit().getImage(imagePath);
			tracker = new MediaTracker(c);
			tracker.addImage(image, 0);
			try {
				tracker.waitForID(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		g.drawImage(image, 0, 0, this.width, this.height, null);
		//g.dispose();
	}
	
	/*public VolatileImage drawVolatileImage(Graphics2D g) {
		Point position = this.getPosition();
		final int MAX_TRIES = 100;
		for (int i=0; i<MAX_TRIES; i++) {
			if (volImage != null) {
//				Draw the volatile image
				g.drawImage(volImage, position.x, position.y, null);

//				Check if it is still valid
				if (!volImage.contentsLost()) {
					return volImage;
				}
			} else {
//				Create the volatile image
				volImage = g.getDeviceConfiguration().createCompatibleVolatileImage(
						origImage.getWidth(null), origImage.getHeight(null));
			}

//			Determine how to fix the volatile image
			switch (volImage.validate(g.getDeviceConfiguration())) {
			case VolatileImage.IMAGE_OK:
//				This should not happen
				break;
			case VolatileImage.IMAGE_INCOMPATIBLE:
//				Create a new volatile image object;
//				this could happen if the component was moved to another device
				volImage.flush();
				volImage = g.getDeviceConfiguration().createCompatibleVolatileImage(
						origImage.getWidth(null), origImage.getHeight(null));
			case VolatileImage.IMAGE_RESTORED:
//				Copy the original image to accelerated image memory
				Graphics2D gc = (Graphics2D)volImage.createGraphics();
				gc.drawImage(origImage, 0, 0, null);
				gc.dispose();
				break;
			}
		}

//		The image failed to be drawn after MAX_TRIES;
//		draw with the non-accelerated image
		g.drawImage(origImage, position.x, position.y, null);
		return volImage;
	}*/
	
	public synchronized void fillRect(int x, int y, int w, int h){
		LuaRectangle rect = new LuaRectangle(this,x,y,w,h,this.currentColor,true);
		bufferedElements.add(rect);
	}
	
	public synchronized void draw(Graphics originalGraphics) {
		Point position = this.getPosition();

		Graphics g = originalGraphics.create(position.x, position.y, this.width, this.height);
		if(clip != null){
			this.setClip(this.clip.x,this.clip.y, this.clip.width, this.clip.height);
		}
		
		if(flushed){
			if(this.width > 0 && this.height >0){
				BufferedImage newBuffer =  ((Graphics2D)originalGraphics).getDeviceConfiguration().createCompatibleImage(this.width, this.height,Transparency.TRANSLUCENT);
				Graphics buffGraphics=newBuffer.createGraphics();
				if(buffer!=null){
					buffer.flush();
					MediaTracker tracker = new MediaTracker(new Container());
					tracker.addImage(buffer, 0);
					try {
						tracker.waitForID(0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					buffGraphics.drawImage(buffer, 0, 0, buffer.getWidth(), buffer.getHeight(), null);
				}
				if(imagePath!=null)
					drawImage(buffGraphics);
				for(Drawable elements : bufferedElements){
					elements.draw(buffGraphics);
				}
				buffGraphics.dispose();
				bufferedElements.clear();
				flushed=false;
				buffer = newBuffer;
			}
		}
		
		
		if(buffer != null){
				buffer.flush();
				MediaTracker tracker = new MediaTracker(new Container());
				tracker.addImage(buffer, 0);
				try {
					tracker.waitForID(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				g.drawImage(buffer, 0, 0, buffer.getWidth(), buffer.getHeight(), null);
		}
	}	
}
