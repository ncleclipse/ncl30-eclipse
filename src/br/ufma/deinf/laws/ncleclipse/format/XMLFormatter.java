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

package br.ufma.deinf.laws.ncleclipse.format;

import java.util.StringTokenizer;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * @author Nick Wilson
 */
public class XMLFormatter {
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

	public String format(Document document) {
		output.append("<?xml version=");
		output.append(quote);
		output.append(xmlVersion);
		output.append(quote);
		output.append(" encoding=");
		output.append(quote);
		output.append(document.getXmlEncoding());
		output.append(quote);
		output.append("?>");
		output.append(lineEnd);
		processChildNodes(document.getChildNodes());
		return output.toString();
	}

	private void processChildNodes(NodeList children) {
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node instanceof Element) {
				processElement((Element) node);
			} else if (node instanceof ProcessingInstruction) {
				processPI((ProcessingInstruction) node);
			} else if (node instanceof Entity) {
				processEntity((Entity) node);
			} else if (node instanceof CDATASection) {
				processCDATA((CDATASection) node);
			} else if (node instanceof Text) {
				processText((Text) node);
			} else if (node instanceof Comment) {
				processComment((Comment) node);
			} else if (node instanceof DocumentType) {
				processDocumentType((DocumentType) node);
			} else {
				System.out.println(node.getClass().toString());
			}
		}
	}

	private void processCDATA(CDATASection section) {
		addIndent();
		output.append("<![CDATA[");
		output.append(section.getNodeValue());
		output.append("]]>");
		output.append(lineEnd);
	}

	/**
	 * @param type
	 */
	private void processDocumentType(DocumentType type) {
		addIndent();
		output.append("<!DOCTYPE ");
		output.append(type.getNodeName());
		if (type.getPublicId() != null) {
			output.append(" PUBLIC ");
			output.append(quote);
			output.append(type.getPublicId());
			output.append(quote);
			output.append(" ");
			output.append(quote);
			output.append(type.getSystemId());
			output.append(quote);
		} else if (type.getSystemId() != null) {
			output.append(" SYSTEM ");
			output.append(quote);
			output.append(type.getSystemId());
			output.append(quote);
		}
		// TODO; Format correctly
		if (type.getInternalSubset() != null) {
			output.append(" [");
			output.append(lineEnd);
			output.append(type.getInternalSubset());
			addIndent();
			output.append("]");
		}
		output.append(">");
		output.append(lineEnd);
	}

	/**
	 * @param entity
	 */
	private void processEntity(Entity entity) {
		// NamedNodeMap attributes = entity.getAttributes();
		addIndent();
		output.append("<!ENTITY ");
		output.append(entity.getNodeName());
		output.append(" ");
		output.append(quote);
		String oldIndent = indent;
		String oldLineEnd = lineEnd;
		indent = "";
		lineEnd = "";
		swapQuote();
		processChildNodes(entity.getChildNodes());
		swapQuote();
		indent = oldIndent;
		lineEnd = oldLineEnd;
		output.append(quote);
		output.append(">");
		output.append(lineEnd);
	}

	/**
	 * @param instruction
	 */
	private void processPI(ProcessingInstruction instruction) {
		addIndent();
		output.append("<?");
		output.append(instruction.getNodeName());
		output.append(" ");
		output.append(instruction.getData());
		output.append("?>");
		output.append(lineEnd);
	}

	/**
	 * @param comment
	 */
	private void processComment(Comment comment) {
		addIndent();
		output.append("<!--");
		output.append(comment.getData());
		output.append("-->");
		output.append(lineEnd);
	}

	/**
	 * @param text
	 */
	private void processText(Text text) {
		String token = "";
		StringTokenizer tokenizer = new StringTokenizer(text.getData(),
				"& \t\r\n\u00A0", true);
		while (tokenizer.hasMoreTokens()) {
			token = tokenizer.nextToken();
			if (token.indexOf(' ') == -1 && token.indexOf('\t') == -1
					&& token.indexOf('\r') == -1 && token.indexOf('\n') == -1) {
				outputText(token);
				break;
			}
		}
		while (tokenizer.hasMoreTokens()) {
			token = tokenizer.nextToken();
			if (token.indexOf(' ') != -1 || token.indexOf('\t') != -1
					|| token.indexOf('\r') != -1 || token.indexOf('\n') != -1) {
				if (tokenizer.hasMoreTokens()) {
					output.append(" ");
				}
			} else {
				outputText(token);
			}
		}
	}

	private void outputText(String token) {
		if (token.equals("&")) {
			output.append("&amp;");
		} else if (token.equals(nbsp)) {
			output.append("&160;");
		} else if (token.equals("<")) {
			output.append("&lt;");
		} else if (token.equals(">")) {
			output.append("&gt;");
		} else {
			output.append(token);
		}
	}

	/**
	 * @param element
	 */
	private void processElement(Element element) {
		NamedNodeMap attributes = element.getAttributes();
		addIndent();
		output.append("<");
		output.append(element.getNodeName());
		for (int i = 0; i < attributes.getLength(); i++) {
			Node node = attributes.item(i);
			if (node instanceof Attr) {
				Attr attr = (Attr) node;
				output.append(" ");
				output.append(attr.getNodeName());
				output.append("=");
				output.append(quote);
				output.append(attr.getValue());
				output.append(quote);
			}
		}
		if (element.hasChildNodes()) {
			NodeList children = element.getChildNodes();
			if (children.getLength() == 1
					&& element.getFirstChild() instanceof Text) {
				Text el = (Text)element.getFirstChild();
				System.out.println("aqui eu " + el.getData());
				if(el.getData().trim().equals("")){ // remove empty text element 
					output.append("/>");
					output.append(lineEnd);
				}
				else{
					output.append(">");
					level++;
					processChildNodes(children);
					level--;
					output.append("</");
					output.append(element.getNodeName());
					output.append(">");
					output.append(lineEnd);
				}
			} else {
				output.append(">");
				output.append(lineEnd);
				level++;
				processChildNodes(children);
				level--;
				addIndent();
				output.append("</");
				output.append(element.getNodeName());
				output.append(">");
				output.append(lineEnd);
			}
		} else {
			output.append("/>");
			output.append(lineEnd);
		}
	}

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
}
