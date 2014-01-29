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
 * @author Rodrigo Costa <rodrim.c@laws.deinf.ufma.br>
 * 
 */
public class CommentSelectionAction implements IEditorActionDelegate{
	final String commentBegin = "<!--";
	final String commentEnd = "-->";
	/**
	 * 
	 */
	public CommentSelectionAction() {
		// TODO Auto-generated constructor stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorActionDelegate#setActiveEditor(org.eclipse.jface.action.IAction, org.eclipse.ui.IEditorPart)
	 */
	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	@Override
	public void run(IAction action) {
		// TODO Auto-generated method stub
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();
		NCLEditor editor = ((NCLMultiPageEditor) page.getActiveEditor())
				.getNCLEditor();

		TextSelection selection = (TextSelection) editor.getSelectionProvider()
				.getSelection();
		IDocument doc = editor.getInputDocument();
		
		try {
			ITypedRegion region = doc.getPartition(selection.getOffset());
			if (region.getType().equals("__xml_comment")) {
				uncommentSelection(region, doc);
			}
			else {
				commentSelection(selection, doc);
			}
			
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public void commentSelection(TextSelection selection, IDocument doc){
		if (selection.getLength() > 0) {
			try {
				int offset;
				offset = selection.getOffset();
				doc.replace(offset, 0, commentBegin);
				offset += selection.getLength() + commentBegin.length();
				doc.replace(offset, 0, commentEnd);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		} else {
			// TODO: Comment the range!
			System.out.println("Comment the range!");
		}
	}
	
	public void uncommentSelection(ITypedRegion region, IDocument doc){
		int offset  = region.getOffset();
		int endOffset = region.getOffset() + (region.getLength() - commentBegin.length() - commentEnd.length());
		
		if(!region.getType().equals("__xml_comment"))
			return; // we should ignore if the region is not a comment
				
		try {
			doc.replace(offset, commentBegin.length(), ""); // remove start comment
			
			if(doc.get(endOffset, commentEnd.length()).equals(commentEnd))
				doc.replace(endOffset, commentEnd.length(), ""); // remove end comment
			
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}
