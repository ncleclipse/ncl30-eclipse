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

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

import br.ufma.deinf.laws.ncleclipse.rules.NonMatchingRule;
import br.ufma.deinf.laws.ncleclipse.rules.StartTagRule;

/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 * 
 */
public class XMLPartitionScanner extends RuleBasedPartitionScanner {

	public final static String XML_DEFAULT = "__xml_default";
	public final static String XML_COMMENT = "__xml_comment";
	public final static String XML_PI = "__xml_pi";
	public final static String XML_DOCTYPE = "__xml_doctype";
	public final static String XML_CDATA = "__xml_cdata";
	public final static String XML_START_TAG = "__xml_start_tag";
	public final static String XML_END_TAG = "__xml_end_tag";
	public final static String XML_TEXT = "__xml_text";
	public final static String XML_TAG = "__xml_tag";

	public XMLPartitionScanner() {

		IToken xmlComment = new Token(XML_COMMENT);
		IToken xmlPI = new Token(XML_PI);
		IToken startTag = new Token(XML_START_TAG);
		IToken endTag = new Token(XML_END_TAG);
		IToken docType = new Token(XML_DOCTYPE);
		// IToken text = new Token(XML_TEXT);
		// IToken tag = new Token(XML_TAG);

		IPredicateRule[] rules = new IPredicateRule[6];

		rules[0] = new NonMatchingRule();
		rules[1] = new MultiLineRule("<!--", "-->", xmlComment, '\\', true);
		rules[2] = new MultiLineRule("<?", "?>", xmlPI);
		rules[3] = new MultiLineRule("</", ">", endTag);
		rules[4] = new StartTagRule(startTag);
		rules[5] = new MultiLineRule("<!DOCTYPE", ">", docType);

		// rules[6] = new XMLTextPredicateRule(text);
		// rules[7] = new TagRule(tag);

		setPredicateRules(rules);
	}
}
