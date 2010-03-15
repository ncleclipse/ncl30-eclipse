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

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 * 
 */
public class XMLFormatter extends DefaultHandler2 {
	private String xmlVersion = "1.0";
	private String encoding = "UTF-8";
	/** The indent string (defaults to the tab character). */
	private String indent = "\t";
	private String lineEnd = "\r\n";
	private char quote = '\"';
	private String nbsp = "\u00A0";

	/** The current indent level. */
	private int level = 0;
	private StringBuffer output = new StringBuffer();

	private LinkedList<Boolean> ischild = new LinkedList<Boolean>();

	public String format(String text) throws ParserConfigurationException,
			SAXException, IOException {
		output.append("<?xml version=");
		output.append(quote);
		output.append(xmlVersion);
		output.append(quote);
		output.append(" encoding=");
		output.append(quote);
		int i = text.indexOf("encoding");
		if (i >= 0) {
			for (; i < text.length(); i++)
				if (text.charAt(i) == '=')
					break;
			for (++i; i < text.length(); i++)
				if (text.charAt(i) == '\"')
					break;
			for (++i; i < text.length(); i++) {
				if (text.charAt(i) == '\"')
					break;
				output.append(text.charAt(i));
			}
		} else
			output.append("ISO-8859-1");
		StringBuffer aux = new StringBuffer(text);
		while ((i = aux.indexOf("<!DOCTYPE")) > 0) {
			aux.replace(i, i + 9, "<!--DOCTYPE");
			int cont = 1;
			for (i+=9; i < aux.length(); i++) {
				if (aux.charAt(i) == '>')
					cont--;
				if (aux.charAt(i) == '<')
					cont++;
				if (cont == 0)
					break;
			}
			aux.insert(i,"--");
		}
		// text = text.replaceAll("<!DOCTYPE", "<!--DOCTYPE");
		// text = text.replaceAll("]>", "]-->");
		text = new String(aux);
		// output.append(document.getXmlEncoding());
		output.append(quote);
		output.append("?>");
		output.append(lineEnd);
		// processChildNodes(document.getChildNodes());
		ischild.addFirst(false);

		XMLReader xmlReader = XMLReaderFactory.createXMLReader();
		xmlReader.setContentHandler(this);
		xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes",
				true);
		xmlReader.setProperty("http://xml.org/sax/properties/lexical-handler",
				this);
		xmlReader.setFeature("http://xml.org/sax/features/validation", false);
		xmlReader
				.setFeature(
						"http://apache.org/xml/features/nonvalidating/load-external-dtd",
						false);

		// SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		// try{
		xmlReader.parse(new InputSource(new StringReader(text)));
		// }catch (Exception e) {
		// MessageDialog.openInformation(null, null, e.getMessage());
		// }
		aux = output;
		while ((i = aux.indexOf("<!--DOCTYPE")) > 0) {
			aux.replace(i, i + 11, "<!DOCTYPE");
			int cont = 1;
			for (i+=11; i < aux.length(); i++) {
				if (aux.charAt(i) == '>')
					cont--;
				if (aux.charAt(i) == '<')
					cont++;
				if (cont == 0)
					break;
			}
			aux.delete(i-2, i);
		}
		// text = text.replaceAll("<!--DOCTYPE", "<!DOCTYPE");
		// text = text.replaceAll("]-->", "]>");
		return aux.toString();
	}

	public void startDocument() {

	}

	public void endDocument() {
	}

	public void startCDATA() {
		if (ischild.element()) {
			output.append(">");
			output.append(lineEnd);
		}
		addIndent();
		output.append("<![CDATA[");
		output.append(lineEnd);
	}

	public void endCDATA() {
		output.append(lineEnd);
		addIndent();
		output.append("]]>");
		output.append(lineEnd);
		if (ischild.element()) {
			ischild.remove();
			ischild.addFirst(false);
		}
	}

	public void startDTD(String name, String publicId, String systemId) {
		MessageDialog.openInformation(null, null, "name = " + name + " "
				+ "publicid = " + publicId + " " + "systemid = " + systemId);
	}

	public void endDTD() {
		MessageDialog.openInformation(null, null, "termino DTD");
	}

	/** comeca uma tag nova */
	public void startElement(String uri, String localName, String tag,
			Attributes atributos) {
		if (ischild.element()) {
			output.append(">");
			output.append(lineEnd);
		}
		addIndent();
		output.append("<");
		output.append(tag);
		for (int i = 0; i < atributos.getLength(); i++) {
			output.append(" ");
			output.append(atributos.getQName(i));
			output.append("=");
			output.append(quote);
			output.append(atributos.getValue(i));
			output.append(quote);
		}
		level++;
		ischild.addFirst(true);
	}

	public void endElement(String uri, String localName, String tag) {
		if (ischild.element()) {
			output.append("/>");
			output.append(lineEnd);
			level--;
		} else {
			level--;
			addIndent();
			output.append("</");
			output.append(tag);
			output.append(">");
			output.append(lineEnd);
		}
		ischild.remove();
		ischild.addFirst(false);
	}

	/*
	 * private void processChildNodes(NodeList children) { for (int i = 0; i <
	 * children.getLength(); i++) { Node node = children.item(i); if (node
	 * instanceof Element) { processElement((Element) node); } else if (node
	 * instanceof ProcessingInstruction) { processPI((ProcessingInstruction)
	 * node); } else if (node instanceof Entity) { processEntity((Entity) node);
	 * } else if (node instanceof CDATASection) { processCDATA((CDATASection)
	 * node); } else if (node instanceof Text) { processText((Text) node); }
	 * else if (node instanceof Comment) { processComment((Comment) node); }
	 * else if (node instanceof DocumentType) {
	 * processDocumentType((DocumentType) node); } else {
	 * System.out.println(node.getClass().toString()); } } }
	 * 
	 * private void processCDATA(CDATASection section) { addIndent();
	 * output.append("<![CDATA["); output.append(section.getNodeValue());
	 * output.append("]]>"); output.append(lineEnd); }
	 * 
	 * private void processDocumentType(DocumentType type) { addIndent();
	 * output.append("<!DOCTYPE "); output.append(type.getNodeName()); if
	 * (type.getPublicId() != null) { output.append(" PUBLIC ");
	 * output.append(quote); output.append(type.getPublicId());
	 * output.append(quote); output.append(" "); output.append(quote);
	 * output.append(type.getSystemId()); output.append(quote); } else if
	 * (type.getSystemId() != null) { output.append(" SYSTEM ");
	 * output.append(quote); output.append(type.getSystemId());
	 * output.append(quote); } // TODO; Format correctly if
	 * (type.getInternalSubset() != null) { output.append(" [");
	 * output.append(lineEnd); output.append(type.getInternalSubset());
	 * addIndent(); output.append("]"); } output.append(">");
	 * output.append(lineEnd); }
	 * 
	 * private void processEntity(Entity entity) { // NamedNodeMap attributes =
	 * entity.getAttributes(); addIndent(); output.append("<!ENTITY ");
	 * output.append(entity.getNodeName()); output.append(" ");
	 * output.append(quote); String oldIndent = indent; String oldLineEnd =
	 * lineEnd; indent = ""; lineEnd = ""; swapQuote();
	 * processChildNodes(entity.getChildNodes()); swapQuote(); indent =
	 * oldIndent; lineEnd = oldLineEnd; output.append(quote);
	 * output.append(">"); output.append(lineEnd); }
	 * 
	 * private void processPI(ProcessingInstruction instruction) { addIndent();
	 * output.append("<?"); output.append(instruction.getNodeName());
	 * output.append(" "); output.append(instruction.getData());
	 * output.append("?>"); output.append(lineEnd); }
	 * 
	 * private void processComment(Comment comment) { addIndent();
	 * output.append("<!--"); output.append(comment.getData());
	 * output.append("-->"); output.append(lineEnd); }
	 * 
	 * private void processText(Text text) { String token = ""; StringTokenizer
	 * tokenizer = new StringTokenizer(text.getData(), "& \t\r\n\u00A0", true);
	 * while (tokenizer.hasMoreTokens()) { token = tokenizer.nextToken(); if
	 * (token.indexOf(' ') == -1 && token.indexOf('\t') == -1 &&
	 * token.indexOf('\r') == -1 && token.indexOf('\n') == -1) {
	 * outputText(token); break; } } while (tokenizer.hasMoreTokens()) { token =
	 * tokenizer.nextToken(); if (token.indexOf(' ') != -1 ||
	 * token.indexOf('\t') != -1 || token.indexOf('\r') != -1 ||
	 * token.indexOf('\n') != -1) { if (tokenizer.hasMoreTokens()) {
	 * output.append(" "); } } else { outputText(token); } } }
	 * 
	 * private void outputText(String token) { if (token.equals("&")) {
	 * output.append("&amp;"); } else if (token.equals(nbsp)) {
	 * output.append("&160;"); } else if (token.equals("<")) {
	 * output.append("&lt;"); } else if (token.equals(">")) {
	 * output.append("&gt;"); } else { output.append(token); } }
	 * 
	 * private void processElement(Element element) { NamedNodeMap attributes =
	 * element.getAttributes(); addIndent(); output.append("<");
	 * output.append(element.getNodeName()); for (int i = 0; i <
	 * attributes.getLength(); i++) { Node node = attributes.item(i); if (node
	 * instanceof Attr) { Attr attr = (Attr) node; output.append(" ");
	 * output.append(attr.getNodeName()); output.append("=");
	 * output.append(quote); output.append(attr.getValue());
	 * output.append(quote); } } if (element.hasChildNodes()) { NodeList
	 * children = element.getChildNodes(); if (children.getLength() == 1 &&
	 * element.getFirstChild() instanceof Text) { Text el = (Text)
	 * element.getFirstChild(); System.out.println("aqui eu " + el.getData());
	 * if (el.getData().trim().equals("")) { // remove empty text // element
	 * output.append("/>"); output.append(lineEnd); } else { output.append(">");
	 * level++; processChildNodes(children); level--; output.append("</");
	 * output.append(element.getNodeName()); output.append(">");
	 * output.append(lineEnd); } } else { output.append(">");
	 * output.append(lineEnd); level++; processChildNodes(children); level--;
	 * addIndent(); output.append("</"); output.append(element.getNodeName());
	 * output.append(">"); output.append(lineEnd); } } else {
	 * output.append("/>"); output.append(lineEnd); } }
	 */

	private void addIndent() {
		for (int i = 0; i < level; i++) {
			output.append(indent);
		}
	}

	private void swapQuote() {
		if (quote == '\"') {
			quote = '\'';
		} else {
			quote = '\"';
		}
	}

	/**
	 * @return Returns the indent.
	 */
	public String getIndent() {
		return indent;
	}

	/**
	 * @param indent
	 *            The indent to set.
	 */
	public void setIndent(String indent) {
		this.indent = indent;
	}

	/**
	 * @return Returns the lineEnd.
	 */
	public String getLineEnd() {
		return lineEnd;
	}

	/**
	 * @param lineEnd
	 *            The lineEnd to set.
	 */
	public void setLineEnd(String lineEnd) {
		this.lineEnd = lineEnd;
	}

	/**
	 * @return Returns the encoding.
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding
	 *            The encoding to set.
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @return Returns the xmlVersion.
	 */
	public String getXmlVersion() {
		return xmlVersion;
	}

	/**
	 * @param xmlVersion
	 *            The xmlVersion to set.
	 */
	public void setXmlVersion(String xmlVersion) {
		this.xmlVersion = xmlVersion;
	}

	public void comment(char[] arg0, int arg1, int arg2) throws SAXException {
		if (ischild.element()) {
			output.append(">");
			ischild.remove();
			ischild.addFirst(false);
			output.append(lineEnd);
		}
		addIndent();
		output.append("<!--");
		for (int i = 0; i < arg2; i++)
			output.append(arg0[i + arg1]);
		output.append("-->");
		output.append(lineEnd);
	}

	public void characters(char[] ch, int start, int length) {
		// MessageDialog.openInformation(null, null, "*"+new
		// String(ch,start,length)+"*");
		while ((ch[start] == '\n' || ch[start] == ' ' || ch[start] == '\t')
				&& length > 0) {
			start++;
			length--;
		}
		if (ischild.element()) {
			output.append(">");
			ischild.remove();
			ischild.addFirst(false);
			output.append(lineEnd);
		}
		if (length <= 0)
			return;
		addIndent();
		output.append(new String(ch, start, length));
		output.append(lineEnd);
	}

}
