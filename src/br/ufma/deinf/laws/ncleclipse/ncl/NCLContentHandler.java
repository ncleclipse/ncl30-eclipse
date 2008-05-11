package br.ufma.deinf.laws.ncleclipse.ncl;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import br.ufma.deinf.laws.ncleclipse.xml.XMLParser;

public class NCLContentHandler implements ContentHandler{
	private NCLDocument nclDocument = null;
	private Stack<String> perspective;
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
		if(localName.equals("body") 
				|| localName.equals("context") 
				|| localName.equals("media")
				|| localName.equals("switch")
				|| localName.equals("causalConnector")
				|| localName.equals("ncl"))
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
		//implementação da perspectiva
		if(localName.equals("body") 
				|| localName.equals("context") 
				|| localName.equals("media")
				|| localName.equals("switch")
				|| localName.equals("causalConnector")
				|| localName.equals("ncl"))
		{
			if (atts.getValue("id") != null){
				String strPerspective = "";
				if(nclDocument.alias != null && !nclDocument.alias.equals(""))
					strPerspective = nclDocument.alias+"#";
				strPerspective += atts.getValue("id");
				perspective.push(strPerspective);
			}
		}
		
		// Carrega o NCLDocument
		System.out.println("Adicionando no NCLContentHandler " + qName + " id = "+ atts.getValue("id"));
		NCLElement nclElement = new NCLElement(localName, perspective.lastElement());
		for(int i = 0; i < atts.getLength(); i++){
			nclElement.setAttributeValue(atts.getLocalName(i), atts.getValue(i));
		}
		nclDocument.addElement(nclElement, atts.getValue("id"));
		
		//nclDocument.addElement(qName, atts.getValue("id"));
		if(localName.equals("importBase")){
			String alias_ant = nclDocument.getAlias();
			String alias = alias_ant;
			System.out.println("importando documento... alias: " + atts.getValue("alias") + " src:" + atts.getValue("documentURI"));
			if(alias != null && !alias.equals("")) alias += atts.getValue("alias");
			else alias = atts.getValue("alias");
			nclDocument.setAlias(alias);
			
			XMLParser parser = new XMLParser();
			parser.setContentHandler(this);
			try {
				parser.doParse(new File(new URI(nclDocument.getParentURI().toString()+"/"+atts.getValue("documentURI"))));
			} catch (Exception e) {
				throw new SAXException(e);
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
