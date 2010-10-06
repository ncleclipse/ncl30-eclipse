/*******************************************************************************
 * Este arquivo é parte da implementação do ambiente de autoria em Nested 
 * Context Language - NCL Eclipse.
 * Direitos Autorais Reservados (c) 2007-2010 UFMA/LAWS (Laboratório de Sistemas 
 * Avançados da Web)
 *
 * Este programa é software livre; você pode redistribuí-lo e/ou modificá-lo sob
 * os termos da Licença Pública Geral GNU versão 2 conforme publicada pela Free 
 * Software Foundation.
 *
 * Este programa é distribuído na expectativa de que seja útil, porém, SEM 
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE OU
 * ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública Geral do
 * GNU versão 2 para mais detalhes. Você deve ter recebido uma cópia da Licença
 * Pública Geral do GNU versão 2 junto com este programa; se não, escreva para a
 * Free Software Foundation, Inc., no endereço 59 Temple Street, Suite 330,
 * Boston, MA 02111-1307 USA.
 *
 * Para maiores informações:
 * - ncleclipse@laws.deinf.ufma.br
 * - http://www.laws.deinf.ufma.br/ncleclipse
 * - http://www.laws.deinf.ufma.br
 *
 *******************************************************************************
 * This file is part of the authoring environment in Nested Context Language -
 * NCL Eclipse.
 * Copyright: 2007-2010 UFMA/LAWS (Laboratory of Advanced Web Systems), All
 * Rights Reserved.
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
package br.ufma.deinf.laws.ncleclipse.document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.commands.ITypedParameter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.sun.org.apache.xpath.internal.axes.ChildTestIterator;

import br.ufma.deinf.laws.ncl.NCLStructure;
import br.ufma.deinf.laws.ncl.help.NCLHelper;
import br.ufma.deinf.laws.ncleclipse.NCLEditor;
import br.ufma.deinf.laws.ncleclipse.NCLEditorMessages;
import br.ufma.deinf.laws.ncleclipse.NCLMultiPageEditor;
import br.ufma.deinf.laws.ncleclipse.format.XMLFormatter;
import br.ufma.deinf.laws.ncleclipse.scanners.XMLPartitionScanner;
import br.ufma.deinf.laws.ncleclipse.scanners.XMLTagScanner;
import br.ufma.deinf.laws.ncleclipse.util.ColorManager;
import br.ufma.deinf.laws.ncleclipse.util.XMLPartitioner;

/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 * 
 */
public class NCLSourceDocument extends Document {
	private XMLTagScanner scanner;

	public NCLSourceDocument() {
		super();
	}

	public NCLSourceDocument(IDocument doc) {
		super(doc.get());
	}

	/**
	 * @param doc
	 */
	public NCLSourceDocument(String doc) {
		super(doc);
		IDocumentPartitioner partitioner = new XMLPartitioner(
				new XMLPartitionScanner(), new String[] {
						XMLPartitionScanner.XML_START_TAG,
						XMLPartitionScanner.XML_PI,
						XMLPartitionScanner.XML_DOCTYPE,
						XMLPartitionScanner.XML_END_TAG,
						XMLPartitionScanner.XML_TEXT,
						XMLPartitionScanner.XML_CDATA,
						XMLPartitionScanner.XML_COMMENT });
		partitioner.connect(this);
		setDocumentPartitioner(partitioner);
	}

	/**
	 * Computa o offset do pai
	 * 
	 * @param document
	 * @param documentOffset
	 * @return
	 */
	public int getFatherPartitionOffset(int documentOffset) {
		try {
			ITypedRegion region = getPartition(documentOffset); // região
			String text; // q eu
			// estou
			int partitionOffset = region.getOffset();
			// resolve o problema do usuário começar digitando <, ignora a
			// partição atual
			while (region.getType().equals(XMLPartitionScanner.XML_START_TAG)) {
				text = get(region.getOffset(), region.getLength());
				region = getPartition(--documentOffset);
			}
			Stack<Integer> pilha = new Stack<Integer>();
			do { // procura a tag pai
				text = get(region.getOffset(), region.getLength());
				if (region.getType().equals(XMLPartitionScanner.XML_END_TAG))
					pilha.push(new Integer(1));
				else if (region.getType().equals(
						XMLPartitionScanner.XML_START_TAG)
						&& !text.endsWith("/>")) {
					if (pilha.size() == 0)
						break;
					pilha.pop();
				}
				partitionOffset--;
				region = getPartition(partitionOffset);
				partitionOffset = region.getOffset();
			} while (true);
			return partitionOffset;
		} catch (BadLocationException e) {
			return -1;
		}
	}

	/**
	 * Returns the father partition. The atual partition is a XML_END_TAG.
	 */
	public int getFatherPartitionOffsetFromEndTag(int documentOffset) {
		try {
			ITypedRegion region = getPartition(documentOffset); // região
			String text; // q eu
			// estou
			int partitionOffset = region.getOffset();
			Stack<Integer> pilha = new Stack<Integer>();
			// ignora a partição atual (já se sabe que a atual é uma
			// XML_END_TAG)

			text = get(region.getOffset(), region.getLength());
			region = getPartition(region.getOffset() - 1);
			partitionOffset = region.getOffset();

			// System.out.println(text);
			do { // procura a tag pai

				text = get(region.getOffset(), region.getLength());
				// System.out.println(text);
				if (region.getType().equals(XMLPartitionScanner.XML_END_TAG))
					pilha.push(new Integer(1));
				else if (region.getType().equals(
						XMLPartitionScanner.XML_START_TAG)
						&& !text.endsWith("/>")) {
					// System.out.println(pilha.size());
					if (pilha.size() == 0)
						break;
					pilha.pop();
				}
				partitionOffset--;
				region = getPartition(partitionOffset);
				partitionOffset = region.getOffset();
			} while (true);
			return partitionOffset;
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public Vector<Integer> getChildrenOffsets(int offset) {

		try {
			Vector<Integer> offsets = new Vector<Integer>();
			ITypedRegion region = getPartition(offset);
			String tagname = getCurrentTagname(offset);
			if (tagname == null)
				return offsets;
			String text = get(region.getOffset(), region.getLength());
			if (text.endsWith("/>"))
				return offsets;
			do {
				region = getNextPartition(region);
				offset = region.getOffset();
				text = get(region.getOffset(), region.getLength());
				if (region.getType().equals(XMLPartitionScanner.XML_START_TAG)) {
					offsets.add(offset);
				} else if (region.getType().equals(
						XMLPartitionScanner.XML_END_TAG)) {
					if (text.equals("</" + tagname + ">"))
						break;
				}
			} while (!text.equals("</ncl>"));
			return offsets;
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Computa a tagname pai da atual
	 * 
	 * @return
	 */
	public String getFatherTagName(int documentOffset) {
		return getCurrentTagname(getFatherPartitionOffset(documentOffset));
	}

	/**
	 * Retorna a partição anterior
	 * 
	 * @param d
	 * @param r
	 * @return
	 */
	public ITypedRegion getPreviousPartition(ITypedRegion r) {
		if (r == null)
			return null;
		if (r.getOffset() < 1)
			return null;
		ITypedRegion pr = null;
		try {
			pr = getPartition(r.getOffset() - 1);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return pr;
	}

	/**
	 * Retorna a partição anterior do tipo XML_START_TAG
	 * 
	 * @param d
	 * @param offset
	 * @return
	 * @throws BadLocationException
	 */
	public ITypedRegion getPreviousTagPartition(int offset)
			throws BadLocationException {
		ITypedRegion partition = getPartition(offset);
		if (partition == null
				|| partition.getType()
						.equals(XMLPartitionScanner.XML_START_TAG)) {
			return partition;
		}
		return getPreviousTagPartition(partition);
	}

	/***************************************************************************
	 * Gets the previous partition of type BEGIN_TAG or END_TAG.
	 * 
	 * @param d
	 *            The document containing the partitions
	 * @param r
	 *            The current partition
	 * @return The partition representing the previous tag in the document, or
	 *         <code>null</code> if no such partition can be found.
	 */
	public ITypedRegion getPreviousTagPartition(ITypedRegion r) {
		ITypedRegion partition = getPreviousPartition(r);
		while (partition != null
				&& !partition.getType().equals(
						XMLPartitionScanner.XML_START_TAG)) {
			partition = getPreviousPartition(partition);
		}
		return partition;
	}

	public String getCurrentTagname(int documentOffset) {
		try {
			ITypedRegion region = getPartition(documentOffset);
			int partitionOffset = region.getOffset();
			int readLength = region.getLength();
			ColorManager colorManager = new ColorManager();
			scanner = new XMLTagScanner(colorManager);

			String text = get(partitionOffset, readLength);
			int p = 0;
			char ch;
			String tagname = "";
			ch = text.charAt(0);
			while (true) {
				if (p + 1 >= text.length()
						|| !Character.isJavaIdentifierPart(text.charAt(p + 1)))
					break;
				ch = text.charAt(++p);
				tagname += ch;
			}
			return tagname;
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return "";
	}

	// Guarda o tamanho do ultimo atributo buscado em
	// getAttributeFromCurrentTagName incluindo o seu valor
	// id='123' tam = 8
	private int tamAttr = 0;

	public String getAttributeValueFromCurrentTagName(int offset,
			String attribute) {
		try {
			ITypedRegion region = getPartition(offset);
			int partitionOffset = region.getOffset();
			int readLength = region.getLength();

			if (attribute == null)
				return null;
			String text = get(partitionOffset, readLength);

			boolean firstQuote = false;
			boolean equal = false;
			String attributeValue = "";
			String attributeName = "";
			/*System.out.println (text);
			System.out.println ("atrribute: " + attribute);*/
			for (int i = 0; i < text.length(); i++) {
				if (text.charAt(i) == '\"' || text.charAt(i) == '\'')
					if (!firstQuote)
						firstQuote = true;
					else {
						String[] str = attributeName.split(" ");
						Vector<String> v = new Vector<String>();
						for (String s : str)
							if (!s.equals(""))
								v.add(s); // caso o attributo q o cara esteja
											// procurando
						// esteja logo depois do nome da tag
						// o vector eh caso a tag tenha muitos espacos
						// Ex: <media id = "media1" esse caso tem q funcionar

						if (v.get(v.size() - 1).equals(attribute)) { // caso a
																		// tag
																		// seja
																		// assim:
																		// <media
																		// dbnsyudb
																		// id="..."
																		// ...
							return attributeValue; // o autocomplete n mostra a
													// tag acima na lista de
													// sugestao
						}
						// System.out.println ("NAO: " + attributeName + ":" +
						// attributeValue);
						attributeName = "";
						attributeValue = "";
						firstQuote = false;
						equal = true;
					}
				else {
					if (firstQuote)
						attributeValue += text.charAt(i);
					else {
						if (text.charAt(i) == '=') {
							continue;
						}
						attributeName += text.charAt(i);
					}
				}

			}

			return null;

			// Continuo achando q não resolveu o problema
			/*int startIndex = 0;
			int p = 0;
			do {
				//Não podemos fazer isso!!!
				p = text.indexOf(attribute, startIndex);
				
				if (p == -1 )
					return null;
				if (p > 0 &&  p + attribute.length() <= text.length()){
					int start = p-1;
					char begin = '\"';
					char end = '\"';
				    while (start >= 0 && text.charAt(start)==' ') start--;
					if (start >= 0)
						begin = text.charAt(start);
					
					start = p + attribute.length();
					while (start < text.length() && text.charAt(start)==' ') start++;
					if (start < text.length())
						end = text.charAt(start);
					if( begin != '\"' || end != '\"')
						break;
				}
				startIndex = p+1;
			} while (true);
			int pInicial = p;
			p += attribute.length();
			String value = "";
			boolean firstQuote = false;
			while (true) {
				p++;
				if (p >= text.length())
					return "";
				if (text.charAt(p) == '\'' || text.charAt(p) == '\"')
					if (!firstQuote) {
						firstQuote = true;
						continue;
					} else {
						tamAttr = p - pInicial + 1;
						return value;
					}
				if (firstQuote)
					value += text.charAt(p);
			}*/
		} catch (BadLocationException e) {
			return "";
		}
	}

	/**
	 * obs: Isto precisa ser melhorado
	 * 
	 * @param offset
	 * @param attr
	 * @return
	 */
	private int getAttributeSize(int offset, String attr) {
		getAttributeValueFromCurrentTagName(offset, attr);
		return tamAttr;
	}

	public String getCurrentAttribute(int offset) {
		if (isAttributeValue(offset))
			return currentAttribute;
		return "";
	}

	String currentAttribute;
	private int startAttributeValueOffset = -1;

	public boolean isAttributeValue(int offset) {
		ITypedRegion region;
		startAttributeValueOffset = -1;
		try {
			region = getPartition(offset--);
			if (region.getType() == XMLPartitionScanner.XML_START_TAG) {
				int partitionOffset = region.getOffset();
				currentAttribute = "";
				// System.out
				// .println(
				// "Verificando se está digitando o valor de um atributo");
				boolean firstQuote = true;
				boolean findingAttributeName = false;
				int beginAttributeName = -1, endAttributeName = -1;
				while (true && offset >= partitionOffset) {
					char ch = getChar(offset--);
					if (findingAttributeName) {
						if (Character.isJavaIdentifierPart(ch)) {
							if (endAttributeName == -1) {
								endAttributeName = offset;
							}
							continue;
						}
						if (Character.isWhitespace(ch)
								&& endAttributeName != -1) {
							currentAttribute = get(offset + 2, endAttributeName
									- (offset));
							return true;
						}
						if (Character.isWhitespace(ch))
							continue;
						return false;
					}
					if (ch == '\'' || ch == '\"') {
						if (firstQuote) {
							startAttributeValueOffset = offset + 1;
							firstQuote = false;
							continue;
						}
						return false;
					}
					if (ch == '=')
						findingAttributeName = true;
				}
			}
			return false;
		} catch (BadLocationException e) {
			return false;
		}
	}

	/**
	 * 
	 * @param offset
	 * @return
	 */
	public int getStartAttributeValueOffset(int offset) {
		if (isAttributeValue(offset)) {
			return startAttributeValueOffset;
		}
		return -1;
	}

	/**
	 * Utilizado para determinar se a palavra corrente que está sendo digitada é
	 * um atributo. Irá retornar verdadeiro se encontrar o padrão no âmbito da
	 * atual partição Tem que melhorar ainda. Falta verificar se é o valor de um
	 * atributo.
	 */
	public boolean isAttribute(int documentOffset) {
		ITypedRegion region;
		try {
			region = getPartition(documentOffset);
			if (region.getType() == XMLPartitionScanner.XML_START_TAG)
				return !isTagname(documentOffset)
						&& !isAttributeValue(documentOffset);
			return false;
		} catch (BadLocationException e) {
			return false;
		}
	}

	public boolean isTagname(int documentOffset) {
		boolean isTagname = false;
		try {
			ITypedRegion region = getPartition(documentOffset);
			String text = get(region.getOffset(),
					documentOffset - region.getOffset());
			char ch;
			int p = text.length() - 1;
			while (true) {
				ch = text.charAt(p--);
				if (ch == '<')
					return true;
				if (Character.isLetter(ch))
					continue;
				// System.out.println("ch = " + ch);
				return false;
			}
		} catch (BadLocationException e) {
			// TODO: handle exception
			return false;
		}
	}

	/**
	 * 
	 * @param documentOffset
	 * @return
	 */
	public boolean isEndTagName(int documentOffset) {
		ITypedRegion region;
		try {
			region = getPartition(documentOffset);
			if (region.getType() == XMLPartitionScanner.XML_END_TAG)
				return true;
			return false;
		} catch (BadLocationException e) {
			return false;
		}
	}

	/**
	 * 
	 * @param offset
	 * @return
	 * @throws BadLocationException
	 */
	public ITypedRegion getNextTagPartition(int offset)
			throws BadLocationException {
		ITypedRegion partition = getPartition(offset);
		if (partition == null
				|| partition.getType()
						.equals(XMLPartitionScanner.XML_START_TAG)) {
			return partition;
		}
		return getNextTagPartition(partition);
	}

	/**
	 * Returns the next XML_START_TAG partition with tagname after offset.
	 * 
	 * @param tagname
	 *            the tagname that must be searched
	 * @param offset
	 *            the initial offset
	 * @return the next partition with tagname
	 * @throws BadLocationException
	 */
	public ITypedRegion getNextTagPartition(String tagname, int offset)
			throws BadLocationException {
		ITypedRegion partition = getPartition(offset);
		while (partition != null) {
			if (partition.getType().equals(XMLPartitionScanner.XML_START_TAG)) {
				if (getCurrentTagname(partition.getOffset()).equals(tagname))
					return partition;
			}
			partition = getNextTagPartition(partition);

		}
		return null;

	}

	/**
	 * Gets the next partition of type BEGIN_TAG or END_TAG.
	 * 
	 * @param d
	 *            The document containing the partitions
	 * @param r
	 *            The current partition
	 * @return The partition representing the previous tag in the document, or
	 *         <code>null</code> if no such partition can be found.
	 */
	public ITypedRegion getNextTagPartition(ITypedRegion r) {
		ITypedRegion partition = getNextPartition(r);
		while (partition != null
				&& !partition.getType().equals(
						XMLPartitionScanner.XML_START_TAG)) {
			partition = getNextPartition(partition);
		}
		return partition;
	}

	public String getCurrentEndTagName(int documentOffset) {
		try {
			ITypedRegion region = getPartition(documentOffset);
			if (!region.getType().equals(XMLPartitionScanner.XML_END_TAG))
				return null;
			int partitionOffset = region.getOffset();
			int readLength = region.getLength();

			String text = get(partitionOffset + 1, readLength);
			int p = 0;
			char ch;
			String tagname = "";
			ch = text.charAt(0);
			while (true) {
				if (p + 1 >= text.length()
						|| !Character.isJavaIdentifierPart(text.charAt(p + 1)))
					break;
				ch = text.charAt(++p);
				tagname += ch;
			}
			return tagname;

		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ITypedRegion getNextEndTagPartition(String tagname, int offset) {
		ITypedRegion partition;
		try {
			partition = getPartition(offset);
			while (partition != null) {
				if (partition.getType().equals(XMLPartitionScanner.XML_END_TAG)) {
					if (getCurrentEndTagName(partition.getOffset()).equals(
							tagname))
						return partition;
				}
				partition = getNextPartition(partition);
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	/**
	 * 
	 * @param r
	 * @return
	 */
	public ITypedRegion getNextEndTagPartition(ITypedRegion r) {
		ITypedRegion partition = getNextPartition(r);
		while (partition != null
				&& !partition.getType().equals(XMLPartitionScanner.XML_END_TAG)) {
			partition = getNextPartition(partition);
		}
		return partition;
	}

	/**
	 * Returns the previous partition
	 * 
	 * @param d
	 * @param r
	 * @return
	 */
	public ITypedRegion getNextPartition(ITypedRegion r) {
		if (r == null)
			return null;
		if (r.getOffset() > getLength())
			return null;
		ITypedRegion pr = null;
		try {
			pr = getPartition(r.getLength() + r.getOffset() + 1);
		} catch (BadLocationException e) {
			// e.printStackTrace();
			return null;
		}
		return pr;
	}

	/**
	 * Set the attribute of the first element with id.
	 * 
	 * @param id
	 * @param attr
	 * @param value
	 * @return
	 */
	public boolean setAttribute(String id, String attr, String value) {
		return setAttribute(id, attr, value, 1);
	}

	public boolean setAttribute(String attr, String value, int offset) {
		try {
			ITypedRegion region = getPartition(offset);
			String startTag;

			startTag = get(region.getOffset(), region.getLength());

			String attrAtual = getAttributeValueFromCurrentTagName(offset, attr);
			int begin = 0;
			String newValue = attr + "=\"" + value + "\"";
			if (attrAtual == null) {
				begin = region.getOffset() + region.getLength() - 1;
				if (startTag.endsWith("/>"))
					begin--;
				if (!get(begin - 1, 1).equals(" "))
					newValue = " " + newValue;

				replace(begin, 0, newValue);
			} else {
				int attrOffset = getAttributePosition(attr, region.getOffset())
						- attr.length() - 1;
				int attrSizeAtual = attrAtual.length();
				begin = region.getOffset() + attrOffset;
				newValue = attr + "=\"" + value + "\"";
				if (!get(begin - 1, 1).equals(" "))
					newValue = " " + newValue;
				if (!get(begin + attrSizeAtual, 1).equals(" "))
					newValue = newValue + " ";
				replace(begin, attrSizeAtual + attr.length() + 3, newValue);
			}
			return true;
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	private int getAttributePosition(String attr, int offset) {
		try {
			ITypedRegion region;
			region = getPartition(offset);

			if (!region.getType().equals(XMLPartitionScanner.XML_START_TAG))
				return -1;

			int partitionOffset = region.getOffset();
			int readLength = region.getLength();

			if (attr == null || attr.equals(""))
				return -1;
			String tag = get(partitionOffset, readLength);

			boolean firstQuote = false;
			boolean equal = false;
			String attributeValue = "";
			String attributeName = "";

			int index = 0;
			for (int i = 0; i < tag.length(); i++) {
				if (tag.charAt(i) == '\"' || tag.charAt(i) == '\'')
					if (!firstQuote) {
						firstQuote = true;
						index = i;
					} else {
						String[] str = attributeName.split(" ");
						Vector<String> v = new Vector<String>();
						for (String s : str)
							if (!s.equals(""))
								v.add(s);

						if (v.get(v.size() - 1).equals(attr)) {
							break;
						}

						attributeName = "";
						attributeValue = "";
						firstQuote = false;
						equal = true;
					}
				else {
					if (firstQuote)
						attributeValue += tag.charAt(i);
					else {
						if (tag.charAt(i) == '=') {
							continue;
						}
						attributeName += tag.charAt(i);
					}
				}

			}
			return index;
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	public boolean removeAttribute(String attr, int offset) {
		try {
			ITypedRegion region;
			region = getPartition(offset);

			if (!region.getType().equals(XMLPartitionScanner.XML_START_TAG))
				return false;

			String tag = get(region.getOffset(), region.getLength());

			int pad = 0;
			int index = getAttributePosition(attr, offset);

			if (index == -1)
				return false;

			for (int i = index - 1; i > 0; i--, pad++) {
				if (tag.charAt(i) == attr.charAt(attr.length() - 1))
					break;
			}
			boolean firstQuote = false;
			int begin = index - pad - attr.length();
			pad = 0;
			for (int i = begin + attr.length(); i < tag.length(); i++, pad++) {
				if (tag.charAt(i) == '\"')
					if (firstQuote == false)
						firstQuote = true;
					else
						break;
			}
			begin += region.getOffset();

			int end = attr.length() + pad + 1;
			replace(begin, end, "");
			return true;

		} catch (BadLocationException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Set the attribute from next element with id
	 * 
	 * @param id
	 * @param attr
	 * @param value
	 * @param offset
	 * @return
	 */
	public boolean setAttribute(String id, String attr, String value, int offset) {
		try {
			ITypedRegion region = getNextTagPartition(offset);
			if (region == null)
				throw new BadLocationException();
			String startTag = get(region.getOffset(), region.getLength());
			String currentId = getAttributeValueFromCurrentTagName(
					region.getOffset(), "id");
			if (currentId != null) {
				if (currentId.equals(id)) {
					String attrAtual = getAttributeValueFromCurrentTagName(
							region.getOffset(), attr);
					int begin = 0;
					String newValue = attr + "=\"" + value + "\"";
					if (attrAtual == null) {
						begin = region.getOffset() + region.getLength() - 1;
						if (startTag.endsWith("/>"))
							begin--;
						if (!get(begin - 1, 1).equals(" "))
							newValue = " " + newValue;

						replace(begin, 0, newValue);
					} else {
						String text = get(region.getOffset(),
								region.getLength());
						int attrOffset = text.indexOf(attr);
						int attrSizeAtual = attrAtual.length();
						begin = region.getOffset() + attrOffset;
						newValue = attr + "=\"" + value + "\"";
						if (!get(begin - 1, 1).equals(" "))
							newValue = " " + newValue;
						if (!get(begin + attrSizeAtual, 1).equals(" "))
							newValue = newValue + " ";
						replace(begin, attrSizeAtual + attr.length() + 3,
								newValue);
					}
					return true;
				}
			}
			setAttribute(id, attr, value,
					region.getOffset() + region.getLength() + 1);
		} catch (BadLocationException e) {
			return false;
		}
		return true;
	}

	public boolean setAttributeFromTagname(String tagname, String attr,
			String value, int offset) {
		try {
			ITypedRegion region = getNextTagPartition(offset);
			if (region == null)
				throw new BadLocationException();
			String startTag = get(region.getOffset(), region.getLength());
			String currentTagname = getCurrentTagname(region.getOffset());
			if (currentTagname != null) {
				if (currentTagname.equals(tagname)) {
					String attrAtual = getAttributeValueFromCurrentTagName(
							region.getOffset(), attr);
					int begin = 0;
					String newValue = attr + "=\"" + value + "\"";
					if (attrAtual == null) {
						begin = region.getOffset() + region.getLength() - 1;
						if (startTag.endsWith("/>"))
							begin--;
						if (!get(begin - 1, 1).equals(" "))
							newValue = " " + newValue;

						replace(begin, 0, newValue);
					} else {
						String text = get(region.getOffset(),
								region.getLength());
						int attrOffset = getAttributePosition(attr, offset);
						int attrSizeAtual = getAttributeSize(offset, attr);
						begin = region.getOffset() + attrOffset;
						newValue = attr + "=\"" + value + "\"";
						if (!get(begin - 1, 1).equals(" "))
							newValue = " " + newValue;
						if (!get(begin + attrSizeAtual, 1).equals(" "))
							newValue = newValue + " ";
						replace(begin, attrSizeAtual, newValue);
					}
					return true;
				}
			}
			setAttribute(tagname, attr, value,
					region.getOffset() + region.getLength() + 1);
		} catch (BadLocationException e) {
			return false;
		}
		return true;
	}

	public List<String> getAttributesTyped(int offset) {
		List list = new ArrayList<String>();
		try {
			ITypedRegion region = getPartition(offset);
			if (region.getType() == XMLPartitionScanner.XML_START_TAG) {
				String currentPartition = get(region.getOffset(),
						region.getLength());
				Pattern p = Pattern.compile("\\s[a-zA-Z]+");
				Matcher m = p.matcher(currentPartition); // get a matcher
				// object
				while (m.find()) {
					list.add(currentPartition.substring(m.start() + 1, m.end()));
				}
			}
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public void removeElement(String id) {
		removeElement(id, 0);
	}

	public boolean removeElement(String id, int offset) {
		try {
			int elementOffset = getElementOffset(id);
			ITypedRegion region = getNextTagPartition(elementOffset);
			String tag = get(region.getOffset(), region.getLength());
			if (tag.endsWith("/>"))
				replace(region.getOffset(), region.getLength(), "");
			else {
				String tagname = getCurrentTagname(offset);
				ITypedRegion endTagRegion = getNextEndTagPartition(tagname,
						offset);
				int begin = region.getOffset();
				int end = endTagRegion.getOffset() + endTagRegion.getLength()
						- begin;
				replace(begin, end, "");
			}
			return true;
		} catch (BadLocationException e) {
			return true; // or false?
		}
	}

	public boolean removeElement(int offset) {
		try {
			ITypedRegion region = getNextTagPartition(offset);
			String tag = get(region.getOffset(), region.getLength());
			if (tag.endsWith("/>"))
				replace(region.getOffset(), region.getLength(), "");
			else {
				String tagname = getCurrentTagname(offset);
				ITypedRegion endTagRegion = getNextEndTagPartition(tagname,
						offset);
				int begin = region.getOffset();
				int end = endTagRegion.getOffset() + endTagRegion.getLength()
						- begin;
				replace(begin, end, "");
			}
			return true;
		} catch (BadLocationException e) {
			return true;
		}
	}

	/**
	 * 
	 * @param tagname
	 * @param offset
	 * @return
	 */
	public boolean addElement(String tagname, int offset) {
		try {
			replace(offset, 1, "<" + tagname + ">\n</" + tagname + ">\n");
		} catch (BadLocationException e) {
			return false;
		}
		return true;
	}

	public int getBaseOffset(String base, String id) {
		if (base == null || base.equals(""))
			return -1;
		try {
			ITypedRegion region = getPartition(0);
			region = getNextTagPartition(region);
			String tagname = getCurrentTagname(region.getOffset());

			while (!tagname.equals("/head")) {
				if (tagname.equals(base))
					if (id == null || id.equals(""))
						return region.getOffset();
					else {
						String baseId = getAttributeValueFromCurrentTagName(
								region.getOffset(), "id");
						if (baseId != null && baseId.equals(id))
							return region.getOffset();
					}

				region = getNextTagPartition(region);
				if (region == null)
					break;
				tagname = getCurrentTagname(region.getOffset());
			}

			return -1;
		} catch (BadLocationException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public boolean addElement(String tagname, String id, int elementOffset) {
		try {
			NCLStructure nclStructure = NCLStructure.getInstance();
			int fatherOffset = getFatherPartitionOffset(elementOffset);
			Map<String, Map<String, Character>> nesting = nclStructure
					.getNesting();

			Map<String, Character> headChildren = nesting.get("head");
			Iterator it = headChildren.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Character> entry = (Entry<String, Character>) it
						.next();
				String elementTagName = entry.getKey();
				Map<String, Character> elementChildren = nesting
						.get(elementTagName);
				if (elementChildren != null
						&& elementChildren.containsKey(tagname)) {
					// filho de algum elemento de <head>
					fatherOffset = getBaseOffset(elementTagName, "");
					if (fatherOffset != -1) {
						ITypedRegion region = getPartition(fatherOffset);
						String indent = getIndentLine(fatherOffset) + "\t";
						String tagStructure = computeTagStructure(tagname,
								indent, id);
						replace(region.getOffset() + region.getLength(), 0,
								"\n" + indent + tagStructure);
					} else {
						fatherOffset = getBaseOffset("head", "");
						ITypedRegion region = getPartition(fatherOffset);
						String indent = getIndentLine(fatherOffset) + "\t";
						String tagStructure = computeTagStructure(
								elementTagName, indent, "");
						replace(region.getOffset() + region.getLength(), 0,
								"\n" + indent + tagStructure);
						fatherOffset = getBaseOffset(elementTagName, "");
						region = getPartition(fatherOffset);
						indent = getIndentLine(fatherOffset) + "\t";
						tagStructure = computeTagStructure(tagname, indent, id);
						replace(region.getOffset() + region.getLength(), 0,
								"\n" + indent + tagStructure);
					}
					return true;
					// e quando tiver mais de uma base de regiões?
				}
			}

			String elementTagname = getCurrentTagname(elementOffset);

			if (elementTagname.equals("mapping")
					|| elementTagname.equals("bind")) {
				fatherOffset = getFatherPartitionOffset(getFatherPartitionOffset(elementOffset));
			}

			ITypedRegion region = getPartition(fatherOffset);
			String indent = getIndentLine(fatherOffset) + "\t";
			String tagStructure = computeTagStructure(tagname, indent, id);
			replace(region.getOffset() + region.getLength(), 0, "\n" + indent
					+ tagStructure);

			return true;

		} catch (BadLocationException e) {
			return false;
		}
	}

	public boolean addChild(String child, String attribute, String value,
			int elementOffset) {
		try {
			ITypedRegion region = getPartition(elementOffset);
			String indent = getIndentLine(elementOffset) + "\t";
			String tagStructue = computeTagStructure(child, indent, "");

			replace(region.getOffset() + region.getLength(), 0, "\n" + indent
					+ tagStructue);

			region = getNextTagPartition(region);
			String tag = get(region.getOffset(), region.getLength());
			setAttribute(attribute, value, region.getOffset());
			return true;
		} catch (BadLocationException e) {
			e.printStackTrace();
			return false;
		}
	}

	private String computeTagStructure(String tagname, String indent, String id) {
		// TODO: retornar a tag com todos os atributos obrigatórios
		NCLStructure nclStructure = NCLStructure.getInstance();
		Map<String, Boolean> atts = nclStructure.getAttributes(tagname);
		Map<String, Character> children = nclStructure
				.getChildrenCardinality(tagname);
		Iterator it = atts.entrySet().iterator();
		String attributes = "";
		while (it.hasNext()) {
			Map.Entry<String, Boolean> entry = (Entry<String, Boolean>) it
					.next();
			if (((Boolean) entry.getValue()).booleanValue()) {
				String attr = entry.getKey();
				if (!id.equals("") && attr.equals("id"))
					attributes += " " + attr + "=\"" + id + "\"";
				else
					attributes += " " + attr + "=\"\"";
			}
		}
		String ret;
		if (children.size() == 0) { // caso nao tenha filhos fecha a tag junto
			// com a start
			ret = "<" + tagname + attributes + "/>" + "\r\n" + indent;
		} else {
			ret = "<" + tagname + attributes + ">" + "\r\n" + indent + "\t";
			if (tagname.equals("context")) {
				ret += "<port id=\"\" component=\"\" />";
			} else if (tagname.equals("causalConnector")) {
				ret += "<simpleCondition role=\"\" />" + "\n" + indent + "\t"
						+ "<simpleAction role=\"\" />";
			}
			ret += "\r\n" + indent + "</" + tagname + ">";
		}
		return ret;
	}

	/**
	 * This method return true if the document has at least one element with
	 * identifier equal to id.
	 * 
	 * @param id
	 *            the id that must be searched
	 * @return true if has at least one element with identifier id
	 */
	public boolean hasElementWithId(String id) {
		return hasElementWithId(id, 1);
	}

	/**
	 * This method returns true if the document has at least one element with
	 * identifier equal to id after the position offset.
	 * 
	 * @param id
	 * @param offset
	 *            the initial offset where the searches begins
	 * @return
	 */
	public boolean hasElementWithId(String id, int offset) {
		return hasElementWithAttribute("id", id, offset);
	}

	/**
	 * This method returns true if the document has at least one element with
	 * identifier equal to id after the position offset.
	 * 
	 * @param attr
	 * @param value
	 * @param offset
	 * @return
	 */
	public boolean hasElementWithAttribute(String attr, String value, int offset) {
		return getElementOffset(attr, value, offset) != -1;
	}

	/**
	 * This method return the position of the first element that has the
	 * attribute attr with value = value starting from offset
	 * 
	 * @param attr
	 * @param value
	 * @param offset
	 * @return
	 */
	public int getElementOffset(String attr, String value, int offset) {
		try {
			ITypedRegion region = getNextTagPartition(offset);
			if (region == null)
				throw new BadLocationException();
			String startTag = get(region.getOffset(), region.getLength());
			String currentAttr = getAttributeValueFromCurrentTagName(
					region.getOffset(), attr);
			if (currentAttr != null) {
				if (currentAttr.equals(value)) {
					return region.getOffset();
				}
			}
			return getElementOffset(attr, value,
					region.getOffset() + region.getLength() + 1);
		} catch (BadLocationException e) {
			return -1;
		}
	}

	/**
	 * This method return all identifies of the elements of a certain type
	 * 
	 * @param type
	 */

	public ArrayList<String> getAllElementsOfType(String type) {
		try {
			if (type.equals(""))
				return null;

				ArrayList<String> ids = new ArrayList<String>();
				ITypedRegion region = getPartition(0);
				String partition;
				do {
					partition = get(region.getOffset(), region.getLength());
					String tagName;
					if (region.getType().equals(
							XMLPartitionScanner.XML_START_TAG)) {
						int offset = region.getOffset();
						tagName = getCurrentTagname(offset);
						if (tagName != null && !tagName.equals("")
								&& tagName.equals(type)) {
							String id = getAttributeValueFromCurrentTagName(
									offset, "id");
							if (id != null && !id.equals(""))
								ids.add(id);
						}
					}
					region = getNextPartition(region);
				} while (!partition.equals("</ncl>"));
				return ids;
		} catch (BadLocationException e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public int getElementOffset(String id) {
		try {
			return getElementOffset(id, 1);
		} catch (BadLocationException e) {
			return -1;
		}
	}

	/**
	 * Return the offset of the element with identificator equals to id
	 * 
	 * @param id
	 * @param offset
	 * @return
	 * @throws BadLocationException
	 */
	public int getElementOffset(String id, int offset)
			throws BadLocationException {
		ITypedRegion region = getNextTagPartition(offset);
		if (region == null)
			throw new BadLocationException();
		String currentId = getAttributeValueFromCurrentTagName(
				region.getOffset(), "id");
		if (currentId != null)
			if (currentId.equals(id))
				return offset;
		return getElementOffset(id, region.getOffset() + region.getLength() + 1);
	}

	/**
	 * Create a new SourceDocument from a IDocument object. The changes in the
	 * SourceDocument will not be made in the original IDocument. Use this
	 * function just to read the content.
	 * 
	 * @param doc
	 * @return
	 */
	public static NCLSourceDocument createNCLSourceDocumentFromIDocument(
			IDocument doc) {
		if (doc instanceof NCLSourceDocument)
			return (NCLSourceDocument) doc;
		NCLSourceDocument document = new NCLSourceDocument();
		document.set(doc.get());
		IDocumentPartitioner partitioner = new XMLPartitioner(
				new XMLPartitionScanner(), new String[] {
						XMLPartitionScanner.XML_START_TAG,
						XMLPartitionScanner.XML_PI,
						XMLPartitionScanner.XML_DOCTYPE,
						XMLPartitionScanner.XML_END_TAG,
						XMLPartitionScanner.XML_TEXT,
						XMLPartitionScanner.XML_CDATA,
						XMLPartitionScanner.XML_COMMENT });
		partitioner.connect(document);
		document.setDocumentPartitioner(partitioner);
		return document;
	}

	/**
	 * Return the list of parents, grandparents, etc. of the element in the
	 * offset.
	 * 
	 * @param offset
	 * @return
	 */
	public List<String> getParentList(int offset) {
		List<String> parents = new ArrayList<String>();
		while (true) {
			int parentOffset = getFatherPartitionOffset(offset);
			if (parentOffset < 0)
				break;
			parents.add(getAttributeValueFromCurrentTagName(parentOffset, "id"));
			offset = parentOffset;
		}
		return parents;
	}

	/**
	 * Retorna o offset da regiao que possui a tag com o id passado como
	 * parametro
	 * 
	 * @return offset -1 se o id nao for valido
	 * 
	 */
	public int getOffsetByID(String id) {

		return getOffsetByValue("id", id);
	}

	public int getOffsetByValue(String attribute, String value) {
		try {
			if (attribute == null || value == null)
				return -1;
			ITypedRegion region = getPartition(0);
			String t;
			do {
				t = get(region.getOffset(), region.getLength());
				String tagId;
				if (region.getType().equals(XMLPartitionScanner.XML_START_TAG)) {
					tagId = getAttributeValueFromCurrentTagName(
							region.getOffset(), attribute);
					if (tagId != null && !tagId.equals(""))
						if (tagId.equals(value))
							return region.getOffset();
				}
				region = getNextPartition(region);
			} while (!t.equals("</ncl>"));
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Return all the offsets of the tags containing the attribute
	 * 
	 * @param attribute
	 * @return
	 */
	public Vector<Integer> getAllTagsWithAttribute(String attribute) {
		Vector<Integer> aliasOffset = new Vector<Integer>();
		try {
			ITypedRegion region = getPartition(0);
			String t;

			do {
				t = get(region.getOffset(), region.getLength());
				String att;
				if (region.getType().equals(XMLPartitionScanner.XML_START_TAG)) {
					att = getAttributeValueFromCurrentTagName(
							region.getOffset(), attribute);
					if (att != null && !att.equals(""))
						aliasOffset.add(region.getOffset());
				}
				region = getNextPartition(region);
			} while (!t.equals("</ncl>") && region != null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return aliasOffset;
		}

		return aliasOffset;
	}

	/**
	 * @param id
	 * @param string
	 * @param path
	 */
	public void setAttributefromTag(String id, String attr, String path,
			int offset) {
		// TODO Auto-generated method stub
		try {

			if (offset == -1)
				return;
			ITypedRegion region;

			region = getPartition(offset);
			int off = region.getOffset();

			String text = get(region.getOffset(), region.getLength());
			boolean firstQuote = false;
			String attributeName = "";
			String attributeValue = "";
			for (int i = 0; i < text.length(); i++) {
				if (text.charAt(i) == '\"' || text.charAt(i) == '\'')
					if (!firstQuote)
						firstQuote = true;
					else {
						String[] str = attributeName.split(" ");
						Vector<String> v = new Vector<String>();
						for (String s : str)
							if (!s.equals(""))
								v.add(s);

						if (v.get(v.size() - 1).equals(attr)) {
							replace(off + i - attributeValue.length(),
									attributeValue.length(), path);
							return;
						}
						attributeName = "";
						attributeValue = "";
						firstQuote = false;
					}
				else {
					if (firstQuote)
						attributeValue += text.charAt(i);
					else {
						if (text.charAt(i) == '=') {
							continue;
						}
						attributeName += text.charAt(i);
					}
				}

			}
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Retorna uma string com o número de tabulação da linha atual. Útil para
	 * colocar o final de tag alinhado com o inicial
	 * 
	 * @param doc
	 * @param offset
	 * @return
	 */
	public String getIndentLine(int offset) {
		int ident = 0;
		int space = 0;
		while (true) {
			try {
				char c = getChar(--offset);
				// System.out.println("Character = " + c + " ident = " +ident);
				if (c == '\n')
					break;
				if (c == '\t')
					++ident;
				else if (c == ' ')
					space++;
				else {
					ident = 0;
					space = 0;
				}
			} catch (BadLocationException e) {
				ident = 0;
				space = 0;
				break;
			}
		}
		String str = "";
		for (int i = 0; i < ident; i++)
			str += "\t";
		for (int i = 0; i < space; i++)
			str += " ";
		return str;
	}

	/**
	 * 
	 */
	public void correctNCLStructure() {
		try {
			ITypedRegion region = getPartition(0);
			Stack<StackElement> stack = new Stack();

			String tagname = getCurrentTagname(region.getOffset());
			int lastOffset = 0;

			do {
				if (region.getType().equals(XMLPartitionScanner.XML_START_TAG)) {
					String tag = get(region.getOffset(), region.getLength());
					if (!tag.endsWith("/>")) {
						StackElement element = new StackElement();
						element.element = new String(tagname);
						element.offset = region.getOffset();
						stack.push(element);
					}
				} else if (region.getType().equals(
						XMLPartitionScanner.XML_END_TAG)) {
					if (stack.empty())
						replace(region.getOffset(), region.getLength(), "");
					else {
						StackElement top = stack.pop();
						if (!top.element.equals(tagname.substring(1))) {
							addEndtag(top.offset);
							if (!stack.empty()) {
								top = stack.peek();
								region = getPartition(top.offset);
							}
						}
					}
				}
				lastOffset = region.getOffset() + region.getLength();
				do {
					region = getNextPartition(region);
					if (region == null)
						break;
					tagname = getTagname(region.getOffset());
				} while (tagname.equals(""));

				if (region == null)
					break;

			} while (!tagname.equals("/ncl"));

			while (stack.size() > 0) {
				StackElement element = stack.pop();
				if (!element.element.equals("ncl"))
					addEndtag(element.offset);
			}

			ITypedRegion lastRegion = getPartition(get().length() - 1);
			while (!lastRegion.getType()
					.equals(XMLPartitionScanner.XML_END_TAG)) {
				lastRegion = getPreviousPartition(lastRegion);
			}

			String tag = get(lastRegion.getOffset(), lastRegion.getLength());
			if (!tag.equals("</ncl>"))
				replace(lastRegion.getOffset() + lastRegion.getLength(), 0,
						"\n</ncl>");

		} catch (BadLocationException e) {
			e.printStackTrace();
		}

	}

	public boolean addEndtag(int offset) {
		try {
			ITypedRegion region = getPartition(offset);
			String tagname = getTagname(region.getOffset());
			String indent = getIndentLine(region.getOffset());
			NCLStructure nclStructure = NCLStructure.getInstance();
			Map<String, Map<String, Character>> nesting = nclStructure
					.getNesting();

			Map<String, Character> children = nesting.get(tagname);
			if (children == null) {
				replace(region.getOffset() + region.getLength() - 1, 0, "/");
				return true;
			}

			boolean hasChild = false;

			while (!tagname.equals("/ncl")) {
				String childTagname;
				int lastOffset = region.getOffset() + region.getLength();
				do {
					region = getNextPartition(region);
					childTagname = getTagname(region.getOffset());
				} while (childTagname.equals(""));

				String tag = get(region.getOffset(), region.getLength());

				if (region.getType().equals(XMLPartitionScanner.XML_START_TAG))
					if (!children.containsKey(childTagname)) {
						if (hasChild)
							replace(lastOffset, 0, "\n" + indent + "</"
									+ tagname + ">");
						else
							replace(lastOffset - 1, 0, "/");
						return true;
					} else {
						if (!tag.endsWith("/>"))
							region = getEndTagPartition(region);
						hasChild = true;
						continue;
					}
				else if (region.getType().equals(
						XMLPartitionScanner.XML_END_TAG)) {
					replace(lastOffset, 0, "\n" + indent + "</" + tagname + ">");
					return true;
				}
			}
			return false;
		} catch (BadLocationException e) {
			e.printStackTrace();
			return false;
		}

	}

	public ITypedRegion getEndTagPartition(ITypedRegion region) {
		try {
			if (!region.getType().equals(XMLPartitionScanner.XML_START_TAG)
					|| region == null)
				return null;
			String tag;

			tag = get(region.getOffset(), region.getLength());

			if (tag.endsWith("/>"))
				return null;

			String tagname = getTagname(region.getOffset());
			int stack = 0;
			do {
				String childTagname;
				do {
					region = getNextPartition(region);
					childTagname = getTagname(region.getOffset());
				} while (childTagname.equals(""));

				tag = get(region.getOffset(), region.getLength());

				if (region.getType().equals(XMLPartitionScanner.XML_START_TAG)) {
					if (tag.endsWith("/>"))
						continue;
					else if (childTagname.equals(tagname))
						stack++;
				} else if (region.getType().equals(
						XMLPartitionScanner.XML_END_TAG)) {
					if (tagname.equals(childTagname.substring(1)))
						if (stack == 0)
							return region;
						else
							stack--;
				}
			} while (!tagname.equals("/ncl"));

			return null;
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param offset
	 * @return
	 */
	private String getTagname(int offset) {
		ITypedRegion region;
		try {
			region = getPartition(offset);

			if (region.getType().equals(XMLPartitionScanner.XML_START_TAG))
				return getCurrentTagname(region.getOffset());
			if (region.getType().equals(XMLPartitionScanner.XML_END_TAG))
				return "/" + getCurrentEndTagName(region.getOffset());
			return "";
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	class StackElement {
		String element;
		int offset;
	}

}