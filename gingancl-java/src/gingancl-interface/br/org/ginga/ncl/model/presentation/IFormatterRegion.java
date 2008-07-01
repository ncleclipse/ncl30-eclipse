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
package br.org.ginga.ncl.model.presentation;

import java.awt.Color;
import java.awt.Component;

import br.org.ginga.core.io.ISurface;
import br.org.ncl.animation.IAnimation;
import br.org.ncl.layout.ILayoutRegion;

public interface IFormatterRegion {
	public static final short UNSELECTED = 0;

	public static final short FOCUSED = 1;

	public static final short SELECTED = 2;

	/**
	 * Return the region clone that is being manipulated. The layout region is
	 * cloned in order to be handled (e.g. resized) without affecting other
	 * objects that points to the same region.
	 * 
	 * @return the NCL/NCM region clone being manipulated.
	 */
	ILayoutRegion getLayoutRegion();

	/**
	 * Return the NCL/NCM region that originated this formatter region.
	 * 
	 * @return the NCL/NCM region that originated this formatter region.
	 */
	ILayoutRegion getOriginalRegion();

	void showContent();

	void hideContent();

	void updateRegionBounds(IAnimation anim);

	void setRegionVisibility(boolean isVisible);

	void toFront();

	void disposeOutputDisplay();

	// void repaintOverlapRegions(IFormatterRegion childRegion);
	boolean isVisible();

	void setFocus(boolean focusOn);

	boolean setSelection(boolean selOn);

	short getFocusState();

	void prepareOutputDisplay(ISurface component);

	void setFocusInfo(Color focusBorderColor, int borderWidth, String focusSrc,
			Color selBorderColor, String selSrc);

	String getFocusIndex();

	Color getBackgroundColor();

	void changeCurrentComponent(ISurface newComponent);
}
