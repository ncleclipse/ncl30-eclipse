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
package br.ufma.deinf.laws.ncleclipse.hover;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Rodrigo Costa <rodrim.c@laws.deinf.ufma.br>
 * 
 */
public class Attributes {
	private HashMap<String, String> attributes;

	public Attributes() {
		attributes = new HashMap<String, String>();
		attributes.put("max", "");
		attributes.put("min", "");
		attributes.put("role", "");
		attributes.put("qualifier", "");
	}

	public String getAttribute(String key) {
		return attributes.get(key);
	}

	public void setAttribute(String key, String value) {
		attributes.put(key, value);
	}

	public String toString() {
		String toShow = "";
		Iterator it = attributes.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			toShow += key + " " + attributes.get(key) + "\n";
		}
		return toShow;
	}
}
