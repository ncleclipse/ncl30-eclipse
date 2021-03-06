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
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * Extra rule which will return specified token if sequence of characters matches
 * 
 */
public class XMLTextPredicateRule implements IPredicateRule {

	private IToken token;
	private int charsRead;
	private boolean whiteSpaceOnly;
	boolean inCdata;

	public XMLTextPredicateRule(IToken text) {
		this.token = text;
	}

	public IToken getSuccessToken() {
		return token;
	}

	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		return evaluate(scanner);
	}

	public IToken evaluate(ICharacterScanner scanner) {

		reinit();

		int c = 0;

		//carry on reading until we find a bad char
		//int chars = 0;
		while (isOK(c = read(scanner), scanner)) {
			//add character to buffer
			if (c == ICharacterScanner.EOF) {
				return Token.UNDEFINED;
			}

			whiteSpaceOnly = whiteSpaceOnly
					&& (Character.isWhitespace((char) c));
		}

		unread(scanner);

		//if we have only read whitespace characters, go back to where evaluation started and return undefined token
		if (whiteSpaceOnly) {
			rewind(scanner, charsRead);
			return Token.UNDEFINED;
		}

		return token;

	}

	private boolean isOK(int cc, ICharacterScanner scanner) {

		char c = (char) cc;

		if (!inCdata) {
			if (c == '<') {

				int cdataCharsRead = 0;

				for (int i = 0; i < "![CDATA[".length(); i++) {
					//whiteSpaceOnly = false;

					c = (char) read(scanner);
					cdataCharsRead++;

					if (c != "![CDATA[".charAt(i)) {

						//we don't have a match - wind back only the cdata characters
						rewind(scanner, cdataCharsRead);
						inCdata = false;
						return false;
					}
				}

				inCdata = true;
				return true;

				//return false;
			}
		} else {

			if (c == ']') {

				for (int i = 0; i < "]>".length(); i++) {

					c = (char) read(scanner);

					if (c != "]>".charAt(i)) {
						//we're still in the CData section, so just continue processing
						return true;
					}
				}

				//we found all the matching characters at the end of the CData section, so break out of this
				inCdata = false;

				//we're still in XML text
				return true;

			}
		}

		return true;

	}

	private void rewind(ICharacterScanner scanner, int theCharsRead) {
		while (theCharsRead > 0) {
			theCharsRead--;
			unread(scanner);
		}
	}

	private void unread(ICharacterScanner scanner) {
		scanner.unread();
		charsRead--;
	}

	private int read(ICharacterScanner scanner) {
		int c = scanner.read();
		charsRead++;
		return c;
	}

	private void reinit() {
		charsRead = 0;
		whiteSpaceOnly = true;
	}

}
