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
package br.pucrio.telemidia.ginga.ncl.converter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import br.org.ginga.ncl.IFormatterScheduler;
import br.org.ginga.ncl.converter.ObjectCreationForbiddenException;
import br.org.ginga.ncl.model.components.ICompositeExecutionObject;
import br.org.ginga.ncl.model.components.IExecutionObject;
import br.org.ginga.ncl.model.components.INodeNesting;
import br.org.ginga.ncl.model.event.IEventListener;
import br.org.ginga.ncl.model.event.IFormatterEvent;
import br.org.ginga.ncl.model.event.IPresentationEvent;
import br.org.ginga.ncl.model.event.ISelectionEvent;
import br.org.ginga.ncl.model.link.IFormatterCausalLink;
import br.org.ginga.ncl.model.link.IFormatterLink;
import br.org.ginga.ncl.model.link.ILinkAction;
import br.org.ginga.ncl.model.link.ILinkCompoundAction;
import br.org.ginga.ncl.model.link.ILinkSimpleAction;
import br.org.ginga.ncl.model.presentation.ICascadingDescriptor;
import br.org.ginga.ncl.model.switches.IExecutionObjectSwitch;
import br.org.ginga.ncl.model.switches.ISwitchEvent;
import br.org.ncl.components.ICompositeNode;
import br.org.ncl.components.IContentNode;
import br.org.ncl.components.IContextNode;
import br.org.ncl.components.INode;
import br.org.ncl.components.INodeEntity;
import br.org.ncl.connectors.IEvent;
import br.org.ncl.descriptor.IGenericDescriptor;
import br.org.ncl.interfaces.IPort;
import br.org.ncl.interfaces.IPropertyAnchor;
import br.org.ncl.interfaces.IContentAnchor;
import br.org.ncl.interfaces.IInterfacePoint;
import br.org.ncl.interfaces.ILambdaAnchor;
import br.org.ncl.interfaces.ISwitchPort;
import br.org.ncl.link.IBind;
import br.org.ncl.link.ICausalLink;
import br.org.ncl.link.ILink;
import br.org.ncl.reuse.IReferNode;
import br.org.ncl.switches.ISwitchNode;
import br.pucrio.telemidia.ginga.ncl.FormatterScheduler;
import br.pucrio.telemidia.ginga.ncl.adaptation.context.RuleAdapter;
import br.pucrio.telemidia.ginga.ncl.adapters.PlayerAdapterManager;
import br.pucrio.telemidia.ginga.ncl.model.components.CompositeExecutionObject;
import br.pucrio.telemidia.ginga.ncl.model.components.ExecutionObject;
import br.pucrio.telemidia.ginga.ncl.model.components.NodeNesting;
import br.pucrio.telemidia.ginga.ncl.model.components.ProceduralExecutionObject;
import br.pucrio.telemidia.ginga.ncl.model.event.AttributionEvent;
import br.pucrio.telemidia.ginga.ncl.model.event.PresentationEvent;
import br.pucrio.telemidia.ginga.ncl.model.event.SelectionEvent;
import br.pucrio.telemidia.ginga.ncl.model.presentation.CascadingDescriptor;
import br.pucrio.telemidia.ginga.ncl.model.switches.ExecutionObjectSwitch;
import br.pucrio.telemidia.ginga.ncl.model.switches.SwitchEvent;

public class FormatterConverter implements IEventListener {
	private int depthLevel;

	private Map<String,IExecutionObject> executionObjects;

	private FormatterLinkConverter linkCompiler;

	private IFormatterScheduler scheduler;

	private RuleAdapter ruleAdapter;

	public FormatterConverter(RuleAdapter ruleAdapter) {

		executionObjects = new Hashtable<String, IExecutionObject>();
		linkCompiler = new FormatterLinkConverter(this);

		this.scheduler = null;
		this.ruleAdapter = ruleAdapter;

		depthLevel = 1;
		// depthLevel = br.org.ginga.ncl.IFormatter.DEEPEST_LEVEL;
	}

	public void setScheduler(IFormatterScheduler scheduler) {
		this.scheduler = scheduler;
	}

	public void setDepthLevel(int level) {
		depthLevel = level;
	}

	public int getDepthLevel() {
		return depthLevel;
	}

	public ICompositeExecutionObject addSameInstance(
			IExecutionObject executionObject, IReferNode referNode) {

		INodeNesting referPerspective;
		ICompositeExecutionObject referParentObject;

		referPerspective = new NodeNesting(referNode.getPerspective());
		try {
			referParentObject = getParentExecutionObject(referPerspective, depthLevel);
			if (referParentObject != null) {
				executionObject.addParentObject(referNode, referParentObject,
						referPerspective.getNode(referPerspective.getNumNodes() - 2));

				// A new entry for the execution object is inserted using
				// the refer node id. As a consequence, links referring to the
				// refer node will generate events in the execution object.
				executionObjects.put(referPerspective.getId() + "/"
						+ executionObject.getDescriptor().getId(), executionObject);
			}
			return referParentObject;
		}
		catch (ObjectCreationForbiddenException exc) {
			// nothing to be done
			return null;
		}
	}

	private void addExecutionObject(IExecutionObject executionObject,
			ICompositeExecutionObject parentObject, int depthLevel) {

		INodeEntity dataObject;
		Iterator sameInstances;
		IReferNode referNode;
		INodeNesting referPerspective;
		ICascadingDescriptor descriptor;

		executionObjects.put(executionObject.getId(), executionObject);

		if (parentObject != null) {
			parentObject.addExecutionObject(executionObject);
		}

		if (executionObject.getDataObject() instanceof INodeEntity) {
			dataObject = (INodeEntity)executionObject.getDataObject();
			sameInstances = dataObject.getSameInstances();
			while (sameInstances.hasNext()) {
				referNode = (IReferNode)sameInstances.next();
				referPerspective = new NodeNesting(referNode.getPerspective());
				if (executionObject.getNodePerspective().getHeadNode() == referPerspective
						.getHeadNode()) {
					addSameInstance(executionObject, referNode);
					// TODO falta lidar com perspectiva e refer para contextos
				}
			}
		}

		descriptor = executionObject.getDescriptor();
		if (descriptor != null) {
			descriptor.setFormatterRegion(scheduler.getLayoutManager());
		}

		if (descriptor != null
				&& descriptor.getPlayerName() != null
				&& (descriptor.getPlayerName().equalsIgnoreCase(
							"VideoChannelPlayerAdapter") || 
						descriptor.getPlayerName().equalsIgnoreCase(
							"AudioChannelPlayerAdapter") ||
						descriptor.getPlayerName().equalsIgnoreCase(
								"JmfVideoChannelPlayerAdapter")	|| 
						descriptor.getPlayerName().equalsIgnoreCase(
								"JmfAudioChannelPlayerAdapter") || 
						descriptor.getPlayerName().equalsIgnoreCase(
								"QtVideoChannelPlayerAdapter") || 
						descriptor.getPlayerName().equalsIgnoreCase(
								"QtAudioChannelPlayerAdapter"))) {
			createMultichannelObject((ICompositeExecutionObject)executionObject,
					depthLevel);
		}

		if (depthLevel != 0) {
			if (depthLevel > 0) {
				depthLevel--;
			}
			compileExecutionObjectLinks(executionObject, depthLevel);
		}
	}

	public void compileExecutionObjectLinks(IExecutionObject executionObject,
			int depthLevel) {
		Iterator nodes;
		INode node;

		/*
		 * System.err.print("FormatterScheduler::compileExecutionObjectLinks");
		 * System.err.println(" EO " + executionObject.getId() + " depth " +
		 * depthLevel);
		 */

		nodes = executionObject.getNodes();
		while (nodes.hasNext()) {
			node = (INode)nodes.next();
			compileExecutionObjectLinks(executionObject, node, executionObject
					.getParentObject(node), depthLevel);
		}
	}

	public IExecutionObject getExecutionObject(INodeNesting perspective,
			IGenericDescriptor descriptor, int depthLevel)
			throws ObjectCreationForbiddenException {

		ICascadingDescriptor cascadingDescriptor;
		String id;
		IExecutionObject executionObject;
		ICompositeExecutionObject parentObject;
		IExecutionObjectSwitch parentSwitch;
		INode selectedNode;

		id = perspective.getId() + "/";
		cascadingDescriptor = getCascadingDescriptor(perspective, descriptor);
		if (cascadingDescriptor != null) {
			id += cascadingDescriptor.getId();
		}

		executionObject = (IExecutionObject)executionObjects.get(id);
		if (executionObject != null) {
			return executionObject;
		}

		parentSwitch = null;
		
		parentObject = getParentExecutionObject(perspective, depthLevel);
		if (parentObject != null && parentObject instanceof IExecutionObjectSwitch) {
			parentSwitch = (IExecutionObjectSwitch)parentObject;
			if (parentSwitch.getSelectedObject() == null) {
				selectedNode = ruleAdapter.adaptSwitch((ISwitchNode)parentSwitch
						.getDataObject().getDataEntity());
				if (selectedNode != perspective.getAnchorNode()) {
					// the exec obj has been created when creating the switch
					throw (new ObjectCreationForbiddenException());
				}
			}
			else if (parentSwitch.getSelectedObject().getDataObject() != perspective
					.getAnchorNode()) {
				// the exec obj has been created when creating the switch
				throw (new ObjectCreationForbiddenException());
			}
		}

		executionObject = createExecutionObject(id, perspective,
				cascadingDescriptor, depthLevel);

		if (executionObject == null) {
			return null;
		}

		addExecutionObject(executionObject, parentObject, depthLevel);

		if (parentSwitch != null) {
			parentSwitch.select(executionObject);
			resolveSwitchEvents(parentSwitch, depthLevel);
		}

		return executionObject;
	}

	private ICompositeExecutionObject getParentExecutionObject(
			INodeNesting perspective, int depthLevel)
			throws ObjectCreationForbiddenException {
		INodeNesting parentPerspective;

		if (perspective.getNumNodes() > 1) {
			parentPerspective = perspective.copy();
			parentPerspective.removeAnchorNode();
			return (ICompositeExecutionObject)getExecutionObject(parentPerspective,
					null, depthLevel);
		}
		else {
			return null;
		}
	}

	public IFormatterEvent getEvent(IExecutionObject executionObject,
			IInterfacePoint interfacePoint, int ncmEventType, String key) {

		String id;
		IFormatterEvent event;
		ICascadingDescriptor descriptor;

		if (key == null) {
			id = interfacePoint.getId() + "_" + ncmEventType;
		}
		else {
			id = interfacePoint.getId() + "_" + ncmEventType + "_" + key;
		}
		event = executionObject.getEvent(id);
		if (event != null) {
			return event;
		}

		if (executionObject instanceof IExecutionObjectSwitch) {
			event = new SwitchEvent(id, (IExecutionObjectSwitch)executionObject,
					interfacePoint, ncmEventType, key);
		}
		else {
			if (ncmEventType == IEvent.EVT_PRESENTATION) {
				event = new PresentationEvent(id, executionObject,
						(IContentAnchor)interfacePoint);

				if (interfacePoint instanceof ILambdaAnchor) {
					descriptor = executionObject.getDescriptor();
					if (descriptor != null && descriptor.getExplicitDuration() != null) {
						((IPresentationEvent)event).setEnd(descriptor.getExplicitDuration()
								.doubleValue());
					}
				}
			}
			else {
				if (executionObject instanceof ICompositeExecutionObject) {
					// TODO: eventos internos da composicao. Estao sendo tratados nos
					// elos.
					if (ncmEventType == IEvent.EVT_ATTRIBUTION) {
						event = new AttributionEvent(id, executionObject,
								(IPropertyAnchor)interfacePoint);
					}
				}
				else {
					switch (ncmEventType) {
					case IEvent.EVT_ATTRIBUTION:
						event = new AttributionEvent(id, executionObject,
								(IPropertyAnchor)interfacePoint);
						break;

					case IEvent.EVT_SELECTION:
						event = new SelectionEvent(id, executionObject,
								(IContentAnchor)interfacePoint);
						if (key != null) {
							((ISelectionEvent)event).setSelectionCode(key);
						}
						break;
					}
				}
			}
		}

		if (event != null) {
			executionObject.addEvent(event);
		}
		return event;
	}

	private void createMultichannelObject(
			ICompositeExecutionObject compositeObject, int depthLevel) {
		ICompositeNode compositeNode;
		Iterator nodes;
		INode node;
		INodeNesting perspective;
		String id;
		ICascadingDescriptor cascadingDescriptor;
		IExecutionObject childObject;

		compositeNode = (ICompositeNode)compositeObject.getDataObject();
		nodes = compositeNode.getNodes();
		while (nodes.hasNext()) {
			node = (INode)nodes.next();
			perspective = new NodeNesting(compositeObject.getNodePerspective());
			perspective.insertAnchorNode(node);

			id = perspective.getId() + "/";
			cascadingDescriptor = getCascadingDescriptor(perspective, null);
			if (cascadingDescriptor != null) {
				id += cascadingDescriptor.getId();
			}
			childObject = createExecutionObject(id, perspective, cascadingDescriptor,
					depthLevel);

			if (childObject != null) {
				getEvent(childObject, ((INodeEntity)node.getDataEntity())
						.getLambdaAnchor(), IEvent.EVT_PRESENTATION, null);
				addExecutionObject(childObject, compositeObject, depthLevel);
			}
		}
	}

	private IExecutionObject createExecutionObject(String id,
			INodeNesting perspective, ICascadingDescriptor descriptor, int depthLevel) {

		INodeEntity nodeEntity;
		INode node;
		INodeNesting nodePerspective;
		IExecutionObject executionObject;
		IPresentationEvent compositeEvent;

		nodePerspective = perspective;
		nodeEntity = (INodeEntity)perspective.getAnchorNode().getDataEntity();
		// solve execution object cross reference coming from refer nodes with
		// new instance = false
		if (nodeEntity instanceof IContentNode
				&& ((IContentNode)nodeEntity).getNodeType() != null
				&& !((IContentNode)nodeEntity).getNodeType().equalsIgnoreCase(
						IContentNode.SETTING_NODE)) {
			node = perspective.getAnchorNode();
			if (node instanceof IReferNode && !((IReferNode)node).isNewInstance()) {
				nodePerspective = new NodeNesting(nodeEntity.getPerspective());

				// verify if both nodes are in the same base.
				if (nodePerspective.getHeadNode() == perspective.getHeadNode()) {
					try {
						executionObject = getExecutionObject(nodePerspective, null,
								depthLevel);
					}
					catch (ObjectCreationForbiddenException exc1) {
						if(PlayerAdapterManager.isProcedural(nodeEntity)){
							executionObject = new ProceduralExecutionObject(id,nodeEntity, descriptor);
						}else{
							executionObject = new ExecutionObject(id, nodeEntity, descriptor);
						}
						// TODO informa a substituicao
					}
				}
				else {
					try {
						((ICompositeNode)perspective.getHeadNode()).addNode(nodePerspective.getHeadNode());
						nodePerspective = new NodeNesting(nodeEntity.getPerspective());
						return getExecutionObject(nodePerspective, null, depthLevel);
					}
					catch (Exception exc) {
						
					}
					
					// not in the same base => create a new version
					if(PlayerAdapterManager.isProcedural(nodeEntity)){
						executionObject = new ProceduralExecutionObject(id,nodeEntity, descriptor);
					}else{
						executionObject = new ExecutionObject(id, nodeEntity, descriptor);
					}
					
					// TODO informa a substituicao
				}

				if (executionObject != null) {
					return executionObject;
				}
			}
		}

		if (nodeEntity instanceof ISwitchNode) {
			executionObject = new ExecutionObjectSwitch(id, perspective
					.getAnchorNode());
			compositeEvent = new PresentationEvent(nodeEntity.getLambdaAnchor()
					.getId()
					+ "_" + IEvent.EVT_PRESENTATION, executionObject,
					(IContentAnchor)nodeEntity.getLambdaAnchor());
			executionObject.addEvent(compositeEvent);
			// to monitor the switch presentation and clear the selection after
			// each execution
			compositeEvent.addEventListener(this);
		}
		else if (nodeEntity instanceof ICompositeNode) {
			executionObject = new CompositeExecutionObject(id, perspective
					.getAnchorNode(), descriptor);
			compositeEvent = new PresentationEvent(nodeEntity.getLambdaAnchor()
					.getId()
					+ "_" + IEvent.EVT_PRESENTATION, executionObject,
					(IContentAnchor)nodeEntity.getLambdaAnchor());
			executionObject.addEvent(compositeEvent);
		}
		else if(PlayerAdapterManager.isProcedural(nodeEntity)){
			executionObject = new ProceduralExecutionObject(id,nodeEntity, descriptor);
		}else{
			executionObject = new ExecutionObject(id, nodeEntity, descriptor);
		}

		return executionObject;
	}

	public static ICascadingDescriptor getCascadingDescriptor(
			INodeNesting nodePerspective, IGenericDescriptor descriptor) {

		ICascadingDescriptor cascadingDescriptor;
		INodeEntity node;
		IContextNode context;
		int size;

		cascadingDescriptor = null;
		node = (INodeEntity)nodePerspective.getAnchorNode().getDataEntity();

		// there is a node descriptor?
		if (node.getDescriptor() != null) {
			cascadingDescriptor = new CascadingDescriptor(node.getDescriptor());
		}

		// there is a node descriptor defined in the node context?
		size = nodePerspective.getNumNodes();
		if (size > 1 && nodePerspective.getNode(size - 2) instanceof IContextNode) {
			context = (IContextNode)nodePerspective.getNode(size - 2).getDataEntity();
			if (context.getNodeDescriptor(node) != null) {
				if (cascadingDescriptor == null) {
					cascadingDescriptor = new CascadingDescriptor(context
							.getNodeDescriptor(node));
				}
				else {
					cascadingDescriptor.cascade(context.getNodeDescriptor(node));
				}
			}
		}

		// there is an explicit descriptor (user descriptor)?
		if (descriptor != null) {
			if (cascadingDescriptor == null)
				cascadingDescriptor = new CascadingDescriptor(descriptor);
			else
				cascadingDescriptor.cascade(descriptor);
		}
		return cascadingDescriptor;
	}

	public void compileExecutionObjectLinks(IExecutionObject executionObject,
			INode dataObject, ICompositeExecutionObject parentObject, int depthLevel) {

		List<ILink> dataLinks;
		int i;
		ILink link;
		List<IGenericDescriptor> descriptors;
		IGenericDescriptor descriptor;
		IFormatterLink formatterLink;

		executionObject.setCompiled(true);
		if (parentObject == null) {
			return;
		}

		dataLinks = new ArrayList<ILink>(parentObject.getUncompiledLinks());
		for (i = 0; i < dataLinks.size(); i++) {
			link = (ILink)dataLinks.get(i);

			// since the link may be removed in a deepest compilation it is
			// necessary to certify that the link was not compiled
			if (parentObject.containsUncompiledLink(link)) {
				descriptor = null;
				if (executionObject.getDescriptor() != null) {
					descriptors = executionObject.getDescriptor().getNcmDescriptors();
					if (!descriptors.isEmpty()) {
						descriptor = (IGenericDescriptor)descriptors
								.get(descriptors.size() - 1);
					}
				}

				if (link instanceof ICausalLink) {
					// verify if execution object is part of link conditions
					if (((ICausalLink)link).containsSourceNode(dataObject, descriptor)) {
						// compile causal link
						parentObject.removeLinkUncompiled(link);
						formatterLink = linkCompiler.createCausalLink((ICausalLink)link,
								parentObject, depthLevel);
						if (formatterLink != null) {
							setActionListener(((IFormatterCausalLink)formatterLink)
									.getAction());
							parentObject.setLinkCompiled(formatterLink);
						}
					}
				}
			}
		}

		compileExecutionObjectLinks(executionObject, dataObject, parentObject
				.getParentObject(), depthLevel);
	}

	private void setActionListener(ILinkAction action) {
		Iterator actions;

		if (action instanceof ILinkSimpleAction) {
			((ILinkSimpleAction)action)
					.addActionListener((FormatterScheduler)scheduler);
		}
		else {
			actions = ((ILinkCompoundAction)action).getActions();
			while (actions.hasNext()) {
				setActionListener((ILinkAction)actions.next());
			}
		}
	}

	public Vector<IExecutionObject> processAllExecutionObjectSwitch(
			IExecutionObjectSwitch switchObject) {
		ISwitchNode switchNode;
		INode selectedNode;
		INodeNesting selectedPerspective;
		String id;
		ICascadingDescriptor descriptor;
		IExecutionObject selectedObject;
		Vector<IExecutionObject> executionObjectsReturn = new Vector<IExecutionObject>();

		switchNode = (ISwitchNode)switchObject.getDataObject().getDataEntity();
		int size = switchNode.getNumNodes();
		for (int i = 0; i < size; i++) {
			selectedNode = switchNode.getNode(i);
			
			selectedPerspective = switchObject.getNodePerspective();
			selectedPerspective.insertAnchorNode(selectedNode);

			id = selectedPerspective.getId() + "/";

			descriptor = FormatterConverter.getCascadingDescriptor(selectedPerspective,
					null);
			if (descriptor != null) {
				id += descriptor.getId();
			}

			selectedObject = (IExecutionObject)executionObjects.get(id);
			if (selectedObject != null) {
				switchObject.select(selectedObject);
				resolveSwitchEvents(switchObject, depthLevel);
				
				executionObjectsReturn.add(selectedObject);
			} else {

				selectedObject = createExecutionObject(id, selectedPerspective, descriptor,
						depthLevel);

				if (selectedObject != null) {
					addExecutionObject(selectedObject, switchObject, depthLevel);
					switchObject.select(selectedObject);
					resolveSwitchEvents(switchObject, depthLevel);
					compileExecutionObjectLinks(selectedObject, depthLevel);
					executionObjectsReturn.add(selectedObject);
				}

			
			}
		}
		
		return executionObjectsReturn;
	}
	
	public IExecutionObject processExecutionObjectSwitch(
			IExecutionObjectSwitch switchObject) {
		ISwitchNode switchNode;
		INode selectedNode;
		INodeNesting selectedPerspective;
		String id;
		ICascadingDescriptor descriptor;
		IExecutionObject selectedObject;

		switchNode = (ISwitchNode)switchObject.getDataObject().getDataEntity();
		selectedNode = ruleAdapter.adaptSwitch(switchNode);

		if (selectedNode == null) {
			return null;
		}

		selectedPerspective = switchObject.getNodePerspective();
		selectedPerspective.insertAnchorNode(selectedNode);

		id = selectedPerspective.getId() + "/";

		descriptor = FormatterConverter.getCascadingDescriptor(selectedPerspective,
				null);
		if (descriptor != null) {
			id += descriptor.getId();
		}

		selectedObject = (IExecutionObject)executionObjects.get(id);
		if (selectedObject != null) {
			switchObject.select(selectedObject);
			resolveSwitchEvents(switchObject, depthLevel);
			return selectedObject;
		}

		selectedObject = createExecutionObject(id, selectedPerspective, descriptor,
				depthLevel);

		if (selectedObject == null) {
			return null;
		}

		addExecutionObject(selectedObject, switchObject, depthLevel);
		switchObject.select(selectedObject);
		resolveSwitchEvents(switchObject, depthLevel);
		return selectedObject;
	}

	private void resolveSwitchEvents(IExecutionObjectSwitch switchObject,
			int depthLevel) {
		IExecutionObject selectedObject, endPointObject;
		INode selectedNode;
		INodeEntity selectedNodeEntity;
		Iterator events;
		ISwitchEvent switchEvent;
		IInterfacePoint interfacePoint;
		ISwitchPort switchPort;
		Iterator mappings;
		IPort mapping;
		INodeNesting nodePerspective;
		IFormatterEvent mappedEvent;

		selectedObject = switchObject.getSelectedObject();
		if (selectedObject == null) {
			return;
		}

		selectedNode = selectedObject.getDataObject();
		selectedNodeEntity = (INodeEntity)selectedNode.getDataEntity();
		events = switchObject.getEvents();
		while (events.hasNext()) {
			mappedEvent = null;
			switchEvent = (ISwitchEvent)events.next();
			interfacePoint = switchEvent.getInterfacePoint();
			if (interfacePoint instanceof ILambdaAnchor) {
				mappedEvent = getEvent(selectedObject, selectedNodeEntity
						.getLambdaAnchor(), switchEvent.getEventType(), switchEvent
						.getKey());
			}
			else {
				switchPort = (ISwitchPort)interfacePoint;
				mappings = ((ISwitchPort)switchPort).getPorts();
				while (mappings.hasNext()) {
					mapping = (IPort)mappings.next();
					if (mapping.getNode() == selectedNode) {
						nodePerspective = switchObject.getNodePerspective();
						nodePerspective.append(mapping.getMapNodeNesting());
						try {
							endPointObject = getExecutionObject(nodePerspective, null,
									depthLevel);
							if (endPointObject != null) {
								mappedEvent = getEvent(endPointObject, mapping
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
			}

			if (mappedEvent != null) {
				switchEvent.setMappedEvent(mappedEvent);
			}
		}
	}

	private IFormatterEvent insertNode(INodeNesting perspective,
			IInterfacePoint interfacePoint, IGenericDescriptor descriptor) {

		IExecutionObject executionObject;
		IFormatterEvent event;

		event = null;
		try {
			executionObject = getExecutionObject(perspective, descriptor, depthLevel);
			if (executionObject != null) {
				// get the event corresponding to the node anchor
				event = getEvent(executionObject, interfacePoint,
						IEvent.EVT_PRESENTATION, null);
			}
			return event;
		}
		catch (ObjectCreationForbiddenException exc) {
			return null;
		}
	}

	public IFormatterEvent insertContext(INodeNesting contextPerspective,
			IPort port) {
		INodeNesting perspective;

		if (contextPerspective == null
				|| port == null
				|| !(port.getEndInterfacePoint() instanceof IContentAnchor || port
						.getEndInterfacePoint() instanceof ISwitchPort)
				|| !(contextPerspective.getAnchorNode().getDataEntity() instanceof IContextNode)) {
			return null;
		}

		perspective = new NodeNesting(contextPerspective);
		perspective.append(port.getMapNodeNesting());
		return insertNode(perspective, port.getEndInterfacePoint(), null);
	}

	public boolean removeExecutionObject(IExecutionObject executionObject,
			IReferNode referNode) {
		INodeNesting referPerspective;

		if (executionObject == null || referNode == null) {
			return false;
		}

		executionObject.removeNode(referNode);
		referPerspective = new NodeNesting(referNode.getPerspective());
		executionObjects.remove(referPerspective.getId() + "/"
				+ executionObject.getDescriptor().getId());
		// TODO: problema se esse era a base para outros objetos
		return true;
	}

	public boolean removeExecutionObject(IExecutionObject executionObject) {
		ICompositeExecutionObject compositeObject;
		IExecutionObject childObject;

		if (executionObject == null) {
			return false;
		}

		if (executionObject instanceof ICompositeExecutionObject) {
			compositeObject = (ICompositeExecutionObject)executionObject;
			while (compositeObject.getExecutionObjects().hasNext()) {
				childObject = (IExecutionObject)compositeObject.getExecutionObjects()
						.next();
				compositeObject.removeExecutionObject(childObject);
				// TODO melhorar a remocao/destruicao
				removeExecutionObject(childObject);
			}
		}

		executionObjects.remove(executionObject.getId());
		// executionObject.destroy();
		return true;
	}

	public IExecutionObject hasExecutionObject(INode node,
			IGenericDescriptor descriptor) {
		INodeNesting perspective;
		String id;
		ICascadingDescriptor cascadingDescriptor;

		// TODO procurar por potenciais substitutos no caso de REFER

		perspective = new NodeNesting(node.getPerspective());
		id = perspective.getId() + "/";
		cascadingDescriptor = getCascadingDescriptor(perspective, descriptor);
		if (cascadingDescriptor != null) {
			id += cascadingDescriptor.getId();
		}

		if (executionObjects.containsKey(id)) {
			return (IExecutionObject)executionObjects.get(id);
		}
		else {
			return null;
		}
	}

	public IFormatterCausalLink addCausalLink(IContextNode context,
			ICausalLink link) {
		IExecutionObject object, childObject;
		ICompositeExecutionObject contextObject;
		Iterator binds;
		IBind bind;
		IFormatterCausalLink formatterLink;

		object = hasExecutionObject(context, null);
		if (object == null) {
			return null;
		}

		contextObject = (ICompositeExecutionObject)object;
		contextObject.addNcmLink(link);

		binds = link.getConditionBinds();
		while (binds.hasNext()) {
			bind = (IBind)binds.next();
			childObject = hasExecutionObject(bind.getNode(), bind.getDescriptor());
			if (childObject != null) {
				// compile causal link
				contextObject.removeLinkUncompiled(link);
				formatterLink = linkCompiler.createCausalLink(link, contextObject,
						depthLevel);
				if (formatterLink != null) {
					setActionListener(formatterLink.getAction());
					contextObject.setLinkCompiled(formatterLink);
				}
				return formatterLink;
			}
		}
		return null;
	}

	public void eventStateChanged(IFormatterEvent event, short transition,
			short previousState) {
		IExecutionObject executionObject;

		executionObject = event.getExecutionObject();
		// System.err.println("FormatterCompiler::eventStateChanged " +
		// executionObject.getId());
		if (executionObject instanceof IExecutionObjectSwitch) {
			if (transition == IEvent.TR_STOPS ||
					transition == IEvent.TR_ABORTS) {

				/*
				 * removeExecutionObject(
				 * ((IExecutionObjectSwitch)executionObject).getSelectedObject());
				 */
				((IExecutionObjectSwitch)executionObject).select(null);
				// removeExecutionObject(executionObject);
			}
		}
		else if (executionObject instanceof ICompositeExecutionObject) {
			if (transition == IEvent.TR_STOPS ||
					transition == IEvent.TR_ABORTS) {

				removeExecutionObject(executionObject);
			}
		}
	}

	public void reset() {
		executionObjects.clear();
	}
}
