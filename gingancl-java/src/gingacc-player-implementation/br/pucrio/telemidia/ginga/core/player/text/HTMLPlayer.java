/******************************************************************************
Este arquivo é parte da implementação do ambiente declarativo do middleware 
Ginga (Ginga-NCL).

Direitos Autorais Reservados (c) 1989-2007 PUC-Rio/Laboratório TeleMídia

Este programa é software livre; você pode redistribuí-lo e/ou modificá-lo sob 
os termos da Licença Pública Geral GNU versão 2 conforme publicada pela Free 
Software Foundation.

Este programa é distribuído na expectativa de que seja útil, porém, SEM 
NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE OU 
ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública Geral do 
GNU versão 2 para mais detalhes. 

Você deve ter recebido uma cópia da Licença Pública Geral do GNU versão 2 junto 
com este programa; se não, escreva para a Free Software Foundation, Inc., no 
endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA. 

Para maiores informações:
ncl @ telemidia.puc-rio.br
http://www.ncl.org.br
http://www.ginga.org.br
http://www.telemidia.puc-rio.br
******************************************************************************
This file is part of the declarative environment of middleware Ginga (Ginga-NCL)

Copyright: 1989-2007 PUC-RIO/LABORATORIO TELEMIDIA, All Rights Reserved.

This program is free software; you can redistribute it and/or modify it under 
the terms of the GNU General Public License version 2 as published by
the Free Software Foundation.

This program is distributed in the hope that it will be useful, but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License version 2 for more 
details.

You should have received a copy of the GNU General Public License version 2
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA

For further information contact:
ncl @ telemidia.puc-rio.br
http://www.ncl.org.br
http://www.ginga.org.br
http://www.telemidia.puc-rio.br
*******************************************************************************/

package br.pucrio.telemidia.ginga.core.player.text;

import java.awt.Insets;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import br.pucrio.telemidia.ginga.core.io.GFXManager;
import br.pucrio.telemidia.ginga.core.player.DefaultPlayerImplementation;

/**
 * @author me
 *
 */
public class HTMLPlayer extends DefaultPlayerImplementation {
	private String htmlContent;
	
	private boolean border;
	
	public static final String BORDER_PROPERTY = "border";
	
	public static final String NO_BORDER_PROPERTY_VAUE = "none";
	/**
	 * Creates an HTML player specifying the the content's URL.
	 * @param contentURL the URL of the content to be played.
	 */
	public HTMLPlayer(URL contentURL, String rawContent) {
		super(contentURL);
		border = false;
		
		int index;
		String baseHref;
		StringBuffer fileName = new StringBuffer(contentURL.getFile());
		if (!fileName.toString().equals("")) {
			index = contentURL.getFile().lastIndexOf('/');
			if (index < fileName.length() - 1)
				fileName.setLength(index + 1);
		}
		if (contentURL.getProtocol().equalsIgnoreCase("file")) {
			baseHref = contentURL.getProtocol() + ":" + fileName.toString();
		}
		else {
			baseHref = contentURL.getProtocol() + "://" + contentURL.getHost();

			if (contentURL.getPort() >= 0)
				baseHref = baseHref + ":" + contentURL.getPort() + fileName.toString();
			else
				baseHref = baseHref + fileName.toString();
		}
		htmlContent = rawContent;

		// criar a fabrica
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		Document document = null;

		try {
			// criando novo construtor de documentos
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			// fazendo o parse inicial do documento
			StringReader stringReader = new StringReader(htmlContent);
			InputSource inputSource = new InputSource(stringReader);
			document = docBuilder.parse(inputSource);
			
			// estabelecendo a url base do documento
			NodeList nodeList = document.getElementsByTagName("head");
			Node headNode = nodeList.item(0);
			Element newElement = document.createElement("base");
			newElement.setAttribute("href", baseHref);
			headNode.appendChild(newElement);
			
			// procura pela regiao a ser exibida
			String ref = contentURL.getRef();
			if (ref != null)
				XmlDocumentUtilities.getDocumentRef(document, ref);

			// inicializa o conteudo na area de texto html
			htmlContent = XmlDocumentUtilities.writeDom2String(document);
			
		}catch (IOException ex) {
			System.err.println("[ERR] Error reading file for HTML Player: " + ex.getMessage());
			return;
		}catch (SAXException ex) {
			System.err.println("[ERR] Error parsing file form HTML player: " + ex.getMessage());
		}catch (Exception ex){
			System.err.println("[ERR] " + ex.getMessage());
		}

		this.setSurface(GFXManager.getInstance().createSurface(""));
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.core.player.IPlayer#eventStateChanged(java.lang.String, short, short, int)
	 */
	public void eventStateChanged(String id, short type, short transition,
			int code) {
		// Nothing
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.core.player.IPlayer#getPropertyValue(java.lang.String)
	 */
	public String getPropertyValue(String name) {
		return null;
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.core.player.IPlayer#setPropertyValue(java.lang.String, java.lang.String)
	 */
	public void setPropertyValue(String name, String value) {
		if(name.equals(BORDER_PROPERTY)){
			if(value.equals(NO_BORDER_PROPERTY_VAUE))
				border = false;
			else
				border = true;
		}
	}
	
	public String getHTMLContent(){
		return htmlContent;
	}
	
	public void setHTMLContent(String htmlContent){
		this.htmlContent = htmlContent;
	}

	@Override
	public void play() {
		JEditorPane htmlArea;
		HTMLEditorKit editorKit;
		htmlArea = new JEditorPane();
		htmlArea.setEditable(false);
		editorKit = new HTMLEditorKit();
		htmlArea.setEditorKit(editorKit);

		HTMLDocument htmlDocument;
		
		StringReader stringReader = new StringReader(getHTMLContent());
		htmlDocument = (HTMLDocument)editorKit.createDefaultDocument();

		try {
			htmlArea.read(stringReader, htmlDocument);
			//htmlArea.addHyperlinkListener(linkHandler);
		}
		catch (Exception exc) {
			System.out.println("Exception: " + exc);
			return;
		}
		if(border)
			htmlArea.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));

		/*descriptor = object.getDescriptor();
		if (descriptor != null) {
			optionValue = (String)descriptor.getParameterValue("border");
			if (optionValue != null && optionValue.equals("none")) {
				htmlArea.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
			}

			/*
			optionValue = (String)descriptor.getParameterValue("scroll");
			if (optionValue != null && optionValue.equals("never")) {
				scroller
						.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
				scroller
						.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			}
			*/

			/*htmlArea.setBackground(descriptor.getFormatterRegion()
					.getBackgroundColor());
		}*/
		this.getSurface().setSurface(htmlArea);
		super.play();
	}

	@Override
	public void stop() {
		this.getSurface().clear();
		super.stop();
	}
}
