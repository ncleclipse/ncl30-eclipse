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
package br.ufma.deinf.laws.ncleclipse.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import br.ufma.deinf.laws.ncleclipse.NCLEditor;
import br.ufma.deinf.laws.ncleclipse.NCLMultiPageEditor;

/**
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 *
 */
public class UncommentSelection implements IEditorActionDelegate {

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	@Override
	public void run(IAction action) {
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();
		NCLEditor editor = ((NCLMultiPageEditor) page.getActiveEditor())
				.getNCLEditor();

		TextSelection selection = (TextSelection) editor.getSelectionProvider()
				.getSelection();
		IDocument doc = editor.getInputDocument();

		String commentBegin = "<!--";
		String commentEnd = "-->";
		
		int startLine  = selection.getStartLine();
		int endLine = selection.getEndLine();

		if (selection.getLength() > 0) {
			try {
				int offset;
				offset = selection.getOffset();
				IRegion region = doc.getLineInformationOfOffset(offset);
				String text = doc.get(doc.getLineOffset(startLine), offset - region.getOffset());
				
				int index = text.lastIndexOf(commentBegin);
				if (index != -1)
					doc.replace(doc.getLineOffset(startLine) + index, commentBegin.length(), "");
				else{
					text = selection.getText();
					index = text.indexOf(commentBegin);
					doc.replace(offset + index, commentBegin.length(), "");
				}
				
				offset += selection.getLength();
				region = doc.getLineInformationOfOffset(offset);
				text = doc.get(offset, doc.getLineLength(endLine) - (offset - region.getOffset()) );
				index = text.indexOf(commentEnd);
				if (index != -1)
					doc.replace(offset, commentEnd.length(), "");
				else{
					text = selection.getText();
					index = text.lastIndexOf(commentEnd);
					if (index != -1)
						doc.replace(selection.getOffset() + index, commentEnd.length(), "");
				}
				
				
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		} else {
			// TODO: Comment the range!
			System.out.println("Comment the range!");
		}

		return;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorActionDelegate#setActiveEditor(org.eclipse.jface.action.IAction, org.eclipse.ui.IEditorPart)
	 */
	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		// TODO Auto-generated method stub

	}

}
