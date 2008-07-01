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

package br.org.ginga.core.io;

public interface IWindow {
	static final int TYPE_NULL = -1;
	static final int TYPE_BARWIPE = 0;
	static final int TYPE_IRISWIPE = 1;
	static final int TYPE_CLOCKWIPE = 2;
	static final int TYPE_SNAKEWIPE = 3;
	static final int TYPE_FADE = 4;

	static final int SUBTYPE_BARWIPE_LEFTTORIGHT = 0;
	static final int SUBTYPE_BARWIPE_TOPTOBOTTOM = 1;

	static final int SUBTYPE_IRISWIPE_RECTANGLE = 20;
	static final int SUBTYPE_IRISWIPE_DIAMOND = 21;

	static final int SUBTYPE_CLOCKWIPE_CLOCKWISETWELVE = 40;
	static final int SUBTYPE_CLOCKWIPE_CLOCKWISETHREE = 41;
	static final int SUBTYPE_CLOCKWIPE_CLOCKWISESIX = 42;
	static final int SUBTYPE_CLOCKWIPE_CLOCKWISENINE = 43;

	static final int SUBTYPE_SNAKEWIPE_TOPLEFTHORIZONTAL = 60;
	static final int SUBTYPE_SNAKEWIPE_TOPLEFTVERTICAL = 61;
	static final int SUBTYPE_SNAKEWIPE_TOPLEFTDIAGONAL = 62;
	static final int SUBTYPE_SNAKEWIPE_TOPRIGHTDIAGONAL = 63;
	static final int SUBTYPE_SNAKEWIPE_BOTTOMRIGHTDIAGONAL = 64;
	static final int SUBTYPE_SNAKEWIPE_BOTTOMLEFTDIAGONAL = 65;

	static final int SUBTYPE_FADE_CROSSFADE = 80;
	static final int SUBTYPE_FADE_FADETOCOLOR = 81;
	static final int SUBTYPE_FADE_FADEFROMCOLOR = 82;

	static final short DIRECTION_FORWARD = 0;
	static final short DIRECTION_REVERSE = 1;
	
	boolean isDeleting();
	void draw();
	
	void setBounds(
			int x,
			int y,
			int width, int height);
	
	void setBounds(
			int x,
			int y,
			int width, int height, double dur/* = 0*/, double by/* = 0*/);

	void setBackgroundColor(int r, int g, int b);
	IColor getBgColor();
	void setColorKey(int r, int g, int b);
	void setOpacity(int alpha);

	void moveTo(int x, int y);
	void resize(int width, int height);
	void raise();
	void lower();
	void raiseToTop();
	void lowerToBottom();
	void setTransparencyValue(int alpha);
	void show(
		    int transitionType,
		    int transitionSubType,
		    double dur,
		    double startProgress,
		    double endProgress,
		    short direction,
		    int fadeColor);

	void show();
	void hide(
		    int transitionType,
		    int transitionSubType,
		    double dur,
		    double startProgress,
		    double endProgress,
		    short direction,
		    int fadeColor);

	void hide();
	int getX();
	int getY();
	int getW();
	int getH();
	Object getWidget();
	Object getWidgetSurface();
	void setColor(int r, int g, int b);
	void setColor(int r, int g, int b, int alpha/*=255*/);
	void setBorder(int r, int g, int b);
	void setBorder(int r, int g, int b, int alpha);
	void setBorder(int r, int g, int b, int alpha/*=255*/, int bWidth/*=1*/);
	void setBorder(int argb);
	void setBorder(int argb, int bWidth /*=1*/);
	//static void dynamicRenderCallBack(Object surfaceData);
	boolean isVisible();
	void validate();
	boolean isTransitioning();
	void busy();
	void addChildSurface(ISurface s);
	boolean removeChildSurface(ISurface s);
	void setFit(boolean fitTo);
	boolean getFit();
	void clear();
	void renderFrom(ISurface s);
}
