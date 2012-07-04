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
package br.ufma.deinf.laws.ncleclipse.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 *
 */
public class XMLValidationErrorHandler extends DefaultHandler {

	private List errorList = new ArrayList();
	private Locator locator;

	public XMLValidationErrorHandler() {
	}

	public void error(SAXParseException e) throws SAXException {

		handleError(e, false);

	}

	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
	}

	private void handleError(SAXParseException e, boolean isFatal) {
		XMLValidationError validationError = nextError(e, isFatal);
		errorList.add(validationError);
		//System.out.println(validationError.toString());

	}

	protected XMLValidationError nextError(SAXParseException e, boolean isFatal) {
		String errorMessage = e.getMessage();

		int lineNumber = locator.getLineNumber();
		int columnNumber = locator.getColumnNumber();

		log(this, (isFatal ? "FATAL " : "Non-Fatal") + "Error on line "
				+ lineNumber + ", column " + columnNumber + ": " + errorMessage);

		XMLValidationError validationError = new XMLValidationError();
		validationError.setLineNumber(lineNumber);
		validationError.setColumnNumber(columnNumber);
		validationError.setErrorMessage(errorMessage);
		return validationError;
	}

	private void log(XMLValidationErrorHandler handler, String string) {
	}

	public void fatalError(SAXParseException e) throws SAXException {
		handleError(e, true);
	}

	public List getErrorList() {
		return errorList;
	}

}
