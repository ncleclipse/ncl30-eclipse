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
package br.ufma.deinf.laws.ncleclipse.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.internal.IPreferenceConstants;

import br.ufma.deinf.laws.ncleclipse.NCLEditorPlugin;

/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 * @deprecated The Language is loaded from system now. The users must load eclipse with -nl parameter.
 */
public class Language extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public Language() {
		super(GRID);
		setPreferenceStore(NCLEditorPlugin.getDefault().getPreferenceStore());
		setDescription("Preferences related to NCL Eclipse Language");
	}

	public void createFieldEditors() {
		addField(new RadioGroupFieldEditor(PreferenceConstants.P_LANGUAGE,
				"Error Language", 1, new String[][] {
						{ "Portuguese", "messagesPt" },
						{ "English", "messagesEn" },
						{ "Spanish", "messagesEs" } }, getFieldEditorParent(),
				true));

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

}
