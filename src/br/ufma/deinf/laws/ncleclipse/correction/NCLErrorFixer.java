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
import org.eclipse.ui.IMarkerResolutionGenerator;

import br.ufma.deinf.gia.labmint.message.Message;
import br.ufma.deinf.laws.ncleclipse.document.NCLSourceDocument;
import br.ufma.deinf.laws.ncleclipse.marker.MarkingErrorHandler;

/**
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 * 
 */
public class NCLErrorFixer implements IMarkerResolutionGenerator {
	public IMarkerResolution[] getResolutions(IMarker mk) {
		try {
			Object nclValidatorErrorMsg = mk
					.getAttribute(MarkingErrorHandler.NCLValidatorMessage);
			Object nclSourceDocument = mk
					.getAttribute(MarkingErrorHandler.NCLSourceDocument);

			if (nclValidatorErrorMsg instanceof Message) {
				Object message = mk.getAttribute(IMarker.MESSAGE);
				List<String> fixMessages = new ArrayList<String>();
				
				fixMessages = NCLFixErrorMessageHandler.getInstance()
						.getAllFixMessagesToErrorMessage(((Message) nclValidatorErrorMsg)
								.getMsgID(), new Object[]{((Message) nclValidatorErrorMsg).getId()});

				ArrayList<IMarkerResolution> fixes = new ArrayList<IMarkerResolution>();
				
				for (int i = 0; i < fixMessages.size(); i++) {
					fixes.add(new QuickFix("Fix #" + (i+1) + ": " + fixMessages.get(i),
							(Message) nclValidatorErrorMsg,
							(NCLSourceDocument) nclSourceDocument));
				}
				
				IMarkerResolution [] resolutions = new IMarkerResolution[fixes.size()];
				fixes.toArray(resolutions);
				return resolutions;

			}
			return new IMarkerResolution[0];

		} catch (CoreException e) {
			return new IMarkerResolution[0];
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IMarkerResolutionGenerator2#hasResolutions(org.eclipse.core.resources.IMarker)
	 */
	public boolean hasResolutions(IMarker marker) {
		System.out.println("NCLErrorFixer::hasResolutions");
		// TODO: All
		return true;
	}

}
