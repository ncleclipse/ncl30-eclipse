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
package br.pucrio.telemidia.ginga.ncl.model.presentation;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.org.ginga.core.io.ISurface;
import br.org.ginga.ncl.model.components.IExecutionObject;
import br.org.ginga.ncl.model.presentation.ICascadingDescriptor;
import br.org.ginga.ncl.model.presentation.IFormatterLayout;
import br.org.ginga.ncl.model.presentation.IFormatterRegion;
import br.org.ncl.layout.ILayoutRegion;

public class FormatterLayout implements IFormatterLayout {
	private ILayoutRegion deviceRegion;

	private Map<String, List<IFormatterRegion>> regionMap;

	public FormatterLayout() {
		createDeviceRegion();
		regionMap = new Hashtable<String, List<IFormatterRegion>>();
	}

	private void createDeviceRegion() {
		Dimension dim;

		deviceRegion = new FormatterDeviceRegion("defaultScreenFormatter");
		deviceRegion.setTop(0, false);
		deviceRegion.setLeft(0, false);
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		deviceRegion.setWidth(dim.getWidth(), false);
		deviceRegion.setHeight(dim.getHeight(), false);
	}

	public synchronized void showObject(IExecutionObject object) {
		IFormatterRegion region;

		if (object == null || object.getDescriptor() == null ||
				object.getDescriptor().getFormatterRegion() == null) {

			return;
		}

		region = object.getDescriptor().getFormatterRegion();
		region.showContent();
	}

	public synchronized void prepareFormatterRegion(
			IExecutionObject object, ISurface renderedSurface) {

		ICascadingDescriptor descriptor;
		IFormatterRegion region;
		ILayoutRegion layoutRegion, parent, grandParent;
		String regionId;
		List<IFormatterRegion> formRegions;

		if (object == null || object.getDescriptor() == null
				|| object.getDescriptor().getFormatterRegion() == null) {
			return;
		}

		descriptor = object.getDescriptor();
		region = descriptor.getFormatterRegion();
		layoutRegion = region.getOriginalRegion();

		/* every presented object has as region root the formatter device region */
		parent = layoutRegion;
		grandParent = layoutRegion.getParent();
		while (grandParent.getParent() != null) {
			parent = grandParent;
			grandParent = grandParent.getParent();
		}
		if (grandParent != deviceRegion) {
			parent.setParent(deviceRegion);
			deviceRegion.addRegion(parent);
		}

		regionId = layoutRegion.getId().toString();
		formRegions = regionMap.get(regionId);
		if (formRegions == null) {
			formRegions = new ArrayList<IFormatterRegion>();
			regionMap.put(regionId, formRegions);
		}
		formRegions.add(region);
		region.prepareOutputDisplay(renderedSurface);
	}

	public synchronized void hideObject(IExecutionObject object) {
		IFormatterRegion region;
		String regionId;
		List<IFormatterRegion> formRegions;

		if (object == null || object.getDescriptor() == null
				|| object.getDescriptor().getFormatterRegion() == null) {
			return;
		}

		region = object.getDescriptor().getFormatterRegion();
		region.hideContent();
		regionId = region.getLayoutRegion().getId().toString();
		formRegions = regionMap.get(regionId);
		if (formRegions != null) {
			formRegions.remove(region);
		}
	}

	public Iterator<IFormatterRegion> getFormatterRegionsFromNcmRegion(String regionId) {
		List<IFormatterRegion> formRegions;

		formRegions = regionMap.get(regionId);
		if (formRegions == null) {
			formRegions = new ArrayList<IFormatterRegion>();
		}
		return formRegions.iterator();
	}

	public void clear() {
		Iterator<List<IFormatterRegion>> regionLists;
		List<IFormatterRegion> formRegions;

		regionLists = regionMap.values().iterator();
		while (regionLists.hasNext()) {
			formRegions = regionLists.next();
			formRegions.clear();
		}
		regionMap.clear();

		deviceRegion.removeRegions();
	}
}
