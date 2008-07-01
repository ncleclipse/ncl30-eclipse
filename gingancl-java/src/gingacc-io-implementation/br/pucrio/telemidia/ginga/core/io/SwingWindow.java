/******************************************************************************
Este arquivo é parte da implementação do ambiente declarativo do middleware 
Ginga (Ginga-NCL).

Direitos Autorais Reservados (c) 1989-2007 PUC-Rio/Laboratório TeleMídia

Este programa é software livre; você pode redistribuí-lo e/ou modificá-lo sob 
os termos da Licença Pública Geral GNU versão 2 conforme publicada pela Free 
Software Foundation.

Este programa é distribuído na expectativa de que seja útil, porém, SEM 
NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE OU 
ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública Geral do 
GNU versão 2 para mais detalhes. 

Você deve ter recebido uma cópia da Licença Pública Geral do GNU versão 2 junto 
com este programa; se não, escreva para a Free Software Foundation, Inc., no 
endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA. 

Para maiores informações:
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

package br.pucrio.telemidia.ginga.core.io;

import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;

import br.org.ginga.core.io.IColor;
import br.org.ginga.core.io.ISurface;
import br.org.ginga.core.io.IWindow;

public class SwingWindow extends JFrame implements IWindow{
	private IColor borderColor;
	private int borderWidth;
	private boolean isTransitionRunning;
	private boolean isShowEffect;
	private int alpha;
	
	private Object resizingLock = new Object();
	
	public SwingWindow() throws HeadlessException {
		super();
		initialize();
	}

	public SwingWindow(GraphicsConfiguration arg0) {
		super(arg0);
		initialize();
	}

	public SwingWindow(String arg0, GraphicsConfiguration arg1) {
		super(arg0, arg1);
		initialize();
	}

	public SwingWindow(String arg0) throws HeadlessException {
		super(arg0);
		initialize();
	}

	private void initialize(){
		borderColor = null;
		isTransitionRunning = false;
		isShowEffect = false;
		alpha=255;
		setUndecorated(true);
		this.setLayout(null);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1754167770830712395L;

	public void addChildSurface(ISurface s) {
		if(s instanceof Component)
			this.add((Component)s);
	}

	public void busy() {
		// TODO Auto-generated method stub
		
	}

	public void clear() {
		this.removeAll();
	}

	public void draw() {
		super.repaint();
	}

	public IColor getBgColor() {
		return this.borderColor;
	}

	public boolean getFit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return super.getX();
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return super.getY();
	}
	
	

	public int getH() {
		return this.getHeight();
	}

	public int getW() {
		return this.getWidth();
	}

	public Object getWidget() {
		// TODO Auto-generated method stub
		return this;
	}

	public Object getWidgetSurface() {
		/*for(int i=0;i < this.getComponentCount();i++){
			Component comp = this.getComponent(i);
			if(comp instanceof ISurface)
				return comp;
		}*/
		return super.getContentPane();
	}

	public void hide(int transitionType, int transitionSubType, double dur,
			double startProgress, double endProgress, short direction,
			int fadeColor) {
	}

	public boolean isDeleting() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isTransitioning() {
		// TODO Auto-generated method stub
		return false;
	}

	public void lower() {
		// TODO Auto-generated method stub
		
	}

	public void lowerToBottom() {
		// TODO Auto-generated method stub
		
	}

	public void raise() {
		this.toFront();
	}

	public void raiseToTop() {
		this.toFront();
	}

	public boolean removeChildSurface(ISurface s) {
		if(s instanceof SwingSurface){
			this.remove((Component)s);
			return true;
		}
		return false;
	}

	public void renderFrom(ISurface s) {
		// TODO Auto-generated method stub
		
	}

	public void setBackgroundColor(int r, int g, int b) {
		this.setBackground(new GingaColor(r,g,b,alpha));
	}

	public void setBorder(int r, int g, int b) {
		this.borderColor = new GingaColor(r,g,b);
	}

	public void setBorder(int r, int g, int b, int alpha) {
		this.borderColor = new GingaColor(r,g,b,alpha);
	}

	public void setBorder(int r, int g, int b, int alpha, int width) {
		this.setBorder(r, g, b,alpha);
		this.borderWidth = width;
	}

	public void setBorder(int color) {
		this.borderColor = new GingaColor(color,true);
	}

	public void setBorder(int color, int width) {
		this.setBorder(color);
		this.borderWidth = width;
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		//super.resize(width, height);
		this.setBounds(this.getX(), this.getY(), width, height);
	}
	
	protected void resizeWithSurface(int width, int height) {
		this.resize(width, height);
		for(Component comp : super.getComponents()){
			if(comp instanceof SwingSurface){
				((SwingSurface)comp).setBounds(comp.getX(), comp.getY(), width, height);
			}
		}
	}
	
	
	public void moveTo(int x, int y) {
		this.setBounds(x, y, this.getW(), this.getH());
	}

	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
	}

	public void setBounds(int posX, int posY, int w, int h, double dur,
			double by) {
		if (dur == 0 && by == 0) {
			this.setBounds(posX, posY, w, h);
		} else {
			if (posX != this.getX()) {
				final AnimeInfo anim = new AnimeInfo();
				anim.coord = posX;
				anim.dur = dur;
				anim.by = by;
				anim.win = this;
				Thread resizeX = new Thread(){
					public void run(){
						animeX(anim);
					}
				};
				resizeX.start();
			}

			if (posY != this.getY()) {
				final AnimeInfo anim = new AnimeInfo();
				anim.coord = posY;
				anim.dur = dur;
				anim.by = by;
				anim.win = this;

				Thread resizeY = new Thread(){
					public void run(){
						animeY(anim);
					}
				};
				resizeY.start();
			}

			if (w != this.getW()) {
				final AnimeInfo anim = new AnimeInfo();
				anim.coord = w;
				anim.dur = dur;
				anim.by = by;
				anim.win = this;

				Thread resizeW = new Thread(){
					public void run(){
						animeW(anim);
					}
				};
				resizeW.start();
			}

			if (h != this.getH()) {
				final AnimeInfo anim = new AnimeInfo();
				anim.coord = h;
				anim.dur = dur;
				anim.by = by;
				anim.win = this;

				Thread resizeH = new Thread(){
					public void run(){
						animeH(anim);
					}
				};
				resizeH.start();
			}
		}
	}
	
	public void animeX(AnimeInfo anim) {
		IWindow win;
		double initTime, time;
		int x;

		
		anim.dur = anim.dur * 1000;
		win = anim.win;
		if (win == null || win.isDeleting()) {
			return;
		}
		x = win.getX();
		if (x > anim.coord) {
			x--;
		} else if (x > anim.coord) {
			x++;
		}

		initTime = System.currentTimeMillis();
		while (win != null && x != anim.coord && x >= 0) {
			if (win.isDeleting()) {
				return;
			}

			synchronized(resizingLock){
				time = System.currentTimeMillis();
				win.moveTo(x, win.getY());
				win.validate();
			}

			if (x < anim.coord) {
				x = SwingWindow.getNextStepValue(
						x, anim.coord,
						1, time, initTime, anim.dur);

			} else if (x > anim.coord) {
				x = SwingWindow.getNextStepValue(
						x, anim.coord,
						-1, time, initTime, anim.dur);
			}
		}

		if (x < 0 && win != null) {
			if (win.isDeleting()) {
				return;
			}

			synchronized(this){
				win.moveTo(anim.coord, win.getY());
				win.validate();
			}
		}
	}
	
	public void animeY(AnimeInfo anim) {
		IWindow win;
		double initTime, time;
		int y;

		anim.dur = anim.dur * 1000;
		win = anim.win;
		if (win == null || win.isDeleting()) {
			return;
		}
		y = win.getY();
		if (y > anim.coord) {
			y--;
		} else if (y > anim.coord) {
			y++;
		}

		initTime = System.currentTimeMillis();
		while (win != null&& y != anim.coord && y >= 0) {
			if (win.isDeleting()) {
				return;
			}

			synchronized(resizingLock){
				time = System.currentTimeMillis();
				win.moveTo(win.getX(), y);
				win.validate();
			}

			if (y < anim.coord) {
				y = SwingWindow.getNextStepValue(
						y, anim.coord,
						1, time, initTime, anim.dur);

			} else if (y > anim.coord) {
				y = SwingWindow.getNextStepValue(
						y, anim.coord,
						-1, time, initTime, anim.dur);
			}
		}

		if (y < 0 && win != null) {
			if (win.isDeleting()) {
				return;
			}

			win.moveTo(win.getX(), anim.coord);
			win.validate();
		}
		return;
	}
	
	public void animeW(AnimeInfo anim) {
		IWindow win;
		double initTime, time;
		int w;

		
		anim.dur = anim.dur * 1000;
		win = anim.win;
		if (win == null || win.isDeleting()) {
			return;
		}
		w = win.getW();
		if (w > anim.coord) {
			w--;
		} else if (w > anim.coord) {
			w++;
		}

		initTime = System.currentTimeMillis();
		while (win != null && w != anim.coord && w > 0) {
			if (win.isDeleting()) {
				return;
			}

			synchronized(resizingLock){
				time = System.currentTimeMillis();
				//win.resize(w, win.getH());
				((SwingWindow)win).resizeWithSurface(w, win.getH());
				win.validate();
			}

			if (w < anim.coord) {
				w = SwingWindow.getNextStepValue(
						w, anim.coord,
						1, time, initTime, anim.dur);

			} else if (w > anim.coord) {
				w = SwingWindow.getNextStepValue(
						w, anim.coord,
						-1, time, initTime, anim.dur);
			}
		}

		if (w < 0 && win != null) {
			if (win.isDeleting()) {
				return;
			}
			win.resize(anim.coord, win.getH());
			win.validate();
		}
		return;
	}
	
	public void animeH(AnimeInfo anim) {
		
		IWindow win;
		double initTime, time;
		int h;

		anim.dur = anim.dur * 1000;
		win = anim.win;
		if (win == null || win.isDeleting()) {
			return;
		}
		h = win.getH();
		if (h > anim.coord) {
			h--;
		} else if (h > anim.coord) {
			h++;
		}

		initTime = System.currentTimeMillis();
		while (win != null && h != anim.coord && h > 0) {
			if (win.isDeleting()) {
				return;
			}
			
			synchronized(resizingLock){
				time = System.currentTimeMillis();
				((SwingWindow)win).resizeWithSurface(win.getW(), h);
				win.validate();
			}

			if (h < anim.coord) {
				h = SwingWindow.getNextStepValue(
						h, anim.coord,
						1, time, initTime, anim.dur);

			} else if (h > anim.coord) {
				h = SwingWindow.getNextStepValue(
						h, anim.coord,
						-1, time, initTime, anim.dur);
			}
		}

		if (h < 0 && win != null) {
			if (win.isDeleting()) {
				return;
			}
			win.resize(win.getW(), anim.coord);
			win.validate();
		}
	}
	
	private static int getNextStepValue(
			int currentStepValue,
			int value,
			int factor, double time, double initTime, double dur) {

		int stepSize = 1;
		int numSteps, nextStepValue;
		double elapsedTime, meanStepTime, stepElapsedTime;

		if (factor > 0) {
			if (((value - currentStepValue) % stepSize) != 0) {
				numSteps = ((value - currentStepValue)/stepSize) + 1;

			} else {
				numSteps = ((value - currentStepValue)/stepSize);
			}

		} else {
			if ((currentStepValue % stepSize) != 0) {
				numSteps = currentStepValue/stepSize + 1;

			} else {
				numSteps = currentStepValue/stepSize;
			}
		}

		stepElapsedTime = System.currentTimeMillis() - time;
		elapsedTime = System.currentTimeMillis() - initTime;

		if (elapsedTime >= dur) {
			return -1;
		}

		meanStepTime = (dur - elapsedTime) / numSteps;

		if (stepElapsedTime <= meanStepTime) {
			nextStepValue = currentStepValue + (factor * stepSize);
			try {
				Thread.sleep((int)((meanStepTime - stepElapsedTime) /* * 1000*/));
			} catch (InterruptedException e) {
				System.err.println("[ERROR] " + SwingWindow.class.getCanonicalName()
						+ " getNextStepValue");
				e.printStackTrace();
			}
		} else {
			nextStepValue = currentStepValue + (factor * (stepSize + (int)(
					stepElapsedTime / meanStepTime) * stepSize));
		}
		return nextStepValue;
	}

	public void setColor(int r, int g, int b) {
		this.setForeground(new Color(r,g,b));
	}

	public void setColor(int r, int g, int b, int alpha) {
		this.setForeground(new Color(r,g,b,alpha));
	}

	public void setColorKey(int r, int g, int b) {
		// TODO Auto-generated method stub
		
	}

	public void setFit(boolean fitTo) {
		
	}

	public void setOpacity(int alpha) {
		this.alpha = alpha;
	}

	public void setTransparencyValue(int alpha) {
		this.alpha = alpha;
	}

	public void show(int transitionType, int transitionSubType, double dur,
			double startProgress, double endProgress, short direction,
			int fadeColor) {
		// TODO Improve
		this.setVisible(true);
	}
	
	private class AnimeInfo {
		IWindow win;
		int coord;
		double dur;
		double by;
	};
	
	private class AnimeInformation {
		IWindow win;
		int coordX;
		int coordY;
		int coordW;
		int coordH;
		double dur;
		double by;
	};

	public void animate(AnimeInformation anim) {
		IWindow win;
		double initTime, time;
		int x,y,h,w;

		
		anim.dur = anim.dur * 1000;
		win = anim.win;
		if (win == null || win.isDeleting()) {
			return;
		}
		x = win.getX();
		if (x > anim.coordX) {
			x--;
		} else if (x > anim.coordX) {
			x++;
		}
		
		y = win.getY();
		if (y > anim.coordY) {
			y--;
		} else if (y > anim.coordY) {
			y++;
		}
		
		h = win.getH();
		if (h > anim.coordH) {
			h--;
		} else if (h > anim.coordH) {
			h++;
		}
		
		w = win.getW();
		if (w > anim.coordW) {
			w--;
		} else if (w > anim.coordW) {
			w++;
		}

		initTime = System.currentTimeMillis();
		while (win != null && x != anim.coordX && x >= 0
				 && y != anim.coordY && y >= 0
				 && x != anim.coordX && x >= 0
				 && x != anim.coordX && x >= 0) {
			if (win.isDeleting()) {
				return;
			}

			time = System.currentTimeMillis();
			win.moveTo(x, y);
			win.resize(w, h);
			win.validate();

			if (x < anim.coordX) {
				x = SwingWindow.getNextStepValue(
						x, anim.coordX,
						1, time, initTime, anim.dur);

			} else if (x > anim.coordX) {
				x = SwingWindow.getNextStepValue(
						x, anim.coordX,
						-1, time, initTime, anim.dur);
			}
			
			if (y < anim.coordY) {
				y = SwingWindow.getNextStepValue(
						y, anim.coordY,
						1, time, initTime, anim.dur);

			} else if (y > anim.coordY) {
				y = SwingWindow.getNextStepValue(
						y, anim.coordY,
						-1, time, initTime, anim.dur);
			}
			
			if (h < anim.coordH) {
				h = SwingWindow.getNextStepValue(
						h, anim.coordH,
						1, time, initTime, anim.dur);

			} else if (h > anim.coordH) {
				h = SwingWindow.getNextStepValue(
						h, anim.coordH,
						-1, time, initTime, anim.dur);
			}
			if (w < anim.coordW) {
				w = SwingWindow.getNextStepValue(
						w, anim.coordW,
						1, time, initTime, anim.dur);

			} else if (w > anim.coordW) {
				w = SwingWindow.getNextStepValue(
						w, anim.coordW,
						-1, time, initTime, anim.dur);
			}
		}

		if (x < 0 && win != null) {
			if (win.isDeleting()) {
				return;
			}

			win.moveTo(anim.coordX, win.getY());
			win.validate();
		}
	}
}
