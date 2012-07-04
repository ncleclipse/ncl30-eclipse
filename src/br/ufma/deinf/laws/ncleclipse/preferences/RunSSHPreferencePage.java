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

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

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

public class RunSSHPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage, IPropertyChangeListener {

	private BooleanFieldEditor enableRemoteSettings;

	public RunSSHPreferencePage() {
		super(GRID);
		setPreferenceStore(NCLEditorPlugin.getDefault().getPreferenceStore());
		setDescription(NCLEditorMessages.getInstance().getString(
				"Preferences.RunSSHIntro"));
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		addField(new StringFieldEditor(PreferenceConstants.P_SSH_RUN_SCRIPT,
				NCLEditorMessages.getInstance().getString(
						"Preferences.RemoteLauncher"), getFieldEditorParent()));

		addField(new StringFieldEditor(PreferenceConstants.P_SSH_RUN_WORKSPACE,
				NCLEditorMessages.getInstance().getString(
						"Preferences.RemoteWorkspace"), getFieldEditorParent()));

		addField(new StringFieldEditor(PreferenceConstants.P_SSH_RUN_IP,
				NCLEditorMessages.getInstance().getString(
						"Preferences.Hostname"), getFieldEditorParent()));

		addField(new StringFieldEditor(PreferenceConstants.P_SSH_RUN_USER,
				NCLEditorMessages.getInstance().getString(
						"Preferences.Username"), getFieldEditorParent()));

		StringFieldEditor passw = new StringFieldEditor(
				PreferenceConstants.P_SSH_RUN_PASSW, NCLEditorMessages
						.getInstance().getString("Preferences.Password"),
				getFieldEditorParent());
		passw.getTextControl(getFieldEditorParent()).setEchoChar('*');

		addField(passw);

		// Enable remote settings variables
		enableRemoteSettings = new BooleanFieldEditor(
				PreferenceConstants.P_ENABLE_REMOTE_SETTINGS, NCLEditorMessages
						.getInstance().getString(
								"Preferences.EnableRemoteSettings"),
				getFieldEditorParent());

		addField(enableRemoteSettings);

		addField(new StringFieldEditor(
				PreferenceConstants.P_REMOTE_SETTINGS_PATH, NCLEditorMessages
						.getInstance().getString(
								"Preferences.RemoteSettingsFile"),
				getFieldEditorParent()));

		String columnName[] = new String[2];
		columnName[0] = NCLEditorMessages.getInstance().getString("variable");
		columnName[1] = NCLEditorMessages.getInstance().getString("value");

		int columSize[] = new int[2];
		columSize[0] = 200;
		columSize[1] = 100;

		addField(new TableFieldEditor(
				PreferenceConstants.P_REMOTE_SETTINGS_VARIABLES,
				NCLEditorMessages.getInstance().getString(
						"Preferences.SettingsTableName"), columnName,
				columSize, getFieldEditorParent()) {
			@Override
			protected String[][] parseString(String string) {
				return PreferenceInitializer.parseString(string);
			}

			@Override
			protected String[] getNewInputObject() {
				// TODO Auto-generated method stub
				return new String[] {
						NCLEditorMessages.getInstance().getString("variable"),
						NCLEditorMessages.getInstance().getString("value") };
			}

			@Override
			protected String createList(String[][] items) {
				return PreferenceInitializer.createList(items);
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

}
