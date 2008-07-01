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

package br.pucrio.telemidia.ginga.core.player;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.sun.corba.se.impl.legacy.connection.USLPort;

import br.org.ginga.core.io.ISurface;
import br.org.ginga.core.io.InputEvent;
import br.org.ginga.core.player.IPlayer;
import br.org.ginga.core.player.IPlayerListener;
import br.pucrio.telemidia.ginga.core.io.CodeMap;
import br.pucrio.telemidia.ginga.core.io.InputEventManager;

public abstract class DefaultPlayerImplementation implements IPlayer {
	private Set<IPlayerListener> listeners;
	
	protected short status = STOP;
	
	protected URL mrl;
	protected ISurface surface;
	protected double initTime, elapsedTime, elapsedPause, pauseTime, offsetTime;
	protected Set<IPlayer> referredPlayers;
	protected IPlayer timeBasePlayer;
	protected boolean presented;
	protected boolean visible;
	protected boolean immediatelyStartVar;
	protected boolean forcedNaturalEnd;
	protected String scope;
	protected double scopeInitTime;
	protected double scopeEndTime;
	
	//private Set<IPlayerListener> listeners;
	
	public DefaultPlayerImplementation(URL contentURL){
		this.mrl = contentURL;
		listeners = new HashSet<IPlayerListener>();
		referredPlayers = new HashSet<IPlayer>();
		surface = null;
		offsetTime =0;
		presented = false;
		visible = true;
		immediatelyStartVar = false;
		status = IPlayer.STOP;
		forcedNaturalEnd = false;
		scope="";
		scopeInitTime = -1;
		scopeEndTime = -1;
		
		elapsedTime = 0;
		elapsedPause = 0;
		initTime = 0;
		offsetTime = 0;
		pauseTime = 0;
	}
	
	protected URL getContentURL(){
		return this.mrl;
	}
	
	protected Set<IPlayerListener> getListeners(){
		return listeners;
	}

	public synchronized void addListener(IPlayerListener listener) {
		this.listeners.add(listener);
	}
	
	public synchronized void removeListener(IPlayerListener listener) {
		this.listeners.remove(listener);
	}
	
	public synchronized void notifyListeners(short code, String parameter) {
		for(IPlayerListener listener : listeners){
			listener.controllerUpdate(code, parameter);
		}
	}

	public void setSurface(ISurface surface) {
		this.surface = surface;
	}
	
	public ISurface getSurface() {
		return surface;
	}
	
	public double getMediaTime() {
		double mediaTime;
		mediaTime = 0;

		if (status == PAUSE) {
			mediaTime = offsetTime + elapsedTime;

		} else {
			mediaTime = offsetTime + elapsedTime + System.currentTimeMillis() -
				    initTime - elapsedPause;
		}

		return mediaTime/1000;
	}

	public void setFocusHandler(boolean isHandler) {
		if (isHandler) {
			try {
				Thread.sleep(160);

			} catch (Exception e) {
				// TODO: handle exception
			}
			InputEventManager.getInstance().postEvent(InputEvent.ESCAPE_CODE);
		}
	}

	public void setScope(String scope, double begin, double end) {
		this.scope = scope;
		this.scopeInitTime = begin;
		this.scopeEndTime = end;
	}

	public void setScope(String scope, double begin) {
		this.setScope(scope,begin,-1);
	}

	public void setScope(String scope) {
		this.setScope(scope,-1,-1);
	}
	
	public void play() {
		//InputEventManager.getInstance().addControllerListener(this);
		this.forcedNaturalEnd = false;
		this.status = IPlayer.PLAY;
		elapsedTime = 0;
		elapsedPause = 0;
		initTime = System.currentTimeMillis();
	}
	
	public void stop() {
		this.status = IPlayer.STOP;
		//InputEventManager.getInstance().removeControllerListener(this);
	}
	
	public void abort() {
		stop();
	}
	
	public void pause() {
		pauseTime = System.currentTimeMillis();
		elapsedTime = elapsedTime + (pauseTime - initTime);
		this.status = IPlayer.PAUSE;
	}

	public void resume() {
		initTime = System.currentTimeMillis();
		elapsedPause = elapsedPause + (initTime - pauseTime);
		this.status = IPlayer.PLAY;
	}
	
	public void addTimeReferPlayer(IPlayer referPlayer) {
		referredPlayers.add(referPlayer);
	}
	
	public void removeTimeReferPlayer(IPlayer referPlayer) {
		referredPlayers.remove(referPlayer);
	}
	
	public void notifyReferPlayers(int transition) {
		for(IPlayer player : referredPlayers)
			player.notifyReferPlayers(transition);
	}
	
	public void timebaseObjectTransitionCallback(int transition) {
		if (transition == IPlayerListener.PL_NOTIFY_STOP) {
			//setReferenceTimePlayer(null);
			this.stop();
		}
	}
	
	public void setTimeBasePlayer(IPlayer timeBasePlayer) {
		if (timeBasePlayer != null) {
			this.timeBasePlayer = timeBasePlayer;
			this.timeBasePlayer.addTimeReferPlayer(this);
		}
	}
	
	public boolean hasPresented() {
		return presented;
	}
	
	public void setPresented(boolean presented){
		this.presented = presented;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean immediatelyStart() {
		return immediatelyStartVar;
	}
	
	public void setImmediatelyStart(boolean immediatelyStartVal) {
		this.immediatelyStartVar= immediatelyStartVal;
	}

	public void forceNaturalEnd() {
		this.forcedNaturalEnd = true;
		this.notifyListeners(IPlayerListener.PL_NOTIFY_STOP, "");
	}

	public boolean isForcedNaturalEnd() {
		return forcedNaturalEnd;
	}

	/*public void setReferenceTimePlayer(IPlayer player) {
		this.addTimeReferPlayer(player);
	}*/

	/*public void userEventReceived(InputEvent ev) {
		//if(AWTEvent instanceof )
		if(ev instanceof KeyEvent){
			this.notifyListeners(IPlayerListener.PL_NOTIFY_USEREVENT, Integer.toString(((KeyEvent)ev).getKeyCode()) );
		}
	}*/

}
