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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
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
public class CommentSelection extends AbstractHandler {

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
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

		// TODO: Check if the line is already commented. If it is uncomment the
		// line(off

		if (selection.getLength() > 0) {
			try {
				int offset;

				int line = selection.getStartLine();
				offset = doc.getLineOffset(line);
				
				String rest = doc.get(offset, selection.getOffset() - offset);
				int index = rest.indexOf(commentBegin);
				if (index != -1){
					doc.replace(offset + index, commentBegin.length(), "");
					
					line = selection.getEndLine();
					offset = doc.getLineOffset(line) + doc.getLineLength(line);
					rest = doc.get (selection.getOffset() + selection.getLength() - 
										commentBegin.length(), offset);
					
					index = rest.indexOf(commentEnd);
					if (index != -1)
						doc.replace(selection.getOffset() + selection.getLength()  - 
								commentBegin.length() + index, commentEnd.length(), "");
					return null;
				}
				
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

		return null;
	}
}
