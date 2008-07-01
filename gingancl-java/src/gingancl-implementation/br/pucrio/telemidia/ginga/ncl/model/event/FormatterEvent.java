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
package br.pucrio.telemidia.ginga.ncl.model.event;

import java.util.ArrayList;
import java.util.List;

import br.org.ginga.ncl.model.components.IExecutionObject;
import br.org.ginga.ncl.model.event.IEventListener;
import br.org.ginga.ncl.model.event.IFormatterEvent;
import br.org.ncl.connectors.IEvent;

public abstract class FormatterEvent implements IFormatterEvent {
	/**
	 * 
	 */
	private static final short ST_ABORTED = 50;

	/**
	 * 
	 */
	protected String id;

	/**
	 * 
	 */
	protected short currentState;

	/**
	 * 
	 */
	protected long occurrences;

	/**
	 * 
	 */
	protected IExecutionObject executionObject;

	/**
	 * 
	 */
	protected List<IEventListener> listeners;

	/**
	 * 
	 */
	private boolean notifying;

	/**
	 * 
	 */
	/**
	 * 
	 */
	private List<IEventListener> toBeAdded, toBeRemoved;

	/**
	 * @param id
	 * @param executionObject
	 */
	public FormatterEvent(String id, IExecutionObject executionObject) {
		this.id = id;
		currentState = IEvent.ST_SLEEPING;
		occurrences = 0;
		listeners = new ArrayList<IEventListener>();
		this.executionObject = executionObject;
		notifying = false;
		toBeAdded = new ArrayList<IEventListener>();
		toBeRemoved = new ArrayList<IEventListener>();
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.event.IFormatterEvent#setId(java.lang.Comparable)
	 */
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.event.IFormatterEvent#addEventListener(br.org.ginga.ncl.model.event.IEventListener)
	 */
	public synchronized void addEventListener(IEventListener listener) {
		if (listeners.contains(listener) || toBeAdded.contains(listener)) {
			return;
		}

		if (notifying) {
			toBeAdded.add(listener);
		}
		else {
			listeners.add(listener);
		}
	}

	// TODO: to be removed - not used
	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.event.IFormatterEvent#containsEventListener(br.org.ginga.ncl.model.event.IEventListener)
	 */
	public boolean containsEventListener(IEventListener listener) {
		if (toBeAdded.contains(listener) || listeners.contains(listener)) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * @param transition
	 * @return
	 */
	protected short getNewState(short transition) {
		switch (transition) {
		case IEvent.TR_STOPS:
			return IEvent.ST_SLEEPING;

		case IEvent.TR_STARTS:
		case IEvent.TR_RESUMES:
			return IEvent.ST_OCCURRING;

		case IEvent.TR_PAUSES:
			return IEvent.ST_PAUSED;

		case IEvent.TR_ABORTS:
			return ST_ABORTED;

		default:
			return -1;
		}
	}

	/**
	 * @param newState
	 * @return
	 */
	protected short getTransition(short newState) {
		switch (currentState) {
		case IEvent.ST_SLEEPING:
			switch (newState) {
			case IEvent.ST_OCCURRING:
				return IEvent.TR_STARTS;
			default:
				return -1;
			}

		case IEvent.ST_OCCURRING:
			switch (newState) {
			case IEvent.ST_SLEEPING:
				return IEvent.TR_STOPS;
			case IEvent.ST_PAUSED:
				return IEvent.TR_PAUSES;
			case ST_ABORTED:
				return IEvent.TR_ABORTS;
			default:
				return -1;
			}

		case IEvent.ST_PAUSED:
			switch (newState) {
			case IEvent.ST_OCCURRING:
				return IEvent.TR_RESUMES;
			case IEvent.ST_SLEEPING:
				return IEvent.TR_STOPS;
			case ST_ABORTED:
				return IEvent.TR_ABORTS;
			default:
				return -1;
			}

		default:
			return -1;
		}
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.event.IFormatterEvent#abort()
	 */
	public boolean abort() {
		switch (currentState) {
		case IEvent.ST_OCCURRING:
		case IEvent.ST_PAUSED:
			return changeState(ST_ABORTED, IEvent.TR_ABORTS);
		default:
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.event.IFormatterEvent#start()
	 */
	public boolean start() {
		switch (currentState) {
		case IEvent.ST_SLEEPING:
			return changeState(IEvent.ST_OCCURRING, IEvent.TR_STARTS);
		default:
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.event.IFormatterEvent#stop()
	 */
	public boolean stop() {
		switch (currentState) {
		case IEvent.ST_OCCURRING:
		case IEvent.ST_PAUSED:
			return changeState(IEvent.ST_SLEEPING, IEvent.TR_STOPS);
		default:
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.event.IFormatterEvent#pause()
	 */
	public boolean pause() {
		switch (currentState) {
		case IEvent.ST_OCCURRING:
			return changeState(IEvent.ST_PAUSED, IEvent.TR_PAUSES);
		default:
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.event.IFormatterEvent#resume()
	 */
	public boolean resume() {
		switch (currentState) {
		case IEvent.ST_PAUSED:
			return changeState(IEvent.ST_OCCURRING, IEvent.TR_RESUMES);
		default:
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.event.IFormatterEvent#setCurrentState(short)
	 */
	public void setCurrentState(short newState) {
		currentState = newState;
	}

	/**
	 * @param newState
	 * @param transition
	 * @return
	 */
	protected synchronized boolean changeState(short newState, short transition) {
		int i, size;
		IEventListener listener;
		short previousState;

		if (transition == IEvent.TR_STOPS) {
			occurrences++;
		}

		previousState = currentState;
		currentState = newState;
/*
		System.err.println("FormatterEvent::changeState " + this.id + " " + this
		+ " mudou para " + newState);
*/		
		notifying = true;
		size = listeners.size();
		for (i = 0; i < size; i++) {
			listener = (IEventListener)listeners.get(i);
			listener.eventStateChanged(this, transition, previousState);
		}

		if (toBeAdded.size() > 0) {
			listeners.addAll(toBeAdded);
			toBeAdded.clear();
		}
		if (toBeRemoved.size() > 0) {
			listeners.removeAll(toBeRemoved);
			toBeRemoved.clear();
		}
		notifying = false;

		if (currentState == ST_ABORTED)
			currentState = IEvent.ST_SLEEPING;
		return true;
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.event.IFormatterEvent#clearEventListeners()
	 */
	public void clearEventListeners() {
		listeners.clear();
	}

	/**
	 * @param object
	 * @return
	 */
	public int compareTo(Object object) {
		IFormatterEvent otherEvent;

		if (object instanceof IFormatterEvent) {
			otherEvent = (IFormatterEvent)object;
			return id.compareTo(otherEvent.getId());
		}
		else
			return -1;
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.event.IFormatterEvent#getCurrentState()
	 */
	public short getCurrentState() {
		return currentState;
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.event.IFormatterEvent#getExecutionObject()
	 */
	public IExecutionObject getExecutionObject() {
		return executionObject;
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.event.IFormatterEvent#setExecutionObject(br.org.ginga.ncl.model.components.IExecutionObject)
	 */
	public void setExecutionObject(IExecutionObject object) {
		executionObject = object;
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.event.IFormatterEvent#getId()
	 */
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.event.IFormatterEvent#getOccurrences()
	 */
	public long getOccurrences() {
		return occurrences;
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.event.IFormatterEvent#removeEventListener(br.org.ginga.ncl.model.event.IEventListener)
	 */
	public synchronized boolean removeEventListener(IEventListener listener) {
		if (notifying) {
			toBeRemoved.add(listener);
			return true;
		}
		else {
			return listeners.remove(listener);
		}
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.event.IFormatterEvent#destroy()
	 */
	public void destroy() {
		toBeRemoved.addAll(listeners);
		// TODO, avoid to leave a link bind with an inconsistent event

		// After TODO
		/*
		 * toBeAdded = null; toBeRemoved = null; listeners = null; executionObject =
		 * null;
		 */
	}

	/**
	 * @param state
	 * @return
	 */
	public static String getStateName(short state) {
		switch (state) {
		case IEvent.ST_OCCURRING:
			return "occurring";

		case IEvent.ST_PAUSED:
			return "paused";

		case IEvent.ST_SLEEPING:
			return "sleeping";

		default:
			return null;
		}
	}
}