/*******************************************************************************
 * This file is part of the NCL authoring environment - NCL Eclipse.
 *
 * Copyright (C) 2007-2012, LAWS/UFMA.
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
package br.ufma.deinf.laws.ncleditor.editor.contentassist;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;

import br.ufma.deinf.laws.ncl.AttributeValues;
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
import br.ufma.deinf.laws.ncleclipse.util.NCLWhitespaceDetector;

/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 * 
 */

public class NCLCompletionProposal implements IContentAssistProcessor {
	private File currentFile;
	private String text;
	private String protocols[] = { "file:///", "http://", "https://",
			"rtsp://", "rtp://", "ncl-mirror://", 
			"sbtvd-ts://", "sbtvd-ts://video", "sbtvd-ts://audio", 
			"isdb-ts://", "isdb-ts://video", "isdb-ts://audio",
			"ts://" };
	private boolean isAttributeValue;
	private boolean isAttribute;
	private boolean isEndTagName;

	private Image connectorImage = null;
	private Image regionImage = null;
	private Image fileImage = null;

	// private HashMap<String, NCLSourceDocument> importedNCLDocs = new HashMap<String, NCLSourceDocument>();

	NCLDocument nclDocument;

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
		List <CompletionProposal> propList = new ArrayList <CompletionProposal>();

		boolean isFileEditor = true;

		try {
			if (editor.getEditorInput() instanceof IFileEditorInput) {
				currentFile = new File(
						((IFileEditorInput) editor.getEditorInput()).getFile()
								.getLocationURI());

				isFileEditor = false;

			} else {
				currentFile = new File(
						((IURIEditorInput) editor.getEditorInput()).getURI());
			}
		} catch (Exception e) {
			e.printStackTrace();
			ICompletionProposal[] proposal = new ICompletionProposal[0];
			return proposal;
		}

		IDocument doc = viewer.getDocument();
		text = doc.get();

		NCLSourceDocument nclDoc = null;

		if (isFileEditor)
			nclDoc = NCLSourceDocument
					.createNCLSourceDocumentFromIDocument(doc);
		else
			nclDoc = (NCLSourceDocument) doc;

		isAttributeValue = nclDoc.isAttributeValue(offset);
		isAttribute = nclDoc.isAttribute(offset);
		isEndTagName = nclDoc.isEndTagName(offset);
		Point selectedRange = viewer.getSelectedRange();
		if (selectedRange.y > 0) {
			// TODO:
		} else {

			// System.out.println("Attributo = " + isAttribute);
			
			String qualifier = getQualifier(nclDoc, offset);
			if (isEndTagName) {
				computeEndTagName(nclDoc, qualifier, offset, propList);
			} else if (isAttributeValue) {
				computeAttributesValuesProposals(nclDoc, qualifier, offset,
						propList);
			} else if (!isAttribute) {
				computeTagsProposals(nclDoc, qualifier, offset, propList);
			} else if (!nclDoc.isTagname(offset))
				computeAttributesProposals(nclDoc, qualifier, offset, propList);
		}
		ICompletionProposal[] proposals = new ICompletionProposal[propList
				.size()];
		propList.toArray(proposals);
		return proposals;
	}

	private void computeEndTagName(IDocument doc, String qualifier, int offset,
			List <CompletionProposal> propList) {
		int qlen = qualifier.length();

		NCLSourceDocument nclDoc = (NCLSourceDocument) doc;

		int fatherOffset = nclDoc.getFatherPartitionOffsetFromEndTag(offset);

		String fatherIdentLine = nclDoc.getIndentLine(fatherOffset);

		String tagname = nclDoc.getCurrentTagname(fatherOffset);

		String prop = "</" + tagname + ">";
		String value = "\n" + fatherIdentLine + prop;

		cursor = value.length();
		CompletionProposal proposal = new CompletionProposal(value, offset
				- qlen, qlen, cursor, null, prop, null, null);
		propList.add(proposal);
		return;

	}

	
	private int cursor; // compute the cursor position after autocomplete insert
						// a text.

	/**
	 * Compute the tags that will be suggested for user.
	 * 
	 * @param qualifier
	 * @param offset
	 * @param propList
	 */
	private void computeTagsProposals(IDocument doc, String qualifier,
			int offset, List <CompletionProposal> propList) {
		
		String qualifier_lower = qualifier.toLowerCase();

		int qlen = qualifier.length();
		NCLStructure nclStructure = NCLStructure.getInstance();
		String indent = ((NCLSourceDocument) doc).getIndentLine(offset);

		NCLSourceDocument nclDoc = NCLSourceDocument
				.createNCLSourceDocumentFromIDocument(doc);

		// fazer um filtro para buscar apenas as tags filhas da getFatherTagname
		/* System.out.println("## Log: Pai da tag onde estou digitando : "
				+ nclDoc.getFatherTagName(offset));*/
		Map<String, Map<String, Character>> nesting = nclStructure.getNesting();

		String fatherTagName = nclDoc.getFatherTagName(offset);
		if (fatherTagName.equals("")) {
			String tagname = "ncl";
			String tagname2 = "<" + tagname;

			String tagname_lower = tagname.toLowerCase();
			String tagname2_lower = tagname2.toLowerCase();

			if (tagname_lower.contains(qualifier_lower)
					|| tagname2_lower.contains(qualifier_lower)) {
				String text = computeTagStructure(tagname, indent);

				// get a help info to user
				// TODO: Description of elements in English and Spanish
				String helpInfo = null;
				
				// Test if the user wants to see help information
				if (NCLEditorPlugin
						.getDefault()
						.getPreferenceStore()
						.getBoolean(
								PreferenceConstants.P_SHOW_HELP_INFO_ON_AUTOCOMPLETE)) {
					helpInfo = NCLHelper.getHelpDescription(tagname);
				}

				CompletionProposal proposal = new CompletionProposal(text,
						offset - qlen, qlen, cursor, null, tagname, null,
						helpInfo);
				propList.add(proposal);
			}

		} else if (nesting.containsKey(fatherTagName)) {
			Map<String, Character> children = nesting.get(fatherTagName);
			Iterator <Map.Entry<String, Character> > it = 
					children.entrySet().iterator();
			
			while (it.hasNext()) {
				Map.Entry<String, Character> entry = 
						(Entry<String, Character>) it.next();
				String tagname = entry.getKey();
				String tagname2 = "<" + tagname;

				String tagname_lower = tagname.toLowerCase();
				String tagname2_lower = tagname2.toLowerCase();

				if (tagname_lower.contains(qualifier_lower)
						|| tagname2_lower.contains(qualifier_lower)) {

					String text = computeTagStructure(tagname, indent);

					// TODO: Description of elements in English and Spanish
					// get a help information to user
					String helpInfo = null;

					// Test if the user wants to see help information
					if (NCLEditorPlugin
							.getDefault()
							.getPreferenceStore()
							.getBoolean(
									PreferenceConstants.P_SHOW_HELP_INFO_ON_AUTOCOMPLETE)) {
						helpInfo = NCLHelper.getHelpDescription(tagname);
					}

					CompletionProposal proposal = new CompletionProposal(text,
							offset - qlen, qlen, cursor, null, tagname, null,
							helpInfo);

					propList.add(proposal);
				}
			}
			return;
		}

		/*
		 * Never makes sense compute everything!
		 * 
		 * //Caso contrário computa tudo Map<String, Map<String, Boolean>> atts
		 * = nclStructure.getAttributes(); Iterator it =
		 * atts.entrySet().iterator(); while(it.hasNext()){ Map.Entry<String,
		 * Map<String, Boolean>> entry = (Entry<String, Map<String, Boolean>>)
		 * it.next(); String tagname = entry.getKey(); String tagname2 =
		 * "<"+tagname; if(tagname.contains(qualifier) ||
		 * tagname2.contains(qualifier)){ String text =
		 * computeTagStructure(tagname, indent);
		 * 
		 * CompletionProposal proposal = new CompletionProposal(text, offset -
		 * qlen, qlen, cursor, null, tagname, null, null);
		 * propList.add(proposal); } }
		 */
	}

	/**
	 * Compute the structure of a tag, i.e., default values, required 
	 * attributes, etc.
	 * 
	 * @param tagname
	 * @return the string composed of the tag and all its required attributes
	 * 		and default values.
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
			ret = "<" + tagname + attributes + "/>" /*+ "\r\n" + indent*/;

			if (!attributes.isEmpty()) {
				cursor = ret.indexOf("\"\"") + 1;
			} else {
				cursor = ret.length();
			}
		} else {
			ret = "<" + tagname + attributes + ">" + "\r\n" + indent + "\t";
			if (tagname.equals("context") || tagname.equals("body") || tagname.equals("switch")) {
				// Test if the user wants to insert a port 
				if (NCLEditorPlugin
						.getDefault()
						.getPreferenceStore()
						.getBoolean(
								PreferenceConstants.P_INSERT_PORT_IN_CONTEXT_ON_AUTOCOMPLETE)) {
					ret += "<port id=\"\" component=\"\" />";
				}
				
			} else if (tagname.equals("causalConnector")) {
				ret += "<simpleCondition role=\"\" />" + "\n" + indent + "\t"
						+ "<simpleAction role=\"\" />";
			}
			if (!attributes.isEmpty()) {
				cursor = ret.indexOf("\"\"") + 1;
			} else {
				cursor = ret.length();
			}
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

		// TODO: in future, show figures representing each element of language
		// loadImages();

		String qualifier_lower = qualifier.toLowerCase();
		int qlen = qualifier.length();
		// Verificar se existe valor pre-definido

		NCLSourceDocument nclDoc = NCLSourceDocument
				.createNCLSourceDocumentFromIDocument(doc);

		String tagname = nclDoc.getCurrentTagname(offset);
		String attribute = nclDoc.getCurrentAttribute(offset);
		// System.out.println("tag: " + tagname + " attr:" + attribute);
		NCLStructure nclStructure = NCLStructure.getInstance();
		Vector<String> prop = AttributeValues.getValues(nclStructure
				.getDataType(tagname, attribute));

		if (prop.size() > 0) {
			for (int i = 0; i < prop.size(); i++) {
				String prop_lower = prop.get(i).toLowerCase();
				if (prop_lower.contains(qualifier_lower)) {
					String text = prop.get(i);
					String texttoshow = text;
					// System.out.println(text);
					// if(!qualifier.contains("\"") &&
					// !qualifier.contains("\'"))
					// text = "\""+text+"\"";
					cursor = text.length();
					CompletionProposal proposal = new CompletionProposal(text,
							offset - qlen, qlen, cursor, null, texttoshow,
							null, null);
					propList.add(proposal);
				}
			}
		}

		// Propoe elementos que referenciam tipos simples
		String nclText = doc.get();
		NCLContentHandler nclContentHandler = new NCLContentHandler();
		nclDocument = new NCLDocument();
		nclDocument.setParentURI(currentFile.getParentFile().toURI());
		nclContentHandler.setNclDocument(nclDocument);
		NCLParser parser = new NCLParser();
		parser.setContentHandler(nclContentHandler);

		try {
			parser.doParse(nclText);
		} catch (RuntimeException e) {
			e.printStackTrace();
			MessageDialog.openError(
					Workbench.getInstance().getActiveWorkbenchWindow()
							.getShell(),
					NCLEditorMessages.getInstance().getString(
							"ContentAssist.Error.Title"),
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

			perspective = nclDoc.getAttributeValueFromCurrentTagName(
					nclDoc.getFatherPartitionOffset(offset), "id");
			// System.out.println(perspective);
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
									nclDoc.getFatherPartitionOffset(nclDoc.getFatherPartitionOffset(nclDoc
											.getFatherPartitionOffset(offset))),
									"id");
					hasContextId = false;
				}
			} else
				hasContextId = true;
		}

		if (tagname.equals("bind") && attribute.equals("role")
				|| (tagname.equals("linkParam") && (attribute.equals("name")))) {
			perspective = nclDoc.getAttributeValueFromCurrentTagName(
					nclDoc.getFatherPartitionOffset(offset), "xconnector");
			if (perspective == null || perspective.equals(""))
				return;
		}
		if (NCLEditorPlugin.getDefault().getPreferenceStore()
				.getBoolean(PreferenceConstants.P_LINK_AUTO_COMPLETE)) {
			if (tagname.equals("link") && attribute.equals("xconnector")) {

				computeLinkValuesWithStructure(nclDoc, offset, qualifier,
						propList);

				return;
			}
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

			Vector<String> referPath = new Vector<String>();
			while (element != null
					&& element.getAttributes().get("refer") != null) {
				Collection nclReference = nclStructure.getNCLReference(tagname,
						attribute);
				// Computa os valores de atributos dos elementos filhos do refer

				// TODO: Refactoring this code. This is repeating what will be
				// done bellow!
				String perspectivetmp = element.getAttributeValue("refer");
				if (referPath.contains(perspectivetmp)) {
					// Warning: This "if" avoids an infinite loop!!!
					// TODO: Returns an error message.
					System.out.println("Ciclic refer");
					return;
				}
				referPath.add(perspectivetmp);
				element = nclDocument.getElementById(perspectivetmp);
				if (nclReference == null)
					return;
				Iterator it = nclReference.iterator();
				while (it.hasNext()) {
					NCLReference nclRefAtual = (NCLReference) it.next();
					Collection elements = nclDocument
							.getElementsFromPerspective(
									nclRefAtual.getRefTagname(), perspectivetmp);
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

						String text_lower = text.toLowerCase();
						if (text_lower.contains(qualifier_lower)) {
							cursor = text.length();
							// System.out.println("Attribute Value Proposal = "
							// + text);
							CompletionProposal proposal = new CompletionProposal(
									text, offset - qlen, qlen, cursor, null,
									text, null, null);

							propList.add(proposal);
						}
					}

				}
			}
		}

		// suggest descriptorParam and property values
		if ((tagname.equals("descriptorParam") || tagname.equals("property"))
				&& attribute.equals("value")) {

			String name = nclDoc.getAttributeValueFromCurrentTagName(offset,
					"name");

			prop = AttributeValues.getValuesFromProperty(name);

			for (String str : prop) {
				String str_lower = str.toLowerCase();
				if (str_lower.contains(qualifier_lower))
					propList.add(new CompletionProposal(str, offset - qlen,
							qlen, str.length(), null, str, null, null));
			}
			return;
		}
		
		if ((tagname.equals("simpleCondition") && attribute.equals("key"))){
			int fatherOffset = offset;
			while ((fatherOffset != -1)
					&& (!nclDoc.getCurrentTagname(fatherOffset).equals(
							"causalConnector"))) {
				fatherOffset = nclDoc.getFatherPartitionOffset(fatherOffset);
			}
			Vector<Integer> childrenOffset = nclDoc
					.getChildrenOffsets(fatherOffset);

			Vector<String> suggest = new Vector<String>();

			for (int i = 0; i < childrenOffset.size(); i++) {
				int childOffset = childrenOffset.elementAt(i);

				String tag = nclDoc.getCurrentTagname(childOffset);

				if (tag != null && tag.equals("connectorParam")) {
					String name = nclDoc.getAttributeValueFromCurrentTagName(
							childOffset, "name");
					if (name != null && !name.equals("")) {
						suggest.add("$" + name);
					}
				}

			}

			for (String str : suggest) {
				String str_lower = str.toLowerCase();
				if (str_lower.contains(qualifier_lower))
					propList.add(new CompletionProposal(str, offset - qlen,
							qlen, str.length(), null, str, null, null));
			}
			return;
		}

		
		
		// suggest the connectorParams
		if ((tagname.equals("simpleAction") && attribute.equals("value"))
				|| (tagname.equals("simpleAction") && attribute.equals("delay"))) {

			int fatherOffset = offset;
			while ((fatherOffset != -1)
					&& (!nclDoc.getCurrentTagname(fatherOffset).equals(
							"causalConnector"))) {
				fatherOffset = nclDoc.getFatherPartitionOffset(fatherOffset);
			}
			Vector<Integer> childrenOffset = nclDoc
					.getChildrenOffsets(fatherOffset);

			Vector<String> suggest = new Vector<String>();

			for (int i = 0; i < childrenOffset.size(); i++) {
				int childOffset = childrenOffset.elementAt(i);

				String tag = nclDoc.getCurrentTagname(childOffset);

				if (tag != null && tag.equals("connectorParam")) {
					String name = nclDoc.getAttributeValueFromCurrentTagName(
							childOffset, "name");
					if (name != null && !name.equals("")) {
						suggest.add("$" + name);
					}
				}

			}

			for (String str : suggest) {
				String str_lower = str.toLowerCase();
				if (str_lower.contains(qualifier_lower))
					propList.add(new CompletionProposal(str, offset - qlen,
							qlen, str.length(), null, str, null, null));
			}
			return;
		}

		// sources attributes
		if ((tagname.equals("media") && attribute.equals("src"))
				|| (attribute.equals("documentURI"))
				|| (tagname.equals("descriptor")
						&& (attribute.equals("focusSrc")) || attribute
							.equals("focusSelSrc"))) {
			// if the user preferences is to open a window to select a file
			if (NCLEditorPlugin.getDefault().getPreferenceStore()
					.getBoolean(PreferenceConstants.P_POPUP_SUGESTION)) {
				FileDialog fileDialog = new FileDialog(PlatformUI
						.getWorkbench().getActiveWorkbenchWindow().getShell(),
						SWT.OPEN);
				fileDialog.setFilterPath(currentFile.getParent());
				String path = fileDialog.open();
				if (path == null)
					return;

				String id = nclDoc.getAttributeValueFromCurrentTagName(offset,
						"id");

				if (path.contains(currentFile.getParent()))
					path = path.substring(currentFile.getParent().length() + 1);
				if (id != null)
					nclDoc.setAttributefromTag(id, attribute, path, offset);
				else
					nclDoc.setAttributefromTag(tagname, "documentURI", path,
							offset);

				return;
			}

			// the user want to be suggested with autocomplete

			// suggest the protocols
			for (int i = 0; i < protocols.length; i++) {
				text = protocols[i];
				if (text.contains(qualifier) && !text.equals(qualifier)) {
					cursor = text.length();
					// System.out.println("Attribute Value Proposal = " + text);
					CompletionProposal proposal = new CompletionProposal(text,
							offset - qlen, qlen, cursor, null, text, null, null);
					if (!(NCLEditorPlugin.getDefault().getPreferenceStore()
							.getBoolean(PreferenceConstants.P_POPUP_SUGESTION)))
						propList.add(proposal);
				}
			}

			File file = null;
			file = new File(currentFile.toURI());

			String pre = "";

			String currentPath = currentFile.getParent();
			if (qualifier.contains("file://")) {
				pre = "file://";
				qualifier = qualifier.substring(pre.length());
				if (qualifier.equals(""))
					currentPath = System.getProperty("user.home");
			}
			
			CompletionProposal completionProposal;
			
			String nclMirror = "ncl-mirror://";
			if (qualifier.contains(nclMirror)) {
				
				String mediaId = qualifier.substring(nclMirror.length());
				ArrayList<String> elements = nclDoc.getAllElementsOfType("media");
				Collections.sort(elements);
				
				String id = nclDoc.getAttributeValueFromCurrentTagName(offset,
						"id");
				
				if (id == null) id = "";
				
				for (String str : elements){
					if (str.contains(mediaId) && !str.equals(id)){
						str = nclMirror + str;
						completionProposal = new CompletionProposal(str, offset
								- qlen, qlen, str.length(), fileImage, str,
								null, null);
						propList.add(completionProposal);
					}
				}
				return;
				
			} else
				try {
					// TODO: this is not sufficient to work with URI
					// codification
					qualifier = qualifier.replace("%20", " ");

					Vector<String> proposal = new URIProposer(currentPath)
							.getSrcSuggest(qualifier);

					Collections.sort(proposal);

					
					for (String str : proposal) {
						str = pre + str;
						completionProposal = new CompletionProposal(str, offset
								- qlen, qlen, str.length(), fileImage, str,
								null, null);
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
			 * { if (v.get(i).contains(qualifier)) { cursor =
			 * v.get(i).length(); CompletionProposal proposal = new
			 * CompletionProposal(v .get(i), offset - qlen, qlen, cursor, null,
			 * v .get(i), null, null); propList.add(proposal); } } fs = new
			 * URIProposer(currentFile.getParent().toString()); v =
			 * fs.getFiles(qualifier); for (int i = 0; i < v.size(); i++) { if
			 * (v.get(i).contains(qualifier)) { cursor = v.get(i).length();
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
					NCLElement nclElement = (NCLElement) it2.next();
					text = nclElement.getAttributeValue(nclRefAtual
							.getRefAttribute());

					if (text == null)
						continue;

					String helpInfo = null;
					// Test if the user wants to see help information
					if (NCLEditorPlugin
							.getDefault()
							.getPreferenceStore()
							.getBoolean(
									PreferenceConstants.P_SHOW_HELP_INFO_ON_AUTOCOMPLETE)) {
						// Get documentation of the element to show
						helpInfo = nclElement.getDoc();
					}

					Image image = null;
					if (nclElement.getTagName().equals("region"))
						image = regionImage;
					else if (nclElement.getTagName().equals("causalConnector"))
						image = connectorImage;

					// refer nao pode sugerir a propria media, switch, etc.
					if (attribute.equals("refer")) {
						String idAtual = nclDoc
								.getAttributeValueFromCurrentTagName(offset,
										"id");
						if (idAtual != null)
							if (text.equals(idAtual))
								continue;
					}

					String text_lower = text.toLowerCase();
					if (text_lower.contains(qualifier_lower)) {
						cursor = text.length();
						// System.out
						// .println("Attribute Value Proposal = " + text);
						proposal = new CompletionProposal(text, offset - qlen,
								qlen, cursor, image, text, null, helpInfo);

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

					if (text == null || text.endsWith("#null"))
						continue; // null

					String helpInfo = null;
					// Test if the user wants to see help information
					if (NCLEditorPlugin
							.getDefault()
							.getPreferenceStore()
							.getBoolean(
									PreferenceConstants.P_SHOW_HELP_INFO_ON_AUTOCOMPLETE)) {
						// Get documentation of the element to show
						helpInfo = refElement.getDoc();
					}
					Image image = null;
					if (refElement.getTagName().equals("region"))
						image = regionImage;
					else if (refElement.getTagName().equals("causalConnector"))
						image = connectorImage;
					// TODO: the refer attribute can not refer its own parent or
					// its childrens
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

					String text_lower = text.toLowerCase();
					if (text_lower.contains(qualifier_lower)) {
						cursor = text.length();
						/* System.out
								.println("Attribute Value Proposal = " + text);
						 */
						proposal = new CompletionProposal(text, offset - qlen,
								qlen, cursor, image, text, null, helpInfo);

						propList.add(proposal);
					}
				}
			}
		}

	}

	private void computeLinkValuesWithStructure(NCLSourceDocument nclSourceDoc,
			int offset, String qualifier, List propList) {

		String qualifier_lower = qualifier.toLowerCase();
		String tagname = nclSourceDoc.getCurrentTagname(offset);
		String attribute = nclSourceDoc.getCurrentAttribute(offset);

		try {
			ITypedRegion region;
			region = nclSourceDoc.getPartition(offset);
			String tag = nclSourceDoc.get(region.getOffset(),
					region.getLength());

			int begin = offset - qualifier.length();
			int end = region.getOffset() + region.getLength() - begin;

			String rest = nclSourceDoc.get(
					begin
							+ nclSourceDoc.getAttributeValueFromCurrentTagName(
									offset, "xconnector").length() + 1,
					end
							- nclSourceDoc.getAttributeValueFromCurrentTagName(
									offset, "xconnector").length() - 1);

			Vector<Integer> childrenOff = nclSourceDoc
					.getChildrenOffsets(offset);
			HashMap<String, Integer> roles = new HashMap<String, Integer>();

			// compute the roles already putted
			if (childrenOff != null) {
				for (Integer i : childrenOff) {
					String role = nclSourceDoc
							.getAttributeValueFromCurrentTagName(i, "role");
					if (role != null && !role.equals("")) {
						if (roles.containsKey(role))
							roles.put(role, roles.get(role) + 1);
						else
							roles.put(role, 1);
					}
				}
			}

			Collection nclReference = NCLStructure.getInstance()
					.getNCLReference(tagname, attribute);

			Iterator it = nclReference.iterator();
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

					if (text == null || text.endsWith("#null"))
						continue; // null

					String id = text;

					NCLElement tmp = nclDocument.getElementById(id);
					String complete = text + "\"" + rest;

					if (tmp != null) {

						Collection conditions = nclDocument
								.getElementsFromCompletePerspective(
										"simpleCondition",
										tmp.getCompletePerspective() + "/" + id);

						Iterator it3 = conditions.iterator();

						while (it3.hasNext()) {
							NCLElement tmp2 = ((NCLElement) it3.next());
							String role = tmp2.getAttributeValue("role");
							String min = tmp2.getAttributeValue("min");
							int Min = 1;
							if (min != null && !min.equals(""))
								try {
									Min = Integer.parseInt(min);
								} catch (NumberFormatException e) {
									Min = 1;
								}
							if (role != null && !role.equals("")) {
								int quant = 0;
								if (roles.containsKey(role))
									quant = roles.get(role);
								for (int j = 0; j < Min - quant; j++) {
									String aux = "<bind role=\"" + role
											+ "\" component=\"\" />";
									complete += "\n"
											+ nclSourceDoc
													.getIndentLine(offset)
											+ "\t" + aux;
								}
							}

						}

						Collection actions = nclDocument
								.getElementsFromCompletePerspective(
										"simpleAction",
										tmp.getCompletePerspective() + "/" + id);
						it3 = actions.iterator();
						while (it3.hasNext()) {
							NCLElement tmp2 = ((NCLElement) it3.next());
							String role = tmp2.getAttributeValue("role");
							String min = tmp2.getAttributeValue("min");
							int Min = 1;
							if (min != null && !min.equals(""))
								try {
									Min = Integer.parseInt(min);
								} catch (NumberFormatException e) {
									Min = 1;
								}
							if (role != null && !role.equals("")) {
								int quant = 0;
								if (roles.containsKey(role))
									quant = roles.get(role);

								for (int j = 0; j < Min - quant; j++) {
									String aux = "<bind role=\"" + role
											+ "\" component=\"\" />";
									complete += "\n"
											+ nclSourceDoc
													.getIndentLine(offset)
											+ "\t" + aux;
								}
							}
						}

						Collection attrAssesments = nclDocument
								.getElementsFromCompletePerspective(
										"attributeAssessment",
										tmp.getCompletePerspective() + "/" + id);

						it3 = attrAssesments.iterator();
						while (it3.hasNext()) {
							NCLElement tmp2 = ((NCLElement) it3.next());
							String role = tmp2.getAttributeValue("role");
							String min = tmp2.getAttributeValue("min");
							int Min = 1;
							if (min != null && !min.equals(""))
								try {
									Min = Integer.parseInt(min);
								} catch (NumberFormatException e) {
									Min = 1;
								}
							if (role != null && !role.equals("")) {
								int quant = 0;
								if (roles.containsKey(role))
									quant = roles.get(role);
								for (int j = 0; j < Min - quant; j++) {
									String aux = "<bind role=\"" + role
											+ "\" component=\"\" />";
									complete += "\n"
											+ nclSourceDoc
													.getIndentLine(offset)
											+ "\t" + aux;
								}
							}
						}
					}

					String helpInfo = null;
					// Test if the user wants to see help information
					if (NCLEditorPlugin
							.getDefault()
							.getPreferenceStore()
							.getBoolean(
									PreferenceConstants.P_SHOW_HELP_INFO_ON_AUTOCOMPLETE)) {
						// Get documentation of the element to show
						helpInfo = refElement.getDoc();
					}

					String text_lower = text.toLowerCase();
					if (text_lower.contains(qualifier_lower)) {
						cursor = complete.length();
						CompletionProposal proposal = new CompletionProposal(
								complete, begin, end, cursor, connectorImage,
								text, null, helpInfo);

						propList.add(proposal);
					}
				}
			}
		} catch (BadLocationException e) {

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

		String qualifier_lower = qualifier.toLowerCase();
		int qlen = qualifier.length();
		
		boolean mustIncludeWhitespaceInTheBegin = false,
				mustIncludeWhitespaceInTheEnd = false;
		
		/* Compute if we must or not include whitespace on the beggining and
		 * on the end of the proposal attibute.
		 */
		try {
			NCLWhitespaceDetector nclwhitespacedetector = 
					new NCLWhitespaceDetector();
			
			char ch;
			ch = doc.getChar(offset-qualifier.length()-1);
			
			boolean iswhitespace = nclwhitespacedetector
					.isWhitespace(ch);
			
			if (!iswhitespace) {
				mustIncludeWhitespaceInTheBegin = true;
			}
			
			ch = doc.getChar(offset);
			iswhitespace = nclwhitespacedetector.isWhitespace(ch);

			if (!iswhitespace && doc.getChar(offset) != '/'
					&& doc.getChar(offset) != '>') {
				mustIncludeWhitespaceInTheEnd = true;
			}
			
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		NCLSourceDocument nclDoc = NCLSourceDocument
				.createNCLSourceDocumentFromIDocument(doc);

		// System.out.println("Computing Attributes proposals...");
		String currentTagname = nclDoc.getCurrentTagname(offset);
		// System.out.println("Current Tag Name = " + currentTagname);

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

			String prop = entry.getKey();
			String prop_lower = prop.toLowerCase();

			if (prop_lower.contains(qualifier_lower)) {
				// fix previous and next Whitespaces
				if (mustIncludeWhitespaceInTheBegin)
					prop = " " + prop + "=\"\"";
				else
					prop = prop + "=\"\"";

				cursor = prop.length();

				if (mustIncludeWhitespaceInTheEnd) {
					prop = prop + " ";
					cursor = prop.length() - 1;
				}

				cursor = cursor - 1;

				// TODO: Description of elements in English and Spanish

				String helpInfo = null;
				// Test if the user wants to see help information
				if (NCLEditorPlugin
						.getDefault()
						.getPreferenceStore()
						.getBoolean(
								PreferenceConstants.P_SHOW_HELP_INFO_ON_AUTOCOMPLETE)) {
					// Get documentation of the element to show
					helpInfo = NCLHelper.getNCLHelper().getHelpDescription(
							currentTagname, view);
				}

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

	private void loadImages() {

		Display d = Display.getDefault();
		connectorImage = new Image(d, this.getClass().getProtectionDomain()
				.getCodeSource().getLocation().toString().substring(5)
				+ "icons" + "/" + "conn.png");

		regionImage = new Image(d, this.getClass().getProtectionDomain()
				.getCodeSource().getLocation().toString().substring(5)
				+ "icons" + "/" + "region_.png");

		fileImage = new Image(d, this.getClass().getProtectionDomain()
				.getCodeSource().getLocation().toString().substring(5)
				+ "icons" + "/" + "file.png");
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

			/*System.out.println("Partition text: "
					+ document.get(partitionOffset, region.getLength()));*/
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
