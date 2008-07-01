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

package br.pucrio.telemidia.ginga.ncl.adapters.procedural.lua;

import java.io.File;

import br.org.ginga.ncl.model.components.IProceduralExecutionObject;
import br.org.ginga.ncl.model.event.IAnchorEvent;
import br.org.ginga.ncl.model.event.IAttributionEvent;
import br.org.ginga.ncl.model.event.IFormatterEvent;
import br.org.ginga.ncl.model.event.ISelectionEvent;
import br.org.ncl.animation.IAnimation;
import br.org.ncl.interfaces.ILambdaAnchor;
import br.pucrio.telemidia.ginga.core.player.procedural.lua.LuaPlayer;
import br.pucrio.telemidia.ginga.ncl.adapters.procedural.ProceduralPlayerAdapter;

public class LuaPlayerAdapter extends ProceduralPlayerAdapter {

	@Override
	protected void createPlayer() {
		/*ICascadingDescriptor descriptor = object.getDescriptor();
		IContent content = ((INodeEntity)object.getDataObject().getDataEntity()).getContent();
		
		String contentPath;
		if (content instanceof IRelativeReferenceContent) {
			contentPath = ((IRelativeReferenceContent)content).getRelativePath();
		}
		else {
			contentPath = "file:";
		}*/
		File contentFile = new File(this.getMRL().getFile());
		if (contentFile.exists()){
			try {
				this.player = new LuaPlayer(this.getMRL());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			System.err.println("[ERR] " + this.getClass().getCanonicalName() +": Could not find Lua script!");
			player = null;
		}
		super.createPlayer();
	}

	public boolean setPropertyValue(IAttributionEvent event, Object value, 
			IAnimation animation) {
		String attName = event.getAnchor().getPropertyName();
		this.player.setPropertyValue(attName, value.toString());
		// calls the super set attribute value too
		return super.setPropertyValue(event, value, animation);
	}
	
	public synchronized void setCurrentEvent(IFormatterEvent event) {
		String areaId;

		if (event != null &&
				preparedEvents.containsKey(event.getId()) &&
				!(event instanceof ISelectionEvent) &&
				event instanceof IAnchorEvent) {

			areaId = (String) ((IAnchorEvent)event).getAnchor().getId();

			if ((((IAnchorEvent)event).getAnchor()) instanceof 
					ILambdaAnchor) {
				areaId = "";
			}
			
			currentEvent = event;
			((IProceduralExecutionObject)object).setCurrentEvent(currentEvent);
			player.setScope(areaId);
		}
	}
}
