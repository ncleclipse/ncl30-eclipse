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
		store.setDefault(PreferenceConstants.P_NCL_LAYOUT_EDITOR, true);
	}

}
