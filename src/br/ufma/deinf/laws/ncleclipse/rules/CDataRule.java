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
package br.ufma.deinf.laws.ncleclipse.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 *
 */
public class CDataRule implements IRule {

	IToken fToken;
	StringBuffer buffer = new StringBuffer();
	int charsRead = 0;

	private String matchString;
	private static final String START_MATCH_STRING = "<![CDATA[";
	private static final String END_MATCH_STRING = "]]>";

	public CDataRule(IToken token, boolean start) {
		super();
		this.fToken = token;
		this.matchString = start ? START_MATCH_STRING : END_MATCH_STRING;
	}

	/*
	 * @see IRule#evaluate(ICharacterScanner)
	 */
	public IToken evaluate(ICharacterScanner scanner) {

		buffer.setLength(0);

		charsRead = 0;
		int c = read(scanner);

		if (c == matchString.charAt(0)) {
			do {
				c = read(scanner);
			} while (isOK((char) c));

			if (charsRead == matchString.length()) {
				return fToken;
			} else {
				rewind(scanner);
				return Token.UNDEFINED;
			}

		}

		scanner.unread();
		return Token.UNDEFINED;
	}

	private void rewind(ICharacterScanner scanner) {
		int rewindLength = charsRead;
		while (rewindLength > 0) {
			scanner.unread();
			rewindLength--;
		}
	}

	private int read(ICharacterScanner scanner) {
		int c = scanner.read();
		buffer.append((char) c);
		charsRead++;
		return c;
	}

	private boolean isOK(char c) {
		if (charsRead >= matchString.length())
			return false;
		if (matchString.charAt(charsRead - 1) == c)
			return true;
		else
			return false;
	}
}
