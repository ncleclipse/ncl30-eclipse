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

import java.io.ByteArrayOutputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlDocumentUtilities {

	public static Element getElementWithAttr(Document document, Element element,
			String attrName, String attrValue) {

		if (element.hasAttribute(attrName)) {
			String idStr = element.getAttribute(attrName);
			if (attrValue.equals(idStr))
				return element;
		}

		NodeList nodeList = element.getChildNodes();
		int size = nodeList.getLength();
		for (int i = 0; i < size; i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element) {
				Element elem = getElementWithAttr(document, (Element)node, attrName,
						attrValue);
				if (elem != null)
					return elem;
			}
		}
		return null;
	}

	public static boolean insertLabeledAnchor(Document document, String attrName,
			String attrValue, String newElementName, String labelAttrName,
			String labelAttrValue) {

		Element element = getElementWithAttr(document, document
				.getDocumentElement(), attrName, attrValue);

		if (element != null) {
			Element newElement = document.createElement(newElementName);
			newElement.setAttribute(labelAttrName, labelAttrValue);
			Node parent = element.getParentNode();
			Node nextNode = element.getNextSibling();
			parent.removeChild(element);
			parent.insertBefore(newElement, nextNode);
			newElement.appendChild(element);
			return true;
		}
		else
			return false;
	}

	private static Element getElementRef(Element element, String label) {
		Element retElement;

		if (element.hasAttribute("id")) {
			String idStr = element.getAttribute("id");
			if (label.equals(idStr))
				return element;
		}

		NodeList nodeList = element.getChildNodes();
		int size = nodeList.getLength();
		for (int i = 0; i < size; i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element) {
				retElement = getElementRef((Element)node, label);
				if (retElement != null)
					return retElement;
			}
		}
		return null;
	}

	public static void getDocumentRef(Document document, String ref) {
		NodeList nodeList = document.getElementsByTagName("body");
		Node bodyNode = nodeList.item(0);

		Node parent = bodyNode.getParentNode();
		parent.removeChild(bodyNode);
		Element newElement = document.createElement("body");
		Element element = XmlDocumentUtilities
				.getElementRef((Element)bodyNode, ref);
		parent.appendChild(newElement);
		newElement.appendChild(element);
	}

	public static String writeDom2String(Document document) {
		ByteArrayOutputStream output;
		String strOutput;

		if (document == null)
			return null;

		try {
			Transformer trans = TransformerFactory.newInstance().newTransformer();
			output = new ByteArrayOutputStream();
			trans.transform(new javax.xml.transform.dom.DOMSource(document),
					new StreamResult(output));

			String outStr = output.toString();
			char[] tempStr = new char[outStr.length()];
			tempStr = outStr.toCharArray();
			strOutput = new String(tempStr);

			// adaptando a string finalContent para HTML
			int strIndex, strIndex2;
			strIndex = strOutput.indexOf("<html>");
			strOutput = strOutput.substring(strIndex, strOutput.length());
			strIndex = strOutput.indexOf("<META");
			strIndex2 = strOutput.indexOf(">", strIndex);
			strOutput = strOutput.substring(0, strIndex)
					+ strOutput.substring(strIndex2 + 1, strOutput.length());
			// fim da adaptacao

			return strOutput;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
