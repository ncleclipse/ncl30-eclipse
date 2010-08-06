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
package br.ufma.deinf.laws.ncleclipse.marker;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.xml.sax.SAXParseException;

import br.ufma.deinf.gia.labmint.composer.NCLValidator;
import br.ufma.deinf.gia.labmint.message.Message;
import br.ufma.deinf.laws.ncleclipse.NCLEditorMessages;
import br.ufma.deinf.laws.ncleclipse.correction.MessagesUtilities;
import br.ufma.deinf.laws.ncleclipse.xml.XMLValidationError;
import br.ufma.deinf.laws.ncleclipse.xml.XMLValidationErrorHandler;

/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 * 
 */
public class MarkingErrorHandler extends XMLValidationErrorHandler {
	public static String NCLValidatorMessage = "NCLValidatorMessageErrorID";
	public static String NCLSourceDocument = "NCLSourceDocument";
	public static String NCLMarkerError = "br.ufma.deinf.laws.ncleclipse.problemmarker";

	private static IDocument document;
	private IResource file;

	public MarkingErrorHandler(IResource file, IDocument document) {
		super();
		this.file = file;
		this.document = document;
	}
	
	public static IDocument getDocument (){
		return document;
	}

	public void removeExistingMarkers() {
		try {
			file.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ZERO);
			MessagesUtilities.clear();
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
	}

	protected XMLValidationError nextError(SAXParseException e, boolean isFatal) {

		XMLValidationError validationError = super.nextError(e, isFatal);

		Map map = new HashMap();
		int lineNumber = e.getLineNumber();
		int columnNumber = e.getColumnNumber();
		MarkerUtilities.setLineNumber(map, lineNumber);
		Object[] tmp = { e.getMessage() };

		MarkerUtilities.setMessage(map, NCLEditorMessages.getInstance()
				.getString("NCLValidator.Error.XMLParserError", tmp));
		map.put(IMarker.LOCATION, file.getFullPath().toString());

		Integer charStart = getCharStart(lineNumber, columnNumber);
		if (charStart != null)
			map.put(IMarker.CHAR_START, charStart);

		Integer charEnd = getCharEnd(lineNumber, columnNumber);
		if (charEnd != null)
			map.put(IMarker.CHAR_END, charEnd);

		map.put(IMarker.SEVERITY, new Integer(IMarker.SEVERITY_ERROR));

		try {
			MarkerUtilities.createMarker(file, map, IMarker.PROBLEM);
		} catch (CoreException ee) {
			ee.printStackTrace();
		}

		return validationError;

	}

	private Integer getCharEnd(int lineNumber, int columnNumber) {
		try {
			return new Integer(document.getLineOffset(lineNumber - 1)
					+ columnNumber);
		} catch (BadLocationException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Integer getCharStart(int lineNumber, int columnNumber) {
		try {
			int lineStartChar = document.getLineOffset(lineNumber - 1);
			Integer charEnd = getCharEnd(lineNumber, columnNumber);
			if (charEnd != null) {
				ITypedRegion typedRegion = document.getPartition(charEnd
						.intValue() - 2);
				int partitionStartChar = typedRegion.getOffset();
				return new Integer(partitionStartChar);
			} else
				return new Integer(lineStartChar);
		} catch (BadLocationException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void MarkNCLValidatorErrorsAndWarnings() {
		// TODO: Falta pegar a posição do erro e/ou warning!
		Vector<Message> warnings = NCLValidator.getWarnings();
		Vector<Message> erros = NCLValidator.getErrors();
		// Imprime os warning
		Map map = new HashMap();
		map.put(IMarker.LOCATION, file.getFullPath().toString());
		map.put(IMarker.SEVERITY, new Integer(IMarker.SEVERITY_WARNING));
		for (int i = 0; i < warnings.size(); i++) {
			try {
				int lineNumber = (new Integer((String) warnings.get(i)
						.getElement().getUserData("startLine"))).intValue();
				int columnNumber = (new Integer((String) warnings.get(i)
						.getElement().getUserData("startColumn"))).intValue();
				Integer charStart = getCharStart(lineNumber, columnNumber);
				if (charStart != null)
					map.put(IMarker.CHAR_START, charStart);

				Integer charEnd = getCharEnd(lineNumber, columnNumber);
				if (charEnd != null)
					map.put(IMarker.CHAR_END, charEnd);

				MarkerUtilities.setMessage(map, warnings.get(i)
						.getDescription());
				MarkerUtilities.setLineNumber(map, new Integer(
						(String) warnings.get(i).getElement().getUserData(
								"startLine")));
				MarkerUtilities.createMarker(file, map, IMarker.PROBLEM);

			} catch (CoreException ee) {
				ee.printStackTrace();
			}
		}
		// Imprime os erros
		map.put(IMarker.SEVERITY, new Integer(IMarker.SEVERITY_ERROR));
		for (int i = 0; i < erros.size(); i++) {
			try {
				int lineNumber = (new Integer((String) erros.get(i)
						.getElement().getUserData("startLine"))).intValue();
				int columnNumber = (new Integer((String) erros.get(i)
						.getElement().getUserData("startColumn"))).intValue();
				Integer charStart = getCharStart(lineNumber, columnNumber);
				if (charStart != null)
					map.put(IMarker.CHAR_START, charStart);

				Integer charEnd = getCharEnd(lineNumber, columnNumber);
				if (charEnd != null)
					map.put(IMarker.CHAR_END, charEnd);

				
				MessagesUtilities.put(erros.get(i).toString(), erros.get(i));
				
				map.put(MarkingErrorHandler.NCLValidatorMessage, erros.get(i).toString());
				map.put(MarkingErrorHandler.NCLSourceDocument, document.get());
				
				
				MarkerUtilities.setMessage(map, erros.get(i).getDescription());
				MarkerUtilities.setLineNumber(map, new Integer((String) erros
						.get(i).getElement().getUserData("startLine")));
				
				
				MarkerUtilities.createMarker(file, map, NCLMarkerError);
				
				
				
			} catch (CoreException ee) {
				ee.printStackTrace();
			}
		}
	}
}
