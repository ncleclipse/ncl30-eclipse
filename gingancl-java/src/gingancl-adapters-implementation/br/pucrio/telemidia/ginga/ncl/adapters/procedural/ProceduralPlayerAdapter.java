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

package br.pucrio.telemidia.ginga.ncl.adapters.procedural;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import br.org.ginga.ncl.adapters.procedural.IProceduralPlayerAdapter;
import br.org.ginga.ncl.model.components.IExecutionObject;
import br.org.ginga.ncl.model.event.IAnchorEvent;
import br.org.ginga.ncl.model.event.IFormatterEvent;
import br.org.ginga.ncl.model.event.IPresentationEvent;
import br.org.ncl.components.IContent;
import br.org.ncl.components.INodeEntity;
import br.org.ncl.components.IReferenceContent;
import br.org.ncl.connectors.IEvent;
import br.org.ncl.interfaces.IAnchor;
import br.org.ncl.interfaces.IIntervalAnchor;
import br.org.ncl.interfaces.ILabeledAnchor;
import br.org.ncl.interfaces.ILambdaAnchor;
import br.pucrio.telemidia.ginga.ncl.adapters.DefaultFormatterPlayerAdapter;

public abstract class ProceduralPlayerAdapter extends DefaultFormatterPlayerAdapter 
		implements IProceduralPlayerAdapter {

	protected Map<String, IFormatterEvent> preparedEvents;
	protected IFormatterEvent currentEvent;
		
	public ProceduralPlayerAdapter() {
		currentEvent = null;
		preparedEvents = new HashMap<String, IFormatterEvent>();
	}
	
	/*public synchronized boolean hasPrepared() {
		if (player == null || player.getSurface() == null ||
				player.getSurface().getParent() == null) {
			return false;
		}
		return true;
	}*/

	public synchronized boolean prepare(IExecutionObject object, IFormatterEvent event) {

		IContent content;
		
		if (object == null) {
			return false;
		}

		if (this.object != object) {
			preparedEvents.clear();
			
			this.object = object;

			if (this.object.getDataObject() != null &&
					this.object.getDataObject().getDataEntity() != null) {

				content = ((INodeEntity)(object.getDataObject()
						.getDataEntity())).getContent();

				if (content != null && content instanceof IReferenceContent) {

					this.setMRL(((IReferenceContent)content)
						    .getCompleteReferenceUrl());
				} else {
					this.setMRL(null);
				}
			}
			createPlayer();
		}

		if (event.getCurrentState() == IEvent.ST_SLEEPING) {
			this.object.prepare(event, 0);
			
			prepare(event);
			return true;
		}
		return false;
	}
	
	public void prepare(IFormatterEvent event) {
		double duration;
		IIntervalAnchor intervalAnchor;

		if (event instanceof IAnchorEvent) {
			if ((((IAnchorEvent)event).getAnchor()) instanceof
					ILambdaAnchor) {

				duration = ((IPresentationEvent)event).getDuration();

				if (duration < IIntervalAnchor.OBJECT_DURATION) {
					player.setScope(
							"", 0.0, duration / 1000);
				}

			} else if ((((IAnchorEvent)event).getAnchor())
					instanceof IIntervalAnchor) {

				intervalAnchor = (IIntervalAnchor)(
						((IAnchorEvent)event).getAnchor());

				player.setScope(
						(String) ((IAnchorEvent)event).getAnchor().getId(),
						(intervalAnchor.getBegin() / 1000),
						(intervalAnchor.getEnd() / 1000));

			} else if ((((IAnchorEvent)event).getAnchor()) 
					instanceof ILabeledAnchor) {

				player.setScope((String) ((IAnchorEvent)event)
						.getAnchor().getId());
			}
		}
		preparedEvents.put(event.getId(), event);
	}
	
	public synchronized boolean start() {
		if (object.start()) {
			player.play();
			return true;
		}
		return false;
	}
	
	public synchronized boolean stop() {
		if(player == null)
			return false;
		player.stop();
		player.notifyReferPlayers(IEvent.TR_STOPS);
		if (player.isForcedNaturalEnd()) {
			for(Entry<String, IFormatterEvent> entry : preparedEvents.entrySet()){
				IFormatterEvent event = entry.getValue();
				if(event instanceof IAnchorEvent &&
					((IAnchorEvent)event).getAnchor() != null &&
					((IAnchorEvent)event).getAnchor() instanceof ILambdaAnchor){
					currentEvent = event;
					currentEvent.stop();
					unprepare();
					return true;
				}
			}
		}
		
		if (object.stop()) {
			unprepare();
			return true;
		}

		return false;
	}
	
	public synchronized boolean pause() {
		if (object.pause()) {
			player.pause();
			player.notifyReferPlayers(IEvent.TR_PAUSES);
			return true;

		} else {
			return false;
		}
	}
	
	public synchronized boolean resume() {
		if (object.resume()) {
			player.resume();
			player.notifyReferPlayers(IEvent.TR_RESUMES);
			return true;
		}
		return false;
	}
	
	public synchronized boolean abort() {
		player.stop();
		player.notifyReferPlayers(IEvent.TR_ABORTS);

		if (object != null) {
			if (object.abort()) {
				unprepare();
				return true;
			}
		}

		return false;
	}
	
	public synchronized boolean unprepare() {
		if (currentEvent != null && (currentEvent.getCurrentState() == IEvent.ST_OCCURRING ||
				currentEvent.getCurrentState() == IEvent.ST_PAUSED)) {
			return stop();
		}

		if (preparedEvents.containsKey(currentEvent.getId()) &&
				preparedEvents.size() == 1) {
			player = null;
			anchorMonitor = null;
			object.unprepare();
			preparedEvents.clear();
			object = null;
		} else {
			object.unprepare();
			preparedEvents.remove(currentEvent.getId());
		}
		System.gc();
		return true;
	}
	
	public void naturalEnd() {
		if (player == null || object == null) {
			return;
		}

		player.notifyReferPlayers(IEvent.TR_STOPS);
		for(Entry<String, IFormatterEvent> entry : preparedEvents.entrySet()){
			IFormatterEvent event = entry.getValue();
			if(event != null && event instanceof IAnchorEvent &&
					((IAnchorEvent)event).getAnchor() != null &&
					((IAnchorEvent)event).getAnchor() instanceof ILambdaAnchor){
				currentEvent = event;
				currentEvent.stop();
				unprepare();
			}
		}
		
		if (object.stop()) {
			unprepare();
		}
	}
	
	public void controllerUpdate(short code, String param) {
		switch(code) {
			case PL_NOTIFY_START:
				//if(object != null && !param.equals("")){
				if(object != null){
					doEvent(param,PL_NOTIFY_START);
				}
				break;
			case PL_NOTIFY_PAUSE:
				if (object != null) {
					doEvent(param,PL_NOTIFY_PAUSE);
				}
				break;
			case PL_NOTIFY_RESUME:
				if (object != null) {
					doEvent(param,PL_NOTIFY_RESUME);
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
				doEvent(param,PL_NOTIFY_ABORT);
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
				break;
		}
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
	}
	
	private IFormatterEvent getEvent(String anchorId) {
		Iterator<IFormatterEvent> events = object.getEvents();
		while(events.hasNext()){
			IFormatterEvent event = events.next();
			if(event instanceof IAnchorEvent){
				IAnchor anchor = ((IAnchorEvent)event).getAnchor();
				if (anchor != null &&
						((anchor instanceof ILabeledAnchor &&
						anchor.getId().equals(anchorId)) ||
						(anchor instanceof ILambdaAnchor &&
								anchor.equals("")))) {
					return event;
				}
			}
		}
		return null;
	}
}
