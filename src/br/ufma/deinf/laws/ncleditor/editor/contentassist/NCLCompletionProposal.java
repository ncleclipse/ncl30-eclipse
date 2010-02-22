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

import javax.swing.JFileChooser;

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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;

import br.ufma.deinf.laws.ncl.AttributeValues;
import br.ufma.deinf.laws.ncl.DataType;
import br.ufma.deinf.laws.ncl.NCLReference;
import br.ufma.deinf.laws.ncl.NCLStructure;
import br.ufma.deinf.laws.ncl.help.NCLHelper;
import br.ufma.deinf.laws.ncleclipse.NCLEditorMessages;
import br.ufma.deinf.laws.ncleclipse.NCLEditorPlugin;
import br.ufma.deinf.laws.ncleclipse.document.NCLSourceDocument;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLContentHandler;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLDocument;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLElement;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLParser;
import br.ufma.deinf.laws.ncleclipse.preferences.PreferenceConstants;
import br.ufma.deinf.laws.ncleclipse.scanners.XMLTagScanner;

/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 * 
 */

public class NCLCompletionProposal implements IContentAssistProcessor {
	private XMLTagScanner scanner;
	private File currentFile;
	private String text;
	private String protocols[] = { "file:///", "http://", "rtsp://", "rtp://",
			"sbtvd-ts://" };
	private boolean isAttributeValue;
	private boolean isAttribute;
	private boolean isEndTagName;

	/**
	 * Responsavel por computar os valores que aparecerao na lista de sugestoes.
	 * Retorna uma lista de ICompletionProposal
	 */
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int offset) {

		// get the active IFile
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();
		IEditorPart editor = page.getActiveEditor();
		List propList = new ArrayList();

		try {
			if (editor.getEditorInput() instanceof IFileEditorInput) {
				currentFile = new File(((IFileEditorInput) editor
						.getEditorInput()).getFile().getLocationURI());
			} else {
				currentFile = new File(((IURIEditorInput) editor
						.getEditorInput()).getURI());
			}
		} catch (Exception e) {
			e.printStackTrace();
			ICompletionProposal[] proposal = new ICompletionProposal[0];
			return proposal;
		}

		IDocument doc = viewer.getDocument();
		text = doc.get();

		NCLSourceDocument nclDoc = NCLSourceDocument
				.createNCLSourceDocumentFromIDocument(doc);

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

		NCLSourceDocument nclDoc = NCLSourceDocument
				.createNCLSourceDocumentFromIDocument(doc);

		int fatherOffset = nclDoc.getFatherPartitionOffsetFromEndTag(offset);
		String tagname = nclDoc.getCurrentTagname(fatherOffset);

		String prop = "</" + tagname + ">";
		cursor = prop.length();
		CompletionProposal proposal = new CompletionProposal(prop, offset
				- qlen, qlen, cursor, null, prop, null, null);
		propList.add(proposal);
		return;

	}

	/**
	 * Computa as tags que serao propostas para o usuario
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

		NCLSourceDocument nclDoc = NCLSourceDocument
				.createNCLSourceDocumentFromIDocument(doc);

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

				// get a help info to user
				String helpInfo = NCLHelper.getNCLHelper().getHelpDescription(
						tagname);
				// String helpInfo = "help";
				// String helpInfo = "help";

				CompletionProposal proposal = new CompletionProposal(text,
						offset - qlen, qlen, cursor, null, tagname, null,
						helpInfo);
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

					// get a help information to user
					String helpInfo = NCLHelper.getNCLHelper()
							.getHelpDescription(tagname);
					// String helpInfo = "help";
					// String helpInfo="help";

					CompletionProposal proposal = new CompletionProposal(text,
							offset - qlen, qlen, cursor, null, tagname, null,
							helpInfo);
					propList.add(proposal);
				}
			}
			return;
		}

		/*
		 * Nao faz sentido computar tudo nunca!
		 * 
		 * //Caso contrário computa tudo Map<String, Map<String, Boolean>> atts
		 * = nclStructure.getAttributes(); Iterator it =
		 * atts.entrySet().iterator(); while(it.hasNext()){ Map.Entry<String,
		 * Map<String, Boolean>> entry = (Entry<String, Map<String, Boolean>>)
		 * it.next(); String tagname = entry.getKey(); String tagname2 =
		 * "<"+tagname; if(tagname.startsWith(qualifier) ||
		 * tagname2.startsWith(qualifier)){ String text =
		 * computeTagStructure(tagname, indent);
		 * 
		 * CompletionProposal proposal = new CompletionProposal(text, offset -
		 * qlen, qlen, cursor, null, tagname, null, null);
		 * propList.add(proposal); } }
		 */
	}

	/**
	 * Computa a estrutura da Tag Valores Defaults, atributos obrigatorios,
	 * coloca tudo de uma vez quando o usuario quer inserir a tag.
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
	 * Computa os valores dos atributos que serao proposto pro usuario
	 * 
	 * @param qualifier
	 * @param offset
	 * @param propList
	 */
	private void computeAttributesValuesProposals(IDocument doc,
			String qualifier, int offset, List propList) {
		int qlen = qualifier.length();
		// Verificar se existe valor pre-definido

		NCLSourceDocument nclDoc = NCLSourceDocument
				.createNCLSourceDocumentFromIDocument(doc);

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

		// Propoe elementos que referenciam tipos simples
		String nclText = doc.get();
		NCLContentHandler nclContentHandler = new NCLContentHandler();
		NCLDocument nclDocument = new NCLDocument();
		nclDocument.setParentURI(currentFile.getParentFile().toURI());
		nclContentHandler.setNclDocument(nclDocument);
		NCLParser parser = new NCLParser();
		parser.setContentHandler(nclContentHandler);

		try {
			parser.doParse(nclText);
		} catch (RuntimeException e) {
			e.printStackTrace();
			MessageDialog.openError(Workbench.getInstance()
					.getActiveWorkbenchWindow().getShell(), NCLEditorMessages
					.getInstance().getString("ContentAssist.Error.Title"),
					NCLEditorMessages.getInstance().getString(
							"ContentAssist.Error.XMLParserError"));
		}

		boolean hasContextId = false; // Usado quando verificar se o contexto
		// tem id (em especial no caso do body,
		// onde o id é opcional)

		// Referencias que precisam de contexto
		String perspective = null;
		// Contexto eh o pai
		
		if ((tagname.equals("port") && attribute.equals("component"))
				|| (tagname.equals("bindRule") && attribute
						.equals("constituent"))
				|| (tagname.equals("defaultComponent") && attribute
						.equals("component"))) {

			String fatherTagName = nclDoc.getFatherTagName(offset);

			perspective = nclDoc.getAttributeValueFromCurrentTagName(nclDoc
					.getFatherPartitionOffset(offset), "id");
			if (perspective == null) {
				if (fatherTagName.equals("body")) {
					perspective = nclDoc.getAttributeValueFromCurrentTagName(
							nclDoc.getFatherPartitionOffset(nclDoc
									.getFatherPartitionOffset(offset)), "id");
					if (perspective == null) {
						MessageDialog
								.openError(
										Workbench.getInstance()
												.getActiveWorkbenchWindow()
												.getShell(),
										NCLEditorMessages
												.getInstance()
												.getString(
														"ContentAssist.Error.Title"),
										NCLEditorMessages
												.getInstance()
												.getString(
														"ContentAssist.Error.BodyAndNCLWithoutId"));
						return;
					}
				} else {
					Object[] tmp = { fatherTagName };
					MessageDialog
							.openError(
									Workbench.getInstance()
											.getActiveWorkbenchWindow()
											.getShell(),
									NCLEditorMessages.getInstance().getString(
											"ContentAssist.Error.Title"),
									NCLEditorMessages
											.getInstance()
											.getString(
													"ContentAssist.Error.FatherTagNameWithoutId",
													tmp));
				}
			}
		}

		// Contexto eh o pai do pai
		if ((tagname.equals("bind") && attribute.equals("component"))
				|| (tagname.equals("mapping") && attribute.equals("component"))) {

			String grandFatherTagName = nclDoc.getFatherTagName(nclDoc
					.getFatherPartitionOffset(offset));

			perspective = nclDoc.getAttributeValueFromCurrentTagName(nclDoc
					.getFatherPartitionOffset(nclDoc
							.getFatherPartitionOffset(offset)), "id");
			if (perspective == null) {
				if (grandFatherTagName.equals("body")) {
					perspective = nclDoc
							.getAttributeValueFromCurrentTagName(
									nclDoc
											.getFatherPartitionOffset(nclDoc
													.getFatherPartitionOffset(nclDoc
															.getFatherPartitionOffset(offset))),
									"id");
					hasContextId = false;
				}
			} else
				hasContextId = true;
		}

		if (tagname.equals("bind") && attribute.equals("role")
				|| (tagname.equals("linkParam") && (attribute.equals("name")))) {
			perspective = nclDoc.getAttributeValueFromCurrentTagName(nclDoc
					.getFatherPartitionOffset(offset), "xconnector");
			if (perspective == null || perspective.equals(""))
				return;
		}

		if (tagname.equals("bindParam") && attribute.equals("name")) {
			perspective = nclDoc.getAttributeValueFromCurrentTagName(nclDoc
					.getFatherPartitionOffset(nclDoc
							.getFatherPartitionOffset(offset)), "xconnector");
		}

		if (tagname.equals("bind") && attribute.equals("interface")
				|| tagname.equals("port") && attribute.equals("interface")
				|| tagname.equals("mapping") && attribute.equals("interface")) {
			NCLElement element;
			perspective = nclDoc.getAttributeValueFromCurrentTagName(offset,
					"component");
			if (perspective == null || perspective.equals(""))
				return;
			element = nclDocument.getElementById(perspective);
			while (element != null
					&& element.getAttributes().get("refer") != null) {
				Collection nclReference = nclStructure.getNCLReference(tagname,
						attribute);
				// Computa os valores de atributos dos elementos filhos do refer

				// Refatorar este codigo... Isto estah repetindo o que estah
				// sendo
				// feito lah embaixo
				String perspectivetmp = element.getAttributeValue("refer");
				element = nclDocument.getElementById(perspectivetmp);
				if (nclReference == null)
					return;
				Iterator it = nclReference.iterator();
				while (it.hasNext()) {
					NCLReference nclRefAtual = (NCLReference) it.next();
					Collection elements = nclDocument
							.getElementsFromPerspective(nclRefAtual
									.getRefTagname(), perspectivetmp);
					if (elements == null)
						continue;
					Iterator it2 = elements.iterator();
					while (it2.hasNext()) {
						text = ((NCLElement) it2.next())
								.getAttributeValue(nclRefAtual
										.getRefAttribute());
						if (text == null)
							continue;

						// refer nao pode sugerir a propria media, switch, etc.
						if (attribute.equals("refer")) {
							String idAtual = nclDoc
									.getAttributeValueFromCurrentTagName(
											offset, "id");
							if (idAtual != null)
								if (text.equals(idAtual))
									continue;
						}

						if (text.startsWith(qualifier)) {
							cursor = text.length();
							//System.out.println("Attribute Value Proposal = "
							//		+ text);
							CompletionProposal proposal = new CompletionProposal(
									text, offset - qlen, qlen, cursor, null,
									text, null, null);

							propList.add(proposal);
						}
					}

				}
			}
		}

		if (tagname.equals("descriptorParam")) {
			if (attribute.equals("name")) {
				String name[] = { "background", "balanceLevel", "bassLevel",
						"bottom", "bounds", "fit", "fontColor", "fontFamily",
						"fontSize", "fontVariant", "fontWeight", "height",
						"left", "location", "playerLife", "reusePlayer",
						"right", "scroll", "size", "soundLevel", "style",
						"top", "transparency", "trebleLevel", "visible",
						"width", "zIndex" };

				for (int i = 0; i < name.length; i++)
					if (name[i].startsWith(qualifier)) {
						propList.add(new CompletionProposal(name[i], offset
								- qlen, qlen, name[i].length(), null, name[i],
								null, null));
					}
				return;
			} else if (attribute.equals("value")) {

				String name = nclDoc.getAttributeValueFromCurrentTagName(
						offset, "name");

				if (name.equals("background"))
					prop = AttributeValues.getValues(DataType.COLOR);

				else if (name.equals("visible"))
					prop = AttributeValues.getValues(DataType.BOOLEAN_VALUE);

				else if (name.equals("fit"))
					prop = AttributeValues.getValues(DataType.FIT_VALUE);

				else if (name.equals("scroll"))
					prop = AttributeValues.getValues(DataType.SCROLL);

				else if (name.equals("fontColor"))
					prop = AttributeValues.getValues(DataType.COLOR);

				else if (name.equals("fontVariant"))
					prop = AttributeValues.getValues(DataType.FONT_VARIANT);

				else if (name.equals("fontWeight"))
					prop = AttributeValues.getValues(DataType.FONT_WEIGHT);

				else if (name.equals("playerLife"))
					prop = AttributeValues.getValues(DataType.PLAYER_LIFE);

				for (String str : prop)
					if (str.startsWith(qualifier))
						propList.add(new CompletionProposal(str, offset - qlen,
								qlen, str.length(), null, str, null, null));
				return;
			}

		}

		if ((tagname.equals("media") && attribute.equals("src"))
				|| (tagname.equals("importBase") && attribute
						.equals("documentURI"))
				|| (tagname.equals("descriptor")
						&& (attribute.equals("focusSrc")) || attribute
						.equals("focusSelSrc"))) {
			// suggest the protocols
			for (int i = 0; i < protocols.length; i++) {
				text = protocols[i];
				if (text.startsWith(qualifier)) {
					cursor = text.length();
					//System.out.println("Attribute Value Proposal = " + text);
					CompletionProposal proposal = new CompletionProposal(text,
							offset - qlen, qlen, cursor, null, text, null, null);
					if (!(NCLEditorPlugin.getDefault().getPreferenceStore()
							.getBoolean(PreferenceConstants.P_POPUP_SUGESTION)))
						propList.add(proposal);
				}
			}

			if (NCLEditorPlugin.getDefault().getPreferenceStore().getBoolean(
					PreferenceConstants.P_POPUP_SUGESTION)) {
				FileDialog fileDialog = new FileDialog(new Shell(), SWT.OPEN);
				fileDialog.setFilterPath(currentFile.getParent());
				fileDialog.setText("OK");
				String path = fileDialog.open();
				if (path == null)
					return;

				String id = nclDoc.getAttributeValueFromCurrentTagName(offset,
						"id");

				if (path.startsWith(currentFile.getParent()))
					path = path.substring(currentFile.getParent().length() + 1);
				if (id != null)
					nclDoc.setAttribute(id, "src", path);
				else
					nclDoc.setAttributeFromTagname(tagname, "documentURI",
							path, offset);

				return;
			}

			File file = null;
			file = new File(currentFile.toURI());

			String pre = "";

			String currentPath = currentFile.getParent();
			if (qualifier.startsWith("file://")) {
				pre = "file://";
				qualifier = qualifier.substring(pre.length());
				if (qualifier.equals(""))
					currentPath = System.getProperty("user.home");
			}
			try {
				Vector<String> proposal = new URIProposer(currentPath)
						.getSrcSuggest(qualifier);
				CompletionProposal completionProposal;
				for (String str : proposal) {
					str = pre + str;
					completionProposal = new CompletionProposal(str, offset
							- qlen, qlen, str.length(), null, str, null, null);
					propList.add(completionProposal);
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/**
			 * Nao sugerindo temporariamente try { URIProposer fs = new
			 * URIProposer(currentFile.getParent()); Vector<String> v =
			 * fs.getDirectories(qualifier); for (int i = 0; i < v.size(); i++)
			 * { if (v.get(i).startsWith(qualifier)) { cursor =
			 * v.get(i).length(); CompletionProposal proposal = new
			 * CompletionProposal(v .get(i), offset - qlen, qlen, cursor, null,
			 * v .get(i), null, null); propList.add(proposal); } } fs = new
			 * URIProposer(currentFile.getParent().toString()); v =
			 * fs.getFiles(qualifier); for (int i = 0; i < v.size(); i++) { if
			 * (v.get(i).startsWith(qualifier)) { cursor = v.get(i).length();
			 * CompletionProposal proposal = new CompletionProposal(v .get(i),
			 * offset - qlen, qlen, cursor, null, v .get(i), null, null);
			 * propList.add(proposal); } } return; } catch (URISyntaxException
			 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
			 */
		}

		// System.out.println("perspective = " + perspective);
		Collection nclReference = nclStructure.getNCLReference(tagname,
				attribute);
		if (nclReference == null)
			return;

		CompletionProposal proposal = null;
		Iterator it = null;
		if (perspective != null) {
			// Pode sugerir o id do context ou body (desde que id do body
			// exista)
			if (hasContextId && !attribute.equals("refer")) {
				cursor = perspective.length();
				proposal = new CompletionProposal(perspective, offset - qlen,
						qlen, cursor, null, perspective, null, null);

				propList.add(proposal);
			}

			it = nclReference.iterator();
			while (it.hasNext()) {
				NCLReference nclRefAtual = (NCLReference) it.next();

				Collection elements = nclDocument.getElementsFromPerspective(
						nclRefAtual.getRefTagname(), perspective);
				if (elements == null)
					continue;
				Iterator it2 = elements.iterator();
				while (it2.hasNext()) {
					text = ((NCLElement) it2.next())
							.getAttributeValue(nclRefAtual.getRefAttribute());
					String helpInfo = nclDoc.getComment(text);
					if (text == null)
						continue;

					// refer nao pode sugerir a propria media, switch, etc.
					if (attribute.equals("refer")) {
						String idAtual = nclDoc
								.getAttributeValueFromCurrentTagName(offset,
										"id");
						if (idAtual != null)
							if (text.equals(idAtual))
								continue;
					}

					if (text.startsWith(qualifier)) {
						cursor = text.length();
						//System.out
						//		.println("Attribute Value Proposal = " + text);
						proposal = new CompletionProposal(text, offset - qlen,
								qlen, cursor, null, text, null, helpInfo);

						propList.add(proposal);
					}
				}
			}
		}

		// Referencias Globais (ou seja, não precisa de contexto)
		it = nclReference.iterator();

		if (perspective == null) {
			NCLElement element;
			String atualId = nclDoc.getAttributeValueFromCurrentTagName(offset,
					"id");
			// if(atualId == null || atualId.equals("")) return;
			// element = nclDocument.getElementById(atualId);

			// String atualCompletePerspective =
			// element.getCompletePerspective();
			it = nclReference.iterator();

			while (it.hasNext()) {
				NCLReference nclRefAtual = (NCLReference) it.next();
				Collection elements = nclDocument.getElements().get(
						nclRefAtual.getRefTagname());
				if (elements == null)
					continue;
				Iterator it2 = elements.iterator();
				while (it2.hasNext()) {
					NCLElement refElement = ((NCLElement) it2.next());
					text = refElement.getAttributeValue(nclRefAtual
							.getRefAttribute());
					String helpInfo = nclDoc.getComment(text);
					if (text == null || text.endsWith("#null"))
						continue; // null

					// TODO: the refer attribute can not refer the own parent or
					// his childrens
					/*
					 * String refCompletePerspective =
					 * refElement.getCompletePerspective();
					 * if(!refCompletePerspective
					 * .equals(atualCompletePerspective)){
					 * if(refCompletePerspective.length() >
					 * atualCompletePerspective.length()){
					 * if(refCompletePerspective
					 * .contains(atualCompletePerspective)) continue; } else
					 * if(atualCompletePerspective
					 * .contains(refCompletePerspective)) continue; }
					 */

					// refer nao pode sugerir a propria media, switch, etc.
					if (attribute.equals("refer")) {
						String idAtual = nclDoc
								.getAttributeValueFromCurrentTagName(offset,
										"id");
						if (idAtual != null)
							if (text.equals(idAtual))
								continue;
					}

					if (text.startsWith(qualifier)) {
						cursor = text.length();
						System.out
								.println("Attribute Value Proposal = " + text);
						proposal = new CompletionProposal(text, offset - qlen,
								qlen, cursor, null, text, null, helpInfo);

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

		NCLSourceDocument nclDoc = NCLSourceDocument
				.createNCLSourceDocumentFromIDocument(doc);

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

				String helpInfo = NCLHelper.getNCLHelper().getHelpDescription(
						currentTagname, view);
				// String helpInfo = "help";
				// String helpInfo = "help";

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
		if (isAttributeValue) {
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
		} else {
			while (true) {
				try {
					char c = doc.getChar(--offset);
					if (Character.isLetter(c) || c == '<' || c == '/'
							|| c == '#' || c == '.' || c == ':'
							|| Character.isDigit(c)) {
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
	 * Retorna uma string com o número de tabulação da linha atual. Útil para
	 * colocar o final de tag alinhado com o inicial
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
