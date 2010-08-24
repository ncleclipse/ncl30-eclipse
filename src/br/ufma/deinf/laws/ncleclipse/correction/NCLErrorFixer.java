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
package br.ufma.deinf.laws.ncleclipse.correction;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator2;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import br.ufma.deinf.gia.labmint.message.Message;
import br.ufma.deinf.laws.ncl.NCLReference;
import br.ufma.deinf.laws.ncl.NCLStructure;
import br.ufma.deinf.laws.ncleclipse.document.NCLSourceDocument;
import br.ufma.deinf.laws.ncleclipse.marker.MarkingErrorHandler;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLContentHandler;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLDocument;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLElement;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLParser;

/**
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 * 
 */
public class NCLErrorFixer implements IMarkerResolutionGenerator2 {
	public IMarkerResolution[] getResolutions(IMarker mk) {
		try {
			Object nclValidatorErrorMsg = mk
					.getAttribute(MarkingErrorHandler.NCLValidatorMessage);
			
			Message message = null;
			NCLSourceDocument nclDoc = (NCLSourceDocument) MarkingErrorHandler
					.getDocument();
			ArrayList<IMarkerResolution> fixes = new ArrayList<IMarkerResolution>();
			String key = (String) nclValidatorErrorMsg;
			
  			if (nclValidatorErrorMsg != null) {
				message = MessagesUtilities.get(key);
			}
  			
  			if (message == null){
  				if (nclValidatorErrorMsg != null && nclDoc != null && key.startsWith("XML sintatic error"))
  					fixes.add(new QuickFix("Try to correct NCL structure", null, nclDoc, FixType.SINTATIC_ERROR, 0, null));
  			}
  				
  			else {
				List<String> fixMessages = new ArrayList<String>();

				
				int msgId = message.getMsgID();
				String description = message.getDescription();

				int offset = 0;
				int lineNumber = (Integer) mk.getAttribute("lineNumber");

				try {
					offset = nclDoc.getPreviousTagPartition(
							nclDoc.getLineInformation(lineNumber).getOffset())
							.getOffset();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}

				String tagname = nclDoc.getCurrentTagname(offset);

 				switch (msgId) {
					// DTD Validator
					case 2002: // Invalid attribute '%s' on <%s> element.
						fixes.add(removeAttributeResolution(message, nclDoc,
								description.substring(
										description.indexOf("\'") + 1,
										description.lastIndexOf("\'")), offset));
						break;

					case 2003: // The attribute '%s' is mandatory but is not
								// present on <%s> element.
						fixes.add(addAttributeResolution(message, nclDoc,
								description.substring(
										description.indexOf("\'") + 1,
										description.lastIndexOf("\'")), offset));
						break;

					case 2005: // The <%s> element is an invalid child of <%s>
								// element.
						fixes.add(removeElementResolution(message, nclDoc,
								offset));
						break;

					case 2006: // The <%s> element has more than one <%s> child
								// element that is optional (0 or 1 occurence).
						// TODO
						break;

					case 2007: // The <%s> element must have at least one <%s>
								// child element.
						fixes.add(addChildResolution(message, nclDoc,
								description.substring(
										description.lastIndexOf('<' + 1),
										description.lastIndexOf('>')), "", "", offset));
						break;

					case 2008: // The <%s> element must have exactly one <%s>
								// child element.
						// TODO
						break;

					case 2009: // The <%s> element must have at least one child
								// element of two possible names.
						// TODO
						break;

					case 2010: // The <%s> element must have one child element.
						// TODO
						break;

					case 2011: // The <%s> element must have one child element.
						// TODO
						break;

					case 2012: // The <assessmentStatement> element must have
								// two children <attributeAssessment>
								// elements or an <attributeAssessment> and a
								// <valueAssessment> children elements.
						// TODO
						break;

					// Semantic Validator
					case 3001: // There are more than one element with
								// identifier "%s".
						fixes.add(removeElementResolution(message, nclDoc,
								offset));
						break;

					case 3002: // There are more than one element with alias
								// "%s"
						fixes.add(removeElementResolution(message, nclDoc,
								offset));
						break;

					// ImportBase
					case 3102: // <importBase> element must have the 'alias'
								// attribute.
						fixes.add(addAttributeResolution(message, nclDoc,
								"alias", offset));
						break;

					// Bind
					case 3201: // The <bind> element has the 'interface'
								// attribute, but not the 'component' attribute.
						int interfaceOffset = nclDoc.getOffsetByID(nclDoc
								.getAttributeValueFromCurrentTagName(offset,
										"interface"));
						if (interfaceOffset == -1) {
							fixes = referenceErrorWithPerspective(message,
									nclDoc, tagname, "component", offset);
							break;
						} else {
							int fatherOffset = nclDoc
									.getFatherPartitionOffset(interfaceOffset);
							String fatherId = nclDoc
									.getAttributeValueFromCurrentTagName(
											fatherOffset, "id");
							fixes.add(addAttributeResolution(message, nclDoc,
									"component", fatherId, offset));
						}
						break;

					case 3202: // Element pointed by <bind>'s 'interface'
								// attribute must be an <area>, <property>,
								// <port> or <switchPort> element.
						fixes = interfaceReferenceError(message, nclDoc, offset);
						break;

					case 3203: // There are not a <descriptor> or
								// <descriptorSwitch> element with '%s'
								// identifier.
						fixes = simpleReferenceError(message, nclDoc,
								"descriptor", offset);
						break;

					case 3204: // The element with '%s' identifier is not a
								// <media>, <context>, <body> or <switch> valid
								// element in this context.
						fixes = referenceErrorWithPerspective(message, nclDoc,
								tagname, "component", offset);
						fixes.add(addElementResolution(message, nclDoc,
								"context", offset));
						fixes.add(addElementResolution(message, nclDoc,
								"media", offset));
						fixes.add(addElementResolution(message, nclDoc,
								"switch", offset));

						break;

					// Context
					case 3401: // The 'refer' attribute (with '%s' value) points
								// to an element that is not a <context>.

						fixes = referErrorReference(message, nclDoc, tagname,
								offset);
						break;

					// DefaultComponent
					case 3501: // There is not a <media>, <context> or <switch>
								// element with '%s' identifier.
					case 3502: // The element pointed by the DefaultComponent
								// (%s) isn't in a valid context.
						fixes = referenceErrorWithPerspective(message, nclDoc,
								tagname, "component", offset);
						break;

					// DefaultDescriptor
					case 3601: // There is not a <descriptor> element with
								// identifier '%s'.

						fixes = simpleReferenceError(message, nclDoc,
								"descriptor", offset);

						break;

					// Descriptor
					case 3701: // There is not a <region> element with
								// identifier '%s'.
						fixes = simpleReferenceError(message, nclDoc, "region",
								offset);
						break;

					// Link
					case 3904: // The <bind> element with 'role' attribute value
								// '%s' must appear at least %s time(s).
						int index = description.lastIndexOf("'");
						String role = "";
						for (int i = index - 1; i >= 0 && description.charAt(i) != '\''; i--)
							role = description.charAt(i) + role;
						
						fixes.add(addChildResolution(message, nclDoc, "bind", "role", role, offset));
						break;

					case 3905: // The 'xconnector' attribute with value '%s' is
								// not a valid identifier of a <causalConnector>
								// element.
						fixes = simpleReferenceError(message, nclDoc,
								"causalConnector", offset);
						break;

					// Mapping
					case 4001: // The <mapping> element has an attribute
								// 'interface', but it has not the attribute
								// 'component'.
					case 4002: // The element pointed by the attribute
								// 'interface' on <mapping> element must be the
								// identifier
								// of a child element of the node with id='%s'.
						fixes = interfaceReferenceError(message, nclDoc, offset);
						break;

					case 4003: // The element pointed by the attribute
								// 'component' (%s) on <mapping> element must be
								// a <context>, <media> or <switch> element.
						fixes = referenceErrorWithPerspective(message, nclDoc,
								tagname, "component", offset);
						fixes.add(addElementResolution(message, nclDoc,
								"context", offset));
						fixes.add(addElementResolution(message, nclDoc,
								"media", offset));
						fixes.add(addElementResolution(message, nclDoc,
								"switch", offset));
						break;

					// Media
					case 4102: // The element pointed by the attribute 'refer'
								// (%s) must be a <media> element.
						fixes = referErrorReference(message, nclDoc, tagname,
								offset);
						break;

					case 4104: // The attribute 'type' is mandatory when the
								// attribute 'src' is not present on <media>
								// element.
						fixes = simpleAttributeError(message, nclDoc, "type",
								offset);
						fixes.add(addAttributeResolution(message, nclDoc,
								"src", offset));
						break;

					case 4106: // The element pointed by the attribute
								// 'descriptor' (%s) must be a <descriptor> or
								// <descriptorSwitch> element.
						fixes = simpleReferenceError(message, nclDoc,
								"descriptor", offset);
						break;

					case 4108: // The attribute 'type' is mandatory if the
								// attribute 'src' is not present.
						fixes = simpleAttributeError(message, nclDoc, "type",
								offset);
						fixes.add(addAttributeResolution(message, nclDoc,
								"src", offset));
						break;

					// Port
					case 4201: // The element pointed by the attribute
								// 'component' (%s) is not a <media>, <context>
								// or <switch> element.
					case 4202: // The element pointed by the attribute
								// 'component' must be in the same context as
								// the <port> element (%s).
						int anchorffset = nclDoc.getOffsetByID(nclDoc
								.getAttributeValueFromCurrentTagName(offset,
										"interface"));
						if (anchorffset == -1) {
							fixes = referenceErrorWithPerspective(message,
									nclDoc, tagname, "component", offset);
							break;
						} else {
							int fatherOffset = nclDoc
									.getFatherPartitionOffset(anchorffset);
							String fatherId = nclDoc
									.getAttributeValueFromCurrentTagName(
											fatherOffset, "id");
							fixes.add(addAttributeResolution(message, nclDoc,
									"component", fatherId, offset));
						}

						break;

					case 4203: // The <port> element has an attribute
								// 'interface' but it has not an attribute
								// 'component'.
						int elementOffset = nclDoc.getOffsetByID(nclDoc
								.getAttributeValueFromCurrentTagName(offset,
										"interface"));
						if (elementOffset == -1)
							fixes.add(addAttributeResolution(message, nclDoc,
									"component", offset));
						else {
							int fatherOffset = nclDoc
									.getFatherPartitionOffset(elementOffset);
							String fatherId = nclDoc
									.getAttributeValueFromCurrentTagName(
											fatherOffset, "id");
							fixes.add(addAttributeResolution(message, nclDoc,
									"component", fatherId, offset));
						}
						break;

					case 4204: // The element pointed by the attribute
								// 'interface' on <port> element (%s) must be an
								// <area>, <property>,
								// <port> or <switchPort> element.
					case 4205: // The element pointed by the attribute
								// 'interface' on <port> element (%s) must be a
								// child element of the element with id='%s'.
						fixes = interfaceReferenceError(message, nclDoc, offset);
						break;

					// Switch
					case 4501: // The element pointed by the attribute 'refer'
								// (%s) must be a <switch> element.
						fixes = referErrorReference(message, nclDoc, tagname,
								offset);
						break;

					// BindRule
					case 4701: // The element pointed by the attribute
								// 'constituent' (%s) on <bindRule> element must
								// be a <context>,
								// <media> or <switch> element.
					case 4703: // The element pointed by the attribute
								// 'constituent' (%s) on <bindRule> element must
								// be in the same perspective that bindRule.
						fixes = referenceErrorWithPerspective(message, nclDoc,
								tagname, "constituent", offset);
						break;

					case 4704: // The element pointed by the attribute 'rule'
								// (%s) on <bindRule> element must be a <rule>
								// element.
						fixes = simpleReferenceError(message, nclDoc, "rule",
								offset);
						break;

					default:
						return new IMarkerResolution[0];
				}
			}
  			if (fixes.size() == 0)
  				return new IMarkerResolution[0];
			IMarkerResolution[] resolutions = new IMarkerResolution[fixes
			                                						.size()];
			                                				fixes.toArray(resolutions);
			                                				return resolutions;

		} catch (CoreException e) {
			e.printStackTrace();
			return new IMarkerResolution[0];
		}
	}

	private ArrayList<IMarkerResolution> interfaceReferenceError(
			Message message, NCLSourceDocument nclDoc, int offset) {
		ArrayList<IMarkerResolution> fixes = new ArrayList<IMarkerResolution>();
		int componentOffset = nclDoc.getOffsetByID(nclDoc
				.getAttributeValueFromCurrentTagName(offset, "component"));
		if (componentOffset == -1)
			return fixes;
		String tag = nclDoc.getCurrentTagname(componentOffset);
		Vector<Integer> childrenOffsets = nclDoc
				.getChildrenOffsets(componentOffset);
		for (int Offset : childrenOffsets) {
			String id = "";
			if ((tag.equals("media") && nclDoc.getCurrentTagname(Offset)
					.equals("area"))
					|| tag.equals("context")
					&& nclDoc.getCurrentTagname(Offset).equals("port")
					|| tag.equals("switch")
					&& nclDoc.getCurrentTagname(Offset).equals("switchPort")) {

				id = nclDoc.getAttributeValueFromCurrentTagName(Offset, "id");
				if (id != null && !id.equals(""))
					fixes.add(changeAttributeResolution(message, nclDoc,
							"interface", id, offset));
			}
		}
		fixes.add(removeAttributeResolution(message, nclDoc, "interface",
				offset));

		return fixes;
	}

	private ArrayList<IMarkerResolution> simpleReferenceError(Message message,
			NCLSourceDocument nclDoc, String element, int offset) {
		ArrayList<IMarkerResolution> fixes = new ArrayList<IMarkerResolution>();

		fixes.add(addElementResolution(message, nclDoc, element, offset));

		fixes.add(removeElementResolution(message, nclDoc, offset));

		ArrayList<String> IDs = nclDoc.getAllElementsOfType(element);
		for (String id : IDs) {
			fixes.add(changeAttributeResolution(message, nclDoc, element, id,
					offset));
		}

		if (element.equals("descriptor")) {
			IDs = nclDoc.getAllElementsOfType("descriptorSwitch");
			for (String id : IDs) {
				fixes.add(changeAttributeResolution(message, nclDoc, element,
						id, offset));
			}
		}

		return fixes;
	}

	private ArrayList<IMarkerResolution> simpleReferenceError(Message message,
			NCLSourceDocument nclDoc, ArrayList<String> element, int offset) {
		ArrayList<IMarkerResolution> fixes = new ArrayList<IMarkerResolution>();

		for (String str : element)
			fixes.addAll(simpleReferenceError(message, nclDoc, str, offset));

		return fixes;
	}

	private ArrayList<IMarkerResolution> simpleAttributeError(Message message,
			NCLSourceDocument nclDoc, String attribute, int offset) {
		ArrayList<IMarkerResolution> fixes = new ArrayList<IMarkerResolution>();

		fixes.add(addAttributeResolution(message, nclDoc, attribute, offset));

		fixes.add(removeElementResolution(message, nclDoc, offset));

		return fixes;
	}

	private ArrayList<IMarkerResolution> referErrorReference(Message message,
			NCLSourceDocument nclDoc, String tagname, int offset) {
		ArrayList<IMarkerResolution> fixes = new ArrayList<IMarkerResolution>();
		ArrayList<String> elementsIds = nclDoc.getAllElementsOfType(tagname);
		for (String id : elementsIds) {
			int elementOffset = nclDoc.getOffsetByID(id);
			String refer = nclDoc.getAttributeValueFromCurrentTagName(
					elementOffset, "refer");
			if (refer == null)
				fixes.add(changeAttributeResolution(message, nclDoc, "refer",
						id, offset));
		}

		fixes.add(removeAttributeResolution(message, nclDoc, "refer", offset));
		fixes.add(removeElementResolution(message, nclDoc, offset));

		return fixes;
	}

	private ArrayList<IMarkerResolution> referenceErrorWithPerspective(
			Message message, NCLSourceDocument nclDoc, String tagname,
			String attribute, int offset) {
		ArrayList<IMarkerResolution> fixes = new ArrayList<IMarkerResolution>();
		File currentFile = null;
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();
		IEditorPart editor = page.getActiveEditor();

		try {
			if (editor.getEditorInput() instanceof IFileEditorInput) {
				currentFile = new File(
						((IFileEditorInput) editor.getEditorInput()).getFile()
								.getLocationURI());
			} else {
				currentFile = new File(
						((IURIEditorInput) editor.getEditorInput()).getURI());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		NCLDocument nclDocument;
		String nclText = nclDoc.get();
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
		}
		String fatherTagName = nclDoc.getFatherTagName(offset);

		String perspective = nclDoc.getAttributeValueFromCurrentTagName(
				nclDoc.getFatherPartitionOffset(offset), "id");

		if ((tagname.equals("mapping") || tagname.equals("bind"))
				&& attribute.equals("component")) {
			perspective = nclDoc.getAttributeValueFromCurrentTagName(nclDoc
					.getFatherPartitionOffset(nclDoc
							.getFatherPartitionOffset(offset)), "id");
		}

		if (perspective == null)
			if (fatherTagName.equals("body"))
				perspective = nclDoc.getAttributeValueFromCurrentTagName(nclDoc
						.getFatherPartitionOffset(nclDoc
								.getFatherPartitionOffset(offset)), "id");

		Collection nclReference = NCLStructure.getInstance().getNCLReference(
				tagname, attribute);
		if (nclReference == null)
			return fixes;

		CompletionProposal proposal = null;
		Iterator it = null;

		if (perspective != null) {
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
					String elementId = nclElement.getAttributeValue(nclRefAtual
							.getRefAttribute());

					if (elementId == null)
						continue;

					fixes.add(changeAttributeResolution(message, nclDoc,
							attribute, elementId, offset));
				}
			}

		}
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
					NCLElement refElement = ((NCLElement) it2.next());
					String elementId = refElement.getAttributeValue(nclRefAtual
							.getRefAttribute());

					if (elementId == null || elementId.endsWith("#null"))
						continue; // null

					fixes.add(changeAttributeResolution(message, nclDoc,
							attribute, elementId, offset));
				}
			}
		}

		return fixes;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IMarkerResolutionGenerator2#hasResolutions(org.eclipse.core.resources.IMarker)
	 */
	public boolean hasResolutions(IMarker marker) {
		System.out.println("NCLErrorFixer::hasResolutions");
		return true;
	}

	private IMarkerResolution addElementResolution(Message message,
			NCLSourceDocument nclDoc, String element, int offset) {
		String newElement = element;
		String tagname = nclDoc.getCurrentTagname(offset);
		if (element.equals("causalConnector"))
			newElement = "xconnector";
		if ((tagname.equals("bind") && !element.equals("descriptor"))
				|| tagname.equals("port"))
			newElement = "component";
		if (tagname.equals("bindRule"))
			newElement = "constituent";

		String idNewElement = message.getElement().getAttribute(newElement);
		String label = "Add a <" + element + "> with identifier \""
				+ idNewElement + "\"";
		return new QuickFix(label, message, nclDoc, FixType.ADD_ELEMENT,
				offset, new String[] { element, idNewElement });
	}

	private IMarkerResolution addChildResolution(Message message,
			NCLSourceDocument nclDoc, String element, String attribute,
			String value, int offset) {
		String newElement = element;
		String fatherId = nclDoc.getCurrentTagname(offset);

		String idNewElement = message.getElement().getAttribute(newElement);
		String label = "Add a <" + element + "> in element \"" + fatherId
				+ "\"";
		return new QuickFix(label, message, nclDoc, FixType.ADD_CHILD, offset,
				new String[] { element, attribute, value });
	}

	private IMarkerResolution addAttributeResolution(Message message,
			NCLSourceDocument nclDoc, String attribute, int offset) {
		String id = message.getId();
		if (id == null || id.equals(""))
			id = message.getElement().getTagName();
		String label = "Add \"" + attribute + "\" attribute in element \"" + id
				+ "\"";
		return new QuickFix(label, message, nclDoc, FixType.SET_ATTRIBUTE,
				offset, new String[] { attribute, "" });
	}

	private IMarkerResolution addAttributeResolution(Message message,
			NCLSourceDocument nclDoc, String attribute, String value, int offset) {
		String id = message.getId();
		if (id == null || id.equals(""))
			id = message.getElement().getTagName();
		String label = "Add \"" + attribute + "\" attribute in element \"" + id
				+ "\" with value \"" + value + "\"";
		return new QuickFix(label, message, nclDoc, FixType.SET_ATTRIBUTE,
				offset, new String[] { attribute, value });
	}

	private IMarkerResolution removeAttributeResolution(Message message,
			NCLSourceDocument nclDoc, String attribute, int offset) {
		String id = message.getId();
		if (id == null || id.equals(""))
			id = message.getElement().getTagName();
		String label = "Remove \"" + attribute
				+ "\" attribute of the element \"" + id + "\"";
		return new QuickFix(label, message, nclDoc, FixType.REMOVE_ATTRIBUTE,
				offset, new String[] { attribute });
	}

	private IMarkerResolution removeElementResolution(Message message,
			NCLSourceDocument nclDoc, int offset) {
		String id = message.getId();
		if (id == null)
			id = "";
		String label = "Remove <" + message.getElement().getTagName()
				+ "> element with identifier \"" + id + "\"";
		return new QuickFix(label, message, nclDoc, FixType.REMOVE_ELEMENT,
				offset, new String[] { message.getId() });
	}

	private IMarkerResolution changeAttributeResolution(Message message,
			NCLSourceDocument nclDoc, String attribute, String value, int offset) {
		String label = "Change \"" + attribute + "\" attribute to \"" + value
				+ "\"";
		return new QuickFix(label, message, nclDoc, FixType.SET_ATTRIBUTE,
				offset, new String[] { attribute, value });

	}
}
