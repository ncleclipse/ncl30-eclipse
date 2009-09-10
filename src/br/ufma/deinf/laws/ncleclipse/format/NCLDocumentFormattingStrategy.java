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
package br.ufma.deinf.laws.ncleclipse.format;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.formatter.ContextBasedFormattingStrategy;
import org.eclipse.jface.text.formatter.FormattingContextProperties;
import org.eclipse.jface.text.formatter.IFormattingContext;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class NCLDocumentFormattingStrategy extends ContextBasedFormattingStrategy {
	 
	/** Documents to be formatted by this strategy */
	private final LinkedList documents= new LinkedList();
    
	public NCLDocumentFormattingStrategy() {
		super();
    }
 
	/**
	 * @see org.eclipse.jface.text.formatter.IFormattingStrategyExtension#format()
	 */
	public void format() {
        super.format();
     	final IDocument document= (IDocument)documents.removeFirst();
		if (document != null) {
			String text = document.get();
			try {
				DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
				builderFactory.setExpandEntityReferences(false);
				DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
				documentBuilder.setEntityResolver(new EntityResolver() {
				
					public InputSource resolveEntity(String publicId, String systemId)
							throws SAXException, IOException {
						InputSource entity;
						entity = new InputSource(new StringReader(""));
						return entity;
					}
				
				});
				org.w3c.dom.Document dom = documentBuilder.parse(new InputSource(new StringReader(text)));
				XMLFormatter formatter = new XMLFormatter();
				document.set(formatter.format(dom));
			} catch (Exception e) {
				e.printStackTrace();
				MessageDialog.openInformation(
						null,
						"XML Format",
						"The XML Formatter can only format valid XML. Please correct the errors first.");
			}
		}
	}
     
	
    /**
 	 * @see org.eclipse.jface.text.formatter.ContextBasedFormattingStrategy#formatterStarts(org.eclipse.jface.text.formatter.IFormattingContext)
 	 */
 	public void formatterStarts(final IFormattingContext context) {
 		super.formatterStarts(context);
 		
 		documents.addLast(context.getProperty(FormattingContextProperties.CONTEXT_MEDIUM));
 	}

 	/**
 	 * @see org.eclipse.jface.text.formatter.ContextBasedFormattingStrategy#formatterStops()
 	 */
 	public void formatterStops() {
 		super.formatterStops();

 		documents.clear();
	}
}
