/*******************************************************************************
 * This file is part of the authoring environment in Nested Context Language -
 * NCL Eclipse.
 * 
 * Copyright: 2007-2009 UFMA/LAWS (Laboratory of Advanced Web Systems), All Rights Reserved.
 * 
 * This program is free software; you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU General Public License version 2 for more 
 * details.
 * 
 * You should have received a copy of the GNU General Public License version 2
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 * For further information contact:
 * 		ncleclipse@laws.deinf.ufma.br
 * 		http://www.laws.deinf.ufma.br/ncleclipse
 * 		http://www.laws.deinf.ufma.br
 ********************************************************************************/
package br.ufma.deinf.laws.ncleclipse.ncl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 * 
 */
public class NCLParser {
	private ErrorHandler errorHandler;
	private ContentHandler contentHandler;

	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	public void setContentHandler(ContentHandler contentHandler) {
		this.contentHandler = contentHandler;
	}

	public static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";

	/**
	 * Does DTD-based validation on File
	 */
	public void doParse(File xmlFilePath) throws RuntimeException {

		InputSource inputSource = null;
		try {
			inputSource = new InputSource(new FileReader(xmlFilePath));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		doParse(inputSource);

	}

	/**
	 * Does DTD-based validation on text
	 */
	public void doParse(String xmlText) throws RuntimeException {

		InputSource inputSource = new InputSource(new StringReader(xmlText));
		doParse(inputSource);

	}

	/**
	 * Does DTD-based validation on inputSource
	 */
	public void doParse(InputSource inputSource) throws RuntimeException {

		try {
			XMLReader reader = new SAXParser();
			reader.setErrorHandler(errorHandler);
			reader.setContentHandler(contentHandler);
			// reader.setFeature(VALIDATION_FEATURE, true);
			reader.parse(inputSource);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
