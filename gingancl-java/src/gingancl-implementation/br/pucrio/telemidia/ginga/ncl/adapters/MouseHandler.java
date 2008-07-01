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
package br.pucrio.telemidia.ginga.ncl.adapters;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import br.org.ginga.ncl.model.components.IExecutionObject;
import br.org.ginga.ncl.model.event.ISelectionEvent;
import br.pucrio.telemidia.ginga.core.io.CodeMap;

public class MouseHandler extends MouseAdapter {
	protected List selectionEvents;

	protected MouseHandler(List selEvents) {
		int i, size;

		selectionEvents = new Vector();
		size = selEvents.size();
		for (i = 0; i < size; i++) {
			selectionEvents.add(selEvents.get(i));
		}
	}

	public static MouseHandler createMouseHandler(IExecutionObject object) {
		Iterator events;
		ISelectionEvent event;
		List selEvents;

		selEvents = new Vector();
		events = object.getSelectionEvents();
		while (events.hasNext()) {
			event = (ISelectionEvent)events.next();
			if (event.getSelectionCode() == CodeMap.getInstance().getCode("NO_CODE")
					|| event.getSelectionCode() == CodeMap.getInstance().getCode(
							"ENTER")) {
				selEvents.add(event);
			}
		}

		if (selEvents.size() > 0) {
			return new MouseHandler(selEvents);
		}
		else {
			return null;
		}
	}

	public List getEvents() {
		return selectionEvents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent arg0) {
		int i, size;
		ISelectionEvent event;

		size = selectionEvents.size();
		for (i = 0; i < size; i++) {
			event = (ISelectionEvent)selectionEvents.get(i);
			event.start();
		}
	}

	public void clear() {
		selectionEvents.clear();
	}
}
