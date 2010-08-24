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

import org.eclipse.core.resources.IMarker;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMarkerResolution2;

import br.ufma.deinf.gia.labmint.message.Message;
import br.ufma.deinf.laws.ncleclipse.document.NCLSourceDocument;

/**
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 * 
 */
public class QuickFix implements IMarkerResolution2 {
	private String label;
	private Message message;
	private NCLSourceDocument nclSourceDoc;
	private int type;
	private int offset;
	private String [] params;
	
	QuickFix(String label, Message message, NCLSourceDocument nclSourceDoc) {
		this.label = label;
		this.message = message;
		this.nclSourceDoc = nclSourceDoc;
		this.type = FixType.UNKNOW;
		this.params = null;
	}
	 
	QuickFix(String label, Message message, NCLSourceDocument nclSourceDoc, int type, int offset, String [] params) {
		this.label = label;
		this.message = message;
		this.nclSourceDoc = nclSourceDoc;
		this.type = type;
		this.params = params;
		this.offset = offset;
	}

	public String getLabel() {
		return label;
	}

	public void run(IMarker marker) {
		if (type == FixType.ADD_ELEMENT){
			if (params == null || params.length < 2) return;
			String tagname = params[0];
			String id = params[1];
			nclSourceDoc.addElement(tagname, id, offset);
		}
		
		else if (type == FixType.ADD_CHILD){
			if (params == null || params.length < 3) return;
			String child = params[0];
			String attribute = params[1];
			String value = params[2];
			nclSourceDoc.addChild(child, attribute, value,  offset);
		}
		
		else if (type == FixType.REMOVE_CHILD){
			
		}
		
		else if (type == FixType.REMOVE_ELEMENT){
			nclSourceDoc.removeElement(offset);
		}
		
		else if (type == FixType.SET_ATTRIBUTE){
			if (params == null || params.length < 2) return;
			String attribute = params[0];
			String value = params[1];
			nclSourceDoc.setAttribute(attribute, value, offset);
		}
		
		else if (type == FixType.REMOVE_ATTRIBUTE){
			if (params == null || params.length < 1) return;
			String attribute = params[0];
			nclSourceDoc.removeAttribute(attribute, offset);
		}
		
		else if (type == FixType.SINTATIC_ERROR){
			nclSourceDoc.correctNCLStructure ();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IMarkerResolution2#getDescription()
	 */
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IMarkerResolution2#getImage()
	 */
	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return null;
	}
}
