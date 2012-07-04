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

import java.util.Vector;

/**
 * @author Rodrigo Costa <rodrim.c@laws.deinf.ufma.br>
 *
 */
public class PreViewConnector {
	private Vector<Attributes> conditionRole;
	private Vector<Attributes> actionRole;
	private String compoundCondition;
	private String compoundAction;

	public PreViewConnector() {
		conditionRole = new Vector<Attributes>();
		actionRole = new Vector<Attributes>();
		compoundAction = "";
		compoundCondition = "";
	}

	public String getCompoundAction() {
		return compoundAction;
	}

	public void setCompoundAction(String compoundAction) {
		this.compoundAction = compoundAction;
	}

	public String getCompoundCondition() {
		return compoundCondition;
	}

	public void setCompoundCondition(String compoundCondition) {
		this.compoundCondition = compoundCondition;
	}

	public void setConditionRole(Attributes attributes) {
		conditionRole.add(attributes);
	}

	public void setActionRole(Attributes attributes) {
		actionRole.add(attributes);
	}

	public Vector<Attributes> getConditionRole() {
		return conditionRole;
	}

	public Vector<Attributes> getActionRole() {
		return actionRole;
	}

	public void reset() {
		actionRole.clear();
		actionRole = new Vector<Attributes>();
		compoundAction = "";
		compoundCondition = "";
		conditionRole.clear();
		conditionRole = new Vector<Attributes>();
	}

}
