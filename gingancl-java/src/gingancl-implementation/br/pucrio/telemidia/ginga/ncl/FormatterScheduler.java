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
package br.pucrio.telemidia.ginga.ncl;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import br.org.ginga.core.io.ISurface;
import br.org.ginga.ncl.IFormatterScheduler;
import br.org.ginga.ncl.IFormatterSchedulerListener;
import br.org.ginga.ncl.adapters.IFormatterPlayerAdapter;
import br.org.ginga.ncl.adapters.IPlayerAdapterManager;
import br.org.ginga.ncl.adapters.procedural.IProceduralPlayerAdapter;
import br.org.ginga.ncl.converter.ObjectCreationForbiddenException;
import br.org.ginga.ncl.focus.IFormatterFocusManager;
import br.org.ginga.ncl.model.components.ICompositeExecutionObject;
import br.org.ginga.ncl.model.components.IExecutionObject;
import br.org.ginga.ncl.model.components.INodeNesting;
import br.org.ginga.ncl.model.components.IProceduralExecutionObject;
import br.org.ginga.ncl.model.event.IAttributionEvent;
import br.org.ginga.ncl.model.event.IEventListener;
import br.org.ginga.ncl.model.event.IFormatterEvent;
import br.org.ginga.ncl.model.event.IPresentationEvent;
import br.org.ginga.ncl.model.link.ILinkActionListener;
import br.org.ginga.ncl.model.link.ILinkAssignmentAction;
import br.org.ginga.ncl.model.link.ILinkSimpleAction;
import br.org.ginga.ncl.model.presentation.ICascadingDescriptor;
import br.org.ginga.ncl.model.presentation.IFormatterLayout;
import br.org.ginga.ncl.model.switches.IExecutionObjectSwitch;
import br.org.ginga.ncl.model.switches.ISwitchEvent;
import br.org.ncl.animation.IAnimation;
import br.org.ncl.components.ICompositeNode;
import br.org.ncl.components.IContentNode;
import br.org.ncl.components.INode;
import br.org.ncl.components.INodeEntity;
import br.org.ncl.connectors.IEvent;
import br.org.ncl.connectors.ISimpleAction;
import br.org.ncl.descriptor.DescriptorUtil;
import br.org.ncl.interfaces.IAnchor;
import br.org.ncl.interfaces.IPort;
import br.org.ncl.interfaces.IPropertyAnchor;
import br.org.ncl.interfaces.IContentAnchor;
import br.org.ncl.interfaces.ISwitchPort;
import br.org.ncl.reuse.IReferNode;
import br.pucrio.telemidia.ginga.ncl.adaptation.context.PresentationContext;
import br.pucrio.telemidia.ginga.ncl.adaptation.context.RuleAdapter;
import br.pucrio.telemidia.ginga.ncl.converter.FormatterConverter;
import br.pucrio.telemidia.ginga.ncl.focus.FormatterFocusManager;
import br.pucrio.telemidia.ginga.ncl.model.components.NodeNesting;
import br.pucrio.telemidia.ginga.ncl.model.link.LinkSimpleAction;
import br.pucrio.telemidia.ginga.ncl.model.presentation.FormatterLayout;

/**
 * 
 *
 */
public class FormatterScheduler implements ILinkActionListener,
		IFormatterScheduler, IEventListener {

	/**
	 * 
	 */
	private RuleAdapter ruleAdapter;

	/**
	 * 
	 */
	private IPlayerAdapterManager playerManager;

	/**
	 * 
	 */
	private FormatterConverter compiler;

	/**
	 * 
	 */
	private IFormatterLayout layoutManager;

	/**
	 * 
	 */
	private IFormatterFocusManager focusManager;

	/**
	 * 
	 */
	private List schedulerListeners;

	/**
	 * 
	 */
	private List documentEvents;

	/**
	 * 
	 */
	private Map documentStatus;

	/**
	 * @param playerManager
	 * @param ruleAdapter
	 * @param compiler
	 */
	public FormatterScheduler(IPlayerAdapterManager playerManager,
			RuleAdapter ruleAdapter, FormatterConverter compiler) {
		this.playerManager = playerManager;
		this.ruleAdapter = ruleAdapter;

		layoutManager = new FormatterLayout();
		focusManager = new FormatterFocusManager(this.playerManager);

		schedulerListeners = new Vector();
		this.compiler = compiler;
		documentEvents = new Vector();
		documentStatus = new Hashtable();
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.IFormatterScheduler#getLayoutManager()
	 */
	public IFormatterLayout getLayoutManager() {
		return layoutManager;
	}

	/**
	 * @param event
	 * @return
	 */
	private boolean isDocumentRunning(IFormatterEvent event) {
		IExecutionObject executionObject;
		ICompositeExecutionObject parentObject;
		IFormatterEvent documentEvent;
		Boolean status;

		executionObject = event.getExecutionObject();
		parentObject = executionObject.getParentObject();
		if (parentObject != null) {
			while (parentObject.getParentObject() != null) {
				executionObject = parentObject;
				parentObject = parentObject.getParentObject();
			}
			documentEvent = executionObject.getWholeContentPresentationEvent();
		}
		else {
			documentEvent = event;
		}

		status = (Boolean)documentStatus.get(documentEvent);
		if (status != null) {
			return status.booleanValue();
		}
		else {
			// System.err.println("FormatterScheduler::isDocumentRunning FALSE for " +
			// documentEvent.getId());
			return false;
		}
	}

	/**
	 * @param object
	 * @param objectPlayer
	 * @param nodeId
	 */
	private void setTimeBaseObject(IExecutionObject object,
			IFormatterPlayerAdapter objectPlayer, String nodeId) {
		IExecutionObject documentObject, parentObject, timeBaseObject;
		INode documentNode;
		INode compositeNode;
		INode timeBaseNode;
		INodeNesting perspective, compositePerspective;
		IFormatterPlayerAdapter timeBasePlayer;

		if (nodeId.lastIndexOf('#') >= 0) {
			return;
		}

		documentObject = object;
		parentObject = documentObject.getParentObject();
		if (parentObject != null) {
			while (parentObject.getParentObject() != null) {
				documentObject = parentObject;
				if (documentObject.getDataObject() instanceof IReferNode) {
					break;
				}
				parentObject = documentObject.getParentObject();
			}
		}

		documentNode = documentObject.getDataObject();
		if (documentNode instanceof IReferNode) {
			compositeNode = (INodeEntity)((IReferNode)documentNode)
					.getReferredEntity();
		}
		else {
			compositeNode = documentNode;
		}

		if (!(compositeNode instanceof ICompositeNode)) {
			return;
		}

		timeBaseNode = ((ICompositeNode)compositeNode).recursivelyGetNode(nodeId);
		if (timeBaseNode == null || !(timeBaseNode instanceof IContentNode)) {
			return;
		}

		perspective = new NodeNesting(timeBaseNode.getPerspective());
		if (documentNode instanceof IReferNode) {
			perspective.removeHeadNode();
			compositePerspective = new NodeNesting(documentNode.getPerspective());
			compositePerspective.append(perspective);
			perspective = compositePerspective;
		}
		try {
			timeBaseObject = compiler.getExecutionObject(perspective, null, compiler
					.getDepthLevel());
			if (timeBaseObject != null) {
				timeBasePlayer = playerManager.getPlayer(timeBaseObject);
				if (timeBasePlayer != null) {
					objectPlayer
							.setTimeBasePlayer(timeBasePlayer);
				}
			}
		}
		catch (ObjectCreationForbiddenException exc) {
			return;
		}
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.link.ILinkActionListener#runAction(br.org.ginga.ncl.model.link.ILinkSimpleAction)
	 */
	public void runAction(ILinkSimpleAction action) {
		runAction(action.getEvent(), action);
	}

	/**
	 * @param event
	 * @param action
	 */
	private void runAction(IFormatterEvent event, ILinkSimpleAction action) {
		IExecutionObject executionObject;
		ICascadingDescriptor descriptor;
		INodeEntity dataObject;
		IFormatterPlayerAdapter player;
		short actionType;
		String attName;
		Object attValue;
		IAnimation animation;

		// long time = System.currentTimeMillis();
/*
		System.err.println("FormatterScheduler::runAction " + action.getType() +
				" over " + event.getExecutionObject().getId());
*/
		executionObject = event.getExecutionObject();

		if (isDocumentRunning(event) && !executionObject.isCompiled()) {
			compiler.compileExecutionObjectLinks(executionObject, compiler
					.getDepthLevel());
		}

		dataObject = (INodeEntity)executionObject.getDataObject().getDataEntity();

		if (dataObject instanceof IContentNode
				&& ((IContentNode)dataObject).getNodeType().equalsIgnoreCase(
						IContentNode.SETTING_NODE)
				&& action instanceof ILinkAssignmentAction
				&& event instanceof IAttributionEvent
				&& ((IAttributionEvent)event)
						.setValue((String)((ILinkAssignmentAction)action).getValue())) {

			attName = ((IAttributionEvent)
					event).getAnchor().getPropertyName();

			attValue = ((ILinkAssignmentAction)action).getValue();

			event.start();
			if (attName != null) {
				if (attName.equals("currentFocus") &&
						attValue != null) {

					focusManager.setFocus(attValue.toString());

				} else if (attName.equals("currentKeyMaster") &&
						attValue != null) {

					focusManager.setKeyMaster(attValue.toString());

				} else {
					PresentationContext.getInstance().setPropertyValue(
							attName, (String)attValue);
				}
			}

			event.stop();

		} else if (executionObject instanceof IExecutionObjectSwitch &&
				event instanceof ISwitchEvent) {

			runActionOverSwitch(
					(IExecutionObjectSwitch)executionObject,
					(ISwitchEvent)event, action);

		} else if (executionObject instanceof ICompositeExecutionObject &&
				(executionObject.getDescriptor() == null ||
				executionObject.getDescriptor().getPlayerName() == null)) {

			runActionOverComposition(
					(ICompositeExecutionObject)executionObject, action);

		} else if (event instanceof IAttributionEvent) {
			runActionOverProperty(event, action);

		} else {
			player = playerManager.getPlayer(executionObject);
			if (player == null) {
				System.err.print("FormatterScheduler::runAction ");
				System.err.println("player is null for " + executionObject.getId());
				return;
			}
			
			if (executionObject instanceof IProceduralExecutionObject &&
					!(event instanceof IAttributionEvent)) {

				runProceduralAction(
						(IProceduralExecutionObject)executionObject,
						event, player, action);

				return;
			}

			actionType = action.getType();
			switch (actionType) {
			case ISimpleAction.ACT_START:
				if (isDocumentRunning(event)) {
					if (!player.hasPrepared()) {
						if (ruleAdapter.adaptDescriptor(executionObject)) {
							descriptor = executionObject.getDescriptor();
							if (descriptor != null) {
								descriptor.setFormatterRegion(layoutManager);
							}
						}

						player.prepare(executionObject, (IPresentationEvent)event);

						// look for a reference time base player
						if (executionObject.getDescriptor() != null) {
							attValue = executionObject.getDescriptor().getParameterValue(
								"x-timeBaseObject");
							if (attValue != null) {
								setTimeBaseObject(executionObject, player, attValue.toString());
							}
						}

						ISurface surface;
						surface = player.getObjectDisplay();
						//Component component;
						//component = player.getObjectDisplay(executionObject);
						if (surface != null) {
							layoutManager.prepareFormatterRegion(
									executionObject, surface);
						}
						
					}

					event.addEventListener(this);

					/*
					 * time = System.currentTimeMillis() - time; if (time > 0) {
					 * System.err.println("FormatterScheduler::runAction " +
					 * executionObject.getId() + " PREP-TIME = " + time); }
					 */

					player.start();
				}
				break;

			case ISimpleAction.ACT_PAUSE:
				player.pause();
				break;

			case ISimpleAction.ACT_RESUME:
				player.resume();
				break;

			case ISimpleAction.ACT_ABORT:
				player.abort();
				break;

			case ISimpleAction.ACT_STOP:
				player.stop();
				break;
			}
		}
	}

	private void runActionOverProperty(
			IFormatterEvent event, ILinkSimpleAction action) {

		short actionType;
		String propValue;

		IExecutionObject executionObject;
		IFormatterPlayerAdapter player;
		IAnimation anim;

		anim = ((ILinkAssignmentAction)action).getAnimation();
		executionObject = event.getExecutionObject();
		player = playerManager.getPlayer(executionObject);
		
		actionType = action.getType();

		switch (actionType) {
		case ISimpleAction.ACT_START:
		case ISimpleAction.ACT_PAUSE:
		case ISimpleAction.ACT_RESUME:
		case ISimpleAction.ACT_ABORT:
		case ISimpleAction.ACT_STOP:
			break;

		case ISimpleAction.ACT_SET:
			if (event.getCurrentState() != IEvent.ST_SLEEPING) {
				return;
			}

			propValue = ((ILinkAssignmentAction)action).getValue().toString();
			event.start();
			((IAttributionEvent)event).setValue(propValue);

			if (player != null && player.hasPrepared()) {
				player.setPropertyValue(
						(IAttributionEvent)event, propValue, anim);

			} else if (!executionObject.setPropertyValue(
					(IAttributionEvent)event,
					propValue, anim)) {

				event.stop();
			}
		}
	}

	private void runProceduralAction(
			IProceduralExecutionObject executionObject,
			IFormatterEvent event,
			IFormatterPlayerAdapter player,
			ILinkSimpleAction action) {

		
		String attValue, attName;
		short actionType;
		actionType = action.getType();
		switch (actionType) {
			case ISimpleAction.ACT_START:
				if (isDocumentRunning(event)) {
	        		if (!player.hasPrepared()) {
						ruleAdapter.adaptDescriptor(executionObject);
						player.prepare(executionObject, event);

	        			if (executionObject.getDescriptor() != null) {
		        			// look for a reference time base player
							attValue = (String) executionObject.getDescriptor()
								    .getParameterValue("x-timeBaseObject");

							if (attValue != null && !attValue.equals("")) {
								setTimeBaseObject(
									    executionObject,
									    player,
									    attValue);
							}
						}

						ISurface renderedSurface;
						renderedSurface = player.getObjectDisplay();
						
	        			if (renderedSurface != null) {
							layoutManager.prepareFormatterRegion(
									executionObject, renderedSurface);
						}
	        		}

	        		event.addEventListener(this);
	        		((IProceduralPlayerAdapter)player).setCurrentEvent(event);
	        		player.start();
	        	}
				break;

			case ISimpleAction.ACT_PAUSE:
				((IProceduralPlayerAdapter)player).setCurrentEvent(event);
				player.pause();
				break;

			case ISimpleAction.ACT_RESUME:
				((IProceduralPlayerAdapter)player).setCurrentEvent(event);
				player.resume();
				break;

			case ISimpleAction.ACT_ABORT:
				((IProceduralPlayerAdapter)player).setCurrentEvent(event);
				player.abort();
				break;

			case ISimpleAction.ACT_STOP:
				((IProceduralPlayerAdapter)player).setCurrentEvent(event);
				player.stop();
				break;
		}
	}

	/**
	 * @param compositeObject
	 * @param action
	 */
	private void runActionOverComposition(
			ICompositeExecutionObject compositeObject, ILinkSimpleAction action) {
		ICompositeNode compositeNode;
		IPort port;
		INodeNesting compositionPerspective, perspective;
		Iterator objects;
		IExecutionObject childObject;
		IFormatterEvent childEvent;
		List events;
		int i, size;

		if (action.getType() == ISimpleAction.ACT_SET) {
			// nothing to be done
		}
		else if (action.getType() == ISimpleAction.ACT_START) {
			compositeNode = (ICompositeNode)compositeObject.getDataObject()
					.getDataEntity();
			size = compositeNode.getNumPorts();
			compositionPerspective = compositeObject.getNodePerspective();
			events = new Vector();
			for (i = 0; i < size; i++) {
				port = compositeNode.getPort(i);
				perspective = compositionPerspective.copy();
				perspective.append(port.getMapNodeNesting());
				try {
					childObject = compiler.getExecutionObject(perspective, null, compiler
							.getDepthLevel());
					if (childObject != null && port.getEndInterfacePoint() != null
							&& port.getEndInterfacePoint() instanceof IContentAnchor) {
						childEvent = (IPresentationEvent)compiler.getEvent(childObject,
								port.getEndInterfacePoint(), IEvent.EVT_PRESENTATION, null);
						if (childEvent != null) {
							events.add(childEvent);
						}
					}
				}
				catch (ObjectCreationForbiddenException exc) {
					// keep on starting child objects
				}
			}
			size = events.size();
			for (i = 0; i < size; i++) {
				runAction((IPresentationEvent)events.get(i), action);
			}
		}
		else {
			events = new Vector();
			objects = compositeObject.getExecutionObjects();
			while (objects.hasNext()) {
				childObject = (IExecutionObject)objects.next();
				childEvent = childObject.getMainEvent();
				if (childEvent == null) {
					childEvent = childObject.getWholeContentPresentationEvent();
				}
				if (childEvent != null) {
					events.add(childEvent);
				}
			}

			size = events.size();
			for (i = 0; i < size; i++) {
				runAction((IPresentationEvent)events.get(i), action);
			}
		}
	}

	/**
	 * @param switchObject
	 * @param event
	 * @param action
	 */
	private void runActionOverSwitch(IExecutionObjectSwitch switchObject,
			ISwitchEvent event, ILinkSimpleAction action) {

		IExecutionObject selectedObject;
		IFormatterEvent selectedEvent;

		/*
		 * System.err.println("FormatterScheduler::runActionOverSwitch " +
		 * action.getType() + " <-> " + switchObject.getId());
		 */

		selectedObject = switchObject.getSelectedObject();
		if (selectedObject == null) {
			selectedObject = compiler.processExecutionObjectSwitch(switchObject);
			if (selectedObject == null) {
				return;
			}
		}

		selectedEvent = event.getMappedEvent();
		if (selectedEvent != null) {
			runAction(selectedEvent, action);
		}
		else {
			runSwitchEvent(switchObject, event, selectedObject, action);
		}

		if (action.getType() == ISimpleAction.ACT_STOP
				|| action.getType() == ISimpleAction.ACT_ABORT) {
			switchObject.select(null);
		}
	}

	/**
	 * @param switchObject
	 * @param switchEvent
	 * @param selectedObject
	 * @param action
	 */
	private void runSwitchEvent(IExecutionObjectSwitch switchObject,
			ISwitchEvent switchEvent, IExecutionObject selectedObject,
			ILinkSimpleAction action) {
		IFormatterEvent selectedEvent;
		ISwitchPort switchPort;
		Iterator mappings;
		IPort mapping;
		INodeNesting nodePerspective;
		IExecutionObject endPointObject;

		selectedEvent = null;
		switchPort = (ISwitchPort)switchEvent.getInterfacePoint();
		mappings = ((ISwitchPort)switchPort).getPorts();
		while (mappings.hasNext()) {
			mapping = (IPort)mappings.next();
			if (mapping.getNode() == selectedObject.getDataObject()) {
				nodePerspective = switchObject.getNodePerspective();
				nodePerspective.append(mapping.getMapNodeNesting());
				try {
					endPointObject = compiler.getExecutionObject(nodePerspective, null,
							compiler.getDepthLevel());
					if (endPointObject != null) {
						selectedEvent = compiler.getEvent(endPointObject, mapping
								.getEndInterfacePoint(), switchEvent.getEventType(),
								switchEvent.getKey());
					}
				}
				catch (ObjectCreationForbiddenException exc) {
					// continue
				}
				break;
			}
		}

		if (selectedEvent != null) {
			switchEvent.setMappedEvent(selectedEvent);
			runAction(selectedEvent, action);
		}
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.IFormatterScheduler#startEvent(br.org.ginga.ncl.model.event.IFormatterEvent)
	 */
	public void startEvent(IFormatterEvent event) {
		ILinkSimpleAction fakeAction;

		fakeAction = new LinkSimpleAction(event, ISimpleAction.ACT_START);
		runAction(event, fakeAction);
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.IFormatterScheduler#stopEvent(br.org.ginga.ncl.model.event.IFormatterEvent)
	 */
	public void stopEvent(IFormatterEvent event) {
		ILinkSimpleAction fakeAction;

		fakeAction = new LinkSimpleAction(event, ISimpleAction.ACT_STOP);
		runAction(event, fakeAction);
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.IFormatterScheduler#pauseEvent(br.org.ginga.ncl.model.event.IFormatterEvent)
	 */
	public void pauseEvent(IFormatterEvent event) {
		ILinkSimpleAction fakeAction;

		fakeAction = new LinkSimpleAction(event, ISimpleAction.ACT_PAUSE);
		runAction(event, fakeAction);
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.IFormatterScheduler#resumeEvent(br.org.ginga.ncl.model.event.IFormatterEvent)
	 */
	public void resumeEvent(IFormatterEvent event) {
		ILinkSimpleAction fakeAction;

		fakeAction = new LinkSimpleAction(event, ISimpleAction.ACT_RESUME);
		runAction(event, fakeAction);
	}

	/**
	 * 
	 */
	private void initializeDefaultSettings() {
		String value;
		float alfa;

		value = (String)PresentationContext.getInstance().getPropertyValue(
				PresentationContext.DEFAULT_FOCUS_BORDER_TRANSPARENCY);
		if (value != null) {
			alfa = Float.parseFloat(value);
		}
		else {
			alfa = 1;
		}

		value = (String)PresentationContext.getInstance().getPropertyValue(
				PresentationContext.DEFAULT_FOCUS_BORDER_COLOR);
		if (value != null) {
			focusManager.setDefaultFocusBorderColor(DescriptorUtil.getColor(value,
					alfa));
		}

		value = (String)PresentationContext.getInstance().getPropertyValue(
				PresentationContext.DEFAULT_FOCUS_BORDER_WIDTH);
		if (value != null) {
			focusManager.setDefaultFocusBorderWidth(Integer.parseInt(value));
		}

		value = (String)PresentationContext.getInstance().getPropertyValue(
				PresentationContext.DEFAULT_SEL_BORDER_COLOR);
		if (value != null) {
			focusManager.setDefaultSelBorderColor(DescriptorUtil
					.getColor(value, alfa));
		}
	}

	/**
	 * @param node
	 */
	private void initializeDocumentSettings(INode node) {
		String nodeType;
		Iterator nodes, anchors;
		IAnchor anchor;
		IPropertyAnchor attributeAnchor;

		if (node instanceof IContentNode) {
			nodeType = ((IContentNode)node).getNodeType();
			if (nodeType.equalsIgnoreCase(IContentNode.SETTING_NODE)) {
				anchors = ((IContentNode)node).getAnchors();
				while (anchors.hasNext()) {
					anchor = (IAnchor)anchors.next();
					if (anchor instanceof IPropertyAnchor) {
						attributeAnchor = (IPropertyAnchor)anchor;
						if (attributeAnchor.getPropertyValue() != null) {
							PresentationContext.getInstance().setPropertyValue(
									attributeAnchor.getPropertyName(),
									(String) attributeAnchor.getPropertyValue());
						}
					}
				}
			}
		}
		else if (node instanceof ICompositeNode) {
			nodes = ((ICompositeNode)node).getNodes();
			while (nodes.hasNext()) {
				initializeDocumentSettings((INode)nodes.next());
			}
		}
		else if (node instanceof IReferNode) {
			initializeDocumentSettings((INodeEntity)((IReferNode)node)
					.getDataEntity());
		}
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.IFormatterScheduler#startDocument(br.org.ginga.ncl.model.event.IFormatterEvent, java.util.List)
	 */
	public void startDocument(IFormatterEvent documentEvent, List entryEvents) {
		int i, size;
		IFormatterEvent event;

		if (documentEvent == null || entryEvents == null) {
			return;
		}

		if (entryEvents.isEmpty() || documentEvents.contains(documentEvent)) {
			return;
		}

		documentEvent.addEventListener(this);
		documentEvents.add(documentEvent);
		documentStatus.put(documentEvent, new Boolean(true));

		initializeDocumentSettings(documentEvent.getExecutionObject()
				.getDataObject());

		initializeDefaultSettings();

		size = entryEvents.size();
		for (i = 0; i < size; i++) {
			event = (IFormatterEvent)entryEvents.get(i);
			startEvent(event);
		}
	}

	/**
	 * @param documentEvent
	 */
	private void removeDocument(IFormatterEvent documentEvent) {
		compiler.removeExecutionObject(documentEvent.getExecutionObject());
		documentEvents.remove(documentEvent);
		documentStatus.remove(documentEvent);
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.IFormatterScheduler#stopDocument(br.org.ginga.ncl.model.event.IFormatterEvent)
	 */
	public void stopDocument(IFormatterEvent documentEvent) {
		IExecutionObject executionObject;
		int i, size;
		IFormatterSchedulerListener listener;

		if (documentEvents.contains(documentEvent)) {
			documentEvent.removeEventListener(this);
			documentStatus.put(documentEvent, new Boolean(false));

			executionObject = documentEvent.getExecutionObject();
			if (executionObject instanceof ICompositeExecutionObject) {
				((ICompositeExecutionObject)executionObject)
						.setAllLinksAsUncompiled(true);
			}

			stopEvent(documentEvent);
			size = schedulerListeners.size();
			for (i = 0; i < size; i++) {
				listener = (IFormatterSchedulerListener)schedulerListeners.get(i);
				listener.presentationCompleted(documentEvent);
			}
			removeDocument(documentEvent);
		}
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.IFormatterScheduler#pauseDocument(br.org.ginga.ncl.model.event.IFormatterEvent)
	 */
	public void pauseDocument(IFormatterEvent documentEvent) {
		if (documentEvents.contains(documentEvent)) {
			documentStatus.put(documentEvent, new Boolean(false));
			pauseEvent(documentEvent);
		}
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.IFormatterScheduler#resumeDocument(br.org.ginga.ncl.model.event.IFormatterEvent)
	 */
	public void resumeDocument(IFormatterEvent documentEvent) {
		if (documentEvents.contains(documentEvent)) {
			resumeEvent(documentEvent);
			documentStatus.put(documentEvent, new Boolean(true));
		}
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.IFormatterScheduler#stopAllDocuments()
	 */
	public void stopAllDocuments() {
		int i, size;
		List auxDocEventList;
		IFormatterEvent documentEvent;

		if (!documentEvents.isEmpty()) {
			auxDocEventList = new Vector(documentEvents);
			size = auxDocEventList.size();
			for (i = 0; i < size; i++) {
				documentEvent = (IFormatterEvent)auxDocEventList.get(i);
				stopDocument(documentEvent);
			}
			auxDocEventList.clear();
		}
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.IFormatterScheduler#pauseAllDocuments()
	 */
	public void pauseAllDocuments() {
		int i, size;
		IFormatterEvent documentEvent;

		if (!documentEvents.isEmpty()) {
			size = documentEvents.size();
			for (i = 0; i < size; i++) {
				documentEvent = (IFormatterEvent)documentEvents.get(i);
				pauseDocument(documentEvent);
			}
		}
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.IFormatterScheduler#resumeAllDocuments()
	 */
	public void resumeAllDocuments() {
		int i, size;
		IFormatterEvent documentEvent;

		if (!documentEvents.isEmpty()) {
			size = documentEvents.size();
			for (i = 0; i < size; i++) {
				documentEvent = (IFormatterEvent)documentEvents.get(i);
				resumeDocument(documentEvent);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see iformatter.model.event.IEventListener#eventStateChanged(short)
	 */
	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.event.IEventListener#eventStateChanged(br.org.ginga.ncl.model.event.IFormatterEvent, short)
	 */
	public void eventStateChanged(IFormatterEvent event, short transition,
			short previousState) {
		IExecutionObject object;
		IFormatterPlayerAdapter player;
		int i, size;
		IFormatterSchedulerListener listener;

		if (documentEvents.contains(event)) {
			switch (transition) {
			case IEvent.TR_STOPS:
			case IEvent.TR_ABORTS:
				size = schedulerListeners.size();
				for (i = 0; i < size; i++) {
					listener = (IFormatterSchedulerListener)schedulerListeners.get(i);
					listener.presentationCompleted(event);
				}
				removeDocument(event);
				break;
			}
		}
		else {
			switch (transition) {
			case IEvent.TR_STARTS:
				if (isDocumentRunning(event)) {
					// System.out.println("FormatterScheduler::eventStateChanged STARTED "
					// + event.getId());
					object = event.getExecutionObject();
					player = playerManager.getPlayer(object);
					if (player != null) {
						layoutManager.showObject(object);
						focusManager.showObject(object);
					}
				}
				break;

			case IEvent.TR_STOPS:
			case IEvent.TR_ABORTS:
				if (((IPresentationEvent)event).getRepetitions() == 0) {
					event.removeEventListener(this);
					object = event.getExecutionObject();
					focusManager.hideObject(object);
					layoutManager.hideObject(object);
					// System.out.println("FormatterScheduler::eventStateChanged OBJECT
					// HIDDEN " + event.getId());
				}
				break;
			}
		}
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.IFormatterScheduler#addSchedulerListener(br.org.ginga.ncl.IFormatterSchedulerListener)
	 */
	public void addSchedulerListener(IFormatterSchedulerListener listener) {
		if (!schedulerListeners.contains(listener)) {
			schedulerListeners.add(listener);
		}
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.IFormatterScheduler#removeSchedulerListener(br.org.ginga.ncl.IFormatterSchedulerListener)
	 */
	public void removeSchedulerListener(IFormatterSchedulerListener listener) {
		schedulerListeners.remove(listener);
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.IFormatterScheduler#reset()
	 */
	public void reset() {
		// TODO implement
		layoutManager.clear();
	}
}