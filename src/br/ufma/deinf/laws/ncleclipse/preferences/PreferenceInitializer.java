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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import br.ufma.deinf.laws.ncleclipse.NCLEditorPlugin;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = NCLEditorPlugin.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_NCL_LAYOUT_EDITOR_ACTIVATE, false);
		store.setDefault(PreferenceConstants.P_SSH_RUN_IP, "192.168.0.1");
		store.setDefault(PreferenceConstants.P_SSH_RUN_USER,
				"root");
		store.setDefault(PreferenceConstants.P_SSH_RUN_PASSW,
			"telemidia");
		
		store.setDefault(PreferenceConstants.P_LANGUAGE, "messagesPt.properties");
		store.setDefault(PreferenceConstants.P_PREVIEW, true);
		store.setDefault(PreferenceConstants.P_VALIDATION,true);
	}

}
