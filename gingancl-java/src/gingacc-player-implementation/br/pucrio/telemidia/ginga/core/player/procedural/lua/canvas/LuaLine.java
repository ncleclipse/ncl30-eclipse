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

package br.pucrio.telemidia.ginga.core.player.procedural.lua.canvas;

import java.awt.Color;
import java.awt.Graphics;

public class LuaLine extends LuaColoredDraw{
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	
	public LuaLine(LuaCanvas parentCanvas,int x1, int y1, int x2, int y2, Color color){
		super(parentCanvas,x1<x2?x1:x2, y1<y2?y1:y2,color);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public LuaLine(LuaCanvas parentCanvas,int x1, int y1, int x2, int y2){
		this(parentCanvas,x1,y1,x2,y2,Color.BLACK);
	}
	
	public void draw(Graphics g) {
		Color oldColor = g.getColor();
		g.setColor(this.getColor());
		//Point parentCanvasPosition = this.getParentCanvasPosition();
		g.drawLine(x1 /*+ parentCanvasPosition.x*/,
				y1 /*+ parentCanvasPosition.y*/,
				x2 /*+ parentCanvasPosition.x*/,
				y2 /*+ parentCanvasPosition.y*/);
		g.setColor(oldColor);
	}
}
