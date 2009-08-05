package br.ufma.deinf.laws.ncleclipse;

import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import br.ufma.deinf.laws.ncleclipse.preferences.PreferenceConstants;

public class NCLValidatorErrorMessages extends Properties{

	private static String RESOURCE_BUNDLE = "messages";

	private static ResourceBundle fgResourceBundle = null;

	NCLValidatorErrorMessages() {
		RESOURCE_BUNDLE = NCLEditorPlugin.getDefault().getPreferenceStore().
			getString(PreferenceConstants.P_LANGUAGE);
		System.out.println(RESOURCE_BUNDLE);
		fgResourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE);
	}

	public static String getString(String key) {
		try {
			return fgResourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return "!" + key + "!";//$NON-NLS-2$ //$NON-NLS-1$
		}
	}

	public static ResourceBundle getResourceBundle() {
		ResourceBundle b = fgResourceBundle;
		return fgResourceBundle;
	}
	
	public String getProperty(String prop){
		return NCLValidatorErrorMessages.getString(prop);
	}
}
