package br.ufma.deinf.laws.ncleclipse;

import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

public class NCLValidatorErrorMessages extends Properties{

	private static final String RESOURCE_BUNDLE = "messages";//$NON-NLS-1$

	private static ResourceBundle fgResourceBundle = ResourceBundle
			.getBundle(RESOURCE_BUNDLE);

	NCLValidatorErrorMessages() {
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
