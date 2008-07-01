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

package br.pucrio.telemidia.ginga.ncl.adapters;

import java.net.URL;
import java.util.Iterator;

import br.org.ginga.core.io.ISurface;
import br.org.ginga.core.io.InputEvent;
import br.org.ginga.core.io.InputEventListener;
import br.org.ginga.core.player.IPlayer;
import br.org.ginga.ncl.adapters.IFormatterPlayerAdapter;
import br.org.ginga.ncl.model.components.IExecutionObject;
import br.org.ginga.ncl.model.event.IAttributionEvent;
import br.org.ginga.ncl.model.event.IFormatterEvent;
import br.org.ginga.ncl.model.event.IPresentationEvent;
import br.org.ginga.ncl.model.presentation.ICascadingDescriptor;
import br.org.ginga.ncl.model.presentation.IFormatterRegion;
import br.org.ncl.animation.IAnimation;
import br.org.ncl.components.IContent;
import br.org.ncl.components.INodeEntity;
import br.org.ncl.components.IReferenceContent;
import br.org.ncl.connectors.IEvent;
import br.org.ncl.interfaces.IIntervalAnchor;
import br.org.ncl.interfaces.ILambdaAnchor;
import br.pucrio.telemidia.ginga.core.io.InputEventManager;

public abstract class DefaultFormatterPlayerAdapter implements
		IFormatterPlayerAdapter,InputEventListener {
	protected NominalEventMonitor anchorMonitor;
	protected IExecutionObject object;
	protected IPlayer player;
	
	private URL mrl;
	
	protected void setMRL(URL newMrl){
		this.mrl = newMrl;
	}
	
	protected URL getMRL(){
		return mrl;
	}
	
	public DefaultFormatterPlayerAdapter(){
		anchorMonitor = null;
		object = null;
		player = null;
		mrl = null;
	}

	public boolean abort() {
		if (player == null) {
			return false;
		}
		
		player.stop();
		if(anchorMonitor != null){
			anchorMonitor.stopMonitor();
		}
		player.notifyReferPlayers(IEvent.TR_ABORTS);
		if (object != null) {
			if (object.abort()) {
				unprepare();
				return true;
			}
		}
		player = null;
		return false;
	}

	public void controllerUpdate(short code, String param) {
		switch(code) {
		case PL_NOTIFY_STOP:
			if (object != null) {
				if (param.equals("")) {
					naturalEnd();
				} /*else {
					doEvent(param,PL_NOTIFY_STOP);
				}*/
			}
			break;
		/*case PL_NOTIFY_USEREVENT:
			if (object != null) {
				try{
					object.select(Integer.parseInt(param), 
						player.getMediaTime() * 1000);
				}catch(NumberFormatException ex){
					System.err.println(this.getClass().getCanonicalName() +
							": Could not parse USER_EVENT parameter");
				}
			}
			break;*/
			/*case PL_NOTIFY_START:
				if(object != null && !param.equals("")){
					doEvent(param,PL_NOTIFY_START);
				}
				break;
			case PL_NOTIFY_PAUSE:
				if (object != null) {
					if (param.equals("")) {
						pause();
					} else {
						doEvent(param,PL_NOTIFY_PAUSE);
					}
				}
				break;
			case PL_NOTIFY_RESUME:
				if (object != null) {
					if (param.equals("")) {
						resume();
					} else {
						doEvent(param,PL_NOTIFY_RESUME);
					}
				}
				break;

			case PL_NOTIFY_STOP:
				if (object != null) {
					if (param.equals("")) {
						naturalEnd();
					} else {
						doEvent(param,PL_NOTIFY_STOP);
					}
				}
				break;

			case PL_NOTIFY_ABORT:
				if (object != null) {
					if (param.equals("")) {
						abort();
					} else {
						doEvent(param,PL_NOTIFY_ABORT);
					}
				}
				break;

			case PL_NOTIFY_USEREVENT:
				if (object != null) {
					try{
						object.select(Integer.parseInt(param), 
							player.getMediaTime() * 1000);
					}catch(NumberFormatException ex){
						System.err.println(this.getClass().getCanonicalName() +
								": Could not parse USER_EVENT parameter");
					}
				}
				break;*/
		}
	}
	
	public void userEventReceived(InputEvent ev) {
		if (object != null && player != null) {
			object.select(ev.getCode(), player.getMediaTime() * 1000);
		}
	}
	
	/*private IFormatterEvent getEvent(String anchorId) {
		Iterator<IPresentationEvent> events;

		events = object.getPresentationEvents();
		if(events!=null){
			while(events.hasNext()){
				IPresentationEvent event = events.next();
				IAnchor anchor = event.getAnchor();
				if(anchor != null){
					if((anchor instanceof ILabeledAnchor) 
						&& anchor.getId().equals(anchorId))
						return event;
				}
			}
		}
		return null;
	}
	
	private boolean doEvent(String anchorId, short eventType){
		IFormatterEvent event = getEvent(anchorId);
		if (event != null)
			switch (eventType) {
			case PL_NOTIFY_START:
				return event.start();
			case PL_NOTIFY_PAUSE:
				return event.pause();
			case PL_NOTIFY_RESUME:
				return event.resume();
			case PL_NOTIFY_STOP:
				return event.stop();
			case PL_NOTIFY_ABORT:
				return event.abort();
			default:
				return false;
			}
		return false;
	}*/

	public String getPropertyValue(IAttributionEvent event) {
		String attName;
		if (player == null || event == null) {
			return "";
		}
		attName = event.getAnchor().getPropertyName();
		return player.getPropertyValue(attName);
	}

	public double getMediaTime() {
		if (player == null) {
			return Double.POSITIVE_INFINITY;
		}

		return player.getMediaTime();
	}

	public ISurface getObjectDisplay() {
		if(player != null){
			return player.getSurface();
		}else
			return null;
	}

	public double getObjectExpectedDuration() {
		return IIntervalAnchor.OBJECT_DURATION;
	}

	public IPlayer getPlayer() {
		return player;
	}

	public synchronized boolean hasPrepared() {
		if (object == null || player == null) {
			return false;
		}

		return !(player.hasPresented());
	}

	public void setFocusHandler(boolean isHandler) {
		player.setFocusHandler(isHandler);
	}

	public void naturalEnd() {
		Boolean freeze;

		if(player ==  null || object == null){
			return;
		}
		
		if(object.getDescriptor() != null){
			freeze = object.getDescriptor().getFreeze();
			if(freeze != null && freeze.booleanValue()){
				return;
			}
		}

		IFormatterEvent mainEvent = object.getMainEvent();
		if(mainEvent == null){
			stop();
			return;
		}
		
		
		if((mainEvent instanceof IPresentationEvent) 
				&& ((IPresentationEvent)mainEvent).getRepetitions() >1) 
			start();
		else
			stop();
	}

	public synchronized boolean pause() {
		if (object != null && object.pause()) {
			player.pause();
			if (anchorMonitor != null) {
				anchorMonitor.pauseMonitor();
			}

			player.notifyReferPlayers(IEvent.TR_PAUSES);
			return true;
		} else {
			return false;
		}
	}

	public boolean prepare(IExecutionObject object, IFormatterEvent mainEvent) {
		IContent content;

		if (object == null) 
			return false;
		this.object = object;
		synchronized(this.object){
			if (object.getDataObject() != null &&
					object.getDataObject().getDataEntity() != null) {
				content = ((INodeEntity)(object.getDataObject().getDataEntity())).getContent();
				if(content != null && (content instanceof IReferenceContent)){
					this.mrl = ((IReferenceContent)content).getCompleteReferenceUrl();
				}else
					this.mrl = null;
			}
			createPlayer();
			if(player == null)
				return false;
			if(mainEvent.getCurrentState() == IEvent.ST_SLEEPING){
				object.prepare(mainEvent, 0);
				prepare();
				return true;
			}else
				return false;
		}
	}
	
	/*protected abstract IPlayer createPlayer(IPresentationEvent mainEvent, double i);
	
	protected void createPlayer(IPresentationEvent mainEvent, IPlayer player) {

		IExecutionObject object;
		NominalEventMonitor monitor;
		List<IPlayer> playerList;
		
		object = mainEvent.getExecutionObject();
		object.prepare(mainEvent, 0);
		synchronized (monitors) {
			if(player == null)
				return;
			
			playerList = objects.get(object);
			if(playerList == null)
				playerList = new ArrayList<IPlayer>(1);
			playerList.add(player);
			monitor = new NominalEventMonitor(object,player);
			monitors.put(player, monitor);
		}
		player.addListener(this);
		setObjectDisplay(player.getSurface());
	}*/
	
	protected void createPlayer() {
		if(player!=null){
			anchorMonitor = new NominalEventMonitor(object, player);
			player.addListener(this);
			
			Iterator<IFormatterEvent> events = object.getEvents();
			
			while(events.hasNext()){
				IFormatterEvent event = events.next();
				if(event instanceof IAttributionEvent){
					((IAttributionEvent)event).setValueMaintainer(this);
				}
			}
		}
	}
	
	protected void prepare() {
		double duration;
		IIntervalAnchor intervalAnchor;
		IFormatterEvent mainEvent;

		mainEvent = object.getMainEvent();
		if (mainEvent instanceof IPresentationEvent) {
			if ((((IPresentationEvent)mainEvent).getAnchor()) instanceof ILambdaAnchor) {
				duration = ((IPresentationEvent)mainEvent).getDuration();
				if (duration < IIntervalAnchor.OBJECT_DURATION) {
					player.setScope(
							(String) ((IPresentationEvent)mainEvent).getAnchor().getId(),
							0.0, duration / 1000);
	
				} else {
					player.setScope((String) ((IPresentationEvent)mainEvent).getAnchor().getId());
				}
	
			} else if (((((IPresentationEvent)mainEvent).getAnchor())) instanceof IIntervalAnchor) {
				intervalAnchor = (IIntervalAnchor)(((IPresentationEvent)mainEvent).getAnchor());
				player.setScope(
						(String) ((IPresentationEvent)mainEvent).getAnchor().getId(),
						(intervalAnchor.getBegin() / 1000),
						(intervalAnchor.getEnd() / 1000));
			}
		}
		if(player.immediatelyStart()){
			player.setImmediatelyStart(false);
			start();
		}
	}

	public synchronized boolean resume() {
		if (object != null && object.resume()) {
			player.resume();
			if (anchorMonitor != null) {
				anchorMonitor.resumeMonitor();
			}

			player.notifyReferPlayers(IEvent.TR_RESUMES);
			return true;
		}
		return false;
	}

	/*public boolean runAction(IExecutionObject object, IFormatterEvent event,
			short action) {
		IPlayer player = getPlayer(object);
		if (player == null) {
			return false;
		}
		// TODO: to implement
		return true;
	}*/

	public boolean setPropertyValue(IAttributionEvent event, Object value, 
				IAnimation animation) {
		if (value.equals("") || player == null || object == null) {
	  		return false;
	  	}

		String attName = (event.getAnchor()).getPropertyName();
		if (attName.equals("visible")) {
			if (value.equals("false")) {
				setVisible(false);
			} else {
				setVisible(true);
			}
		} else {
			object.setPropertyValue(event, value, animation);
			if(value == null)
				player.setPropertyValue(attName, "");
			else
				player.setPropertyValue(attName, value.toString());
		}
	    return true;
	}
	
	protected void setVisible(boolean visible){
		ICascadingDescriptor descriptor;
		IFormatterRegion region;

  		descriptor = object.getDescriptor();
  		if (descriptor != null) {
			region = descriptor.getFormatterRegion();
			if (region.isVisible() != visible) {
				region.setRegionVisibility(visible);
				player.setVisible(visible);
			}
  		}
	}

	public void setObjectDisplay(ISurface surface) {
		
	}

	public void setTimeBasePlayer(
			IFormatterPlayerAdapter timeBasePlayerAdapter) {
		if (player == null) 
			return;

		IPlayer timeBasePlayer;
		timeBasePlayer = timeBasePlayerAdapter.getPlayer();
		if (timeBasePlayer != null) {
			//player.setReferenceTimePlayer(timeBasePlayer);
			player.setTimeBasePlayer(timeBasePlayer);
		}
	}

	public synchronized boolean start() {
		if (object.start()) {
			InputEventManager.getInstance().addInputEventListener(this, object.getInputEvents());
			player.play();
			if (anchorMonitor != null) {
				anchorMonitor.startMonitor();
			}
			return true;
		}
		return false;
	}

	public synchronized boolean stop() {
		if (player == null || object == null) {
			return false;
		}
		InputEventManager.getInstance().removeInputEventListener(this);
		Iterator<IFormatterEvent> events;
		
		events = object.getEvents();
		while(events.hasNext()){
			IFormatterEvent event = events.next();
			if(event instanceof IAttributionEvent){
				((IAttributionEvent)event).setValueMaintainer(null);
			}
		}

		player.setPresented(true);
		player.stop();

		if (anchorMonitor != null) {
			anchorMonitor.stopMonitor();
		}

		player.notifyReferPlayers(IEvent.TR_STOPS);
		if (player.isForcedNaturalEnd()) {
			unprepare();
			return true;
		} else if (object.stop()) {
			unprepare();
			return true;
		}
		return false;
		
	}

	public boolean unprepare() {
		if (object.getMainEvent() != null && (object.getMainEvent().getCurrentState() == IEvent.ST_OCCURRING ||
				object.getMainEvent().getCurrentState() == IEvent.ST_PAUSED)) {
			return stop();
		}
		player = null;
		anchorMonitor = null;
		object.unprepare();
		object = null;
		System.gc();
		return true;
	}

	public void updateObjectExpectedDuration() {
		IPresentationEvent wholeContentEvent;
		double duration;
		double implicitDur;

		wholeContentEvent = object.getWholeContentPresentationEvent();
		duration = wholeContentEvent.getDuration();
		if (object.getDescriptor() == null
				|| object.getDescriptor().getExplicitDuration() == null
				|| Double.isNaN(object.getDescriptor().getExplicitDuration()
						.doubleValue()) || (duration < 0) || (Double.isNaN(duration))) {
			implicitDur = getObjectExpectedDuration();
			((IIntervalAnchor)wholeContentEvent.getAnchor()).setEnd(implicitDur);
			wholeContentEvent.setDuration(implicitDur);
		}
	}
}
