/******************************************************************************
Este arquivo é parte da implementação do ambiente de autoria em Nested Context
Language - NCL Eclipse.

Direitos Autorais Reservados (c) 2007-2008 UFMA/LAWS (Laboratório de Sistemas Avançados da Web) 

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
ncleclipse@laws.deinf.ufma.br
http://www.laws.deinf.ufma.br/ncleclipse
http://www.laws.deinf.ufma.br

******************************************************************************
This file is part of the authoring environment in Nested Context Language -
NCL Eclipse.

Copyright: 2007-2008 UFMA/LAWS (Laboratory of Advanced Web Systems), All Rights Reserved.

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
ncleclipse@laws.deinf.ufma.br
http://www.laws.deinf.ufma.br/ncleclipse
http://www.laws.deinf.ufma.br

*******************************************************************************/

package br.ufma.deinf.laws.ncleclipse.ncl;

import java.io.File;
import java.net.URI;
import java.util.Stack;

import org.eclipse.jface.text.Position;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import br.ufma.deinf.laws.ncleclipse.xml.XMLParser;

public class NCLContentHandler implements ContentHandler{
	private NCLDocument nclDocument = null;
	private Stack<String> perspective;
	private String perspectiveSemId = "0";
	private Locator locator;
	
	@Override
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endElement(String localName, String arg1, String arg2)
			throws SAXException {
		// TODO Auto-generated method stub
		System.out.println("end element " + arg1);
		if(arg1.equals("body")
				|| arg1.equals("ncl")
				|| arg1.equals("context") 
				|| arg1.equals("media")
				|| arg1.equals("switch")
				|| arg1.equals("causalConnector")
				|| arg1.equals("descriptorSwitch"))
		{
			perspective.pop();
		}
	}

	@Override
	public void endPrefixMapping(String arg0) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processingInstruction(String arg0, String arg1)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDocumentLocator(Locator arg0) {
		// TODO Auto-generated method stub
		this.locator = arg0;
	}

	@Override
	public void skippedEntity(String arg0) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		if(nclDocument == null)
			nclDocument = new NCLDocument();
		if(perspective == null)
			perspective = new Stack();
	}

	@Override
	public void startElement(String URI, String localName, String qName,
			Attributes atts) throws SAXException {
		String strPerspective = "";
		if(perspective.size() > 0 ) strPerspective = perspective.lastElement(); 
		
		// Carrega o NCLDocument
		System.out.println("Adicionando no NCLContentHandler " + qName + " id = "+ atts.getValue("id") + " - perspective = " + strPerspective);
		
		NCLElement nclElement = new NCLElement(localName, strPerspective, locator.getLineNumber()-1);
		for(int i = 0; i < atts.getLength(); i++){
			nclElement.setAttributeValue(atts.getLocalName(i), atts.getValue(i));
		}
		nclDocument.addElement(nclElement, atts.getValue("id"));
		
		//Verifica se o elemento atual define uma perspectiva 
		if(localName.equals("body")
				|| localName.equals("ncl")
				|| localName.equals("context") 
				|| localName.equals("media")
				|| localName.equals("switch")
				|| localName.equals("causalConnector")
				|| localName.equals("descriptorSwitch"))
		{
			String strNewPerspective = "";
			if(nclDocument.alias != null && !nclDocument.alias.equals(""))
				strNewPerspective = nclDocument.alias+"#";
			if(atts.getValue("id") != null)
				strNewPerspective += atts.getValue("id");
			else {
				if(perspective.size() > 0){
					strNewPerspective += perspective.lastElement();
				}
				else{
					Integer pTmp = new Integer(perspectiveSemId);
					pTmp += 1;
					perspectiveSemId = pTmp.toString();
					strNewPerspective += perspectiveSemId;
				}
			}
			perspective.push(strNewPerspective);
		}
		//nclDocument.addElement(qName, atts.getValue("id"));
		if(localName.equals("importBase") || localName.equals("importNCL")){
			String alias_ant = nclDocument.getAlias();
			String alias = alias_ant;
			System.out.println("importando documento... alias: " + atts.getValue("alias") + " src:" + atts.getValue("documentURI"));
			if(alias != null && !alias.equals("")) alias += atts.getValue("alias");
			else alias = atts.getValue("alias");
			nclDocument.setAlias(alias);
			
			XMLParser parser = new XMLParser();
			parser.setContentHandler(this);
			try {
				if(atts.getValue("documentURI") != null && !atts.getValue("documentURI").equals("")){
				URI uri = new URI(atts.getValue("documentURI"));
					if(uri.isAbsolute()){
						parser.doParse(new File(uri));
					}
					else 
						parser.doParse(new File(new URI(nclDocument.getParentURI().toString()+"/"+atts.getValue("documentURI"))));
				}
			} catch (Exception e) {
				System.out.println("N�o foi poss�vel fazer o parse do documento " + atts.getValue("documentURI"));
			}
			nclDocument.setAlias(alias_ant);
		}
	}

	@Override
	public void startPrefixMapping(String arg0, String arg1)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	public NCLDocument getNclDocument() {
		return nclDocument;
	}

	public void setNclDocument(NCLDocument nclDocument) {
		this.nclDocument = nclDocument;
	}
	
}
