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
package br.ufma.deinf.laws.ncleclipse.document;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.ITypedRegion;

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
			e.printStackTrace();
		}
		return -1;
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
			System.out.println(text);
			do { // procura a tag pai
				text = get(region.getOffset(), region.getLength());
				System.out.println(text);
				if (region.getType().equals(XMLPartitionScanner.XML_END_TAG))
					pilha.push(new Integer(1));
				else if (region.getType().equals(
						XMLPartitionScanner.XML_START_TAG)
						&& !text.endsWith("/>")) {
					System.out.println(pilha.size());
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

			String text = get(partitionOffset, readLength);
			int p = text.indexOf(attribute);
			if (p == -1)
				return null;
			int pInicial = p;
			p += attribute.length();
			String value = "";
			boolean firstQuote = false;
			while (true) {
				p++;
				if (p > text.length())
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
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return "";
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
	 * Utilizado para determinar se a palavra corrente que está sendo digitada
	 * é um atributo. Irá retornar verdadeiro se encontrar o padrão no
	 * âmbito da atual partição Tem que melhorar ainda. Falta verificar se é
	 * o valor de um atributo.
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
			String text = get(region.getOffset(), documentOffset
					- region.getOffset());
			char ch;
			int p = text.length() - 1;
			while (true) {
				ch = text.charAt(p--);
				if (ch == '<')
					return true;
				if (Character.isLetter(ch))
					continue;
				System.out.println("ch = " + ch);
				return false;
			}
		} catch (BadLocationException e) {
			// TODO: handle exception
			return false;
		}
	}

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

	/***************************************************************************
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

	/**
	 * Retorna a partição anterior
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
			e.printStackTrace();
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
			String currentId = getAttributeValueFromCurrentTagName(region
					.getOffset(), "id");
			if (currentId != null) {
				if (currentId.equals(id)) {
					String attrAtual = getAttributeValueFromCurrentTagName(
							region.getOffset(), attr);
					int begin = 0;
					String newValue = attr + "=\"" + value + "\"";
					if (attrAtual == null) {
						begin = region.getOffset() + region.getLength() - 2;
						if (startTag.endsWith("/>"))
							begin--;
						if (!get(begin - 1, 1).equals(" "))
							newValue = " " + newValue;

						replace(begin, 0, newValue);
					} else {
						String text = get(region.getOffset(), region
								.getLength());
						int attrOffset = text.indexOf(attr);
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
			setAttribute(id, attr, value, region.getOffset()
					+ region.getLength() + 1);
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
				String currentPartition = get(region.getOffset(), region
						.getLength());
				Pattern p = Pattern.compile("\\s[a-zA-Z]+");
				Matcher m = p.matcher(currentPartition); // get a matcher
				// object
				while (m.find()) {
					list
							.add(currentPartition.substring(m.start() + 1, m
									.end()));
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
			int elementOffset = getElementOffset(id, offset);
			ITypedRegion region = getPartition(offset);
			replace(region.getOffset(), region.getLength(), "");
			// faltando tratar o caso de ser aninhado!
			return true;
		} catch (BadLocationException e) {
			return true; // or false?
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
		String startTag = get(region.getOffset(), region.getLength());
		String currentId = getAttributeValueFromCurrentTagName(region
				.getOffset(), "id");
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
}
