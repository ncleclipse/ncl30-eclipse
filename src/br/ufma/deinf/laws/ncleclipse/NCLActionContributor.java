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
package br.ufma.deinf.laws.ncleclipse;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.editors.text.TextEditorActionContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.RetargetTextEditorAction;

/**
 * Contributes interesting NCL actions to the desktop's Edit menu and the
 * toolbar.
 */
public class NCLActionContributor extends TextEditorActionContributor {

	protected RetargetTextEditorAction fContentAssistProposal;
	protected RetargetTextEditorAction fContentFormat;
	protected RetargetTextEditorAction fContentGotoLastEditPosition;

	// protected RetargetTextEditorAction fContentAssistTip;
	// protected TextEditorAction fTogglePresentation;

	/**
	 * Default constructor.
	 */
	public NCLActionContributor() {
		super();
		fContentAssistProposal = new RetargetTextEditorAction(NCLEditorMessages
				.getInstance().getResourceBundle(), "ContentAssistProposal."); //$NON-NLS-1$
		fContentAssistProposal
				.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
		fContentFormat = new RetargetTextEditorAction(NCLEditorMessages
				.getInstance().getResourceBundle(), "ContentFormat.");
		fContentFormat.setActionDefinitionId(NCLEditor.FORMAT_ACTION);
		fContentGotoLastEditPosition = new RetargetTextEditorAction(
				NCLEditorMessages.getInstance().getResourceBundle(),
				"GotoLastEditPositio");
		fContentGotoLastEditPosition
				.setActionDefinitionId(ITextEditorActionDefinitionIds.GOTO_LAST_EDIT_POSITION);
		//fContentAssistTip= new RetargetTextEditorAction(NCLEditorMessages.getResourceBundle(), "ContentAssistTip."); //$NON-NLS-1$
		// fContentAssistTip.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_CONTEXT_INFORMATION);
		// fTogglePresentation= new PresentationAction();
	}

	/*
	 * @see IEditorActionBarContributor#init(IActionBars)
	 */
	public void init(IActionBars bars) {
		super.init(bars);

		IMenuManager menuManager = bars.getMenuManager();
		IMenuManager editMenu = menuManager
				.findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);
		if (editMenu != null) {
			editMenu.add(new Separator());
			editMenu.add(fContentAssistProposal);
			editMenu.add(fContentFormat);
			editMenu.add(fContentGotoLastEditPosition);
		}

		IToolBarManager toolBarManager = bars.getToolBarManager();
		if (toolBarManager != null) {
			toolBarManager.add(new Separator());
			// toolBarManager.add(fTogglePresentation);
		}
		bars.getToolBarManager().remove(ITextEditorActionConstants.DELETE);
	}

	public void setActiveEditor(IEditorPart part) {
		super.setActiveEditor(part);

		ITextEditor editor = null;
		if (part instanceof ITextEditor)
			editor = (ITextEditor) part;

		fContentAssistProposal.setAction(getAction(editor,
				"ContentAssistProposal")); //$NON-NLS-1$
		fContentFormat.setAction(getAction(editor, "ContentFormat"));
		fContentGotoLastEditPosition.setAction(getAction(editor,
				"GoToLastEditPosition"));
		//fContentAssistTip.setAction(getAction(editor, "ContentAssistTip")); //$NON-NLS-1$

		// fTogglePresentation.setEditor(editor);
		// fTogglePresentation.update();
	}

	/*
	 * @see IEditorActionBarContributor#dispose()
	 */
	public void dispose() {
		setActiveEditor(null);
		super.dispose();
	}
}
