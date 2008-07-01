/******************************************************************************
Este arquivo � parte da implementa��o do ambiente declarativo do middleware 
Ginga (Ginga-NCL).

Direitos Autorais Reservados (c) 1989-2007 PUC-Rio/Laborat�rio TeleM�dia

Este programa � software livre; voc� pode redistribu�-lo e/ou modific�-lo sob 
os termos da Licen�a P�blica Geral GNU vers�o 2 conforme publicada pela Free 
Software Foundation.

Este programa � distribu�do na expectativa de que seja �til, por�m, SEM 
NENHUMA GARANTIA; nem mesmo a garantia impl�cita de COMERCIABILIDADE OU 
ADEQUA��O A UMA FINALIDADE ESPEC�FICA. Consulte a Licen�a P�blica Geral do 
GNU vers�o 2 para mais detalhes. 

Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral do GNU vers�o 2 junto 
com este programa; se n�o, escreva para a Free Software Foundation, Inc., no 
endere�o 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA. 

Para maiores informa��es:
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
package br.pucrio.telemidia.ginga.ncl.model.link;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import br.org.ginga.ncl.model.event.IFormatterEvent;
import br.org.ginga.ncl.model.link.ILinkActionListener;
import br.org.ginga.ncl.model.link.ILinkSimpleAction;
import br.org.ncl.connectors.ISimpleAction;

public class LinkSimpleAction extends LinkAction implements ILinkSimpleAction {
	protected IFormatterEvent event;

	protected short actionType;

	protected List listeners;

	public LinkSimpleAction(IFormatterEvent event, short type) {
		super();
		this.event = event;
		this.actionType = type;
		this.listeners = new Vector();
	}

	public IFormatterEvent getEvent() {
		return event;
	}

	public short getType() {
		return actionType;
	}

	public void run() {
		int i, size;
		ILinkActionListener listener;

		super.run();
		size = listeners.size();
		for (i = 0; i < size; i++) {
			listener = (ILinkActionListener)listeners.get(i);
			listener.runAction(this);
		}

		if (actionType == ISimpleAction.ACT_START) {
			super.notifyProgressionListeners(true);
		}
		else {
			super.notifyProgressionListeners(false);
		}
	}

	public void addActionListener(ILinkActionListener listener) {
		listeners.add(listener);
	}

	public void removeActionListener(ILinkActionListener listener) {
		listeners.remove(listener);
	}

	public Iterator getActionListeners() {
		return listeners.iterator();
	}

	public List getEvents() {
		List events;

		events = new Vector();
		events.add(event);
		return events;
	}

	public void destroy() {
		listeners.clear();
		listeners = null;
		event = null;
	}
}