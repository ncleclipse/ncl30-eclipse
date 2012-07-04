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
package br.ufma.deinf.laws.ncleclipse;

import java.util.ResourceBundle;

import br.ufma.deinf.laws.util.Messages;

/**
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 * 
 */
public class NCLEditorMessages extends Messages {
	private static String RESOURCE_BUNDLE = "br.ufma.deinf.laws.ncleclipse.NCLEditorMessages";//$NON-NLS-1$
	protected static NCLEditorMessages instance = null;

	/**
	 * The constructor is private, because this class is a Singleton. So, to
	 * access the Singleton object use the getInstance() method.
	 */
	private NCLEditorMessages() {
		fgResourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE);
	}

	/**
	 * 
	 * @return the Singleton object instance of this class.
	 */
	public static NCLEditorMessages getInstance() {
		if (instance == null)
			instance = new NCLEditorMessages();
		return instance;
	}
}
