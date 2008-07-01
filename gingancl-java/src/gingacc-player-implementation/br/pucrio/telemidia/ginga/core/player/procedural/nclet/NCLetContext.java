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
package br.pucrio.telemidia.ginga.core.player.procedural.nclet;

import java.awt.Container;

import javax.swing.JPanel;
import javax.tv.xlet.XletContext;

import br.org.ginga.core.io.ISurface;
import br.org.ginga.core.player.IPlayer;
import br.org.ginga.core.player.procedural.nclet.INCLetContext;
import br.pucrio.telemidia.ginga.ncl.adaptation.context.PresentationContext;

public class NCLetContext implements INCLetContext {
	private IPlayer player;
	private String applicationArguments;
	private Container rootContainer;

	public NCLetContext(IPlayer playerObject) {
		this.player = playerObject;
		applicationArguments = "";
	}
	
	public NCLetContext(IPlayer player, String args) {
		this.player = player;
		applicationArguments = args;
	}
	
	/*
	public boolean startNclEvent(String anchorId) {
		return player.startEvent(anchorId);
	}

	public boolean stopNclEvent(String anchorId) {
		return player.stopEvent(anchorId);
	}

	public boolean pauseNclEvent(String anchorId) {
		return player.pauseEvent(anchorId);
	}

	public boolean resumeNclEvent(String anchorId) {
		return player.resumeEvent(anchorId);
	}

	public boolean abortNclEvent(String anchorId) {
		return player.abortEvent(anchorId);
	}
	 */

	/**
	 * Allows procedural code to query settings node information
	 * 
	 * @param attName
	 *           the attribute name whose value is queried.
	 */ 
	public Object getSettingValue(String attName) {
		return PresentationContext.getInstance().getPropertyValue(attName);
	}

	public Object getXletProperty(String key) {
		if(key.equals(XletContext.ARGS)){
			return this.applicationArguments;
		}else if(key.equals("javax.tv.xlet.container")){
			ISurface surface = player.getSurface();
			if(surface instanceof JPanel){
				if(rootContainer == null){
					rootContainer = new Container();
					this.player.getSurface().setSurface(rootContainer);
					this.player.getSurface().setSurface(rootContainer);
					return rootContainer;
				}
				return rootContainer;
			}else
				return this.player.getSurface().getSurface();
		}else{
			Object property = PresentationContext.getInstance().getPropertyValue(key);
			return property;
		}
	}

	public void notifyDestroyed() {
		player.stop();
		player.getSurface().clear();
	}

	public void notifyPaused() {
		player.pause();
	}

	public void resumeRequest() {
		player.resume();
	}
}
