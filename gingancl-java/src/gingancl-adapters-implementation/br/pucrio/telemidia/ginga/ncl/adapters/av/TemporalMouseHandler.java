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
package br.pucrio.telemidia.ginga.ncl.adapters.av;

import java.awt.event.MouseEvent;

import br.org.ginga.ncl.adapters.IFormatterPlayerAdapter;
import br.org.ginga.ncl.model.event.ISelectionEvent;
import br.org.ncl.interfaces.IIntervalAnchor;
import br.pucrio.telemidia.ginga.ncl.adapters.MouseHandler;

public class TemporalMouseHandler extends MouseHandler {
	private IFormatterPlayerAdapter playerAdapter;

	protected TemporalMouseHandler(MouseHandler mh, IFormatterPlayerAdapter playerAdapter) {
		super(mh.getEvents());
		this.playerAdapter = playerAdapter;
	}

	public void mouseClicked(MouseEvent arg0) {
		int i, size;
		ISelectionEvent event;
		IIntervalAnchor intervalAnchor;

		size = selectionEvents.size();
		for (i = 0; i < size; i++) {
			event = (ISelectionEvent)selectionEvents.get(i);
			if (event.getAnchor() instanceof IIntervalAnchor) {
				intervalAnchor = (IIntervalAnchor)event.getAnchor();
				if (intervalAnchor.getBegin() <= playerAdapter.getMediaTime()) {
					if (intervalAnchor.getEnd() < 0
							|| playerAdapter.getMediaTime() < intervalAnchor.getEnd()) {
						event.start();
					}
				}
			}
			else {
				event.start();
			}
		}
	}
}
