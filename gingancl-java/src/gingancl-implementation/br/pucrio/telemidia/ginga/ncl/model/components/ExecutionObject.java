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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import br.org.ginga.ncl.model.components.ICompositeExecutionObject;
import br.org.ginga.ncl.model.components.IExecutionObject;
import br.org.ginga.ncl.model.components.INodeNesting;
import br.org.ginga.ncl.model.event.IAnchorEvent;
import br.org.ginga.ncl.model.event.IAttributionEvent;
import br.org.ginga.ncl.model.event.IFormatterEvent;
import br.org.ginga.ncl.model.event.IPresentationEvent;
import br.org.ginga.ncl.model.event.ISelectionEvent;
import br.org.ginga.ncl.model.event.transition.IBeginEventTransition;
import br.org.ginga.ncl.model.event.transition.IEndEventTransition;
import br.org.ginga.ncl.model.event.transition.IEventTransition;
import br.org.ginga.ncl.model.presentation.ICascadingDescriptor;
import br.org.ginga.ncl.model.presentation.IFormatterRegion;
import br.org.ncl.animation.IAnimation;
import br.org.ncl.components.INode;
import br.org.ncl.connectors.IEvent;
import br.org.ncl.descriptor.IGenericDescriptor;
import br.org.ncl.interfaces.IContentAnchor;
import br.org.ncl.interfaces.ILabeledAnchor;
import br.org.ncl.interfaces.IPropertyAnchor;
import br.org.ncl.interfaces.IIntervalAnchor;
import br.org.ncl.interfaces.ILambdaAnchor;
import br.org.ncl.layout.ILayoutRegion;
import br.pucrio.telemidia.ginga.ncl.model.event.PresentationEvent;
import br.pucrio.telemidia.ginga.ncl.model.event.transition.BeginEventTransition;
import br.pucrio.telemidia.ginga.ncl.model.event.transition.EndEventTransition;
import br.pucrio.telemidia.ginga.ncl.model.presentation.CascadingDescriptor;

/**
 * The ExecutionObject class represents the implementation of a hypermedia node
 * (simple or composite) when being presented. An execution object brings
 * together not only the node information but also its presentation
 * characteristics (descriptor and perspective). Execution object presentation
 * are controlled by the formatter.
 */
public class ExecutionObject implements IExecutionObject {
	private static final long serialVersionUID = 2698226518899980405L;

	protected String id;

	protected INode dataObject;

	protected ICascadingDescriptor descriptor;

	protected double offsetTime;

	protected double startTime;

	protected IPresentationEvent wholeContent;

	private Map<INode,INode> nodeParentTable;

	protected Map<INode,ICompositeExecutionObject> parentTable;

	private boolean isCompiled;

	protected Map<String,IFormatterEvent> events;

	private List<IPresentationEvent> presentationEvents;

	private List<ISelectionEvent> selectionEvents;

	protected List<IFormatterEvent> otherEvents;

	protected int pauseCount;

	protected List<IEventTransition> transitionTable;

	private IFormatterEvent mainEvent;

	protected int currentTransitionIndex;

	protected int startTransitionIndex;

	public ExecutionObject(String id, INode node) {
		// System.err.println("ExecutionObject::ExecutionObject: NEW => " + id);

		this.id = id;
		dataObject = node;
		wholeContent = null;
		startTime = Double.POSITIVE_INFINITY;
		this.descriptor = null;

		nodeParentTable = new Hashtable<INode, INode>();
		parentTable = new Hashtable<INode, ICompositeExecutionObject>();

		isCompiled = false;

		events = new Hashtable<String, IFormatterEvent>();
		presentationEvents = new ArrayList<IPresentationEvent>();
		selectionEvents = new ArrayList<ISelectionEvent>();
		otherEvents = new ArrayList<IFormatterEvent>();
		pauseCount = 0;
		transitionTable = new ArrayList<IEventTransition>();
		mainEvent = null;
	}

	/**
	 * Receives a unique identifier, a data object and a descriptor, and creates a
	 * new execution object instance. The constructor automatically instantiates
	 * an execution object instance.
	 * 
	 * @param id
	 *          execution object unique identifier.
	 * @param node
	 *          data object
	 * @param descriptor
	 *          descriptor base for the cascading descriptor
	 */
	public ExecutionObject(String id, INode node,
			IGenericDescriptor descriptor) {
		this(id, node, new CascadingDescriptor(descriptor));
	}

	public ExecutionObject(String id, INode node,
			ICascadingDescriptor descriptor) {
		this(id, node);
		this.descriptor = descriptor;
	}

	public int compareToUsingId(IExecutionObject object) {
		return id.compareTo((String)object.getId());
	}

	public INode getDataObject() {
		return dataObject;
	}

	public ICascadingDescriptor getDescriptor() {
		return descriptor;
	}

	public String getId() {
		return id;
	}

	public ICompositeExecutionObject getParentObject() {
		return getParentObject(dataObject);
	}

	public ICompositeExecutionObject getParentObject(INode node) {
		INode parentNode;

		parentNode = (INode)nodeParentTable.get(node);
		if (parentNode != null) {
			return (ICompositeExecutionObject)parentTable.get(parentNode);
		}
		else {
			return null;
		}
	}

	public void addParentObject(ICompositeExecutionObject parentObject,
			INode parentNode) {
		addParentObject(dataObject, parentObject, parentNode);
	}

	public void addParentObject(INode node,
			ICompositeExecutionObject parentObject, INode parentNode) {
		nodeParentTable.put(node, parentNode);
		parentTable.put(parentNode, parentObject);
	}

	/**
	 * Configura o descritor do objeto de execucao.
	 * 
	 * @param cascadingDescriptor
	 *          novo descritor (ja' cascateado) para o objeto de execucao.
	 */
	public void setDescriptor(ICascadingDescriptor cascadingDescriptor) {
		this.descriptor = cascadingDescriptor;
	}

	/**
	 * Configura o descritor do objeto de execucao. Cria um descritor cascateado
	 * com o descritor passado como parametro.
	 * 
	 * @param descriptor
	 *          descritor para o objeto de execucao.
	 */
	public void setDescriptor(IGenericDescriptor descriptor) {
		this.descriptor = new CascadingDescriptor(descriptor);
	}

	/**
	 * Retorna a string do identificador unico do objeto de execucao.
	 * 
	 * @return String correpondendo ao identificador do objeto.
	 */
	public String toString() {
		return id.toString();
	}

	/**
	 * Insere um evento no conjunto de eventos do objeto de execucao.
	 * 
	 * @param event
	 *          evento a ser inserido.
	 * @return true se o evento foi inserido e false se o evento ja existia no
	 *         conjunto de eventos.
	 */
	public boolean addEvent(IFormatterEvent event) {
		if (events.containsKey(event.getId())) {
			return false;
		}

		events.put(event.getId(), event);
		if (event instanceof IPresentationEvent) {
			addPresentationEvent((IPresentationEvent)event);
		}
		else if (event instanceof ISelectionEvent) {
			selectionEvents.add((ISelectionEvent)event);
		}
		else {
			otherEvents.add(event);
		}

		return true;
	}

	public void addPresentationEvent(IPresentationEvent event) {
		IPresentationEvent auxEvent;
		double begin, auxBegin, end;
		int posBeg, posEnd, posMid;
		IBeginEventTransition beginTransition;
		IEndEventTransition endTransition;

		if (event.getAnchor() instanceof ILambdaAnchor) {
			presentationEvents.add(0, event);
			wholeContent = (IPresentationEvent)event;

			beginTransition = new BeginEventTransition(0, event);
			transitionTable.add(0, beginTransition);
			if (event.getEnd() >= 0) {
				endTransition = new EndEventTransition(event.getEnd(), event,
						beginTransition);
				transitionTable.add(endTransition);
			}
		}
		else {
			begin = event.getBegin();

			// undefined events are not inserted into transition table
			if (PresentationEvent.isUndefinedInstant(begin)) {
				return;
			}

			posBeg = 0;
			posEnd = presentationEvents.size() - 1;
			while (posBeg <= posEnd) {
				posMid = (posBeg + posEnd) / 2;
				auxEvent = (IPresentationEvent)presentationEvents.get(posMid);
				auxBegin = auxEvent.getBegin();
				if (begin < auxBegin) {
					posEnd = posMid - 1;
				}
				else if (begin > auxBegin) {
					posBeg = posMid + 1;
				}
				else {
					posBeg = posMid + 1;
					break;
				}
			}

			presentationEvents.add(posBeg, event);

			beginTransition = new BeginEventTransition(begin, event);
			addEventTransition(beginTransition);
			end = event.getEnd();

			if (!PresentationEvent.isUndefinedInstant(end)) {
				endTransition = new EndEventTransition(end, event, beginTransition);
				addEventTransition(endTransition);
			}
		}
	}

	public void addEventTransition(IEventTransition transition) {
		int beg, end, pos;
		IEventTransition auxTransition;

		// binary search
		beg = 0;
		end = transitionTable.size() - 1;
		while (beg <= end) {
			pos = (beg + end) / 2;
			auxTransition = (IEventTransition)transitionTable.get(pos);
			switch (transition.compareTo(auxTransition)) {
			case 0:
				// entrada corresponde a um evento que ja' foi inserido
				return;

			case -1:
				end = pos - 1;
				break;

			case 1:
				beg = pos + 1;
				break;
			}
		}
		transitionTable.add(beg, transition);
	}

	public void removeEventTransition(IPresentationEvent event) {
		int i, size;
		IEventTransition transition;

		size = transitionTable.size();
		for (i = 0; i < size; i++) {
			transition = (IEventTransition)transitionTable.get(i);
			if (transition.getEvent().equals(event)) {
				if (transition instanceof IBeginEventTransition
						&& ((IBeginEventTransition)transition).getEndTransition() != null) {
					transitionTable.remove(((IBeginEventTransition)transition)
							.getEndTransition());
				}
				transitionTable.remove(transition);
				break;
			}
		}
	}

	public int compareTo(IExecutionObject object) {
		int ret;

		ret = compareToUsingStartTime(object);
		if (ret == 0)
			return compareToUsingId(object);
		else
			return ret;
	}

	public int compareToUsingStartTime(IExecutionObject object) {
		double thisTime, otherTime;

		thisTime = startTime;
		otherTime = object.getExpectedStartTime();
		if (thisTime < otherTime)
			return -1;
		else if (thisTime > otherTime)
			return 1;
		else
			return 0;
	}

	/**
	 * Informa se o objeto de execucao contem o evento.
	 * 
	 * @return true se o evento estiver contido, false caso contrario.
	 * @param event
	 *          evento a ser pesquisado.
	 */
	public boolean containsEvent(IFormatterEvent event) {
		return events.containsKey(event.getId());
	}

	/**
	 * Retorna o evento cujo identificador unico corresponde ao identificador
	 * passado como parametro.
	 * 
	 * @param id
	 *          identificador unico do evento a ser pesquisado.
	 * @return o evento, ou null, caso nao exista um evento no objeto com o
	 *         identificador passado como parametro.
	 */
	public IFormatterEvent getEvent(String id) {
		if (events != null && events.containsKey(id)) {
			return events.get(id);
		}
		return null;
	}

	/**
	 * Retorna um iterador para percorrer os eventos do objeto de execucao.
	 * 
	 * @return iterador para percorrer os eventos.
	 */
	public Iterator<IFormatterEvent> getEvents() {
		return events.values().iterator();
	}

	/**
	 * Retorna o tempo esperado para iniciar a apresentacao do objeto de
	 * execucao.
	 * 
	 * @return tempo esperado para iniciar a exibicao.
	 */
	public double getExpectedStartTime() {
		return startTime;
	}

	/**
	 * Retorna o evento de apresentacao que corresponde a exibicao do
	 * objeto inteiro.
	 * 
	 * @return evento de apresentacao do conteudo inteiro.
	 */
	public IPresentationEvent getWholeContentPresentationEvent() {
		return wholeContent;
	}

	/**
	 * Especifica o tempo esperado para inicar a exibicao do objeto de execucao.
	 * 
	 * @param t
	 *        tempo esperado para iniciar a exibicao.
	 */
	public void setStartTime(double t) {
		startTime = t;
	}

	public void updateEventDurations() {
		int i, size;

		size = presentationEvents.size();
		for (i = 0; i < size; i++) {
			updateEventDuration((IPresentationEvent)presentationEvents.get(i));
		}
	}

	public void updateEventDuration(IPresentationEvent event) {
		double duration;

		if (!containsEvent(event)) {
			return;
		}

		// decidindo entre a duracao implicita ou explicita
		if (descriptor != null && descriptor.getExplicitDuration() != null
				&& event == wholeContent) {
			duration = descriptor.getExplicitDuration().longValue();
		}
		else if (event.getDuration() > 0) {
			duration = event.getDuration();
		}
		else {
			duration = 0;
		}

		if (duration < 0) {
			event.setDuration(Double.NaN);
			/*
			 * event.setDuration(new LinearCostFunctionDuration(Double.NaN, 0,
			 * Double.POSITIVE_INFINITY, dontCareFunction));
			 */
		}
		else {
			event.setDuration(duration);
		}
	}

	public Iterator<IPresentationEvent> getPresentationEvents() {
		return presentationEvents.iterator();
	}

	public boolean removeEvent(IFormatterEvent event) {
		if (!containsEvent(event)) {
			return false;
		}

		if (event instanceof IPresentationEvent) {
			presentationEvents.remove(event);
			removeEventTransition((IPresentationEvent)event);
		}
		else if (event instanceof ISelectionEvent) {
			selectionEvents.remove(event);
		}
		else {
			otherEvents.remove(event);
		}

		events.remove(event.getId());
		return true;
	}

	public Iterator<ISelectionEvent> getSelectionEvents() {
		return selectionEvents.iterator();
	}

	public boolean isCompiled() {
		return isCompiled;
	}

	public void setCompiled(boolean status) {
		isCompiled = status;
	}

	public void removeNode(INode node) {
		INode parentNode;

		if (node != dataObject) {
			parentNode = (INode)nodeParentTable.get(node);
			if (parentNode != null) {
				parentTable.remove(parentNode);
				nodeParentTable.remove(node);
			}
		}
	}

	public Iterator<INode> getNodes() {
		List<INode> nodes;

		nodes = new ArrayList<INode>(nodeParentTable.keySet());
		if (!nodeParentTable.containsKey(dataObject)) {
			nodes.add(dataObject);
		}
		return nodes.iterator();
	}

	public INodeNesting getNodePerspective() {
		return getNodePerspective(dataObject);
	}

	public INodeNesting getNodePerspective(INode node) {
		INode parentNode;
		INodeNesting perspective;
		ICompositeExecutionObject parentObject;

		parentNode = (INode)nodeParentTable.get(node);
		if (parentNode == null) {
			if (dataObject == node || nodeParentTable.containsKey(node)) {
				perspective = new NodeNesting();
			}
			else {
				return null;
			}
		}
		else {
			parentObject = (ICompositeExecutionObject)parentTable.get(parentNode);
			perspective = parentObject.getNodePerspective(parentNode);
		}
		perspective.insertAnchorNode(node);
		return perspective;
	}

	public List<IExecutionObject> getObjectPerspective() {
		return getObjectPerspective(dataObject);
	}

	public List<IExecutionObject> getObjectPerspective(INode node) {
		INode parentNode;
		List<IExecutionObject> perspective;
		ICompositeExecutionObject parentObject;

		parentNode = (INode)nodeParentTable.get(node);
		if (parentNode == null) {
			if (dataObject == node || nodeParentTable.containsKey(node)) {
				perspective = new ArrayList<IExecutionObject>();
			}
			else {
				return null;
			}
		}
		else {
			parentObject = (ICompositeExecutionObject)parentTable.get(parentNode);
			perspective = parentObject.getObjectPerspective(parentNode);
		}
		perspective.add(this);
		return perspective;
	}

	public Iterator<INode> getParentNodes() {
		return nodeParentTable.values().iterator();
	}

	public IFormatterEvent getMainEvent() {
		return mainEvent;
	}

	public boolean prepare(IFormatterEvent event, double offsetTime) {
		Iterator<ICompositeExecutionObject> parentObjects;
		CompositeExecutionObject parentObject;
		int i, size;
		IEventTransition transition;
		double startTime = 0.0;
		IFormatterEvent auxEvent;
		IAttributionEvent attributeEvent;
		IPropertyAnchor attributeAnchor;

		if (event == null || !containsEvent(event)/*
				|| PresentationEvent.isUndefinedInstant(event.getBegin())
				|| PresentationEvent.isUndefinedInstant(event.getEnd())*/) {
			return false;
		}

		mainEvent = event;

		if (mainEvent.getCurrentState() != IEvent.ST_SLEEPING) {
			return false;
		}
		
		if (mainEvent instanceof IAnchorEvent) {
			IContentAnchor contentAnchor = ((IAnchorEvent)mainEvent).getAnchor();
			if (contentAnchor != null &&
					contentAnchor instanceof ILabeledAnchor) {
				for(Entry<INode,ICompositeExecutionObject> entries : parentTable.entrySet()){
					mainEvent.addEventListener((CompositeExecutionObject)entries.getValue());
				}
				return true;
			}
		}
		
		if (mainEvent instanceof IPresentationEvent) {
			startTime = ((IPresentationEvent)mainEvent).getBegin() + offsetTime;
			if (startTime > ((IPresentationEvent)mainEvent).getEnd()) {
				return false;
			}
		}

		parentObjects = parentTable.values().iterator();
		while (parentObjects.hasNext()) {
			parentObject = (CompositeExecutionObject)parentObjects.next();
			// register parent as a mainEvent listener
			mainEvent.addEventListener(parentObject);
		}

		if (mainEvent == wholeContent && startTime == 0.0) {
			startTransitionIndex = 0;
		}
		else {
			size = transitionTable.size();
			startTransitionIndex = 0;
			while (startTransitionIndex < size) {
				transition = (IEventTransition)transitionTable
						.get(startTransitionIndex);
				if (transition.getTime() >= startTime) {
					break;
				}

				if (transition instanceof IBeginEventTransition) {
					transition.getEvent().setCurrentState(IEvent.ST_OCCURRING);
				}
				else {
					transition.getEvent().setCurrentState(IEvent.ST_SLEEPING);
					transition.getEvent().incrementOccurrences();
				}
				startTransitionIndex++;
			}
		}

		size = otherEvents.size();
		for (i = 0; i < size; i++) {
			auxEvent = (IFormatterEvent)otherEvents.get(i);
			if (auxEvent instanceof IAttributionEvent) {
				attributeEvent = (IAttributionEvent)auxEvent;
				attributeAnchor = attributeEvent.getAnchor();
				if (attributeAnchor.getPropertyValue() != null) {
					attributeEvent.setValue((String) attributeAnchor.getPropertyValue());
				}
			}
		}

		this.offsetTime = startTime;
		currentTransitionIndex = startTransitionIndex;
		return true;
	}

	public boolean start() {
		IEventTransition transition;

		if (mainEvent == null && wholeContent == null) {
			return false;
		}

		if (mainEvent != null && mainEvent.getCurrentState() != IEvent.ST_SLEEPING) {
			return false;
		}

		if (mainEvent == null) {
			prepare(wholeContent, 0.0);
		}
		
		if (mainEvent instanceof IAnchorEvent) {
			IContentAnchor contentAnchor = ((IAnchorEvent)mainEvent).getAnchor();
			if (contentAnchor != null &&
					(contentAnchor instanceof ILabeledAnchor)) {

				mainEvent.start();
				return true;
			}
		}

		while (currentTransitionIndex < transitionTable.size()) {
			transition = (IEventTransition)transitionTable
					.get(currentTransitionIndex);
			if (transition.getTime() <= offsetTime) {
				if (transition instanceof IBeginEventTransition) {
					transition.getEvent().start();
				}
				currentTransitionIndex++;
			}
			else {
				break;
			}
		}

		return true;
	}

	public void updateTransitionTable(double currentTime) {
		IEventTransition transition;
		//TransitionDispatcher transitionDispatcher;

		while (currentTransitionIndex < transitionTable.size()) {
			transition = (IEventTransition)transitionTable
					.get(currentTransitionIndex);
			if (transition.getTime() <= currentTime) {
				/*transitionDispatcher = new TransitionDispatcher(transition);
				transitionDispatcher.start();*/
				if(transition instanceof IBeginEventTransition)
					transition.getEvent().start();
				else
					transition.getEvent().stop();
				currentTransitionIndex++;
			}
			else {
				break;
			}
		}
	}

	public IEventTransition getNextTransition() {
		IEventTransition transition;

		if (mainEvent == null || mainEvent.getCurrentState() == IEvent.ST_SLEEPING
				|| !(mainEvent instanceof IPresentationEvent)) {
			return null;
		}

		if (currentTransitionIndex < transitionTable.size()) {
			transition = (IEventTransition)transitionTable
					.get(currentTransitionIndex);
			if (transition.getTime() <= ((IPresentationEvent)mainEvent).getEnd()) {
				return transition;
			}
		}

		return null;
	}

	public boolean stop() {
		int i, size;
		IEventTransition transition;

		if (mainEvent == null || mainEvent.getCurrentState() == IEvent.ST_SLEEPING) {
			return false;
		}

		if(mainEvent instanceof IPresentationEvent){
			size = transitionTable.size();
			for (i = currentTransitionIndex; i < size; i++) {
				transition = (IEventTransition)transitionTable.get(i);
	
				if (transition.getTime() > 
						((IPresentationEvent)mainEvent).getEnd()) {
					transition.getEvent().setCurrentState(IEvent.ST_SLEEPING);
				}
				else if (transition instanceof IEndEventTransition) {
					transition.getEvent().stop();
				}
			}
		} else if (mainEvent instanceof IAnchorEvent) {
			IContentAnchor contentAnchor = ((IAnchorEvent)mainEvent).getAnchor();
			if (contentAnchor != null &&
					contentAnchor instanceof ILabeledAnchor) {
				mainEvent.stop();
			}
		}

		currentTransitionIndex = startTransitionIndex;
		pauseCount = 0;
		return true;
	}

	public boolean abort() {
		int i, size;
		IEventTransition transition;
		short objectState;

		if (mainEvent == null) {
			return false;
		}

		objectState = mainEvent.getCurrentState();

		if (objectState == IEvent.ST_SLEEPING) {
			return false;
		}

		size = transitionTable.size();
		if(mainEvent instanceof IPresentationEvent){
			for (i = currentTransitionIndex; i < size; i++) {
				transition = (IEventTransition)transitionTable.get(i);
	
				if (transition.getTime() > ((IPresentationEvent)mainEvent).getEnd()) {
					transition.getEvent().setCurrentState(IEvent.ST_SLEEPING);
				}
				else if (transition instanceof IEndEventTransition) {
					transition.getEvent().abort();
				}
			}
		}

		currentTransitionIndex = startTransitionIndex;
		pauseCount = 0;
		return true;
	}

	public boolean pause() {
		if (mainEvent == null || mainEvent.getCurrentState() == IEvent.ST_SLEEPING) {
			return false;
		}
		
		if(pauseCount ==0){
			Iterator<IFormatterEvent> events = getEvents();
			while(events.hasNext()){
				IFormatterEvent event = events.next();

				if (event.getCurrentState() == IEvent.ST_OCCURRING) {
					event.pause();
				}
			}
		}

		pauseCount++;
		return true;
	}

	public boolean resume() {
		if (pauseCount == 0) {
			return false;
		}
		else {
			pauseCount--;
			if (pauseCount > 0) {
				return false;
			}
		}

		if (pauseCount == 0) {
			Iterator<IFormatterEvent> events = getEvents();
			while(events.hasNext()){
				IFormatterEvent event = events.next();
				if (event.getCurrentState() == IEvent.ST_PAUSED) {
					event.resume();
				}
			}
		}
		return true;
	}

	public boolean setPropertyValue(
			IAttributionEvent event, Object value, IAnimation animation) {

		String propName;
		IFormatterRegion region;
		ILayoutRegion ncmRegion;

		if (!containsEvent(event) || value == null) {
			return false;
		}

		propName = event.getAnchor().getPropertyName();
		if (propName.equals("size")) {
			String[] params = ((String)value).split(",", -1);
			if (params.length == 2) {
				trimStrings(params);
				region = descriptor.getFormatterRegion();
				ncmRegion = region.getLayoutRegion();
				if (isPercentualValue(params[0])) {
					ncmRegion.setWidth(getPercentualValue(params[0]), true);
				}
				else {
					ncmRegion.setWidth(Integer.parseInt(params[0]), false);
				}
				if (isPercentualValue(params[1])) {
					ncmRegion.setHeight(getPercentualValue(params[1]), true);
				}
				else {
					ncmRegion.setHeight(Integer.parseInt(params[1]), false);
				}
				region.updateRegionBounds(animation);
				event.stop();
				return true;
			}
		}
		else if (propName.equals("location")) {
			String[] params = ((String)value).split(",", -1);
			if (params.length == 2) {
				trimStrings(params);
				region = descriptor.getFormatterRegion();
				ncmRegion = region.getLayoutRegion();
				if (isPercentualValue(params[0])) {
					ncmRegion.setLeft(getPercentualValue(params[0]), true);
				}
				else {
					ncmRegion.setLeft(Integer.parseInt(params[0]), false);
				}
				if (isPercentualValue(params[1])) {
					ncmRegion.setTop(getPercentualValue(params[1]), true);
				}
				else {
					ncmRegion.setTop(Integer.parseInt(params[1]), false);
				}
				region.updateRegionBounds(animation);
				event.stop();
				return true;
			}
		}
		else if (propName.equals("bounds")) {
			String[] params = ((String)value).split(",", -1);
			if (params.length == 4) {
				trimStrings(params);
				region = descriptor.getFormatterRegion();
				ncmRegion = region.getLayoutRegion();

				if (ncmRegion.compareWidthSize(params[2]) <= 0) {
					// first resize the region, then update its location
					if (isPercentualValue(params[2])) {
						ncmRegion.setWidth(getPercentualValue(params[2]), true);
					}
					else {
						ncmRegion.setWidth(Integer.parseInt(params[2]), false);
					}
					if (isPercentualValue(params[0])) {
						ncmRegion.setLeft(getPercentualValue(params[0]), true);
					}
					else {
						ncmRegion.setLeft(Integer.parseInt(params[0]), false);
					}
				}
				else {
					// first update and then resize
					if (isPercentualValue(params[0])) {
						ncmRegion.setLeft(getPercentualValue(params[0]), true);
					}
					else {
						ncmRegion.setLeft(Integer.parseInt(params[0]), false);
					}
					if (isPercentualValue(params[2])) {
						ncmRegion.setWidth(getPercentualValue(params[2]), true);
					}
					else {
						ncmRegion.setWidth(Integer.parseInt(params[2]), false);
					}
				}
				if (ncmRegion.compareHeightSize(params[3]) <= 0) {
					// first resize the region, then update its location
					if (isPercentualValue(params[3])) {
						ncmRegion.setHeight(getPercentualValue(params[3]), true);
					}
					else {
						ncmRegion.setHeight(Integer.parseInt(params[3]), false);
					}
					if (isPercentualValue(params[1])) {
						ncmRegion.setTop(getPercentualValue(params[1]), true);
					}
					else {
						ncmRegion.setTop(Integer.parseInt(params[1]), false);
					}
				}
				else {
					// first update and then resize
					if (isPercentualValue(params[1])) {
						ncmRegion.setTop(getPercentualValue(params[1]), true);
					}
					else {
						ncmRegion.setTop(Integer.parseInt(params[1]), false);
					}
					if (isPercentualValue(params[3])) {
						ncmRegion.setHeight(getPercentualValue(params[3]), true);
					}
					else {
						ncmRegion.setHeight(Integer.parseInt(params[3]), false);
					}
				}
				region.updateRegionBounds(animation);
				event.stop();
				return true;
			}
		}
		else if (propName.equals("top") || propName.equals("left")
				|| propName.equals("bottom") || propName.equals("right")
				|| propName.equals("width") || propName.equals("height")) {
			region = descriptor.getFormatterRegion();
			ncmRegion = region.getLayoutRegion();
			if (propName.equals("top")) {
				if (isPercentualValue(value.toString())) {
					ncmRegion.setTop(getPercentualValue(value.toString()), true);
				}
				else {
					ncmRegion.setTop(Integer.parseInt(value.toString()), false);
				}
			}
			else if (propName.equals("left")) {
				if (isPercentualValue(value.toString())) {
					ncmRegion.setLeft(getPercentualValue(value.toString()), true);
				}
				else {
					ncmRegion.setLeft(Integer.parseInt(value.toString()), false);
				}
			}
			else if (propName.equals("width")) {
				if (isPercentualValue(value.toString())) {
					ncmRegion.setWidth(getPercentualValue(value.toString()), true);
				}
				else {
					ncmRegion.setWidth(Integer.parseInt(value.toString()), false);
				}
			}
			else if (propName.equals("height")) {
				if (isPercentualValue(value.toString())) {
					ncmRegion.setHeight(getPercentualValue(value.toString()), true);
				}
				else {
					ncmRegion.setHeight(Integer.parseInt(value.toString()), false);
				}
			}
			else if (propName.equals("bottom")) {
				if (isPercentualValue(value.toString())) {
					ncmRegion.setBottom(getPercentualValue(value.toString()), true);
				}
				else {
					ncmRegion.setBottom(Integer.parseInt(value.toString()), false);
				}
			}
			else if (propName.equals("right")) {
				if (isPercentualValue(value.toString())) {
					ncmRegion.setRight(getPercentualValue(value.toString()), true);
				}
				else {
					ncmRegion.setRight(Integer.parseInt(value.toString()), false);
				}
			}
			region.updateRegionBounds(animation);
			event.stop();
			return true;
		}
		return false;
	}

	private void trimStrings(String[] params) {
		int i;

		for (i = 0; i < params.length; i++) {
			params[i] = params[i].trim();
		}
	}

	/**
	 * Retorna o valor percentual a partir de uma string representando esse valor
	 * 
	 * @param value
	 *          string com o valor a ser computado
	 * @return valor percentual representando pelo parametro. Se valor maior que
	 *         100, retorna 100; se valor menor que zero, retorna 0.
	 */
	private float getPercentualValue(String value) {
		String actualValue;
		float floatValue;

		// retirar o caracter percentual da string
		actualValue = value.substring(0, value.length() - 1);
		// converter para float
		floatValue = (new Float(actualValue)).floatValue();

		// se menor que zero, retornar zero
		if (floatValue < 0)
			floatValue = 0;
		else if (floatValue > 100)
			// se maior que 100, retornar 100
			floatValue = 100;

		// retornar valor percentual
		return floatValue;
	}

	/**
	 * Testa se uma string indica um valor percentual
	 * 
	 * @param value
	 *          string com um valor
	 * @return true se o valor e' percentual; false caso contrario.
	 */
	private boolean isPercentualValue(String value) {
		if (value.endsWith("%"))
			return true;
		else
			return false;
	}

	public boolean unprepare() {
		Iterator<ICompositeExecutionObject> parentObjects;
		CompositeExecutionObject parentObject;

		if (mainEvent == null || mainEvent.getCurrentState() != IEvent.ST_SLEEPING) {
			return false;
		}

		parentObjects = parentTable.values().iterator();
		while (parentObjects.hasNext()) {
			parentObject = (CompositeExecutionObject)parentObjects.next();
			// register parent as a mainEvent listener
			mainEvent.removeEventListener(parentObject);
		}

		mainEvent = null;
		return true;
	}

	public void select(int accessCode, double currentTime) {
		int i, size;
		ISelectionEvent selectionEvent;
		IIntervalAnchor intervalAnchor;
		List<ISelectionEvent> selectedEvents;

		selectedEvents = new ArrayList<ISelectionEvent>();
		size = selectionEvents.size();
		for (i = 0; i < size; i++) {
			selectionEvent = (ISelectionEvent)selectionEvents.get(i);
			if (selectionEvent.getSelectionCode() == accessCode) {
				if (selectionEvent.getAnchor() instanceof IIntervalAnchor) {
					intervalAnchor = (IIntervalAnchor)selectionEvent.getAnchor();
					if (intervalAnchor.getBegin() <= currentTime
							&& currentTime <= intervalAnchor.getEnd()) {
						selectedEvents.add(selectionEvent);
					}
				}
				else {
					selectedEvents.add(selectionEvent);
				}
			}
		}

		size = selectedEvents.size();
		for (i = 0; i < size; i++) {
			selectionEvent = (ISelectionEvent)selectedEvents.get(i);
			selectionEvent.start();
		}
	}
	
	public Set<Integer> getInputEvents() {
		Set<Integer> evts = new HashSet<Integer>();
		for(Entry<String, IFormatterEvent> entry : events.entrySet()){
			IFormatterEvent event = entry.getValue();
			if(event instanceof ISelectionEvent){
				ISelectionEvent selEvent = (ISelectionEvent)event;
				int keycode = selEvent.getSelectionCode();
				evts.add(keycode);
			}
		}
		return evts;
	}

	public boolean destroy() {
		Iterator<IFormatterEvent> objectEvents;
		IFormatterEvent event;

		/*
		 * Iterator parentNodes; INode parentNode; ICompositeExecutionObject
		 * parentObject;
		 */

		transitionTable.clear();
		transitionTable = null;

		wholeContent = null;
		objectEvents = events.values().iterator();
		while (objectEvents.hasNext()) {
			event = (IFormatterEvent)objectEvents.next();
			event.destroy();
		}
		events.clear();
		events = null;
		presentationEvents.clear();
		presentationEvents = null;
		selectionEvents.clear();
		selectionEvents = null;
		otherEvents.clear();
		otherEvents = null;

		/*
		 * parentNodes = nodeParentTable.values().iterator(); while
		 * (parentNodes.hasNext()) { parentNode = (INode)parentNodes.next();
		 * parentObject = (ICompositeExecutionObject)parentTable.get(parentNode);
		 * parentObject.removeExecutionObject(this); }
		 */

		parentTable.clear();
		parentTable = null;
		nodeParentTable.clear();
		nodeParentTable = null;
		System.gc();
		return true;
	}
}