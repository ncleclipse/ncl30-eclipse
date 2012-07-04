/*******************************************************************************
 * This file is part of the NCL authoring environment - NCL Eclipse.
 *
 * Copyright (C) 2007-2012, LAWS/UFMA.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License version 2 for
 * more details. You should have received a copy of the GNU General Public 
 * License version 2 along with this program; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
 * 02110-1301, USA.
 *
 * For further information contact:
 * - ncleclipse@laws.deinf.ufma.br
 * - http://www.laws.deinf.ufma.br/ncleclipse
 * - http://www.laws.deinf.ufma.br
 *
 ******************************************************************************/
package br.ufma.deinf.laws.ncleclipse.outline;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import br.ufma.deinf.laws.ncleclipse.xml.XMLElement;

/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 *
 */
public class OutlineLabelProvider implements ILabelProvider {
	private Image linkImage;

	public OutlineLabelProvider() {
		super();
		/*System.out.println(NCLEditorPlugin.getDefault().getImageRegistry());
		linkImage = NCLEditorPlugin.getDefault().getImageRegistry().get(NCLEditorPlugin.LINK_ICON);*/
		//TODO: Imagem nos elementos (N�o estou conseguindo fazer)
	}

	public Image getImage(Object element) {
		if (element instanceof XMLElement)
		{
			XMLElement dtdElement = (XMLElement) element;
			if(dtdElement.getName().equals("causalConnector")){
				return new Image(Display.getDefault(), this.getClass().getProtectionDomain()
						.getCodeSource().getLocation().toString().substring(5)
						+ "icons" + "/" + "conn.png");
			}
		}
		return null;
	}

	public String getText(Object element) {
		if (element instanceof XMLElement) {
			XMLElement dtdElement = (XMLElement) element;
			String textToShow = dtdElement.getName();
			//System.out.println("text to show = " + textToShow);
			String idAttributte = dtdElement.getAttributeValue("id");
			if (idAttributte == null || idAttributte.equals("")) {
				String nameAttribute = dtdElement.getAttributeValue("name");
				if (nameAttribute != null && !nameAttribute.equals(""))
					textToShow += " (" + nameAttribute + ")";
			} else
				textToShow += " (" + idAttributte + ")";

			return textToShow;
		}
		return null;
	}

	public void addListener(ILabelProviderListener listener) {
	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object element, String property) {
		System.out.println (property);
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
	}

}
