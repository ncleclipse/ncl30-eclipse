/**
 *  Copyright � 2005 Nick Wilson (SvcDelivery)
 *  Email: nick@svcdelivery.com
 *  
 *  This file is part of the XmlAuthor Eclipse plugin.
 *
 *  XmlAuthor is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  XmlAuthor is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with XmlAuthor; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
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

/**
 * @author Nick Wilson
 */
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
