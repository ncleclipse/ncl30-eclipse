/*******************************************************************************
 * This file is part of the authoring environment in Nested Context Language -
 * NCL Eclipse.
 * 
 * Copyright: 2007-2009 UFMA/LAWS (Laboratory of Advanced Web Systems), All Rights Reserved.
 * 
 * This program is free software; you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU General Public License version 2 for more 
 * details.
 * 
 * You should have received a copy of the GNU General Public License version 2
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 * For further information contact:
 * 		ncleclipse@laws.deinf.ufma.br
 * 		http://www.laws.deinf.ufma.br/ncleclipse
 * 		http://www.laws.deinf.ufma.br
 ********************************************************************************/
package br.ufma.deinf.laws.ncleclipse.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import br.ufma.deinf.laws.ncleclipse.NCLEditorMessages;
import br.ufma.deinf.laws.ncleclipse.NCLEditorPlugin;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class ViewsPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public ViewsPreferencePage() {
		super(GRID);
		setPreferenceStore(NCLEditorPlugin.getDefault().getPreferenceStore());
		setDescription(NCLEditorMessages.getInstance().getString(
				"Preferences.Description"));
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		/*addField(new BooleanFieldEditor(
				PreferenceConstants.P_NCL_LAYOUT_EDITOR_ACTIVATE,
				NCLEditorMessages.getInstance().getString(
						"Preferences.OpenLayoutEditor"), getFieldEditorParent()));
		*/
		
		addField(new BooleanFieldEditor(PreferenceConstants.P_PREVIEW,
				NCLEditorMessages.getInstance().getString(
						"Preferences.OpenPrevieOnMouseOver"),
				getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.P_VALIDATION,
				NCLEditorMessages.getInstance().getString(
						"Preferences.OnTimeValidation"), getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.P_POPUP_SUGESTION,
				NCLEditorMessages.getInstance().getString(
						"Preferences.PopupSRC"), getFieldEditorParent()));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

}