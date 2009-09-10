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
