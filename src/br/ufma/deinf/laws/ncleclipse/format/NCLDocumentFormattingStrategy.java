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
package br.ufma.deinf.laws.ncleclipse.format;

import java.util.LinkedList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.formatter.ContextBasedFormattingStrategy;
import org.eclipse.jface.text.formatter.FormattingContextProperties;
import org.eclipse.jface.text.formatter.IFormattingContext;

import br.ufma.deinf.laws.ncleclipse.NCLEditorMessages;

public class NCLDocumentFormattingStrategy extends
		ContextBasedFormattingStrategy {

	/** Documents to be formatted by this strategy */
	private final LinkedList documents = new LinkedList();

	public NCLDocumentFormattingStrategy() {
		super();
	}

	/**
	 * @see org.eclipse.jface.text.formatter.IFormattingStrategyExtension#format()
	 */
	public void format() {
		super.format();
		final IDocument document = (IDocument) documents.removeFirst();
		if (document != null) {
			String text = document.get();
			try {
				XMLFormatter formatter = new XMLFormatter();
				document.set(formatter.format(text));
			} catch (Exception e) {
				e.printStackTrace();
				MessageDialog.openInformation(null, NCLEditorMessages
						.getInstance().getString("ContentFormat.Error.Title"),
						NCLEditorMessages.getInstance().getString(
								"ContentFormat.Error.XMLParserError")
								+ "(" + e.getMessage() + ")");
			}
		}
	}

	/**
	 * @see org.eclipse.jface.text.formatter.ContextBasedFormattingStrategy#formatterStarts(org.eclipse.jface.text.formatter.IFormattingContext)
	 */
	public void formatterStarts(final IFormattingContext context) {
		super.formatterStarts(context);

		documents.addLast(context
				.getProperty(FormattingContextProperties.CONTEXT_MEDIUM));
	}

	/**
	 * @see org.eclipse.jface.text.formatter.ContextBasedFormattingStrategy#formatterStops()
	 */
	public void formatterStops() {
		super.formatterStops();

		documents.clear();
	}
}
