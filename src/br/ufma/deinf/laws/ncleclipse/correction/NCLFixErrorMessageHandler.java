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
package br.ufma.deinf.laws.ncleclipse.correction;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

import br.ufma.deinf.laws.ncleclipse.NCLEditorMessages;
import br.ufma.deinf.laws.util.Messages;

/**
 * 
 * @author <a href="mailto:robertogerson@telemidia.puc-rio.br">Roberto Gerson
 *         Azevedo</a>
 * 
 */
public class NCLFixErrorMessageHandler extends Messages {
	private static String RESOURCE_BUNDLE = "br.ufma.deinf.laws.ncleclipse.correction.NCLFixErrorMessages";//$NON-NLS-1$
	protected static NCLFixErrorMessageHandler instance = null;

	/**
	 * The constructor is private, because this class is a Singleton. So, to
	 * access the Singleton object use the getInstance() method.
	 */
	private NCLFixErrorMessageHandler() {
		fgResourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE);
	}

	/**
	 * 
	 * @return the Singleton object instance of this class.
	 */
	public static NCLFixErrorMessageHandler getInstance() {
		if (instance == null)
			instance = new NCLFixErrorMessageHandler();
		return instance;
	}

	public List<String> getAllFixMessagesToErrorMessage(int errorMsgID, Object [] params) {
		getInstance();
		ArrayList<String> fixMessages = new ArrayList<String>();
		for (int i = 1;; i++) {
			try {
				String fixMessageID = (new Integer(errorMsgID).toString())
						+ "_" + (new Integer(i)).toString();
				
				String message = getString(fixMessageID, params);
				//if there aren't more messages
				if (message == null || message.equals("!"+fixMessageID+"!"))
					break;
				
				fixMessages.add(message);
				
			} catch (Exception e) {
				break;
			}
		}
		return fixMessages;
	}
}
