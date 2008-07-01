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

import br.org.ginga.ncl.IFormatter;
import br.org.ginga.ncl.IFormatterListener;
import br.org.ginga.ncl.IFormatterScheduler;
import br.org.ginga.ncl.IFormatterSchedulerListener;
import br.org.ginga.ncl.adapters.IPlayerAdapterManager;
import br.org.ginga.ncl.converter.ObjectCreationForbiddenException;
import br.org.ginga.ncl.model.components.ICompositeExecutionObject;
import br.org.ginga.ncl.model.components.IExecutionObject;
import br.org.ginga.ncl.model.components.INodeNesting;
import br.org.ginga.ncl.model.event.IAttributionEvent;
import br.org.ginga.ncl.model.event.IFormatterEvent;
import br.org.ginga.ncl.model.link.ILinkActionListener;
import br.org.ginga.ncl.model.link.ILinkAssignmentAction;
import br.org.ncl.IBase;
import br.org.ncl.INclDocument;
import br.org.ncl.INclDocumentManager;
import br.org.ncl.components.ICompositeNode;
import br.org.ncl.components.IContentNode;
import br.org.ncl.components.IContextNode;
import br.org.ncl.components.INode;
import br.org.ncl.components.INodeEntity;
import br.org.ncl.connectors.IConnector;
import br.org.ncl.connectors.IConnectorBase;
import br.org.ncl.connectors.IEvent;
import br.org.ncl.connectors.ISimpleAction;
import br.org.ncl.descriptor.IDescriptorBase;
import br.org.ncl.descriptor.IGenericDescriptor;
import br.org.ncl.interfaces.IAnchor;
import br.org.ncl.interfaces.IPort;
import br.org.ncl.interfaces.IPropertyAnchor;
import br.org.ncl.interfaces.IContentAnchor;
import br.org.ncl.interfaces.IInterfacePoint;
import br.org.ncl.interfaces.ISwitchPort;
import br.org.ncl.layout.ILayoutRegion;
import br.org.ncl.layout.IRegionBase;
import br.org.ncl.link.IBind;
import br.org.ncl.link.ICausalLink;
import br.org.ncl.link.ILink;
import br.org.ncl.link.ILinkComposition;
import br.org.ncl.reuse.IReferNode;
import br.org.ncl.switches.IRule;
import br.org.ncl.switches.IRuleBase;
import br.org.ncl.transition.ITransition;
import br.org.ncl.transition.ITransitionBase;
import br.pucrio.telemidia.ginga.ncl.adaptation.context.RuleAdapter;
import br.pucrio.telemidia.ginga.ncl.adapters.PlayerAdapterManager;
import br.pucrio.telemidia.ginga.ncl.converter.FormatterConverter;
import br.pucrio.telemidia.ginga.ncl.model.components.NodeNesting;
import br.pucrio.telemidia.ginga.ncl.model.link.LinkAssignmentAction;

/**
 * 
 */
public class Formatter implements IFormatter, IFormatterSchedulerListener {

	/**
	 * 
	 */
	private INclDocumentManager nclDocumentManager;

	/**
	 * 
	 */
	private Map documentEvents;

	/**
	 * 
	 */
	private Map documentEntryEvents;

	/**
	 * 
	 */
	private RuleAdapter ruleAdapter;

	/**
	 * 
	 */
	private IFormatterScheduler scheduler;

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
	private List formatterListeners;

	public Formatter(String id, INclDocumentManager docManager) {
		initializeContextProxy();
		playerManager = new PlayerAdapterManager();
		compiler = new FormatterConverter(ruleAdapter);
		scheduler = new FormatterScheduler(playerManager, ruleAdapter, compiler);
		scheduler.addSchedulerListener(this);
		compiler.setScheduler(scheduler);

		nclDocumentManager = docManager;
		documentEvents = new Hashtable();
		documentEntryEvents = new Hashtable();

		formatterListeners = new Vector();
	}

	/**
	 * 
	 */
	private void initializeContextProxy() {
		ruleAdapter = new RuleAdapter();
	}

	/**
	 * Clear all formatter data structures
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#reset()
	 */
	public void reset() {
		ruleAdapter.reset();
		playerManager.reset();
		scheduler.reset();
		compiler.reset();
		nclDocumentManager.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#close()
	 */
	public void close() {
		// PresentationContext.getInstance().save();
		playerManager.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#addDocument(java.lang.String)
	 */
	public INclDocument addDocument(String docLocation) {
		return nclDocumentManager.addDocument(docLocation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#removeDocument(java.lang.String)
	 */
	public boolean removeDocument(String documentId) {
		INclDocument document;

		if (documentEvents.containsKey(documentId)) {
			stopDocument(documentId);
		}

		document = nclDocumentManager.removeDocument(documentId);
		if (document != null) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * @param documentId
	 * @return
	 */
	private IContextNode getDocumentContext(String documentId) {
		INclDocument nclDocument;

		if (documentEvents.containsKey(documentId)) {
			return null;
		}

		nclDocument = nclDocumentManager.getDocument(documentId);
		if (nclDocument == null) {
			return null;
		}

		return nclDocument.getBody();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#setDepthLevel(int)
	 */
	public void setDepthLevel(int level) {
		compiler.setDepthLevel(level);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#getDepthLevel()
	 */
	public int getDepthLevel() {
		return compiler.getDepthLevel();
	}

	/**
	 * Receives a document id and an interface id, compiles the
	 * document and creates the entry event for this document, based on the
	 * interface id. If the interface id is null, all the presentation events
	 * associated with the document ports are generated and returned. The idea is
	 * to allow to start a document through its collection of ports.
	 * 
	 * @param documentId
	 *          the document identification
	 * @param interfaceId
	 *          the document port identification. If null, this parameter means
	 *          "all document ports".
	 * @return the list of entry events for this document. Return null if it is
	 *         not possible to compile the document or to find its entry event.
	 */
	/**
	 * @param documentId
	 * @param interfaceId
	 * @return
	 */
	private List processDocument(String documentId, String interfaceId) {
		List entryEvents;
		List ports;
		int i, size;
		IContextNode context;
		IPort port;
		INodeNesting contextPerspective;
		IFormatterEvent event;

		// look for the entry point perspective
		context = getDocumentContext(documentId);
		if (context == null) {
			// document has no body
			return null;
		}

		ports = new Vector();
		if (interfaceId == null) {
			size = context.getNumPorts();
			for (i = 0; i < size; i++) {
				port = context.getPort(i);
				if (port.getEndInterfacePoint() instanceof IContentAnchor) {
					ports.add(port);
				}
			}
		}
		else {
			port = context.getPort(interfaceId);
			if (port != null) {
				ports.add(port);
			}
		}

		if (ports.isEmpty()) {
			// interfaceId not found
			return null;
		}

		contextPerspective = new NodeNesting(nclDocumentManager
				.getPrivateBaseContext());
		contextPerspective.insertAnchorNode(context);

		entryEvents = new Vector();
		size = ports.size();
		for (i = 0; i < size; i++) {
			port = (IPort)ports.get(i);
			event = compiler.insertContext(contextPerspective, port);
			if (event != null) {
				entryEvents.add(event);
			}
		}

		if (entryEvents.isEmpty()) {
			return null;
		}

		return entryEvents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#getDocumentEntryEvents(java.lang.String)
	 */
	public List getDocumentEntryEvents(String documentId) {
		if (documentEntryEvents.containsKey(documentId)) {
			return (List)documentEntryEvents.get(documentId);
		}
		else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#compileDocument(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean compileDocument(String documentId, String interfaceId) {
		List entryEvents;
		IFormatterEvent event;
		IExecutionObject executionObject;
		ICompositeExecutionObject parentObject;
		IFormatterEvent documentEvent;
		long time;

		time = System.currentTimeMillis();

		if (documentEvents.containsKey(documentId)) {
			return true;
		}

		entryEvents = processDocument(documentId, interfaceId);
		if (entryEvents == null) {
			return false;
		}

		event = (IFormatterEvent)entryEvents.get(0);
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

		documentEvents.put(documentId, documentEvent);
		documentEntryEvents.put(documentId, entryEvents);

		time = System.currentTimeMillis() - time;
		/*
		 * System.err.println( "Formatter::processDocument - document process time: " +
		 * time);
		 */
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#startDocument(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean startDocument(String documentId, String interfaceId) {
		List entryEvents;
		IFormatterEvent documentEvent;

		if (compileDocument(documentId, interfaceId)) {
			documentEvent = (IFormatterEvent)documentEvents.get(documentId);
			if (documentEvent != null) {
				entryEvents = (List)documentEntryEvents.get(documentId);
				scheduler.startDocument(documentEvent, entryEvents);
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#stopDocument(java.lang.String)
	 */
	public boolean stopDocument(String documentId) {
		IFormatterEvent documentEvent;

		if (!documentEvents.containsKey(documentId)) {
			return false;
		}

		documentEvent = (IFormatterEvent)documentEvents.get(documentId);
		scheduler.stopDocument(documentEvent);
		documentEvents.remove(documentId);
		documentEntryEvents.remove(documentId);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#pauseDocument(java.lang.String)
	 */
	public boolean pauseDocument(String documentId) {
		IFormatterEvent documentEvent;

		if (!documentEvents.containsKey(documentId)) {
			return false;
		}

		documentEvent = (IFormatterEvent)documentEvents.get(documentId);
		scheduler.pauseDocument(documentEvent);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#resumeDocument(java.lang.String)
	 */
	public boolean resumeDocument(String documentId) {
		IFormatterEvent documentEvent;

		if (!documentEvents.containsKey(documentId)) {
			return false;
		}

		documentEvent = (IFormatterEvent)documentEvents.get(documentId);
		scheduler.resumeDocument(documentEvent);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#addFormatterListener(br.org.ginga.ncl.IFormatterListener)
	 */
	public void addFormatterListener(IFormatterListener listener) {
		if (!formatterListeners.contains(listener)) {
			formatterListeners.add(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#removeFormatterListener(br.org.ginga.ncl.IFormatterListener)
	 */
	public void removeFormatterListener(IFormatterListener listener) {
		formatterListeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatterSchedulerListener#presentationCompleted(br.org.ginga.ncl.model.event.IFormatterEvent)
	 */
	public void presentationCompleted(IFormatterEvent documentEvent) {
		int i, size;
		String documentId;
		IFormatterListener listener;

		documentId = (String)documentEvent.getExecutionObject().getDataObject()
				.getId();
		if (documentEvents.containsKey(documentId)) {
			size = formatterListeners.size();
			for (i = 0; i < size; i++) {
				listener = (IFormatterListener)formatterListeners.get(i);
				listener.presentationCompleted(documentId);
			}

			documentEvents.remove(documentId);
			documentEntryEvents.remove(documentId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#addRegion(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public ILayoutRegion addRegion(String documentId, String regionId,
			String xmlRegion) {
		return nclDocumentManager.addRegion(documentId, regionId, xmlRegion);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#removeRegion(java.lang.String,
	 *      java.lang.String)
	 */
	public ILayoutRegion removeRegion(String documentId, String regionId) {
		return nclDocumentManager.removeRegion(documentId, regionId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#addRegionBase(java.lang.String,
	 *      java.lang.String)
	 */
	public IRegionBase addRegionBase(String documentId, String xmlRegionBase) {
		return nclDocumentManager.addRegionBase(documentId, xmlRegionBase);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#removeRegionBase(java.lang.String,
	 *      java.lang.String)
	 */
	public IRegionBase removeRegionBase(String documentId, String regionBaseId) {
		return nclDocumentManager.removeRegionBase(documentId, regionBaseId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#addRule(java.lang.String,
	 *      java.lang.String)
	 */
	public IRule addRule(String documentId, String xmlRule) {
		return nclDocumentManager.addRule(documentId, xmlRule);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#removeRule(java.lang.String,
	 *      java.lang.String)
	 */
	public IRule removeRule(String documentId, String ruleId) {
		return nclDocumentManager.removeRule(documentId, ruleId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#addRuleBase(java.lang.String,
	 *      java.lang.String)
	 */
	public IRuleBase addRuleBase(String documentId, String xmlRuleBase) {
		return nclDocumentManager.addRuleBase(documentId, xmlRuleBase);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#removeRuleBase(java.lang.String,
	 *      java.lang.String)
	 */
	public IRuleBase removeRuleBase(String documentId, String ruleBaseId) {
		return nclDocumentManager.removeRuleBase(documentId, ruleBaseId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#addTransition(java.lang.String,
	 *      java.lang.String)
	 */
	public ITransition addTransition(String documentId, String xmlTransition) {
		return nclDocumentManager.addTransition(documentId, xmlTransition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#removeTransition(java.lang.String,
	 *      java.lang.String)
	 */
	public ITransition removeTransition(String documentId, String transitionId) {
		return nclDocumentManager.removeTransition(documentId, transitionId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#addTransitionBase(java.lang.String,
	 *      java.lang.String)
	 */
	public ITransitionBase addTransitionBase(String documentId,
			String xmlTransitionBase) {
		return nclDocumentManager.addTransitionBase(documentId, xmlTransitionBase);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#removeTransitionBase(java.lang.String,
	 *      java.lang.String)
	 */
	public ITransitionBase removeTransitionBase(String documentId,
			String transitionBaseId) {
		return nclDocumentManager
				.removeTransitionBase(documentId, transitionBaseId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#addConnector(java.lang.String,
	 *      java.lang.String)
	 */
	public IConnector addConnector(String documentId, String xmlConnector) {
		return nclDocumentManager.addConnector(documentId, xmlConnector);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#removeConnector(java.lang.String,
	 *      java.lang.String)
	 */
	public IConnector removeConnector(String documentId, String connectorId) {
		return nclDocumentManager.removeConnector(documentId, connectorId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#addConnectorBase(java.lang.String,
	 *      java.lang.String)
	 */
	public IConnectorBase addConnectorBase(String documentId,
			String xmlConnectorBase) {
		return nclDocumentManager.addConnectorBase(documentId, xmlConnectorBase);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#removeConnectorBase(java.lang.String,
	 *      java.lang.String)
	 */
	public IConnectorBase removeConnectorBase(String documentId,
			String connectorBaseId) {
		return nclDocumentManager.removeConnectorBase(documentId, connectorBaseId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#addDescriptor(java.lang.String,
	 *      java.lang.String)
	 */
	public IGenericDescriptor addDescriptor(String documentId,
			String xmlDescriptor) {
		return nclDocumentManager.addDescriptor(documentId, xmlDescriptor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#removeDescriptor(java.lang.String,
	 *      java.lang.String)
	 */
	public IGenericDescriptor removeDescriptor(String documentId,
			String descriptorId) {
		return nclDocumentManager.removeDescriptor(documentId, descriptorId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#addDescriptorBase(java.lang.String,
	 *      java.lang.String)
	 */
	public IDescriptorBase addDescriptorBase(String documentId,
			String xmlDescriptorBase) {
		return nclDocumentManager.addDescriptorBase(documentId, xmlDescriptorBase);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#removeDescriptorBase(java.lang.String,
	 *      java.lang.String)
	 */
	public IDescriptorBase removeDescriptorBase(String documentId,
			String descriptorBaseId) {
		return nclDocumentManager
				.removeDescriptorBase(documentId, descriptorBaseId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#addImportBase(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public IBase addImportBase(String documentId, String docBaseId,
			String xmlImportBase) {
		return nclDocumentManager.addImportBase(documentId, docBaseId,
				xmlImportBase);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#removeImportBase(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public IBase removeImportBase(String documentId, String docBaseId,
			String documentURI) {
		return nclDocumentManager.removeImportBase(documentId, docBaseId,
				documentURI);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#addImportedDocumentBase(java.lang.String,
	 *      java.lang.String)
	 */
	public INclDocument addImportedDocumentBase(String documentId,
			String xmlImportedDocumentBase) {
		return nclDocumentManager.addImportedDocumentBase(documentId,
				xmlImportedDocumentBase);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#removeImportedDocumentBase(java.lang.String,
	 *      java.lang.String)
	 */
	public INclDocument removeImportedDocumentBase(String documentId,
			String importedDocumentBaseId) {
		return nclDocumentManager.removeImportedDocumentBase(documentId,
				importedDocumentBaseId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#addImportNCL(java.lang.String,
	 *      java.lang.String)
	 */
	public INclDocument addImportNCL(String documentId, String xmlImportNCL) {
		return nclDocumentManager.addImportNCL(documentId, xmlImportNCL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#removeImportNCL(java.lang.String,
	 *      java.lang.String)
	 */
	public INclDocument removeImportNCL(String documentId, String documentURI) {
		return nclDocumentManager.removeImportNCL(documentId, documentURI);
	}

	/**
	 * @param referNode
	 */
	private void processInsertedReferNode(IReferNode referNode) {
		INodeEntity nodeEntity;
		IExecutionObject executionObject;
		ICompositeExecutionObject parentObject;
		int depthLevel;

		nodeEntity = (INodeEntity)referNode.getDataEntity();
		if (nodeEntity instanceof IContentNode && !referNode.isNewInstance()) {
			executionObject = compiler.hasExecutionObject(nodeEntity, null);
			if (executionObject != null) {
				parentObject = compiler.addSameInstance(executionObject, referNode);
				if (parentObject != null) {
					depthLevel = compiler.getDepthLevel();
					if (depthLevel > 0) {
						depthLevel = depthLevel - 1;
					}
					compiler.compileExecutionObjectLinks(executionObject, referNode,
							parentObject, depthLevel);
				}
			}
		}
	}

	/**
	 * @param composition
	 */
	private void processInsertedComposition(ICompositeNode composition) {
		Iterator nodes;
		INode node;

		nodes = composition.getNodes();
		while (nodes.hasNext()) {
			node = (INode)nodes.next();
			if (node instanceof IReferNode) {
				processInsertedReferNode((IReferNode)node);
			}
			else if (node instanceof ICompositeNode) {
				processInsertedComposition((ICompositeNode)node);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#addNode(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public INode addNode(String documentId, String compositeId, String xmlNode) {
		INode node;

		node = nclDocumentManager.addNode(documentId, compositeId, xmlNode);
		if (node == null) {
			return null;
		}

		if (node instanceof IReferNode) {
			processInsertedReferNode((IReferNode)node);
		}
		else if (node instanceof ICompositeNode) {
			// look for child nodes with refer and newInstance=false
			processInsertedComposition((ICompositeNode)node);
		}

		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#removeNode(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public INode removeNode(String documentId, String compositeId, String nodeId) {
		INclDocument document;
		INode parentNode, node;
		ICompositeNode compositeNode;
		IExecutionObject executionObject;
		List nodeInterfaces;
		Iterator anchors, ports;
		int i, size;
		IInterfacePoint nodeInterface;

		document = nclDocumentManager.getDocument(documentId);
		if (document == null) {
			// document does not exist
			return null;
		}

		parentNode = document.getNode(compositeId);
		if (parentNode == null || !(parentNode instanceof IContextNode)) {
			// composite node (compositeId) does exist or is not a context node
			return null;
		}

		compositeNode = (IContextNode)parentNode;
		node = compositeNode.getNode(nodeId);
		if (node == null) {
			// node (nodeId) is not a compositeId child node
			return null;
		}

		// remove all node interfaces
		nodeInterfaces = new Vector();
		anchors = node.getAnchors();
		while (anchors.hasNext()) {
			nodeInterfaces.add(anchors.next());
		}

		if (node instanceof ICompositeNode) {
			ports = ((ICompositeNode)node).getPorts();
			while (ports.hasNext()) {
				nodeInterfaces.add(ports.next());
			}
		}

		size = nodeInterfaces.size();
		for (i = 0; i < size; i++) {
			nodeInterface = (IInterfacePoint)nodeInterfaces.get(i);
			removeInterface(node, nodeInterface);
		}

		// remove the execution object
		executionObject = compiler.hasExecutionObject(node, null);
		if (executionObject != null) {
			if (node instanceof IReferNode && !((IReferNode)node).isNewInstance()) {
				// remove the object entry
				compiler.removeExecutionObject(executionObject, (IReferNode)node);
			}
			else {
				// remove the whole execution object
				if (executionObject.getMainEvent() != null) {
					scheduler.stopEvent(executionObject.getMainEvent());
				}
				compiler.removeExecutionObject(executionObject);
			}
		}

		compositeNode.removeNode(node);
		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#addInterface(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public IInterfacePoint addInterface(String documentId, String nodeId,
			String xmlInterface) {
		return nclDocumentManager.addInterface(documentId, nodeId, xmlInterface);
	}

	/**
	 * @param node
	 * @param interfacePoint
	 * @param composition
	 */
	private void removeInterfaceMappings(INode node,
			IInterfacePoint interfacePoint, ICompositeNode composition) {
		List portsToBeRemoved;
		Iterator ports, mappings;
		int i, size;
		IPort port, mapping;

		portsToBeRemoved = new Vector();

		ports = composition.getPorts();
		while (ports.hasNext()) {
			port = (IPort)ports.next();

			if (port instanceof ISwitchPort) {
				mappings = ((ISwitchPort)port).getPorts();
				while (mappings.hasNext()) {
					mapping = (IPort)mappings.next();
					if (mapping.getNode() == node
							&& mapping.getInterfacePoint() == interfacePoint) {
						portsToBeRemoved.add(port);
						break;
					}
				}
			}
			else if (port.getNode() == node
					&& port.getInterfacePoint() == interfacePoint) {
				portsToBeRemoved.add(port);
			}
		}

		size = portsToBeRemoved.size();
		for (i = 0; i < size; i++) {
			port = (IPort)portsToBeRemoved.get(i);
			removeInterface(composition, port);
		}
	}

	/**
	 * @param node
	 * @param interfacePoint
	 * @param composition
	 */
	private void removeInterfaceLinks(INode node, IInterfacePoint interfacePoint,
			ILinkComposition composition) {
		List linksToBeRemoved;
		Iterator links, binds;
		ILink link;
		IBind bind;
		int i, size;

		linksToBeRemoved = new Vector();
		links = composition.getLinks();
		while (links.hasNext()) {
			link = (ILink)links.next();
			// verify if node and interface point participate in link
			binds = link.getBinds();
			while (binds.hasNext()) {
				bind = (IBind)binds.next();
				if (bind.getNode() == node
						&& bind.getInterfacePoint() == interfacePoint) {
					linksToBeRemoved.add(link);
					break;
				}
			}
		}

		size = linksToBeRemoved.size();
		for (i = 0; i < size; i++) {
			link = (ILink)linksToBeRemoved.get(i);
			removeLink(composition, link);
		}
	}

	/**
	 * @param node
	 * @param interfacePoint
	 */
	private void removeInterface(INode node, IInterfacePoint interfacePoint) {
		ICompositeNode parentNode;

		parentNode = node.getParentComposition();

		removeInterfaceMappings(node, interfacePoint, parentNode);

		if (parentNode != null && parentNode instanceof ILinkComposition) {
			removeInterfaceLinks(node, interfacePoint, (ILinkComposition)parentNode);
		}

		if (interfacePoint instanceof IAnchor) {
			node.removeAnchor((IAnchor)interfacePoint);
		}
		else {
			((ICompositeNode)node).removePort((IPort)interfacePoint);
			// TODO verify if a special treatment is necessary for switch ports
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#removeInterface(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public IInterfacePoint removeInterface(String documentId, String nodeId,
			String interfaceId) {
		INclDocument document;
		INode node;
		IInterfacePoint interfacePoint;

		document = nclDocumentManager.getDocument(documentId);
		if (document == null) {
			// document does not exist
			return null;
		}

		node = document.getNode(nodeId);
		if (node == null) {
			// node (nodeId) does not exist
			return null;
		}

		interfacePoint = node.getAnchor(interfaceId);
		if (interfacePoint == null && node instanceof ICompositeNode) {
			interfacePoint = ((ICompositeNode)node).getPort(interfaceId);
		}

		if (interfacePoint == null) {
			// interface (interfaceId) does not exist or does not pertain to node
			return null;
		}

		removeInterface(node, interfacePoint);
		return interfacePoint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#addLink(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public ILink addLink(String documentId, String compositeId, String xmlLink) {
		ILink link;
		INclDocument document;
		IContextNode contextNode;

		link = nclDocumentManager.addLink(documentId, compositeId, xmlLink);
		if (link != null) {
			document = nclDocumentManager.getDocument(documentId);
			contextNode = (IContextNode)document.getNode(compositeId);
			if (link instanceof ICausalLink) {
				compiler.addCausalLink(contextNode, (ICausalLink)link);
			}
		}
		return link;
	}

	/**
	 * @param composition
	 * @param link
	 */
	private void removeLink(ILinkComposition composition, ILink link) {
		ICompositeExecutionObject compositeObject;

		if (composition instanceof ICompositeNode) {
			compositeObject = (ICompositeExecutionObject)compiler.hasExecutionObject(
					(ICompositeNode)composition, null);
			if (compositeObject != null) {
				compositeObject.removeNcmLink(link);
			}
		}
		composition.removeLink(link);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#removeLink(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public ILink removeLink(String documentId, String compositeId, String linkId) {
		INclDocument document;
		INode node;
		IContextNode contextNode;
		ILink link;

		document = nclDocumentManager.getDocument(documentId);
		if (document == null) {
			// document does not exist
			return null;
		}

		node = document.getNode(compositeId);
		if (node == null || !(node instanceof IContextNode)) {
			// composite node (compositeId) does exist or is not a context node
			return null;
		}

		contextNode = (IContextNode)node;
		link = contextNode.getLink(linkId);
		if (link == null) {
			// link (linkId) is not a nodeId child link
			return null;
		}

		removeLink(contextNode, link);
		return link;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.ginga.ncl.IFormatter#setPropertyValue(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean setPropertyValue(String documentId, String nodeId,
			String propertyId, String value) {
		INclDocument document;
		INode node;
		IAnchor anchor;
		INodeNesting perspective;
		IExecutionObject executionObject;
		IFormatterEvent event;
		ILinkAssignmentAction setAction;

		document = nclDocumentManager.getDocument(documentId);
		if (document == null) {
			// document does not exist
			return false;
		}

		node = document.getNode(nodeId);
		if (node == null) {
			// node (nodeId) does exist
			return false;
		}

		anchor = node.getAnchor(propertyId);
		if (!(anchor instanceof IPropertyAnchor)) {
			// interface (interfaceId) is not a property
			return false;
		}

		perspective = new NodeNesting(node.getPerspective());
		try {
			executionObject = compiler.getExecutionObject(perspective, null, compiler
					.getDepthLevel());
		}
		catch (ObjectCreationForbiddenException exc) {
			return false;
		}

		event = compiler.getEvent(executionObject, anchor, IEvent.EVT_ATTRIBUTION,
				null);
		if (event == null || !(event instanceof IAttributionEvent)) {
			return false;
		}

		setAction = new LinkAssignmentAction((IAttributionEvent)event,
				ISimpleAction.ACT_SET, value);
		((ILinkActionListener)scheduler).runAction(setAction);

		return true;
	}
}