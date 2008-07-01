/******************************************************************************
Este arquivo é parte da implementação do ambiente declarativo do middleware 
Ginga (Ginga-NCL).

Direitos Autorais Reservados (c) 1989-2007 PUC-Rio/Laboratório TeleMídia

Este programa é software livre; você pode redistribuí-lo e/ou modificá-lo sob 
os termos da Licença Pública Geral GNU versão 2 conforme publicada pela Free 
Software Foundation.

Este programa é distribuído na expectativa de que seja útil, porém, SEM 
NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE OU 
ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública Geral do 
GNU versão 2 para mais detalhes. 

Você deve ter recebido uma cópia da Licença Pública Geral do GNU versão 2 junto 
com este programa; se não, escreva para a Free Software Foundation, Inc., no 
endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA. 

Para maiores informações:
ncl @ telemidia.puc-rio.br
http://www.ncl.org.br
http://www.ginga.org.br
http://www.telemidia.puc-rio.br
******************************************************************************
This file is part of the declarative environment of middleware Ginga (Ginga-NCL)

Copyright: 1989-2007 PUC-RIO/LABORATORIO TELEMIDIA, All Rights Reserved.

This program is free software; you can redistribute it and/or modify it under 
the terms of the GNU General Public License version 2 as published by
the Free Software Foundation.

This program is distributed in the hope that it will be useful, but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License version 2 for more 
details.

You should have received a copy of the GNU General Public License version 2
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA

For further information contact:
ncl @ telemidia.puc-rio.br
http://www.ncl.org.br
http://www.ginga.org.br
http://www.telemidia.puc-rio.br
*******************************************************************************/
package br.pucrio.telemidia.ginga.ncl.adaptation.context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Properties;
import java.util.Vector;

import br.org.ginga.ncl.adaptation.IContextBase;

public class PresentationContext extends Observable implements IContextBase {
	/**
	 * 
	 */
	private static final String contextFile = "./gingaNclConfig/context/context.ini";

	/**
	 * 
	 */
	private static IContextBase _instance = null;

	/**
	 * 
	 */
	private Properties contextTable;

	/**
	 * 
	 */
	private PresentationContext() {
		super();
		open();
	}

	/**
	 * @return
	 */
	public static IContextBase getInstance() {
		if (_instance == null) {
			_instance = new PresentationContext();
		}
		return _instance;
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.adaptation.IContextBase#setAttributeValue(java.lang.String, java.lang.Comparable)
	 */
	public void setPropertyValue(String attributeId, String value) {
		Comparable oldValue;

		oldValue = (Comparable)contextTable.get(attributeId);
		contextTable.put(attributeId, value);
		if (value != null && !value.equals(oldValue)) {
			// notify context observers about the change
			setChanged();
			notifyObservers(attributeId);
			clearChanged();
		}
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.adaptation.IContextBase#getAttributeNames()
	 */
	public Iterator getPropertyNames() {
		Enumeration names;
		List attNames;

		names = contextTable.propertyNames();
		attNames = new Vector();
		while (names.hasMoreElements()) {
			attNames.add(names.nextElement());
		}
		return attNames.iterator();
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.adaptation.IContextBase#getAttributeValue(java.lang.String)
	 */
	public String getPropertyValue(String attributeId) {
		return contextTable.getProperty(attributeId);
	}

	/**
	 * 
	 */
	private void initializeDefaultValues() {
		if (!contextTable.containsKey(SYSTEM_LANGUAGE)) {
			contextTable.setProperty(SYSTEM_LANGUAGE, "pt");
		}

		if (!contextTable.containsKey(SYSTEM_CAPTION)) {
			contextTable.setProperty(SYSTEM_CAPTION, "pt");
		}

		if (!contextTable.containsKey(SYSTEM_SUBTITLE)) {
			contextTable.setProperty(SYSTEM_SUBTITLE, "pt");
		}

		if (!contextTable.containsKey(SYSTEM_RETURN_BIT_RATE)) {
			contextTable.setProperty(SYSTEM_RETURN_BIT_RATE, "0");
		}

		if (!contextTable.containsKey(SYSTEM_SCREEN_SIZE)) {
			contextTable.setProperty(SYSTEM_SCREEN_SIZE, "(800,600)");
		}

		if (!contextTable.containsKey(SYSTEM_SCREEN_GRAPHIC_SIZE)) {
			contextTable.setProperty(SYSTEM_SCREEN_GRAPHIC_SIZE, "(800,600)");
		}

		if (!contextTable.containsKey(SYSTEM_AUDIO_TYPE)) {
			contextTable.setProperty(SYSTEM_AUDIO_TYPE, "stereo");
		}

		if (!contextTable.containsKey(SYSTEM_CPU)) {
			contextTable.setProperty(SYSTEM_CPU, "266");
		}

		if (!contextTable.containsKey(SYSTEM_MEMORY)) {
			contextTable.setProperty(SYSTEM_MEMORY, "32");
		}

		if (!contextTable.containsKey(SYSTEM_OPERATING_SYSTEM)) {
			contextTable.setProperty(SYSTEM_OPERATING_SYSTEM, System
					.getProperty("os.name"));
		}

		if (!contextTable.containsKey(USER_AGE)) {
			contextTable.setProperty(USER_AGE, "5");
		}

		if (!contextTable.containsKey(USER_LOCATION)) {
			contextTable.setProperty(USER_LOCATION, "00000-000");
		}

		if (!contextTable.containsKey(USER_GENRE)) {
			contextTable.setProperty(USER_GENRE, "f");
		}

		/*
		 * if (!contextTable.containsKey(DEFAULT_FOCUS_BORDER_COLOR)) {
		 * contextTable.setProperty(DEFAULT_FOCUS_BORDER_COLOR, "blue"); }
		 * 
		 * if (!contextTable.containsKey(DEFAULT_SEL_BORDER_COLOR)) {
		 * contextTable.setProperty(DEFAULT_SEL_BORDER_COLOR, "green"); }
		 * 
		 * if (!contextTable.containsKey(DEFAULT_FOCUS_BORDER_WIDTH)) {
		 * contextTable.setProperty(DEFAULT_FOCUS_BORDER_WIDTH, "3"); }
		 * 
		 * if (!contextTable.containsKey(DEFAULT_FOCUS_BORDER_TRANSPARENCY)) {
		 * contextTable.setProperty(DEFAULT_FOCUS_BORDER_TRANSPARENCY, "1.0"); }
		 */
	}

	/**
	 * Insert the method's description here.
	 */
	/**
	 * 
	 */
	public void open() {
		FileInputStream fis;

		try {
			contextTable = new Properties();
			fis = new FileInputStream(contextFile);
			contextTable.load(fis);
			fis.close();
			initializeDefaultValues();
		}
		catch (Exception exc) {
			System.out.println("Error: " + exc);
		}
	}

	/**
	 * 
	 */
	public void save() {
		FileOutputStream fos;

		try {
			fos = new FileOutputStream(contextFile);
			contextTable.store(fos, "");
			fos.close();
		}
		catch (Exception exc) {
			System.out.println("Error: " + exc);
		}
	}

}
