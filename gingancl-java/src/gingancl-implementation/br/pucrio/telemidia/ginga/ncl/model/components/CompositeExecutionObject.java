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
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.org.ginga.ncl.model.components.ICompositeExecutionObject;
import br.org.ginga.ncl.model.components.IExecutionObject;
import br.org.ginga.ncl.model.event.IEventListener;
import br.org.ginga.ncl.model.event.IFormatterEvent;
import br.org.ginga.ncl.model.event.IPresentationEvent;
import br.org.ginga.ncl.model.link.IFormatterCausalLink;
import br.org.ginga.ncl.model.link.IFormatterLink;
import br.org.ginga.ncl.model.link.ILinkListener;
import br.org.ginga.ncl.model.presentation.ICascadingDescriptor;
import br.org.ncl.components.INode;
import br.org.ncl.connectors.IEvent;
import br.org.ncl.link.ILink;
import br.org.ncl.link.ILinkComposition;
import br.pucrio.telemidia.ginga.ncl.model.components.ExecutionObject;

public class CompositeExecutionObject extends ExecutionObject implements
		ICompositeExecutionObject, IEventListener, ILinkListener {
	private static final long serialVersionUID = -8937767811942960551L;

	protected List<IExecutionObject> execObjList;

	private List<IFormatterLink> links;

	private List<ILink> uncompiledLinks;

	private int numRunningObjects; // number of child objects occurring

	private int numPausedObjects; // number of child objects paused

	private short lastTransition;

	private Map<IFormatterLink,Integer> pendingLinks;

	public CompositeExecutionObject(String id, INode dataObject) {
		this(id, dataObject, null);
	}

	public CompositeExecutionObject(String id, INode dataObject,
			ICascadingDescriptor descriptor) {

		super(id, dataObject, descriptor);

		ILinkComposition compositeNode;
		Iterator<ILink> compositionLinks;

		execObjList = new ArrayList<IExecutionObject>();
		links = new ArrayList<IFormatterLink>();

		uncompiledLinks = new ArrayList<ILink>();
		if (dataObject.getDataEntity() instanceof ILinkComposition) {
			compositeNode = (ILinkComposition)dataObject.getDataEntity();
			compositionLinks = compositeNode.getLinks();
			while (compositionLinks.hasNext()) {
				uncompiledLinks.add(compositionLinks.next());
			}
		}

		numRunningObjects = 0;
		numPausedObjects = 0;
		pendingLinks = new Hashtable<IFormatterLink, Integer>();
	}

	public boolean addExecutionObject(IExecutionObject execObj) {
		if (execObj == null) {
			return false;
		}

		execObjList.add(execObj);
		execObj.addParentObject(this, getDataObject());
		return true;
	}

	public boolean containsExecutionObject(String execObjId) {
		if (getExecutionObject(execObjId) != null)
			return true;
		else
			return false;
	}

	public IExecutionObject getExecutionObject(String execObjId) {
		Iterator<IExecutionObject> iterator;
		IExecutionObject execObj;

		// verifica se alguma das alternativas possui id igual ao passado c/ argum.
		iterator = execObjList.iterator();
		while (iterator.hasNext()) {
			execObj = (IExecutionObject)iterator.next();
			if (execObj.getId().compareTo(execObjId) == 0)
				return execObj;
		}

		// nenhum no' no contexto possui id igual ao passado como argumento
		return null;
	}

	public Iterator<IExecutionObject> getExecutionObjects() {
		return execObjList.iterator();
	}

	public Iterator<IExecutionObject> recursivellyGetExecutionObjects() {
		List<IExecutionObject> objects;
		int i, size;
		IExecutionObject childObject;
		Iterator<IExecutionObject> grandChildrenObjects;

		objects = new ArrayList<IExecutionObject>();
		size = execObjList.size();
		for (i = 0; i < size; i++) {
			childObject = (IExecutionObject)execObjList.get(i);
			objects.add(childObject);
			if (childObject instanceof ICompositeExecutionObject) {
				grandChildrenObjects = ((ICompositeExecutionObject)childObject)
						.recursivellyGetExecutionObjects();
				while (grandChildrenObjects.hasNext()) {
					objects.add(grandChildrenObjects.next());
				}
			}
		}
		return objects.iterator();
	}

	public int getNumExecutionObjects() {
		return execObjList.size();
	}

	public boolean removeExecutionObject(IExecutionObject execObj) {
		if (execObjList.contains(execObj)) {
			execObjList.remove(execObj);
			return true;
		}
		return false;
	}

	public List<ILink> getUncompiledLinks() {
		return uncompiledLinks;
	}

	public boolean containsUncompiledLink(ILink dataLink) {
		return uncompiledLinks.contains(dataLink);
	}

	public void removeLinkUncompiled(ILink ncmLink) {
		uncompiledLinks.remove(ncmLink);
	}

	public void setLinkCompiled(IFormatterLink formatterLink) {
		links.add(formatterLink);
	}

	public void setLinkUncompiled(IFormatterLink formatterLink) {
		uncompiledLinks.add(formatterLink.getNcmLink());
		formatterLink.destroy();
		links.remove(formatterLink);
	}

	public void addNcmLink(ILink ncmLink) {
		uncompiledLinks.add(ncmLink);
	}

	public void removeNcmLink(ILink ncmLink) {
		int i, size;
		IFormatterLink link;

		if (uncompiledLinks.contains(ncmLink)) {
			uncompiledLinks.remove(ncmLink);
		}
		else {
			size = links.size();
			for (i = 0; i < size; i++) {
				link = (IFormatterLink)links.get(i);
				if (ncmLink == link.getNcmLink()) {
					link.destroy();
					links.remove(i);
					return;
				}
			}
		}
	}

	public void setAllLinksAsUncompiled(boolean isRecursive) {
		IFormatterLink link;
		ILink ncmLink;
		int i, size;
		IExecutionObject childObject;

		while (!links.isEmpty()) {
			link = (IFormatterLink)links.get(links.size() - 1);
			ncmLink = link.getNcmLink();
			uncompiledLinks.add(ncmLink);
			link.destroy();
			links.remove(links.size() - 1);
		}

		if (isRecursive) {
			size = execObjList.size();
			for (i = 0; i < size; i++) {
				childObject = (IExecutionObject)execObjList.get(i);
				if (childObject instanceof ICompositeExecutionObject) {
					((ICompositeExecutionObject)childObject)
							.setAllLinksAsUncompiled(isRecursive);
				}
			}
		}
	}

	public Iterator<IFormatterLink> getLinks() {
		return links.iterator();
	}

	private void setParentsAsListeners() {
		Iterator<ICompositeExecutionObject> parentObjects;
		CompositeExecutionObject parentObject;

		parentObjects = super.parentTable.values().iterator();
		while (parentObjects.hasNext()) {
			parentObject = (CompositeExecutionObject)parentObjects.next();
			// register parent as a composite presentation listener
			super.wholeContent.addEventListener(parentObject);
		}
	}

	private void unsetParentsAsListeners() {
		Iterator<ICompositeExecutionObject> parentObjects;
		CompositeExecutionObject parentObject;

		parentObjects = super.parentTable.values().iterator();
		while (parentObjects.hasNext()) {
			parentObject = (CompositeExecutionObject)parentObjects.next();
			// register parent as a composite presentation listener
			super.wholeContent.removeEventListener(parentObject);
		}
	}

	public void eventStateChanged(IFormatterEvent event, short transition,
			short previousState) {
		IPresentationEvent childEvent;

		if (!(event instanceof IPresentationEvent)) {
			return;
		}

		childEvent = (IPresentationEvent)event;

		switch (transition) {
		case IEvent.TR_STARTS:
			if (numRunningObjects == 0 && numPausedObjects == 0) {
				setParentsAsListeners();
				if (super.wholeContent.start()) {
					/*
					 * System.err.println("CompositeExecutionObject::eventStateChanged " +
					 * super.getId() + " STARTED");
					 */
				}
			}
			numRunningObjects++;
			/*
			 * System.err.println("CompositeExecutionObject::eventStateChanged " +
			 * super.getId() + " RUNNING " + event.getExecutionObject().getId() + "
			 * RUNNING=" + numRunningObjects);
			 */
			break;

		case IEvent.TR_ABORTS:
			lastTransition = transition;
			numRunningObjects--;
			if (numRunningObjects == 0 && numPausedObjects == 0
					&& pendingLinks.isEmpty()) {
				super.wholeContent.abort();
				unsetParentsAsListeners();
				/*
				 * System.err.println("CompositeExecutionObject::eventStateChanged " +
				 * super.getId() + " ABORTED");
				 */
			}
			break;

		case IEvent.TR_STOPS:
			lastTransition = transition;
			if (childEvent.getRepetitions() == 0) {
				if (previousState == IEvent.ST_OCCURRING) {
					numRunningObjects--;
				}
				else { // previousState == IEvent.ST_PAUSED
					numPausedObjects--;
				}
				
				/*
				 * System.err.println("CompositeExecutionObject::eventStateChanged " +
				 * super.getId() + " STOP-RUNNING " + event.getExecutionObject().getId() + "
				 * RUNNING=" + numRunningObjects);
				 */
				if (numRunningObjects == 0 && numPausedObjects == 0
						&& pendingLinks.isEmpty()) {
					/*
					 * System.err.println("CompositeExecutionObject::eventStateChanged " +
					 * super.getId() + " STOPPED (Event STATE:" +
					 * super.wholeContent.getCurrentState() + ")");
					 */
					super.wholeContent.stop();
					unsetParentsAsListeners();
				}
				/*
				 * else {
				 * System.err.println("CompositeExecutionObject::eventStateChanged " +
				 * super.getId() + " DO NOT STOP " + "numRunningObjects=" +
				 * numRunningObjects + " numPausedObjects=" + numPausedObjects + "
				 * numPendingLinks=" + pendingLinks.size()); Iterator links =
				 * pendingLinks.keySet().iterator(); while (links.hasNext()) {
				 * IFormatterCausalLink link = (IFormatterCausalLink)links.next();
				 * System.err.println("CompositeExecutionObject::eventStateChanged " +
				 * super.getId() + " link=" + link.getNcmLink().getId() + "Integer=" +
				 * pendingLinks.get(link)); } }
				 */
			}
			break;

		case IEvent.TR_PAUSES:
			numRunningObjects--;
			numPausedObjects++;
			if (numRunningObjects == 0) {
				/*
				 * System.err.println("CompositeExecutionObject::eventStateChanged " +
				 * super.getId() + " PAUSED");
				 */
				super.wholeContent.pause();
			}
			break;

		case IEvent.TR_RESUMES:
			numPausedObjects--;
			numRunningObjects++;
			if (numRunningObjects == 1) {
				/*
				 * System.err.println("CompositeExecutionObject::eventStateChanged " +
				 * super.getId() + " RESUMED");
				 */
				super.wholeContent.resume();
			}
			break;

		}
	}

	public void linkEvaluationStarted(IFormatterCausalLink link) {
		Integer linkNumber;

		if (pendingLinks.containsKey(link)) {
			linkNumber = (Integer)pendingLinks.get(link);
			pendingLinks.put(link, new Integer(linkNumber.intValue() + 1));
		}
		else {
			pendingLinks.put(link, new Integer(1));
		}
	}

	public void linkEvaluationFinished(IFormatterCausalLink link, boolean start) {
		Integer linkNumber;

		if (pendingLinks.containsKey(link)) {
			linkNumber = (Integer)pendingLinks.get(link);
			if (linkNumber.intValue() == 1) {
				pendingLinks.remove(link);
				if (numRunningObjects == 0 && numPausedObjects == 0 &&
						pendingLinks.isEmpty()) {
					if (start) {
						// if nothing starts the composition may stay locked as occurring
					}
					else if (lastTransition == IEvent.TR_STOPS) {
						/*
						 * System.err.println("CompositeExecutionObject::linkEvaluationFinished " +
						 * super.getId() + " STOPPED");
						 */
						super.wholeContent.stop();
						unsetParentsAsListeners();
					}
					else {
						/*
						 * System.err.println("CompositeExecutionObject::linkEvaluationFinished " +
						 * super.getId() + " ABORTED");
						 */
						super.wholeContent.abort();
						unsetParentsAsListeners();
					}
				}
			}
			else {
				pendingLinks.put(link, new Integer(linkNumber.intValue() - 1));
			}
		}
	}

	public boolean destroy() {
		int i, size;
		IFormatterLink link;
		IExecutionObject object;

		pendingLinks.clear();
		pendingLinks = null;

		size = links.size();
		for (i = 0; i < size; i++) {
			link = (IFormatterLink)links.get(i);
			link.destroy();
		}
		links.clear();
		links = null;

		uncompiledLinks.clear();
		uncompiledLinks = null;

		size = execObjList.size();
		for (i = 0; i < size; i++) {
			object = (IExecutionObject)execObjList.get(i);
			object.destroy();
		}

		execObjList.clear();
		execObjList = null;

		return super.destroy();
	}
}
