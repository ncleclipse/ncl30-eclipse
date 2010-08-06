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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator2;

import br.ufma.deinf.gia.labmint.message.Message;
import br.ufma.deinf.laws.ncleclipse.document.NCLSourceDocument;
import br.ufma.deinf.laws.ncleclipse.marker.MarkingErrorHandler;

/**
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 * 
 */
public class NCLErrorFixer implements IMarkerResolutionGenerator2 {
	public IMarkerResolution[] getResolutions(IMarker mk) {
		try {
			Object nclValidatorErrorMsg = mk
					.getAttribute(MarkingErrorHandler.NCLValidatorMessage);
			Object nclSourceDocument = mk
					.getAttribute(MarkingErrorHandler.NCLSourceDocument);

			Message message = null;
			NCLSourceDocument nclDoc = (NCLSourceDocument) MarkingErrorHandler
					.getDocument();

			if (nclValidatorErrorMsg != null) {
				String key = (String) nclValidatorErrorMsg;
				message = MessagesUtilities.get(key);
			}

			if (message != null) {
				List<String> fixMessages = new ArrayList<String>();

				fixMessages = NCLFixErrorMessageHandler.getInstance()
						.getAllFixMessagesToErrorMessage(message.getMsgID(),
								new Object[] { message.getId() });

				ArrayList<IMarkerResolution> fixes = new ArrayList<IMarkerResolution>();
				int msgId = message.getMsgID();
				String description = message.getDescription();
				
				switch (msgId) {

					// DTD Validator
					case 2002: // Invalid attribute '%s' on <%s> element.
						fixes.add (removeAttributeResolution(message, nclDoc, 
								description.substring(description.indexOf("\'") + 1, description.lastIndexOf("\'"))));
						break;

					case 2003: // The attribute '%s' is mandatory but is not
								// present on <%s> element.
						fixes.add (addAttributeResolution(message, nclDoc, 
								description.substring(description.indexOf("\'") + 1, description.lastIndexOf("\'"))));
						break;

					case 2005: // The <%s> element is an invalid child of <%s>
								// element.
						//TODO
						break;

					case 2006: // The <%s> element has more than one <%s> child
								// element that is optional (0 or 1 occurence).
						//TODO
						break;

					case 2007: // The <%s> element must have at least one <%s>
								// child element.
						//TODO
						break;

					case 2008: // The <%s> element must have exactly one <%s>
								// child element.
						//TODO
						break;

					case 2009: // The <%s> element must have at least one child
								// element of two possible names.
						//TODO
						break;

					case 2010: // The <%s> element must have one child element.
						//TODO
						break;

					case 2011: // The <%s> element must have one child element.
						//TODO
						break;

					case 2012: // The <assessmentStatement> element must have
								// two children <attributeAssessment>
								// elements or an <attributeAssessment> and a
								// <valueAssessment> children elements.
						//TODO
						break;

					// Semantic Validator
					case 3001: // There are more than one element with
								// identifier "%s".
						fixes.add ( removeElementResolution(message, nclDoc) );
						break;

					case 3002: // There are more than one element with alias
								// "%s"
						//TODO
						break;

					// ImportBase
					case 3101: // <importBase> element must have a valid
								// 'documentURI' attribute.
						//TODO
						break;

					case 3102: // <importBase> element must have the 'alias'
								// attribute.
						//TODO
						break;

					// Bind
					case 3201: // The <bind> element has the 'interface'
								// attribute, but not the 'component' attribute.
						//TODO
						break;

					case 3202: // Element pointed by <bind>'s 'interface'
								// attribute must be an <area>, <property>,
								// <port> or <switchPort> element.
						//TODO
						break;

					case 3203: // There are not a <descriptor> or
								// <descriptorSwitch> element with '%s'
								// identifier.
						//TODO
						break;

					case 3204: // The element with '%s' identifier is not a
								// <media>, <context>, <body> or <switch> valid
								// element in this context.
						//TODO
						break;

					// CausalConnector
					case 3301: // The 'role' attribute value must be unique in
								// the <causalConnector> with id '%s'.
						//TODO
						break;

					// Context
					case 3401: // The 'refer' attribute (with '%s' value) points
								// to an element that is not a <context>.

						//TODO
						break;

					// DefaultComponent
					case 3501: // There is not a <media>, <context> or <switch>
								// element with '%s' identifier.
						//TODO
						break;

					case 3502: // The element pointed by the DefaultComponent
								// (%s) isn't in a valid context.
						//TODO
						break;

					// DefaultDescriptor
					case 3601: // There is not a <descriptor> element with
							   // identifier '%s'.
						
						fixes = simpleReferenceError(message, nclDoc, "descriptor");

						break;

					// Descriptor
					case 3701: // There is not a <region> element with
								// identifier '%s'.
						fixes = simpleReferenceError(message, nclDoc, "region");						
						break;

					// Link
					case 3901: // The role '%s' was not defined on
								// <causalConnector> with id '%s'.
						//TODO
						break;

					case 3902: // There are %s elements with attribute
								// 'role'='%s'. The minimum cardinality is %s.
						//TODO
						break;

					case 3903: // There are %s elements with attribute
								// 'role'='%s'. The maximum cardinality is %s.
						//TODO
						break;

					case 3904: // The <bind> element with 'role' attribute value
								// '%s' must appear at least %s time(s).
						//TODO
						break;

					case 3905: // The 'xconnector' attribute with value '%s' is
								// not a valid identifier of a <causalConnector>
								// element.
						//TODO
						break;

					case 3906: // The parameter with name '%s' must be defined
								// in this link.
						//TODO
						break;

					case 3907: // The attribute 'role' with value '%s' is not
								// defined on 'casualConnector' %s.
						//TODO
						break;

					// Mapping
					case 4001: // The <mapping> element has an attribute
								// 'interface', but it has not the attribute
								// 'component'.
						//TODO
						break;

					case 4002: // The element pointed by the attribute
								// 'interface' on <mapping> element must be the
								// identifier
								// of a child element of the node with id='%s'.
						//TODO
						break;

					case 4003: // The element pointed by the attribute
								// 'component' (%s) on <mapping> element must be
								// a <context>, <media> or <switch> element.
						//TODO
						break;

					// Media
					case 4102: // The element pointed by the attribute 'refer'
								// (%s) must be a <media> element.
						//TODO
						break;

					case 4104: // The attribute 'type' is mandatory when the
								// attribute 'src' is not present on <media>
								// element.
						fixes.add ( addAttributeResolution(message, nclDoc, "src") );
						fixes = simpleAttributeError(message, nclDoc, "type");
						break;

					case 4106: // The element pointed by the attribute
								// 'descriptor' (%s) must be a <descriptor> or
								// <descriptorSwitch> element.
						fixes = simpleReferenceError(message, nclDoc, "descriptor");
						break;


					case 4108: // The attribute 'type' is mandatory if the
								// attribute 'src' is not present.
						fixes.add ( addAttributeResolution(message, nclDoc, "src") );
						fixes = simpleAttributeError(message, nclDoc, "type");
						
						break;

					// Port
					case 4201: // The element pointed by the attribute
								// 'component' (%s) is not a <media>, <context>
								// or <switch> element.
						//TODO
						break;

					case 4202: // The element pointed by the attribute
								// 'component' must be in the same context as
								// the <port> element (%s).
						//TODO
						break;

					case 4203: // The <port> element has an attribute
								// 'interface' but it has not an attribute
								// 'component'.
						//TODO
						break;

					case 4204: // The element pointed by the attribute
								// 'interface' on <port> element (%s) must be an
								// <area>, <property>,
								// <port> or <switchPort> element.
						//TODO
						break;

					case 4205: // The element pointed by the attribute
								// 'interface' on <port> element (%s) must be a
								// child element of the element with id='%s'.
						//TODO
						break;

					// RegionBase
					case 4301: // The attribute 'device' must be of the format
								// "systemScreen(i)" or "systemAudio(i)", where
								// i is an integer.
						//TODO
						break;

					// SimpleAction e SimpleCondition
					case 4401: // The attribute 'qualifier' is mandatory when
								// the attribute 'max' value is greater than 1
								// or 'unbounded'.
						//TODO
						break;

					// Switch
					case 4501: // The element pointed by the attribute 'refer'
								// (%s) must be a <switch> element.
						//TODO
						break;

					case 4502: // The element (%s) does not have a
								// defaultComponent or bindRule pointing to it.
						//TODO
						break;

					// BindParam and LinkParam
					case 4601: // There isn't the connectorParam with name '%s'
								// in the connector '%s'.
						//TODO
						break;

					// BindRule
					case 4701: // The element pointed by the attribute
								// 'constituent' (%s) on <bindRule> element must
								// be a <context>,
								// <media> or <switch> element.
						//TODO
						break;

					case 4702: // The element pointed by the attribute
								// 'constituent' (%s) on <bindRule> element must
								// be a <descriptor> element.
						//TODO
						break;

					case 4703: // The element pointed by the attribute
								// 'constituent' (%s) on <bindRule> element must
								// be in the same perspective that bindRule.
						//TODO
						break;

					case 4704: // The element pointed by the attribute 'rule'
								// (%s) on <bindRule> element must be a <rule>
								// element.
						//TODO
						break;

					default:
						return null;
				}

				/*for (int i = 0; i < fixMessages.size(); i++) {
					fixes.add(new QuickFix("Fix #" + (i + 1) + ": "
							+ fixMessages.get(i), message, nclDoc));
				}*/

				IMarkerResolution[] resolutions = new IMarkerResolution[fixes
						.size()];
				fixes.toArray(resolutions);
				return resolutions;

			}
			return new IMarkerResolution[0];

		} catch (CoreException e) {
			e.printStackTrace();
			return new IMarkerResolution[0];
		}
	}
	
	private ArrayList <IMarkerResolution> simpleReferenceError (Message message, NCLSourceDocument nclDoc, String element){
		ArrayList <IMarkerResolution> fixes = new ArrayList<IMarkerResolution>();
		
		fixes.add ( addElementResolution(message, nclDoc, element) );
		
		fixes.add ( removeElementResolution(message, nclDoc) );
		
		ArrayList <String> IDs = nclDoc.getAllElementsOfType(element);
		for (String Id : IDs){
			fixes.add( changeAttributeResolution(message, nclDoc, element, Id) );
		}
		
		return fixes;
	}
	
	private ArrayList <IMarkerResolution> simpleReferenceError (Message message, NCLSourceDocument nclDoc, ArrayList <String> element){
		ArrayList <IMarkerResolution> fixes = new ArrayList<IMarkerResolution>();
		
		for (String str : element)
			fixes.addAll( simpleReferenceError(message, nclDoc, str) );
		
		return fixes;
	}
	
	private ArrayList <IMarkerResolution> simpleAttributeError (Message message, NCLSourceDocument nclDoc, String attribute){
		ArrayList <IMarkerResolution> fixes = new ArrayList<IMarkerResolution>();
		
		fixes.add ( addAttributeResolution(message, nclDoc, attribute) );
		
		fixes.add ( removeElementResolution(message, nclDoc) );
		
		return fixes;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IMarkerResolutionGenerator2#hasResolutions(org.eclipse.core.resources.IMarker)
	 */
	public boolean hasResolutions(IMarker marker) {
		System.out.println("NCLErrorFixer::hasResolutions");
		return true;
	}

	
	private IMarkerResolution addElementResolution (Message message, NCLSourceDocument nclDoc, String element){
		String idNewElement = message.getElement().getAttribute(element); 
		String label = "Add a <" + element + "> with identifier \""
				+ idNewElement + "\"";
		return new QuickFix(label, message, nclDoc, FixType.ADD_ELEMENT,
				new String[] { element, idNewElement });
	}
	
	private IMarkerResolution addAttributeResolution (Message message, NCLSourceDocument nclDoc, String attribute){ 
		String label = "Add \"" + attribute + "\" attribute in element \"" + message.getId() +"\"";
		return new QuickFix(label, message, nclDoc, FixType.SET_ATTRIBUTE,
				new String[] { attribute, "" });
	}
	
	private IMarkerResolution removeAttributeResolution (Message message, NCLSourceDocument nclDoc, String attribute){ 
		String label = "Remove \"" + attribute + "\" attribute of the element \"" + message.getId() +"\"";
		return new QuickFix(label, message, nclDoc, FixType.REMOVE_ATTRIBUTE,
				new String[] { attribute});
	}
	
	private IMarkerResolution removeElementResolution (Message message, NCLSourceDocument nclDoc){
		String label = "Remove <" + message.getElement().getTagName() + 
						"> element with identifier \"" + message.getId() + "\"";
		return new QuickFix(label, message, nclDoc, FixType.REMOVE_ELEMENT, 
			new String [] { message.getId() });
	}
	
	private IMarkerResolution changeAttributeResolution (Message message, NCLSourceDocument nclDoc, String attribute, String value){
		String label = "Change \"" + attribute + "\" attribute to \"" + value + "\"";
		return new QuickFix(label, message, nclDoc, FixType.SET_ATTRIBUTE, 
					  new String [] { attribute, value});
		
	}
}
