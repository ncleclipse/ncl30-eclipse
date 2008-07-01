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

package br.pucrio.telemidia.ginga.core.io;

import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.dvb.event.EventManager;
import org.havi.ui.event.HRcEvent;

import br.org.ginga.core.io.ISurface;
import br.org.ginga.core.io.IWindow;

public class GFXManager {
	private static GFXManager _instance;
	private static EventProcessor eventProcessor;
	
	private GFXManager(){
		eventProcessor = new EventProcessor();
	}
	
	public void clearWidgetsPools(){
	}
	
	public static GFXManager getInstance(){
		if(_instance == null){
			_instance = new GFXManager();
		}
		return _instance;
	}
	
	public int getDeviceWidth(){
		return 0;
	}
	
	public int getDeviceHeight(){
		return 0;
	}
	
	public void setLayerColorKey(int r, int g, int b){
		
	}
	public IWindow createWindow(String dsc){
		IWindow window = new SwingWindow();
		((SwingWindow)window).addKeyListener(eventProcessor);
		return window;
	}
	
	public void releaseWindow(IWindow win){
		if(win instanceof SwingWindow)
			((SwingWindow)win).removeKeyListener(eventProcessor);
	}
	
	public ISurface createSurface(String dsc){
		ISurface surface = new SwingSurface();
		//((SwingSurface)surface).addKeyListener(new EventProcessor());
		return surface;
	}
	
	public void releaseSurface(ISurface sur){
		
	}

	public EventQueue createInputEventBuffer(){
		return null;
	}
	
	public void releaseInputBuffer(EventQueue buffer){
	
	}
	
	private class EventProcessor implements MouseListener, KeyListener{

		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void keyPressed(KeyEvent e) {
			KeyEvent newEvent;

			switch (e.getKeyCode()) {
			case KeyEvent.VK_F1: // RED
			case KeyEvent.VK_NUM_LOCK:
				newEvent = new KeyEvent(e.getComponent(), e.getID(),
						e.getWhen(), e.getModifiers(), HRcEvent.VK_COLORED_KEY_0, '?');
				break;
			case KeyEvent.VK_F2: // GREEN
			case KeyEvent.VK_DIVIDE:
				newEvent = new KeyEvent(e.getComponent(), e.getID(),
						e.getWhen(), e.getModifiers(), HRcEvent.VK_COLORED_KEY_1, '?');
				break;
			case KeyEvent.VK_F3: // YELLOW
			case KeyEvent.VK_MULTIPLY:
				newEvent = new KeyEvent(e.getComponent(), e.getID(),
						e.getWhen(), e.getModifiers(), HRcEvent.VK_COLORED_KEY_2, '?');
				break;
			case KeyEvent.VK_F4: // BLUE
			case KeyEvent.VK_MINUS:
				newEvent = new KeyEvent(e.getComponent(), e.getID(),
						e.getWhen(), e.getModifiers(), HRcEvent.VK_COLORED_KEY_3, '?');
				break;

			case KeyEvent.VK_M:
			case KeyEvent.VK_PLUS:
				newEvent = new KeyEvent(e.getComponent(), e.getID(),
						e.getWhen(), e.getModifiers(), HRcEvent.VK_GUIDE, '?');
				break;
			case KeyEvent.VK_I:
			case KeyEvent.VK_PERIOD:
				newEvent = new KeyEvent(e.getComponent(), e.getID(),
						e.getWhen(), e.getModifiers(), HRcEvent.VK_INFO, '?');
				break;

			default:
				newEvent = e;
			break;
			}
			EventManager.getInstance().fireKeyEvent(
					e.getComponent(), newEvent);
			InputEventManager.getInstance().dispatchEvent(newEvent);
		}

		public void keyReleased(KeyEvent e) {
			/*KeyEvent newEvent;

			switch (e.getKeyCode()) {
			case KeyEvent.VK_F1: // RED
			case KeyEvent.VK_NUM_LOCK:
				newEvent = new KeyEvent(e.getComponent(), e.getID(),
						e.getWhen(), e.getModifiers(), HRcEvent.VK_COLORED_KEY_0, '?');
				break;
			case KeyEvent.VK_F2: // GREEN
			case KeyEvent.VK_DIVIDE:
				newEvent = new KeyEvent(e.getComponent(), e.getID(),
						e.getWhen(), e.getModifiers(), HRcEvent.VK_COLORED_KEY_1, '?');
				break;
			case KeyEvent.VK_F3: // YELLOW
			case KeyEvent.VK_MULTIPLY:
				newEvent = new KeyEvent(e.getComponent(), e.getID(),
						e.getWhen(), e.getModifiers(), HRcEvent.VK_COLORED_KEY_2, '?');
				break;
			case KeyEvent.VK_F4: // BLUE
			case KeyEvent.VK_MINUS:
				newEvent = new KeyEvent(e.getComponent(), e.getID(),
						e.getWhen(), e.getModifiers(), HRcEvent.VK_COLORED_KEY_3, '?');
				break;

			case KeyEvent.VK_M:
			case KeyEvent.VK_PLUS:
				newEvent = new KeyEvent(e.getComponent(), e.getID(),
						e.getWhen(), e.getModifiers(), HRcEvent.VK_GUIDE, '?');
				break;
			case KeyEvent.VK_I:
			case KeyEvent.VK_PERIOD:
				newEvent = new KeyEvent(e.getComponent(), e.getID(),
						e.getWhen(), e.getModifiers(), HRcEvent.VK_INFO, '?');
				break;

			default:
				newEvent = e;
			break;
			}
			EventManager.getInstance().fireKeyEvent(
					e.getComponent(), newEvent);
			InputEventManager.getInstance().dispatchEvent(newEvent);*/
		}

		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
