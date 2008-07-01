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
package br.pucrio.telemidia.ginga.ncl.model.presentation;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.org.ncl.IEntity;
import br.org.ncl.layout.ILayoutRegion;

public class FormatterDeviceRegion implements ILayoutRegion {
	private static final long serialVersionUID = -7118629828985691534L;

	private String id;

	private int top, left, width, height;

	private List<ILayoutRegion> regions;

	public FormatterDeviceRegion(String id) {
		this.id = id;
		regions = new ArrayList<ILayoutRegion>();
	}

	public void addRegion(ILayoutRegion region) {
		regions.add(region);
		region.setParent(this);
	}

	public ILayoutRegion cloneRegion() {
		ILayoutRegion cloneRegion;
		Iterator<ILayoutRegion> childRegions;
		ILayoutRegion childRegion;

		cloneRegion = new FormatterDeviceRegion(id);

		cloneRegion.setTitle(getTitle());
		cloneRegion.setLeft(left, false);
		cloneRegion.setTop(top, false);
		cloneRegion.setWidth(width, false);
		cloneRegion.setHeight(height, false);
		cloneRegion.setDecorated(false);
		cloneRegion.setMovable(false);
		cloneRegion.setResizable(false);

		childRegions = getRegions();
		while (childRegions.hasNext()) {
			childRegion = (ILayoutRegion)childRegions.next();
			cloneRegion.addRegion(childRegion);
		}

		return cloneRegion;
	}

	public int compareWidthSize(String w) {
		int newW;

		newW = Integer.parseInt(w);
		if (newW == width) {
			return 0;
		}
		else if (newW > width) {
			return 1;
		}
		else {
			return -1;
		}
	}

	public int compareHeightSize(String h) {
		int newH;

		newH = Integer.parseInt(h);
		if (newH == height) {
			return 0;
		}
		else if (newH > height) {
			return 1;
		}
		else {
			return -1;
		}
	}

	public Color getBackgroundColor() {
		return null;
	}

	public Double getBottom() {
		return null;
	}

	public Double getHeight() {
		return new Double(height);
	}

	public Double getLeft() {
		return new Double(left);
	}

	public Double getRight() {
		return null;
	}

	public ILayoutRegion getRegion(String id) {
		int i, size;
		ILayoutRegion region;

		size = regions.size();
		for (i = 0; i < size; i++) {
			region = (ILayoutRegion)regions.get(i);
			if (region.getId() != null && region.getId().equals(id)) {
				return region;
			}
		}
		return null;
	}

	public ILayoutRegion getRegionRecursively(String id) {
		int i, size;
		ILayoutRegion region, auxRegion;

		size = regions.size();
		for (i = 0; i < size; i++) {
			region = (ILayoutRegion)regions.get(i);
			if (region.getId() != null && region.getId().equals(id)) {
				return region;
			}
			auxRegion = region.getRegionRecursively(id);
			if (auxRegion != null) {
				return auxRegion;
			}
		}
		return null;
	}

	public Iterator<ILayoutRegion> getRegions() {
		return regions.iterator();
	}

	public String getTitle() {
		return null;
	}

	public Double getTop() {
		return new Double(top);
	}

	public Double getWidth() {
		return new Double(width);
	}

	public Integer getZIndex() {
		return null;
	}

	public int getZIndexValue() {
		Integer zIndex;

		zIndex = getZIndex();
		if (zIndex != null) {
			return zIndex.intValue();
		}
		else {
			return 0;
		}
	}

	public boolean isBottomPercentual() {
		return false;
	}

	public boolean isHeightPercentual() {
		return false;
	}

	public boolean isLeftPercentual() {
		return false;
	}

	public boolean isRightPercentual() {
		return false;
	}

	public boolean isTopPercentual() {
		return false;
	}

	public boolean isWidthPercentual() {
		return false;
	}

	public String toString() {
		String str;
		int i, size;
		ILayoutRegion region;

		str = "id: " + getId() + '\n';
		size = regions.size();
		for (i = 0; i < size; i++) {
			region = (ILayoutRegion)regions.get(i);
			str = str + region.toString();
		}
		return str + '\n';
	}

	public boolean removeRegion(ILayoutRegion region) {
		return regions.remove(region);
	}

	public void removeRegions() {
		ILayoutRegion region;

		while (regions.size() > 0) {
			region = (ILayoutRegion)regions.get(regions.size() - 1);
			removeRegion(region);
		}
	}

	public void setBackgroundColor(Color newBackgroundColor) {
	}

	public boolean setBottom(double newBottom, boolean isPercentual) {
		return false;
	}

	public boolean setHeight(double newHeight, boolean isPercentual) {
		this.height = (int)newHeight;
		return true;
	}

	public boolean setLeft(double newLeft, boolean isPercentual) {
		this.left = (int)newLeft;
		return true;
	}

	public boolean setRight(double newRight, boolean isPercentual) {
		return false;
	}

	public void setTitle(String newTitle) {
	}

	public boolean setTop(double newTop, boolean isPercentual) {
		this.top = (int)newTop;
		return true;
	}

	public boolean setWidth(double newWidth, boolean isPercentual) {
		this.width = (int)newWidth;
		return true;
	}

	public void setZIndex(int newZIndex) {
	}

	public Iterator<ILayoutRegion> getRegionsSortedByZIndex() {
		List<ILayoutRegion> sortedRegions;
		ILayoutRegion ncmRegion, auxRegion;
		int i1, size1, i2, size2;

		sortedRegions = new ArrayList<ILayoutRegion>();

		size1 = regions.size();
		for (i1 = 0; i1 < size1; i1++) {
			ncmRegion = (ILayoutRegion)regions.get(i1);

			size2 = sortedRegions.size();
			for (i2 = 0; i2 < size2; i2++) {
				auxRegion = (ILayoutRegion)sortedRegions.get(i2);
				if (ncmRegion.getZIndexValue() <= auxRegion.getZIndexValue()) {
					break;
				}
			}
			sortedRegions.add(i2, ncmRegion);
		}

		return sortedRegions.iterator();
	}

	public Iterator<ILayoutRegion> getRegionsOverRegion(ILayoutRegion region) {
		Iterator<ILayoutRegion> allRegions;
		List<ILayoutRegion> frontRegions;
		ILayoutRegion childRegion;

		frontRegions = new ArrayList<ILayoutRegion>();
		allRegions = getRegionsSortedByZIndex();
		while (allRegions.hasNext()) {
			childRegion = (ILayoutRegion)allRegions.next();
			if (childRegion.getZIndexValue() > region.getZIndexValue()) {
				frontRegions.add(0, childRegion);
			}
		}
		return frontRegions.iterator();
	}

	public ILayoutRegion getParent() {
		return null;
	}

	public void setParent(ILayoutRegion parent) {
	}

	public int getTopInPixels() {
		return top;
	}

	public int getBottomInPixels() {
		return top + height;
	}

	public int getRightInPixels() {
		return left + width;
	}

	public int getLeftInPixels() {
		return left;
	}

	public int getHeightInPixels() {
		return height;
	}

	public int getWidthInPixels() {
		return width;
	}

	public Boolean isMovable() {
		return null;
	}

	public Boolean isResizable() {
		return null;
	}

	public Boolean isDecorated() {
		return null;
	}

	public void setMovable(boolean movable) {
	}

	public void setResizable(boolean resizable) {
	}

	public void setDecorated(boolean decorated) {
	}

	public void resetTop() {
	}

	public void resetBottom() {
	}

	public void resetLeft() {
	}

	public void resetHeight() {
	}

	public void resetWidth() {
	}

	public void resetZIndex() {
	}

	public void resetDecorated() {
	}

	public void resetMovable() {
	}

	public void resetResizable() {
	}

	public int getAbsoluteLeft() {
		return left;
	}

	public int getAbsoluteTop() {
		return top;
	}

	public Rectangle getRectangle() {
		Rectangle rect;

		rect = new Rectangle();
		rect.setBounds(left, top, width, height);
		return rect;
	}

	public void dispose() {
		removeRegions();
		regions = null;
	}

	public String getId() {
		return id;
	}

	public void setId(Comparable id) {
		this.id = id.toString();
	}

	public IEntity getDataEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}