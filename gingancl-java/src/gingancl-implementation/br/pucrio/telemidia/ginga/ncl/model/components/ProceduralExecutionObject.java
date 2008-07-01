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

package br.pucrio.telemidia.ginga.ncl.model.components;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import br.org.ginga.ncl.model.components.ICompositeExecutionObject;
import br.org.ginga.ncl.model.components.IProceduralExecutionObject;
import br.org.ginga.ncl.model.event.IAnchorEvent;
import br.org.ginga.ncl.model.event.IAttributionEvent;
import br.org.ginga.ncl.model.event.IFormatterEvent;
import br.org.ginga.ncl.model.event.IPresentationEvent;
import br.org.ginga.ncl.model.event.transition.IBeginEventTransition;
import br.org.ginga.ncl.model.event.transition.IEndEventTransition;
import br.org.ginga.ncl.model.event.transition.IEventTransition;
import br.org.ginga.ncl.model.presentation.ICascadingDescriptor;
import br.org.ncl.components.INode;
import br.org.ncl.connectors.IEvent;
import br.org.ncl.descriptor.IGenericDescriptor;
import br.org.ncl.interfaces.IContentAnchor;
import br.org.ncl.interfaces.ILabeledAnchor;
import br.org.ncl.interfaces.ILambdaAnchor;
import br.org.ncl.interfaces.IPropertyAnchor;

public class ProceduralExecutionObject extends ExecutionObject implements
		IProceduralExecutionObject {

	private static final long serialVersionUID = 8169144118705387786L;
	
	protected Map<String, IFormatterEvent> preparedEvents;
	protected IFormatterEvent currentEvent;
	
	public ProceduralExecutionObject(String id, INode node){
		super(id,node);
		initializeProceduralObject();
	}

	public ProceduralExecutionObject(
			String id, INode node, IGenericDescriptor descriptor){
		super(id, node, descriptor);

		initializeProceduralObject();
	}

	public ProceduralExecutionObject(
			String id, INode node,
			ICascadingDescriptor descriptor){ 
		super(id, node, descriptor);

		initializeProceduralObject();
	}
	
	private void initializeProceduralObject(){
		currentEvent = null;
		preparedEvents = new HashMap<String, IFormatterEvent>();
	}

	public void setCurrentEvent(IFormatterEvent event) {
		if(!this.containsEvent(event)){
			this.currentEvent = null;
		}else
			this.currentEvent = event;
	}
	
	public boolean prepare(
		    IFormatterEvent event, double offsetTime) {
		int size;
		IEventTransition transition;
		double startTime = 0;
		IContentAnchor contentAnchor;

		if (event.getCurrentState() != IEvent.ST_SLEEPING) {
			return false;
		}

		if (event instanceof IAnchorEvent) {
			contentAnchor = ((IAnchorEvent)event).getAnchor();
			if (contentAnchor != null &&
					contentAnchor instanceof ILabeledAnchor) {
				for(Entry<INode, ICompositeExecutionObject> entry : parentTable.entrySet()){
					event.addEventListener((CompositeExecutionObject)entry.getValue());
				}
				preparedEvents.put(event.getId(), event);
				return true;
			}
		}

		if (event instanceof IPresentationEvent) {
			startTime = ((IPresentationEvent)event).getBegin() + offsetTime;
			if (startTime > ((IPresentationEvent)event).getEnd()) {
				return false;
			}
		}

		for(Entry<INode, ICompositeExecutionObject> entry : parentTable.entrySet()){
			event.addEventListener((CompositeExecutionObject)entry.getValue());
		}

		if (event == wholeContent && startTime == 0.0) {
			startTransitionIndex = 0;
		} else {
			size = transitionTable.size();
			startTransitionIndex = 0;
			while (startTransitionIndex < size) {
				transition = transitionTable.get(startTransitionIndex);
				if (transition.getTime() >= startTime) {
					break;
				}

				if (transition instanceof IBeginEventTransition) {
					transition.getEvent()
						    .setCurrentState(IEvent.ST_OCCURRING);
				} else {
					transition.getEvent()
						    .setCurrentState(IEvent.ST_SLEEPING);

					transition.getEvent().incrementOccurrences();
				}
				startTransitionIndex++;
			}
		}

		IFormatterEvent auxEvent;
		IAttributionEvent attributeEvent;
		IPropertyAnchor attributeAnchor;
		int j;

		if (otherEvents != null) {
			size = otherEvents.size();
			for (j = 0; j < size; j++) {
				auxEvent = otherEvents.get(j);
				if (auxEvent instanceof IAttributionEvent) {
					attributeEvent = (IAttributionEvent)auxEvent;
					attributeAnchor = attributeEvent.getAnchor();
					if (attributeAnchor.getPropertyValue() != null &&
							!attributeAnchor.getPropertyValue().equals("")) {
						attributeEvent.setValue(
							    (String) attributeAnchor.getPropertyValue());
					}
				}
			}
		}

		this.offsetTime = startTime;
		currentTransitionIndex = startTransitionIndex;
		preparedEvents.put(event.getId(), event);
		return true;
	}
	
	public boolean start() {
		IEventTransition transition;
		IContentAnchor contentAnchor;

		if (currentEvent == null ||
				!preparedEvents.containsKey(currentEvent.getId())) {

			return false;
		}

		/*
		 * TODO: follow the event state machine or start instruction behavior
		 * if (currentEvent.getCurrentState() == IEvent.ST_PAUSED) {
			return resume();
		}*/

		if (currentEvent.getCurrentState() != IEvent.ST_SLEEPING) {
			return false;
		}

		if (currentEvent instanceof IAnchorEvent) {
			contentAnchor = ((IAnchorEvent)currentEvent).getAnchor();
			if (contentAnchor != null &&
					contentAnchor instanceof ILabeledAnchor) {

				currentEvent.start();
				return true;
			}
		}

		while (currentTransitionIndex < transitionTable.size()) {

			transition = transitionTable.get(currentTransitionIndex);
			if (transition.getTime() <= offsetTime) {
				if (transition instanceof IBeginEventTransition) {
					transition.getEvent().start();
				}
				currentTransitionIndex++;
			} else {
				break;
			}
		}
		return true;
	}
	
	public boolean stop() {
		IContentAnchor contentAnchor;
		//vector<EventTransition*>::iterator i;

		if (currentEvent == null ||
				currentEvent.getCurrentState() == IEvent.ST_SLEEPING ||
				!preparedEvents.containsKey(currentEvent.getId())) {
			return false;
		}

		if (currentEvent instanceof IPresentationEvent) {
			for(IEventTransition transition : transitionTable){
				if(transition.getTime() > ((IPresentationEvent)currentEvent).getEnd()){
					transition.getEvent().setCurrentState(IEvent.ST_SLEEPING);
				}else if(transition instanceof IEndEventTransition){
					transition.getEvent().stop();
				}
			}

		} else if (currentEvent instanceof IAnchorEvent) {
			contentAnchor = ((IAnchorEvent)currentEvent).getAnchor();
			if (contentAnchor != null &&
					contentAnchor instanceof ILabeledAnchor) {

				currentEvent.stop();
			}
		}

		currentTransitionIndex = startTransitionIndex;
		pauseCount = 0;
		return true;
	}
	
	public boolean abort() {
		Iterator<IFormatterEvent> evs;

		if (currentEvent == null ||
				currentEvent.getCurrentState() == IEvent.ST_SLEEPING ||
				!preparedEvents.containsKey(currentEvent.getId())) {
			return false;
		}

		evs = getEvents();
		while(evs.hasNext()){
			IFormatterEvent event = evs.next();
			if(event.getCurrentState() != IEvent.ST_SLEEPING){
				event.abort();
			}
		}

		currentTransitionIndex = startTransitionIndex;
		pauseCount = 0;
		return true;
	}
	
	public boolean pause() {
		IFormatterEvent event;
		Iterator<IFormatterEvent> evs;

		if (currentEvent == null ||
				currentEvent.getCurrentState() != IEvent.ST_OCCURRING ||
				!preparedEvents.containsKey(currentEvent.getId())) {
			return false;
		}

		evs = getEvents();
		while(evs.hasNext()){
			event = evs.next();
			if(event.getCurrentState() == IEvent.ST_OCCURRING){
				event.pause();
			}
		}
		pauseCount++;
		return true;
	}
	
	public boolean resume() {
		IFormatterEvent event;
		Iterator<IFormatterEvent> evs;

		if (pauseCount == 0) {
			return false;

		} else {
			pauseCount--;
			if (pauseCount > 0) {
				return false;
			}
		}

		evs = getEvents();
		while(evs.hasNext()){
			event = evs.next();
			if(event.getCurrentState() == IEvent.ST_PAUSED){
				event.resume();
			}
		}

		return true;
	}
	
	public boolean unprepare() {
		if (currentEvent == null ||
				currentEvent.getCurrentState() != IEvent.ST_SLEEPING ||
				!preparedEvents.containsKey(currentEvent.getId())) {
			return false;
		}
		
		if( currentEvent instanceof IAnchorEvent &&
			((IAnchorEvent)currentEvent).getAnchor() != null &&
			((IAnchorEvent)currentEvent).getAnchor() instanceof ILambdaAnchor){
			
			Iterator<IFormatterEvent> events = this.getEvents();
			while(events.hasNext()){
				IFormatterEvent event = events.next();
				if(event.getCurrentState() != IEvent.ST_SLEEPING){
					event.stop();
				}
			}
		}

		for(Entry<INode, ICompositeExecutionObject> entry : parentTable.entrySet()){
			currentEvent.removeEventListener((CompositeExecutionObject)entry.getValue());
		}

		preparedEvents.remove(currentEvent.getId());
		currentEvent = null;
		return true;
	}

}
