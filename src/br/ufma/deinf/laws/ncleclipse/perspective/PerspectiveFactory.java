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
package br.ufma.deinf.laws.ncleclipse.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * @author Rodrigo Costa <rodrim.c@laws.deinf.ufma.br>
 * 
 */
public class PerspectiveFactory implements IPerspectiveFactory {

	private static final String VIEW_ID = 
		"br.ufma.deinf.laws.ncleclipse.NCLPerspective";

	private static final String BOTTOM = "bottom";

	public void createInitialLayout(IPageLayout myLayout) {

		myLayout.addView(IPageLayout.ID_OUTLINE, 
				IPageLayout.LEFT, 0.30f,
				myLayout.getEditorArea());

		IFolderLayout bot = 
			myLayout.createFolder(BOTTOM, IPageLayout.BOTTOM,
				0.76f, myLayout.getEditorArea());
		bot.addView(VIEW_ID);

	}

}
