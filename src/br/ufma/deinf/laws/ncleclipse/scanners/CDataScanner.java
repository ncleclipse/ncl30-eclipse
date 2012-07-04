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
package br.ufma.deinf.laws.ncleclipse.scanners;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;

import br.ufma.deinf.laws.ncleclipse.rules.CDataRule;
import br.ufma.deinf.laws.ncleclipse.util.ColorManager;
import br.ufma.deinf.laws.ncleclipse.util.IXMLColorConstants;

/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 * 
 */
public class CDataScanner extends RuleBasedScanner {

	public IToken ESCAPED_CHAR;
	public IToken CDATA;

	public CDataScanner(ColorManager colorManager) {

		CDATA = new Token(new TextAttribute(colorManager
				.getColor(IXMLColorConstants.CDATA)));

		IRule[] rules = new IRule[2];

		// Add rule to pick up start of c section
		rules[0] = new CDataRule(CDATA, true);
		// Add a rule to pick up end of CDATA sections
		rules[1] = new CDataRule(CDATA, false);

		setRules(rules);
	}

	public IToken nextToken() {
		return super.nextToken();
	}
}
