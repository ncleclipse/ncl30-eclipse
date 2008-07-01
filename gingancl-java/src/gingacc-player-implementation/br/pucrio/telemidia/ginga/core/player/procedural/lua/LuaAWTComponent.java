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

package br.pucrio.telemidia.ginga.core.player.procedural.lua;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import org.keplerproject.luajava.LuaState;

import br.pucrio.telemidia.ginga.core.player.procedural.lua.canvas.LuaCanvas;

public class LuaAWTComponent extends Component {
	@Override
	public void setSize(Dimension arg0) {
		super.setSize(arg0);
		main.setSize(arg0.width, arg0.height);
	}

	@Override
	public void setSize(int arg0, int arg1) {
		super.setSize(arg0, arg1);
		main.setSize(arg0, arg1);
	}

	private LuaCanvas main;
	private LuaComponentListener resizer;
	public LuaAWTComponent(LuaState L){
		resizer = new LuaComponentListener();
		this.addComponentListener(resizer);
		main=new LuaCanvas(this,L);
	}
	
	public LuaCanvas getCanvas(){
		if(main==null){
			//main=new LuaCanvas(this);
			repaint();
		}
		return main;
	}
	public void paint(Graphics g){
		main.draw(g);
	}
	
	public class LuaComponentListener implements ComponentListener{

		public void componentHidden(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void componentMoved(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void componentResized(ComponentEvent e) {
			main.setSize(e.getComponent().getWidth(), e.getComponent().getHeight());
			//main.flush();
		}

		public void componentShown(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
