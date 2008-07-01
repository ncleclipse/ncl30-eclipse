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

package br.pucrio.telemidia.ginga.core.io;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Panel;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import br.org.ginga.core.io.IColor;
import br.org.ginga.core.io.ISurface;

public class SwingSurface extends JPanel implements ISurface {
	public static final int DEFAULT_BORDER_SIZE=5;
	
	private IColor borderColor;
	private int borderSize = DEFAULT_BORDER_SIZE;
	private IColor chromaColor;

	private static final long serialVersionUID = 8908955729414304750L;
	
	public SwingSurface(){
		this.setLayout(new BorderLayout());
		chromaColor = new GingaColor(Color.GREEN);
		this.setVisible(true);
	}

	public void clear() {
		this.removeAll();
	}

	public void clearSurface() {
		this.removeAll();
	}

	public IColor getChromaColor() {
		return chromaColor;
	}

	public int getH() {
		return super.getHeight();
	}

	public ISurface getSubSurface(int x, int y, int w, int h) {
		return null;
	}

	public Object getSurface() {
		return this;
	}

	public int getW() {
		return this.getWidth();
	}

	public void refreshContent() {
		this.invalidate();
		this.repaint();
	}

	public void setBgColor(IColor bgColor) {
		this.setBackground((GingaColor)bgColor);
	}

	public void setBorder(IColor borderColor) {
		if(borderColor == null){
			super.setBorder(null);
		}
		else
			super.setBorder(new LineBorder((GingaColor)borderColor));
	}

	public void setChromaColor(IColor color) {
		this.chromaColor = color;
	}

	public boolean setParent(Object parentWindow) {
		if(parentWindow instanceof SwingWindow){
			((SwingWindow)parentWindow).add(this);
			return true;
		}
		return false;
	}

	public void setSurface(Object surface) {
		if(surface instanceof Component){
			this.removeAll();
			this.add((Component)surface, BorderLayout.CENTER);
			//this.setPreferredSize(((Component)surface).getPreferredSize());
			//this.add((Component)surface);
			/*this.setBounds(0, 0, 
					((Component)surface).getPreferredSize().width, 
					((Component)surface).getPreferredSize().height);*/
			//this.setBackground(Color.BLUE);
			this.validate();
		}
	}

	/*@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(borderColor != null){
			Color oldColor = g.getColor();
			g.setColor(borderColor);
			g.fillRect(0, 0, this.getW(), borderSize);
			g.fillRect(0, 0, borderSize, this.getH());
			g.fillRect(this.getW() - borderSize, 0, borderSize, this.getH());
			g.fillRect(0, this.getH()-borderSize, this.getW(), borderSize);
			g.setColor(oldColor);
		}
	}*/
}
