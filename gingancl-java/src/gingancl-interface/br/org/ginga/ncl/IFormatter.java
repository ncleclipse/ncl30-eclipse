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
package br.org.ginga.ncl;

import java.util.List;

import br.org.ncl.IBase;
import br.org.ncl.INclDocument;
import br.org.ncl.components.INode;
import br.org.ncl.connectors.IConnector;
import br.org.ncl.connectors.IConnectorBase;
import br.org.ncl.descriptor.IDescriptorBase;
import br.org.ncl.descriptor.IGenericDescriptor;
import br.org.ncl.interfaces.IInterfacePoint;
import br.org.ncl.layout.ILayoutRegion;
import br.org.ncl.layout.IRegionBase;
import br.org.ncl.link.ILink;
import br.org.ncl.switches.IRule;
import br.org.ncl.switches.IRuleBase;
import br.org.ncl.transition.ITransition;
import br.org.ncl.transition.ITransitionBase;

/**
 * The IFormatter interface is the main entry point of the NCL presentation 
 * engine.
 *
 */
public interface IFormatter {
	/*
	 * This constant should be used if it is desired to compile all the execution
	 * objects reached in the main time chain.
	 */
	public static final int DEEPEST_LEVEL = -1;

	/**
	 * Clears all the formatter data structures.
	 *
	 */
	void reset();

	/**
	 * Closes the formatter and after this call the formatter instance cannot
	 * be used anymore.
	 *
	 */
	void close();

	/**
	 * Registers a new listener for this formatter.
	 * @param listener the new listener to be registred.
	 */
	void addFormatterListener(IFormatterListener listener);

	/**
	 * Removes a previous added formatter listener
	 * @param listener the listener to be removed
	 */
	void removeFormatterListener(IFormatterListener listener);

	/**
	 * Adds a document in the formatter private base
	 * @param docLocation the document locator
	 * @return the new document instance compiled and inserted. Return null
	 * if the document could not be added.
	 */
	INclDocument addDocument(String docLocation);

	/**
	 * Removes a document from the formatter private base.
	 * @param documentId the id of the document to be removed.
	 * @return true if the document could be removed and false otherwise.
	 */
	boolean removeDocument(String documentId);

	/**
	 * Gets the events that are entry points of a specific document. These are
	 * the events started when the corresponding document is started.
	 * @param documentId the id of the document
	 * @return the list of entry events
	 */
	List getDocumentEntryEvents(String documentId);

	/**
	 * Sets the nesting level for document compilation. A level n will compile
	 * the object, its links and the related objects with level n-1. A level 0
	 * do not compile the object links. This parameter allows a progressive 
	 * compilation of NCL documents. A DEEPEST_LEVEL will compile all the 
	 * reached objects from a defined start point.
	 * @param level the compilation level
	 */
	void setDepthLevel(int level);

	/**
	 * Returns the compilation level.
	 * @return the compilation level.
	 */
	int getDepthLevel();

	/**
	 * Compiles an NCL document creating the formatter data structure.
	 * @param documentId the id of the document to be compiled.
	 * @param interfaceId the id of an interface of the document. If null
	 * all the document interfaces are inserted as entry point.
	 * @return true if the document could be compiled and false otherwise.
	 */
	boolean compileDocument(String documentId, String interfaceId);

	/**
	 * Starts an NCL document presentation. If the document was not compiled the 
	 * compileDocument is first called.
	 * @param documentId the id of the document to be started.
	 * @param interfaceId the id of the document interface to be considered as
	 * the document entry point. If null all document interfaces are started.
	 * @return true if the document could be started and false otherwise.
	 */
	boolean startDocument(String documentId, String interfaceId);

	/**
	 * Stops an NCL document presentation.
	 * @param documentId the id of the document to be stopped.
	 * @return true if the document could be stopped and false otherwise.
	 */
	boolean stopDocument(String documentId);

	/**
	 * Pauses an NCL document presentation.
	 * @param documentId the id of the document to be paused.
	 * @return true if the document could be paused and false otherwise.
	 */
	boolean pauseDocument(String documentId);

	/**
	 * Resumes an NCL document presentation.
	 * @param documentId the id of the document to be resumed.
	 * @return true if the document could be resumed and false otherwise.
	 */
	boolean resumeDocument(String documentId);

	/* Methods for live edition */
	
	ILayoutRegion addRegion(String documentId, String regionId, String xmlRegion);

	ILayoutRegion removeRegion(String documentId, String regionId);

	IRegionBase addRegionBase(String documentId, String xmlRegionBase);

	IRegionBase removeRegionBase(String documentId, String regionBaseId);

	IRule addRule(String documentId, String xmlRule);

	IRule removeRule(String documentId, String ruleId);

	IRuleBase addRuleBase(String documentId, String xmlRuleBase);

	IRuleBase removeRuleBase(String documentId, String ruleBaseId);

	ITransition addTransition(String documentId, String xmlTransition);

	ITransition removeTransition(String documentId, String transitionId);

	ITransitionBase addTransitionBase(String documentId, String xmlTransitionBase);

	ITransitionBase removeTransitionBase(String documentId,
			String ruleTransitionId);

	IConnector addConnector(String documentId, String xmlConnector);

	IConnector removeConnector(String documentId, String connectorId);

	IConnectorBase addConnectorBase(String documentId, String xmlConnectorBase);

	IConnectorBase removeConnectorBase(String documentId, String connectorBaseId);

	IGenericDescriptor addDescriptor(String documentId, String xmlDescriptor);

	IGenericDescriptor removeDescriptor(String documentId, String descriptorId);

	IDescriptorBase addDescriptorBase(String documentId, String xmlDescriptorBase);

	IDescriptorBase removeDescriptorBase(String documentId,
			String descriptorBaseId);

	IBase addImportBase(String documentId, String docBaseId, String xmlImportBase);

	IBase removeImportBase(String documentId, String docBaseId, String documentURI);

	INclDocument addImportedDocumentBase(String documentId,
			String xmlImportedDocumentBase);

	INclDocument removeImportedDocumentBase(String documentId,
			String importedDocumentBaseId);

	INclDocument addImportNCL(String documentId, String xmlImportNCL);

	INclDocument removeImportNCL(String documentId, String documentURI);

	INode addNode(String documentId, String compositeId, String xmlNode);

	INode removeNode(String documentId, String compositeId, String nodeId);

	IInterfacePoint addInterface(String documentId, String nodeId,
			String xmlInterface);

	IInterfacePoint removeInterface(String documentId, String nodeId,
			String interfaceId);

	ILink addLink(String documentId, String compositeId, String xmlLink);

	ILink removeLink(String documentId, String compositeId, String linkId);

	boolean setPropertyValue(String documentId, String nodeId, String propertyId,
			String value);
}
