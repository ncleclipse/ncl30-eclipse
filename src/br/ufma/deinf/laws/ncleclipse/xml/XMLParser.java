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
 * Performs DTD validation on supplied XML document
 */
public class XMLParser {

	private ErrorHandler errorHandler;
	private ContentHandler contentHandler;

	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	public void setContentHandler(ContentHandler contentHandler) {
		this.contentHandler = contentHandler;
	}

	public static void main(String[] args) {
		try {
			XMLParser parser = new XMLParser();
			parser.setErrorHandler(new XMLValidationErrorHandler());
			parser.doParse(new File(args[0]));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
	public static final String VALIDATE_PREFIX = "http://xml.org/sax/features/namespaces";

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
			reader.setFeature(VALIDATE_PREFIX, false);
			//reader.setFeature(VALIDATION_FEATURE, true);
			reader.parse(inputSource);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
