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

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import br.org.ginga.core.io.InputEvent;
import br.org.ginga.core.io.InputEventListener;

public class InputEventManager extends Thread {
	private static InputEventManager _instance;
	private Map<InputEventListener,Set<Integer>> eventListeners;
	private Set<InputEventListener> proceduralListeners;
	
	private boolean running;
	
	private double lastEventTime;
	
	private double timeStamp;
	
	private static Component defaultComponentSource = new Component(){};
	
	private InputEventManager(){
		eventListeners = new Hashtable<InputEventListener, Set<Integer>>();
		proceduralListeners = new HashSet<InputEventListener>();
		running = false;
		lastEventTime = 0;
		timeStamp = -500;
	}
	
	public static InputEventManager getInstance(){
		if(_instance == null){
			_instance = new InputEventManager();
		}
		return _instance;
	}

	public synchronized void addInputEventListener(InputEventListener listener, 
			Set<Integer> events){
		if(!eventListeners.containsKey(listener));
			eventListeners.put(listener, events);
	}

	public synchronized void removeInputEventListener(InputEventListener listener){
		eventListeners.remove(listener);
	}

	public synchronized void release(){
		this.interrupt();
		eventListeners.clear();
	}

	public void postEvent(final int code) {

		Thread dispatcher = new Thread(){	
			public void run(){
				try {
					this.sleep(1600);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				KeyInputEvent inputEvent = new KeyInputEvent(
						defaultComponentSource,
						KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,
						code, '?');
				dispatchEvent(inputEvent);
			}
		};
		dispatcher.start();
	}

	public synchronized void dispatchEvent(AWTEvent inputEvent){
		List<InputEventListener> toBeNotified = new ArrayList<InputEventListener>();
		if(inputEvent instanceof KeyEvent){
			InputEvent event = new KeyInputEvent(((KeyEvent)inputEvent));
			for(Entry<InputEventListener, Set<Integer>> entry : eventListeners.entrySet()){
				if(entry.getValue().size() == 0){
					toBeNotified.add(entry.getKey());
					//entry.getKey().userEventReceived(event);
				}else{
					for(Integer ev : entry.getValue()){
						if(ev.intValue() ==((KeyEvent)inputEvent).getKeyCode()){
							toBeNotified.add(entry.getKey());;
							//entry.getKey().userEventReceived(event);
							break;
						}
					}
				}
			}
			for(InputEventListener listener : toBeNotified){
				listener.userEventReceived(event);
			}
		}
	}
	
	public synchronized void dispatchProceduralEvent( AWTEvent inputEvent) {

		if(inputEvent instanceof KeyEvent){
			InputEvent event = new KeyInputEvent(((KeyEvent)inputEvent));
			for (InputEventListener listener : proceduralListeners){
				listener.userEventReceived(event);
			}
		}
	}
	
	public synchronized void addProceduralInputEventListener(
			InputEventListener listener) {

		proceduralListeners.add(listener);
	}

	public synchronized void removeProceduralInputEventListener(
			InputEventListener listener) {

		proceduralListeners.remove(listener);
	}
	
	public void run(){
		
	}
}
