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

import br.org.ginga.ncl.model.link.ILinkAction;
import br.org.ginga.ncl.model.link.ILinkActionProgressionListener;
import br.org.ginga.ncl.model.link.ILinkCompoundAction;
import br.org.ncl.connectors.ICompoundAction;

public class LinkCompoundAction extends LinkAction implements
		ILinkCompoundAction, ILinkActionProgressionListener {
	protected List actions;

	protected short operator;

	private int pendingActions;

	private boolean hasStart;

	public LinkCompoundAction(short operator) {
		super();
		actions = new Vector();
		this.operator = operator;
	}

	public short getOperator() {
		return operator;
	}

	public void addAction(ILinkAction action) {
		actions.add(action);
		action.addActionProgressionListener(this);
	}

	public Iterator getActions() {
		return actions.iterator();
	}

	public List getEvents() {
		List events;
		int i, size;
		ILinkAction action;

		events = new Vector();
		size = actions.size();
		for (i = 0; i < size; i++) {
			action = (ILinkAction)actions.get(i);
			events.addAll(action.getEvents());
		}
		return events;
	}

	public void destroy() {
		int i, size;
		ILinkAction action;

		size = actions.size();
		for (i = 0; i < size; i++) {
			action = (ILinkAction)actions.get(i);
			action.removeActionProgressionListener(this);
			action.destroy();
		}
		actions.clear();
		actions = null;
	}

	public void run() {
		int i, size;
		ILinkAction action;
		Thread actionThread;

		size = actions.size();
		pendingActions = size;
		hasStart = false;

		super.run();

		if (operator == ICompoundAction.OP_PAR) {
			for (i = 0; i < size; i++) {
				action = (ILinkAction)actions.get(i);
				actionThread = new Thread(action);
				actionThread.start();
			}
		}
		else {
			for (i = 0; i < size; i++) {
				action = (ILinkAction)actions.get(i);
				action.run();
			}
		}
	}

	public void actionProcessed(boolean start) {
		pendingActions--;
		hasStart = (hasStart || start);
		if (pendingActions == 0) {
			super.notifyProgressionListeners(hasStart);
		}
	}
}
