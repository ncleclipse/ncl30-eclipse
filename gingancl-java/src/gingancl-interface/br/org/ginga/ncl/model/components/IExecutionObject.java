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
package br.org.ginga.ncl.model.components;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import br.org.ginga.ncl.model.event.IAttributionEvent;
import br.org.ginga.ncl.model.event.IFormatterEvent;
import br.org.ginga.ncl.model.event.IPresentationEvent;
import br.org.ginga.ncl.model.event.ISelectionEvent;
import br.org.ginga.ncl.model.event.transition.IEventTransition;
import br.org.ginga.ncl.model.presentation.ICascadingDescriptor;
import br.org.ncl.animation.IAnimation;
import br.org.ncl.components.INode;
import br.org.ncl.descriptor.IGenericDescriptor;

/**
 * The IExecutionObject interface represents a hypermedia node (simple or
 * composite) when being presented. An execution object brings together not only
 * the node information but also its presentation characteristics (descriptor
 * and perspective). Execution object presentation is controlled by the
 * formatter, specifically a formatter player adapter.
 */
public interface IExecutionObject extends Comparable<IExecutionObject>, Serializable {
	/**
	 * Compares the id of this execution object with the id of another one.
	 * 
	 * @param object
	 *          the execution object to be compared with.
	 * @return 0 if both ids are equal; a negative value if this execution object
	 *         id is lesser than the the other execution object id; and a positive
	 *         value if this execution object id is greater than the the other
	 *         execution object id.
	 */
	int compareToUsingId(IExecutionObject object);

	int compareToUsingStartTime(IExecutionObject object);

	INode getDataObject();

	ICascadingDescriptor getDescriptor();

	String getId();

	void setDescriptor(ICascadingDescriptor cascadingDescriptor);

	void setDescriptor(IGenericDescriptor descriptor);

	boolean addEvent(IFormatterEvent event);
	
	void addEventTransition(IEventTransition transition);

	boolean containsEvent(IFormatterEvent event);

	IFormatterEvent getEvent(String id);

	Iterator<IFormatterEvent> getEvents();

	double getExpectedStartTime();

	IPresentationEvent getWholeContentPresentationEvent();

	void setStartTime(double t);

	void updateEventDurations();

	void updateEventDuration(IPresentationEvent event);

	Iterator<IPresentationEvent> getPresentationEvents();

	Iterator<ISelectionEvent> getSelectionEvents();

	boolean removeEvent(IFormatterEvent event);
	
	void removeEventTransition(IPresentationEvent event);

	boolean isCompiled();

	void setCompiled(boolean status);

	Iterator<INode> getNodes();

	void removeNode(INode node);

	INodeNesting getNodePerspective();

	INodeNesting getNodePerspective(INode node);

	List<IExecutionObject> getObjectPerspective();

	List<IExecutionObject> getObjectPerspective(INode node);

	void addParentObject(ICompositeExecutionObject parentObject, INode parentNode);

	void addParentObject(INode node, ICompositeExecutionObject parentObject,
			INode parentNode);
	
	void addPresentationEvent(IPresentationEvent event);

	ICompositeExecutionObject getParentObject(INode node);

	ICompositeExecutionObject getParentObject();

	Iterator<INode> getParentNodes();

	IFormatterEvent getMainEvent();

	boolean prepare(IFormatterEvent event, double offsetTime);

	boolean start();

	boolean stop();

	boolean pause();

	boolean resume();

	boolean abort();

	boolean unprepare();

	boolean setPropertyValue(
			IAttributionEvent event, Object value, IAnimation animation);

	void select(int accessCode, double currentTime);
	Set<Integer> getInputEvents();

	void updateTransitionTable(double currentTime);

	IEventTransition getNextTransition();

	boolean destroy();
}
