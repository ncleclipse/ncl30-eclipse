package br.ufma.deinf.laws.ncleditor.editor.contentassist;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import br.ufma.deinf.laws.ncleclipse.NCLEditor;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLContentHandler;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLDocument;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLElement;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLParser;
import br.ufma.deinf.laws.ncleclipse.scanners.XMLPartitionScanner;
import br.ufma.deinf.laws.ncleclipse.scanners.XMLTagScanner;
import br.ufma.deinf.laws.ncleclipse.util.ColorManager;

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
		isAttributeValue = isAttributeValue(doc, offset);
		isAttribute = isAttribute(doc, offset);
		isEndTagName = isEndTagName(doc, offset);
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
			} else if (!isTagname(doc, offset))
				computeAttributesProposals(doc, qualifier, offset, propList);
		}
		ICompletionProposal[] proposals = new ICompletionProposal[propList
				.size()];
		propList.toArray(proposals);
		return proposals;
	}

	private void computeEndTagName(IDocument doc, String qualifier, int offset,
			List propList) {
		// TODO Auto-generated method stub
		return;

		/*
		 * int qlen = qualifier.length(); System.out.println("qualifier = " +
		 * qualifier); String fatherTagName = getFatherTagName(doc, offset);
		 * String text = "</"+fatherTagName+">"; CompletionProposal proposal =
		 * new CompletionProposal(text, offset - qlen, qlen, cursor, null, text,
		 * null, null); propList.add(proposal);
		 */
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

		// fazer um filtro para buscar apenas as tags filhas da getFatherTagname
		System.out.println("## Log: Pai da tag onde estou digitando : "
				+ getFatherTagName(doc, offset));
		Map<String, Map<String, Character>> nesting = nclStructure.getNesting();
		Vector<String> childrenStr = new Vector<String>();
		// Procuro todos os filhos da tagname do meu pai e coloco no vector
		// childrenStr

		String fatherTagName = getFatherTagName(doc, offset);
		if (fatherTagName.equals("")) {
			String tagname = "ncl";
			String tagname2 = "<" + tagname;
			if (tagname.startsWith(qualifier) || tagname2.startsWith(qualifier)) {
				String text = computeTagStructure(tagname, indent);

				CompletionProposal proposal = new CompletionProposal(text,
						offset - qlen, qlen, cursor, null, tagname, null, null);
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

					CompletionProposal proposal = new CompletionProposal(text,
							offset - qlen, qlen, cursor, null, tagname, null,
							null);
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
	 * Computa o offset do pai
	 * 
	 * @param document
	 * @param documentOffset
	 * @return
	 */
	private int getFatherPartitionOffset(IDocument document, int documentOffset) {
		try {
			ITypedRegion region = document.getPartition(documentOffset); // região
																			// q eu
																			// estou
			int partitionOffset = region.getOffset();
			// resolve o problema do usuário começar digitando <, ignora a
			// partição atual
			while (region.getType().equals(XMLPartitionScanner.XML_START_TAG)) {
				text = document.get(region.getOffset(), region.getLength());
				region = document.getPartition(--documentOffset);
			}
			Stack<Integer> pilha = new Stack<Integer>();
			do { // procura a tag pai
				text = document.get(region.getOffset(), region.getLength());
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
				region = document.getPartition(partitionOffset);
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
	private String getFatherTagName(IDocument document, int documentOffset) {
		return getCurrentTagname(document, getFatherPartitionOffset(document,
				documentOffset));
	}

	/**
	 * Retorna a partição anterior
	 * 
	 * @param d
	 * @param r
	 * @return
	 */
	public static ITypedRegion getPreviousPartition(IDocument d, ITypedRegion r) {
		if (r == null)
			return null;
		if (r.getOffset() < 1)
			return null;
		ITypedRegion pr = null;
		try {
			pr = d.getPartition(r.getOffset() - 1);
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
	public static ITypedRegion getPreviousTagPartition(IDocument d, int offset)
			throws BadLocationException {
		ITypedRegion partition = d.getPartition(offset);
		if (partition == null
				|| partition.getType()
						.equals(XMLPartitionScanner.XML_START_TAG)) {
			return partition;
		}
		return getPreviousTagPartition(d, partition);
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
	public static ITypedRegion getPreviousTagPartition(IDocument d,
			ITypedRegion r) {
		ITypedRegion partition = getPreviousPartition(d, r);
		while (partition != null
				&& !partition.getType().equals(
						XMLPartitionScanner.XML_START_TAG)) {
			partition = getPreviousPartition(d, partition);
		}
		return partition;
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
	// TODO: fazer contextual, saber de qual tag o cara está digitando e quais
	// os atributos
	// que já foram preenchidos
	private void computeAttributesValuesProposals(IDocument doc,
			String qualifier, int offset, List propList) {
		int qlen = qualifier.length();
		// Verificar se existe valor pré-definido
		String tagname = getCurrentTagname(doc, offset);
		String attribute = getCurrentAttribute(doc, offset);
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
		nclDocument.setParentURI(((NCLEditor) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor())
				.getInputFile().getParent().getLocationURI());
		nclContentHandler.setNclDocument(nclDocument);
		NCLParser parser = new NCLParser();
		parser.setContentHandler(nclContentHandler);
		parser.doParse(nclText);

		// Referencias que precisam de contexto
		String perspective = null;

		// TODO: caso o id esteja definido no ncl, aqui temos um problema
		// Contexto � o pai
		if ((tagname.equals("port") && attribute.equals("component"))
				|| (tagname.equals("context") && attribute.equals("refer"))
				|| (tagname.equals("media") && attribute.equals("refer"))
				|| (tagname.equals("bindRule") && attribute
						.equals("constituint"))
				|| (tagname.equals("switch") && attribute.equals("refer"))
				|| (tagname.equals("defaultComponent") && attribute
						.equals("component"))) {

			String fatherTagName = getFatherTagName(doc, offset);
			try {
				perspective = getAttributeValueFromCurrentTagName(doc,
						getFatherPartitionOffset(doc, offset), "id");
			} catch (Exception e) {
				if (fatherTagName.equals("body")) {
					try {
						perspective = getAttributeValueFromCurrentTagName(doc,
								getFatherPartitionOffset(doc,
										getFatherPartitionOffset(doc, offset)),
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

			String grandFatherTagName = getFatherTagName(doc,
					getFatherPartitionOffset(doc, offset));
			try {
				perspective = getAttributeValueFromCurrentTagName(doc,
						getFatherPartitionOffset(doc, getFatherPartitionOffset(
								doc, offset)), "id");
			} catch (Exception e) {
				if (grandFatherTagName.equals("body")) {
					perspective = getAttributeValueFromCurrentTagName(doc,
							getFatherPartitionOffset(doc,
									getFatherPartitionOffset(doc,
											getFatherPartitionOffset(doc,
													offset))), "id");
				}
			}
		}

		if (tagname.equals("bind") && attribute.equals("role")
				|| (tagname.equals("linkParam") && (attribute.equals("name")))) {
			perspective = getAttributeValueFromCurrentTagName(doc,
					getFatherPartitionOffset(doc, offset), "xconnector");
		}

		if (tagname.equals("bindParam") && attribute.equals("name")) {
			perspective = getAttributeValueFromCurrentTagName(doc,
					getFatherPartitionOffset(doc, getFatherPartitionOffset(doc,
							offset)), "xconnector");
		}

		if (tagname.equals("bind") && attribute.equals("interface")
				|| tagname.equals("port") && attribute.equals("interface")) {
			NCLElement element;
			perspective = getAttributeValueFromCurrentTagName(doc, offset,
					"component");
			element = nclDocument.getElementById(perspective);
			while (element != null
					&& element.getAttributes().get("refer") != null) {
				perspective = element.getAttributeValue("refer");
				System.out.println(perspective);
				element = nclDocument.getElementById(perspective);
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
			
			//compute paths
			File file = null;
			File parent = null;
			URI uri = null;
			int lastIndexofSeparator = qualifier.lastIndexOf("/");
			String children[];
			
			//relative path
			file = new File(currentFile.getLocationURI());
			file = new File(file.getParentFile().toString());
			if(!qualifier.equals("")){
				file = new File(file.toString()+"/"+qualifier);
				if(!file.isDirectory())
					file = new File(file.getParent()+"/");
			}
			//System.out.println("Parent URI = "+file.toURI());
			children = file.list();
			if (children == null) {
				// Either dir does not exist or is not a directory
			} else {
				for (int i = 0; i < children.length; i++) {
					if(lastIndexofSeparator != -1)
						text = qualifier.substring(0, lastIndexofSeparator)+"/"+children[i];
					else text = children[i];
					String parentPath = file.toURI().getPath();
					if(!parentPath.endsWith("/")) parentPath += "/";
					String path = parentPath+children[i];
					//System.out.println("Text = "+ text);
					//System.out.println("Parent = "+ parentPath);
					//System.out.println("Path = "+ path);
					String startWith = null;
					
					if(lastIndexofSeparator != -1) 
							startWith = parentPath+qualifier.substring(lastIndexofSeparator+1);
					else startWith = parentPath+qualifier;
					
					if(path.startsWith(startWith))
					{
						cursor = text.length();
						System.out.println("Attribute Value Proposal = "
								+ text);
						CompletionProposal proposal = new CompletionProposal(
								text, offset - qlen, qlen, cursor, null,
								text, null, null);
						propList.add(proposal);
					}
				}
			}
			if(qualifier.equals("")) return;
			
			
			//TODO: absolute path
			try {
				uri = new URI(qualifier);
				file = new File(uri.getPath());
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				file = new File(qualifier.replaceFirst("file:///", ""));
			}
			System.out.println("Qualifier = "+qualifier);
			System.out.println(file.toString());
			if(!file.isDirectory())
				file = new File(file.getParent()+"/");
			children = file.list();
			if (children == null) {
				// Either dir does not exist or is not a directory
			} else {
				for (int i = 0; i < children.length; i++) {
					if(lastIndexofSeparator != -1)
						text = qualifier.substring(0, lastIndexofSeparator)+"/"+children[i];
					else text = children[i];
					String parentPath = file.toURI().getPath();
					if(!parentPath.endsWith("/")) parentPath += "/";
					String path = parentPath+children[i];
					//System.out.println("Text = "+ text);
					//System.out.println("Parent = "+ parentPath);
					//System.out.println("Path = "+ path);
					String startWith = null;
					
					if(lastIndexofSeparator != -1) 
							startWith = parentPath+qualifier.substring(lastIndexofSeparator+1);
					else startWith = parentPath+qualifier;
					
					if(path.startsWith(startWith))
					{
						cursor = text.length();
						System.out.println("Attribute Value Proposal = "
								+ text);
						CompletionProposal proposal = new CompletionProposal(
								text, offset - qlen, qlen, cursor, null,
								text, null, null);
						propList.add(proposal);
					}
				}
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
					String idAtual = getAttributeValueFromCurrentTagName(doc,
							offset, "id");
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
		System.out.println("Computing Attributes proposals...");
		String currentTagname = getCurrentTagname(doc, offset);
		System.out.println("Current Tag Name = " + currentTagname);

		List<String> attributeTyped = getAttributesTyped(doc, offset);

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

				System.out.println("Attribute Proposal = " + prop);
				CompletionProposal proposal = new CompletionProposal(prop,
						offset - qlen, qlen, cursor, null, view, null, null);

				propList.add(proposal);
			}
		}
	}

	private String getCurrentTagname(IDocument document, int documentOffset) {
		try {
			ITypedRegion region = document.getPartition(documentOffset);
			int partitionOffset = region.getOffset();
			int readLength = region.getLength();
			ColorManager colorManager = new ColorManager();
			scanner = new XMLTagScanner(colorManager);

			String text = document.get(partitionOffset, readLength);
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

	private String getAttributeValueFromCurrentTagName(IDocument doc,
			int offset, String attribute) {
		try {
			ITypedRegion region = doc.getPartition(offset);
			int partitionOffset = region.getOffset();
			int readLength = region.getLength();

			String text = doc.get(partitionOffset, readLength);
			int p = text.indexOf(attribute);
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
					} else
						return value;
				if (firstQuote)
					value += text.charAt(p);
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return "";
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

	private boolean isTagname(IDocument document, int documentOffset) {
		boolean isTagname = false;
		try {
			ITypedRegion region = document.getPartition(documentOffset);
			String text = document.get(region.getOffset(), documentOffset
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

	private boolean isEndTagName(IDocument document, int documentOffset) {
		ITypedRegion region;
		try {
			region = document.getPartition(documentOffset);
			if (region.getType() == XMLPartitionScanner.XML_END_TAG)
				return true;
			return false;
		} catch (BadLocationException e) {
			return false;
		}
	}

	/**
	 * Utilizado para determinar se a palavra corrente que está sendo digitada
	 * é um atributo. Irá retornar verdadeiro se encontrar o padrão no
	 * âmbito da atual partição Tem que melhorar ainda. Falta verificar se é
	 * o valor de um atributo.
	 */
	private boolean isAttribute(IDocument document, int documentOffset) {
		ITypedRegion region;
		try {
			region = document.getPartition(documentOffset);
			if (region.getType() == XMLPartitionScanner.XML_START_TAG)
				return !isTagname(document, documentOffset)
						&& !isAttributeValue(document, documentOffset);
			return false;
		} catch (BadLocationException e) {
			return false;
		}
	}

	String currentAttribute;

	private boolean isAttributeValue(IDocument doc, int offset) {
		ITypedRegion region;
		try {
			region = doc.getPartition(offset--);
			if (region.getType() == XMLPartitionScanner.XML_START_TAG) {
				int partitionOffset = region.getOffset();
				currentAttribute = "";
				System.out
						.println("Verificando se está digitando o valor de um atributo");
				boolean firstQuote = true;
				boolean findingAttributeName = false;
				int beginAttributeName = -1, endAttributeName = -1;
				while (true && offset >= partitionOffset) {
					char ch = doc.getChar(offset--);
					if (findingAttributeName) {
						if (Character.isJavaIdentifierPart(ch)) {
							if (endAttributeName == -1) {
								endAttributeName = offset;
							}
							continue;
						}
						if (Character.isWhitespace(ch)
								&& endAttributeName != -1) {
							currentAttribute = doc.get(offset + 2,
									endAttributeName - (offset));
							return true;
						}
						if (Character.isWhitespace(ch))
							continue;
						return false;
					}
					if (ch == '\'' || ch == '\"') {
						if (firstQuote) {
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

	private String getCurrentAttribute(IDocument doc, int offset) {
		if (isAttributeValue(doc, offset))
			return currentAttribute;
		return "";
	}

	private List<String> getAttributesTyped(IDocument doc, int offset) {
		List list = new ArrayList<String>();
		try {
			ITypedRegion region = doc.getPartition(offset);
			if (region.getType() == XMLPartitionScanner.XML_START_TAG) {
				String currentPartition = doc.get(region.getOffset(), region
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
