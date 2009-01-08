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

package br.ufma.deinf.laws.ncleditor.editor.contentassist;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.part.FileEditorInput;

import br.ufma.deinf.laws.ncl.AttributeValues;
import br.ufma.deinf.laws.ncl.NCLReference;
import br.ufma.deinf.laws.ncl.NCLStructure;
import br.ufma.deinf.laws.ncl.help.NCLHelper;
import br.ufma.deinf.laws.ncleclipse.NCLMultiPageEditor;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLContentHandler;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLDocument;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLElement;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLParser;
import br.ufma.deinf.laws.ncleclipse.scanners.XMLTagScanner;

/**
 * Implementa o ContentAssist
 * 
 * @author roberto
 * 
 */
public class NCLCompletionProposal implements IContentAssistProcessor {
	private XMLTagScanner scanner;
	private IFile currentFile;
	private String text;
	private String protocols[] = {"file:///", "http://", "rtsp://", "rtp://", "sbtvd-ts://"};
	private boolean isAttributeValue;
	private boolean isAttribute;
	private boolean isEndTagName;
	/**
	 * Responsável por computar os valores que aparecerão na lista de
	 * sugestões. Retorna uma lista de ICompletionProposal
	 */
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int offset) {

		// get the active IFile
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();
		IEditorPart editor = page.getActiveEditor();
		currentFile = ((FileEditorInput) editor.getEditorInput()).getFile();

		List propList = new ArrayList();
		IDocument doc = viewer.getDocument();
		text = doc.get();
		NCLSourceDocument nclDoc = (NCLSourceDocument)doc;
		isAttributeValue = nclDoc.isAttributeValue(offset);
		isAttribute = nclDoc.isAttribute(offset);
		isEndTagName = nclDoc.isEndTagName(offset);
		Point selectedRange = viewer.getSelectedRange();
		if (selectedRange.y > 0) {
			// TODO:
		} else {
			System.out.println("Attributo = " + isAttribute);
			String qualifier = getQualifier(doc, offset);
			if (isEndTagName) {
				computeEndTagName(doc, qualifier, offset, propList);
			} else if (isAttributeValue) {
				computeAttributesValuesProposals(doc, qualifier, offset,
						propList);
			} else if (!isAttribute) {
				computeTagsProposals(doc, qualifier, offset, propList);
			} else if (!nclDoc.isTagname(offset))
				computeAttributesProposals(doc, qualifier, offset, propList);
		}
		ICompletionProposal[] proposals = new ICompletionProposal[propList
				.size()];
		propList.toArray(proposals);
		return proposals;
	}

	private void computeEndTagName(IDocument doc, String qualifier, int offset,
			List propList) {
		int qlen = qualifier.length();
		NCLSourceDocument nclDoc = (NCLSourceDocument) doc;
		int fatherOffset = nclDoc.getFatherPartitionOffsetFromEndTag(offset);
		String tagname = nclDoc.getCurrentTagname(fatherOffset);
		
		String prop = "</"+tagname+">";
		
		CompletionProposal proposal = new CompletionProposal(prop,
				offset - qlen, qlen, cursor, null, prop, null, null);
		propList.add(proposal);
		return;

	}

	/**
	 * Computa as tags que serão propostas para o usuário
	 * 
	 * @param qualifier
	 * @param offset
	 * @param propList
	 */
	private int cursor; // calcula a posição que o cursor ficará para cada
						// estrutura proposta

	private void computeTagsProposals(IDocument doc, String qualifier,
			int offset, List propList) {
		int qlen = qualifier.length();
		NCLStructure nclStructure = NCLStructure.getInstance();
		String indent = getIndentLine(doc, offset);
		NCLSourceDocument nclDoc = (NCLSourceDocument) doc;

		// fazer um filtro para buscar apenas as tags filhas da getFatherTagname
		System.out.println("## Log: Pai da tag onde estou digitando : "
				+ nclDoc.getFatherTagName(offset));
		Map<String, Map<String, Character>> nesting = nclStructure.getNesting();
		Vector<String> childrenStr = new Vector<String>();
		// Procuro todos os filhos da tagname do meu pai e coloco no vector
		// childrenStr

		String fatherTagName = nclDoc.getFatherTagName(offset);
		if (fatherTagName.equals("")) {
			String tagname = "ncl";
			String tagname2 = "<" + tagname;
			if (tagname.startsWith(qualifier) || tagname2.startsWith(qualifier)) {
				String text = computeTagStructure(tagname, indent);

				//get a help info to user
				String helpInfo = NCLHelper.getNCLHelper().getHelpDescription(tagname);
				//String helpInfo = "help";
				//String helpInfo = "help";
				
				CompletionProposal proposal = new CompletionProposal(text,
						offset - qlen, qlen, cursor, null, tagname, null, helpInfo);
				propList.add(proposal);
			}

		} else if (nesting.containsKey(fatherTagName)) {
			Map<String, Character> children = nesting.get(fatherTagName);
			Iterator it = children.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Character> entry = (Entry<String, Character>) it
						.next();
				String tagname = entry.getKey();
				String tagname2 = "<" + tagname;
				if (tagname.startsWith(qualifier)
						|| tagname2.startsWith(qualifier)) {
					String text = computeTagStructure(tagname, indent);

					//get a help information to user
					String helpInfo = NCLHelper.getNCLHelper().getHelpDescription(tagname);
					//String helpInfo = "help";
					//String helpInfo="help";
					
					CompletionProposal proposal = new CompletionProposal(text,
							offset - qlen, qlen, cursor, null, tagname, null,
							helpInfo);
					propList.add(proposal);
				}
			}
			return;
		}

		/*
		 * N�o faz sentido computar tudo nunca! //Caso contrário computa tudo
		 * Map<String, Map<String, Boolean>> atts =
		 * nclStructure.getAttributes(); Iterator it =
		 * atts.entrySet().iterator(); while(it.hasNext()){ Map.Entry<String,
		 * Map<String, Boolean>> entry = (Entry<String, Map<String,
		 * Boolean>>) it.next(); String tagname = entry.getKey(); String
		 * tagname2 = "<"+tagname; if(tagname.startsWith(qualifier) ||
		 * tagname2.startsWith(qualifier)){ String text =
		 * computeTagStructure(tagname, indent);
		 * 
		 * CompletionProposal proposal = new CompletionProposal(text, offset -
		 * qlen, qlen, cursor, null, tagname, null, null);
		 * propList.add(proposal); } }
		 */
	}


	/**
	 * Computa a estrutura da Tag Valores Defaults, atributos obrigatórios,
	 * coloca tudo de uma vez quando o usuário quer inserir a tag.
	 * 
	 * @param tagname
	 * @return
	 */
	String computeTagStructure(String tagname, String indent) {
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
				attributes += " " + entry.getKey() + "=\"\"";
			}
		}
		String ret;
		if (children.size() == 0) { // caso nao tenha filhos fecha a tag junto
									// com a start
			ret = "<" + tagname + attributes + "/>" + "\r\n" + indent;
			cursor = ret.length();
		} else {
			ret = "<" + tagname + attributes + ">" + "\r\n" + indent + "\t";
			cursor = ret.length();
			ret += "\r\n" + indent + "</" + tagname + ">";
		}
		return ret;
	}

	/**
	 * Computa os valores dos atributos que serão proposto pro usuário
	 * 
	 * @param qualifier
	 * @param offset
	 * @param propList
	 */
	private void computeAttributesValuesProposals(IDocument doc,
			String qualifier, int offset, List propList) {
		int qlen = qualifier.length();
		// Verificar se existe valor pré-definido
		NCLSourceDocument nclDoc = (NCLSourceDocument) doc;
		String tagname = nclDoc.getCurrentTagname(offset);
		String attribute = nclDoc.getCurrentAttribute(offset);
		System.out.println("tag: " + tagname + " attr:" + attribute);
		NCLStructure nclStructure = NCLStructure.getInstance();
		Vector<String> prop = AttributeValues.getValues(nclStructure
				.getDataType(tagname, attribute));
		if (prop.size() > 0) {
			for (int i = 0; i < prop.size(); i++) {
				if (prop.get(i).startsWith(qualifier)) {
					String text = prop.get(i);
					String texttoshow = text;
					System.out.println(text);
					// if(!qualifier.startsWith("\"") &&
					// !qualifier.startsWith("\'"))
					// text = "\""+text+"\"";
					cursor = text.length();
					CompletionProposal proposal = new CompletionProposal(text,
							offset - qlen, qlen, cursor, null, texttoshow,
							null, null);
					propList.add(proposal);
				}
			}
			return;
		}
		// Propõe elementos que referenciam tipos simples
		String nclText = doc.get();
		NCLContentHandler nclContentHandler = new NCLContentHandler();
		NCLDocument nclDocument = new NCLDocument();
		nclDocument.setParentURI(((NCLMultiPageEditor) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor())
				.getNCLEditor().getInputFile().getParent().getLocationURI());
		nclContentHandler.setNclDocument(nclDocument);
		NCLParser parser = new NCLParser();
		parser.setContentHandler(nclContentHandler);
		parser.doParse(nclText);

		// Referencias que precisam de contexto
		String perspective = null;
		// TODO: caso o id esteja definido no ncl, aqui temos um problema
		// Contexto � o pai
		if ((tagname.equals("port") && attribute.equals("component"))
				|| (tagname.equals("bindRule") && attribute.equals("constituent"))
				|| (tagname.equals("defaultComponent") && attribute.equals("component"))) {

			String fatherTagName = nclDoc.getFatherTagName(offset);
			try {
				perspective = nclDoc.getAttributeValueFromCurrentTagName(
						nclDoc.getFatherPartitionOffset(offset), "id");
			} catch (Exception e) {
				if (fatherTagName.equals("body")) {
					try {
						perspective = nclDoc.getAttributeValueFromCurrentTagName(
								nclDoc.getFatherPartitionOffset(
										nclDoc.getFatherPartitionOffset(offset)),
								"id");
					} catch (Exception e1) {
						MessageDialog
								.openError(
										Workbench.getInstance()
												.getActiveWorkbenchWindow()
												.getShell(),
										"Erro",
										"Elemento <ncl> ou <body> deve possuir um id para o funcionamento correto do Autocomplete!");
					}
				} else {
					MessageDialog
							.openError(
									Workbench.getInstance()
											.getActiveWorkbenchWindow()
											.getShell(),
									"Erro",
									"Elemento <"
											+ fatherTagName
											+ "> deve possuir um id para o funcionamento correto do Autocomplete!");
				}
			}
		}

		// Contexto � o pai do pai
		if ((tagname.equals("bind") && attribute.equals("component"))
				|| (tagname.equals("mapping") && attribute.equals("component"))) {

			String grandFatherTagName = nclDoc.getFatherTagName(nclDoc.getFatherPartitionOffset(offset));
			try {
				perspective = nclDoc.getAttributeValueFromCurrentTagName(
						nclDoc.getFatherPartitionOffset(nclDoc.getFatherPartitionOffset(offset)), "id");
			} catch (Exception e) {
				if (grandFatherTagName.equals("body")) {
					perspective = nclDoc.getAttributeValueFromCurrentTagName(
							nclDoc.getFatherPartitionOffset(
									nclDoc.getFatherPartitionOffset(
											nclDoc.getFatherPartitionOffset(
													offset))), "id");
				}
			}
		}

		if (tagname.equals("bind") && attribute.equals("role")
				|| (tagname.equals("linkParam") && (attribute.equals("name")))) {
			perspective = nclDoc.getAttributeValueFromCurrentTagName(
					nclDoc.getFatherPartitionOffset(offset), "xconnector");
		}

		if (tagname.equals("bindParam") && attribute.equals("name")) {
			perspective = nclDoc.getAttributeValueFromCurrentTagName(
					nclDoc.getFatherPartitionOffset(nclDoc.getFatherPartitionOffset(offset)), "xconnector");
		}

		if (tagname.equals("bind") && attribute.equals("interface")
				|| tagname.equals("port") && attribute.equals("interface")
				|| tagname.equals("mapping") && attribute.equals("interface")) {
			NCLElement element;
			perspective = nclDoc.getAttributeValueFromCurrentTagName(offset,
					"component");
			element = nclDocument.getElementById(perspective);
			while (element != null
					&& element.getAttributes().get("refer") != null) {
				Collection nclReference = nclStructure.getNCLReference(tagname,
						attribute);
				//Computa os valores de atributos dos elementos filhos do refer
				
				//Refatorar este código... Isto está repetindo o que está sendo feito lá embaixo
				String perspectivetmp = element.getAttributeValue("refer");
				element = nclDocument.getElementById(perspectivetmp);
				if (nclReference == null)
					return;
				Iterator it = nclReference.iterator();
				while (it.hasNext()) {
					NCLReference nclRefAtual = (NCLReference) it.next();
					Collection elements = nclDocument.getElementsFromPerspective(
							nclRefAtual.getRefTagname(), perspectivetmp);
					if (elements == null)
						continue;
					Iterator it2 = elements.iterator();
					while (it2.hasNext()) {
						text = ((NCLElement) it2.next()).getAttributeValue(nclRefAtual
								.getRefAttribute());
						if (text == null)
							continue;

						// refer n�o pode sugerir a pr�pria media, switch, etc.
						if (attribute.equals("refer")) {
							String idAtual = nclDoc.getAttributeValueFromCurrentTagName(offset, "id");
							if (idAtual != null)
								if (text.equals(idAtual))
									continue;
						}

						if (text.startsWith(qualifier)) {
							cursor = text.length();
							System.out.println("Attribute Value Proposal = " + text);
							CompletionProposal proposal = new CompletionProposal(text,
									offset - qlen, qlen, cursor, null, text, null, null);

							propList.add(proposal);
						}
					}
				}
			}
		}

		if ((tagname.equals("media") && attribute.equals("src"))
			|| (tagname.equals("importBase") && attribute.equals("documentURI"))) {
			//suggest the protocols
			for (int i = 0; i < protocols.length; i++) {
				text = protocols[i];
				if (text.startsWith(qualifier)) {
					cursor = text.length();
					System.out.println("Attribute Value Proposal = "
							+ text);
						CompletionProposal proposal = new CompletionProposal(
							text, offset - qlen, qlen, cursor, null,
							text, null, null);
						propList.add(proposal);
					}
				}
			File file = null;
			file = new File(currentFile.getLocationURI());
			file = new File(file.getParentFile().toString());
			try {
				URIProposer fs = new URIProposer(currentFile.getParent().getLocationURI().toString());
				Vector <String> v = fs.getDirectories(qualifier);
				for(int i = 0; i < v.size(); i++){
						if(v.get(i).startsWith(qualifier)){
							cursor = v.get(i).length();
							CompletionProposal proposal = new CompletionProposal(
								v.get(i), offset - qlen, qlen, cursor, null,
								v.get(i), null, null);
							propList.add(proposal);
						}
				}
				fs = new URIProposer(currentFile.getParent().getLocationURI().toString());
				v = fs.getFiles(qualifier);
				for(int i = 0; i < v.size(); i++){
						if(v.get(i).startsWith(qualifier)){
							cursor = v.get(i).length();
							CompletionProposal proposal = new CompletionProposal(
								v.get(i), offset - qlen, qlen, cursor, null,
								v.get(i), null, null);
							propList.add(proposal);
						}
				}
				return;
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("perspective = " + perspective);
		Collection nclReference = nclStructure.getNCLReference(tagname,
				attribute);
		if (nclReference == null)
			return;
		Iterator it = nclReference.iterator();
		while (it.hasNext()) {
			NCLReference nclRefAtual = (NCLReference) it.next();
			Collection elements = nclDocument.getElementsFromPerspective(
					nclRefAtual.getRefTagname(), perspective);
			if (elements == null)
				continue;
			Iterator it2 = elements.iterator();
			while (it2.hasNext()) {
				text = ((NCLElement) it2.next()).getAttributeValue(nclRefAtual
						.getRefAttribute());
				if (text == null)
					continue;

				// refer n�o pode sugerir a pr�pria media, switch, etc.
				if (attribute.equals("refer")) {
					String idAtual = nclDoc.getAttributeValueFromCurrentTagName(offset, "id");
					if (idAtual != null)
						if (text.equals(idAtual))
							continue;
				}

				if (text.startsWith(qualifier)) {
					cursor = text.length();
					System.out.println("Attribute Value Proposal = " + text);
					CompletionProposal proposal = new CompletionProposal(text,
							offset - qlen, qlen, cursor, null, text, null, null);

					propList.add(proposal);
				}
			}
		}

		// Referencias Globais (ou seja, n�o precisa de contexto)
		it = nclReference.iterator();
		if (perspective == null) {
			it = nclReference.iterator();
			while (it.hasNext()) {
				NCLReference nclRefAtual = (NCLReference) it.next();
				Collection elements = nclDocument.getElements().get(
						nclRefAtual.getRefTagname());
				if (elements == null)
					continue;
				Iterator it2 = elements.iterator();
				while (it2.hasNext()) {
					text = ((NCLElement) it2.next())
							.getAttributeValue(nclRefAtual.getRefAttribute());
					System.out.println(text);
					// refer n�o pode sugerir a pr�pria media, switch, etc.
					if (attribute.equals("refer")) {
						String idAtual = nclDoc.getAttributeValueFromCurrentTagName(offset, "id");
						if (idAtual != null)
							if (text.equals(idAtual))
								continue;
					}
					if (text.startsWith(qualifier)) {
						cursor = text.length();
						System.out
								.println("Attribute Value Proposal = " + text);
						CompletionProposal proposal = new CompletionProposal(
								text, offset - qlen, qlen, cursor, null, text,
								null, null);

						propList.add(proposal);
					}
				}
			}
		}

	}

	/**
	 * 
	 * @param doc
	 * @param qualifier
	 * @param offset
	 * @param propList
	 */
	private void computeAttributesProposals(IDocument doc, String qualifier,
			int offset, List propList) {
		int qlen = qualifier.length();
		NCLSourceDocument nclDoc = (NCLSourceDocument) doc;
		System.out.println("Computing Attributes proposals...");
		String currentTagname = nclDoc.getCurrentTagname(offset);
		System.out.println("Current Tag Name = " + currentTagname);

		List<String> attributeTyped = nclDoc.getAttributesTyped(offset);

		NCLStructure nclStructure = NCLStructure.getInstance();
		Map<String, Boolean> atts = nclStructure.getAttributes(currentTagname);

		Iterator it = atts.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Boolean> entry = (Entry<String, Boolean>) it
					.next();
			String view = entry.getKey();
			if (attributeTyped.contains(view) || view == null)
				continue;
			String prop = entry.getKey() + "=\"\"";
			if (prop.startsWith(qualifier)) {
				cursor = prop.length();

				String helpInfo = NCLHelper.getNCLHelper().getHelpDescription(currentTagname, view);
				//String helpInfo = "help";
				//String helpInfo = "help";

				CompletionProposal proposal = new CompletionProposal(prop,
						offset - qlen, qlen, cursor, null, view, null, helpInfo);

				propList.add(proposal);
			}
		}
	}



	/**
	 * Retorna o qualificador, ou seja, o que o usuário já digitou, utilizado
	 * para filtrar a lista a medida que o usuário vai adicionando texto.
	 * 
	 * @param doc
	 * @param offset
	 * @return
	 */
	private String getQualifier(IDocument doc, int offset) {
		// TODO Auto-generated method stub
		StringBuffer buf = new StringBuffer();
		if(isAttributeValue){
			while (true) {
				try {
					char c = doc.getChar(--offset);
					if (c == '\"' || c == '\'')
						return buf.reverse().toString();
					 else
						buf.append(c);
				} catch (BadLocationException e) {
					return "";
				}
			}
		}
		else{
			while (true) {
				try {
					char c = doc.getChar(--offset);
					if (Character.isLetter(c) || c == '<' || c == '/' || c == '#'
						|| c == '.' || c == ':' || Character.isDigit(c)) {
						buf.append(c);
					} else
						return buf.reverse().toString();
				} catch (BadLocationException e) {
					return "";
				}
			}
		}
	}

	/**
	 * Retorna uma string com o número de tabulação da linha atual. Útil
	 * para colocar o final de tag alinhado com o inicial
	 * 
	 * @param doc
	 * @param offset
	 * @return
	 */
	private String getIndentLine(IDocument doc, int offset) {
		int ident = 0;
		while (true) {
			try {
				char c = doc.getChar(--offset);
				// System.out.println("Character = " + c + " ident = " +ident);
				if (c == '\n')
					break;
				if (c == '\t')
					++ident;
				else
					ident = 0;
			} catch (BadLocationException e) {
				ident = 0;
				break;
			}
		}
		String str = "";
		for (int i = 0; i < ident; i++)
			str += "\t";

		return str;
	}

	/**
	 * 
	 */
	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	public char[] getCompletionProposalAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return new char[] { '<' };
	}

	public char[] getContextInformationAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return new char[] { '<' };
	}

	public IContextInformationValidator getContextInformationValidator() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}



	private TextInfo currentText(IDocument document, int documentOffset) {

		try {

			ITypedRegion region = document.getPartition(documentOffset);

			int partitionOffset = region.getOffset();
			int partitionLength = region.getLength();

			int index = documentOffset - partitionOffset;

			String partitionText = document.get(partitionOffset,
					partitionLength);

			System.out.println("Partition text: "
					+ document.get(partitionOffset, region.getLength()));
			char c = partitionText.charAt(index);

			if (Character.isWhitespace(c)
					|| Character.isWhitespace(partitionText.charAt(index - 1))) {
				return new TextInfo("", documentOffset, true);
			} else if (c == '<') {
				return new TextInfo("", documentOffset, true);
			} else {
				int start = index;
				c = partitionText.charAt(start);

				while (!Character.isWhitespace(c) && c != '<' && start >= 0) {
					start--;
					c = partitionText.charAt(start);
				}
				start++;

				int end = index;
				c = partitionText.charAt(end);

				while (!Character.isWhitespace(c) && c != '>'
						&& end < partitionLength - 1) {
					end++;
					c = partitionText.charAt(end);
				}

				String substring = partitionText.substring(start, end);
				return new TextInfo(substring, partitionOffset + start, false);

			}

		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}

	static class TextInfo {
		TextInfo(String text, int documentOffset, boolean isWhiteSpace) {
			this.text = text;
			this.isWhiteSpace = isWhiteSpace;
			this.documentOffset = documentOffset;
		}

		String text;

		boolean isWhiteSpace;

		int documentOffset;
	}

}
